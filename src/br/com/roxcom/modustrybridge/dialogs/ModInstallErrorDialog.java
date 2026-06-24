package br.com.roxcom.modustrybridge.dialogs;

import arc.Core;
import arc.input.KeyCode;
import arc.scene.ui.Dialog;
import arc.scene.ui.ScrollPane;
import arc.scene.ui.layout.Scl;
import arc.scene.ui.layout.Table;
import arc.util.Align;
import mindustry.ui.dialogs.BaseDialog;

public class ModInstallErrorDialog extends BaseDialog {
    private static final float bestWidth = 500f;
    private static final float padding = 30f;

    public ModInstallErrorDialog (String modName) {
        super("@mods.install.error.title", Core.scene.getStyle(Dialog.DialogStyle.class));

        float width = Math.min(bestWidth, Core.graphics.getWidth() / Scl.scl(1) - padding);
        float maxHeight = Core.graphics.getHeight() / Scl.scl(1);

        Table table = new Table();

        table.add(modName)
                .wrap()
                .width(width)
                .pad(5f)
                .get().setAlignment(Align.center, Align.center);
        table.row();
        table.add("@mods.install.error.message")
                .wrap()
                .width(width)
                .pad(5f)
                .get().setAlignment(Align.center, Align.center);

        ScrollPane pane = new ScrollPane(table);
        pane.setScrollingDisabled(true, false);
        pane.setFadeScrollBars(false);
        cont.add(pane).maxHeight(maxHeight).width(width).row();

        buttons.defaults().size(150f, 50f);
        setFillParent(false);

        keyDown(KeyCode.enter, this::hide);
        keyDown(KeyCode.escape, this::hide);

        buttons.button("@mods.install.error.button", this::hide);

        this.show();
    }
}
