package br.com.roxcom.modustrybridge.services;

import arc.files.Fi;
import arc.struct.Seq;
import arc.util.Http;
import arc.util.Http.HttpResponse;
import arc.util.Log;
import arc.util.Nullable;
import arc.util.io.Streams;
import arc.util.serialization.Json;
import arc.util.serialization.JsonWriter;
import arc.util.serialization.Jval;
import mindustry.Vars;
import mindustry.mod.Mods;

import static mindustry.Vars.mods;
import static mindustry.Vars.tmpDirectory;

public class ModsService {
    private final String api = "https://modustry.com.br/api/v1";
    private static final Json json = new Json();

    public static String installedModsJson() {
        json.setOutputType(JsonWriter.OutputType.json);
        Seq<Object> result = new Seq<>();

        for (Mods.LoadedMod mod : Vars.mods.list()) {
            Mods.ModMeta meta = mod.meta;

            ModInfo info = new ModInfo();

            info.id = mod.name;
            info.name = meta.displayName == null ? mod.name : meta.displayName;
            info.author = meta.author;
            info.description = meta.description;
            info.version = meta.version;
            info.minGameVersion = meta.minGameVersion;
            info.repo = mod.getRepo();

            info.enabled = mod.enabled();
            info.state = mod.state == null ? null : mod.state.name();

            info.fileName = mod.file == null ? null : mod.file.name();
            info.filePath = mod.file == null ? null : mod.file.absolutePath();

            info.dependencies = mod.dependencies.map(dep -> dep.name);
            info.softDependencies = mod.softDependencies.map(dep -> dep.name);
            info.missingDependencies = mod.missingDependencies.copy();
            info.missingSoftDependencies = mod.missingSoftDependencies.copy();

            result.add(info);
        }

        return json.toJson(result);
    }

    public boolean installMod(String id, String version) {
        final boolean[] success = {false};

        Http.get(api + "/mods/" + id).block(res -> {
            Jval json = Jval.read(res.getResultAsString());
            boolean isJava = json.getBool("isJava", false);

            success[0] = isJava
                    ? importJavaMod(id, version)
                    : importBranchMod(id);
        });

        return success[0];
    }

    private boolean handleMod(String repo, HttpResponse result) {
        Log.info("[Modustry Bridge] " + repo + " starting install.");
        try {
            Fi file = tmpDirectory.child(repo.replace("/", "") + ".zip");

            try (var stream = file.write(false)) {
                Streams.copy(result.getResultAsStream(), stream);
            } catch (java.io.IOException e) {
                Log.info("[Modustry Bridge] " + repo + " failed to download.");
                return false;
            }

            var mod = mods.importMod(file);
            mod.setRepo(repo);
            file.delete();

            Log.info("[Modustry Bridge] " + repo + " installed.");
            return true;
        } catch (Throwable e) {
            Log.info(e);
            Log.info("[Modustry Bridge] " + repo + " failed to install.");
            return false;
        }
    }

    private boolean importJavaMod(String id, @Nullable String version) {
        final boolean[] success = {false};

        Http.get(api + "/mods/" + id).block(mod -> {
            Jval jsonMod = Jval.read(mod.getResultAsString());

            Http.get(api + "/mods/" + id + "/versions").block(res -> {
                Jval.JsonArray versions = Jval.read(res.getResultAsString()).asArray();

                Jval asset = version != null && !version.isEmpty()
                        ? versions.find(j -> j.getString("name").equals(version))
                        : versions.get(0);

                Http.get(asset.getString("download_url")).block(result -> {
                    success[0] = handleMod(jsonMod.getString("repo"), result);
                });
            });
        });

        return success[0];
    }

    private boolean importBranchMod(String id) {
        final boolean[] success = {false};

        Http.get(api + "/mods/" + id).block(res -> {
            Jval json = Jval.read(res.getResultAsString());
            String repo = json.getString("repo");
            Http.get("https://api.github.com/repos/" + repo + "/zipball/" + json.getString("branch")).block(loc -> {
                if (loc.getHeader("Location") != null) {
                    Http.get(loc.getHeader("Location")).block(result -> {
                        success[0] = handleMod(repo, result);
                    });
                } else {
                    success[0] = handleMod(repo, loc);
                }
            });
        });

        return success[0];
    }

    public static class ModInfo {
        public String id;
        public String name;
        public String author;
        public String description;
        public String version;
        public String minGameVersion;
        public String repo;

        public boolean enabled;
        public String state;

        public String fileName;
        public String filePath;

        public Seq<String> dependencies;
        public Seq<String> softDependencies;
        public Seq<String> missingDependencies;
        public Seq<String> missingSoftDependencies;
    }
}
