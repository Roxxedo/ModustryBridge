package br.com.roxcom.modustrybridge;

import arc.Events;
import arc.util.Log;
import arc.util.Time;
import br.com.roxcom.modustrybridge.handlers.HealthHandler;
import com.sun.net.httpserver.HttpServer;
import mindustry.game.EventType;
import mindustry.mod.Mod;
import mindustry.ui.dialogs.BaseDialog;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ModustryBridge extends Mod {
    private HttpServer server;

    public ModustryBridge() {
        Log.info("[Modustry Bridge] Modustry Bridge is starting...");

        try {
            server = HttpServer.create(new InetSocketAddress(48123), 0);
            server.start();
            Log.info("[Modustry Bridge] Server started successfully.");
        } catch (IOException err) {
            Log.info("[Modustry Bridge] Server failed to start.");
            Events.on(EventType.ClientLoadEvent.class, e -> {
                Time.runTask(10f, () -> {
                    BaseDialog dialog = new BaseDialog("Modustry Bridge");
                    dialog.cont.add("Modustry Bridge failed to start.").row();
                    dialog.cont.button("Ok", dialog::hide).size(100f, 50f);
                });
            });
        }

        if (server == null) return;

        server.createContext("/health", new HealthHandler());
    }
}
