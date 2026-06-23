package br.com.roxcom.modustrybridge.dialogs;

import arc.Core;
import arc.input.KeyCode;
import arc.scene.ui.ScrollPane;
import arc.scene.ui.layout.Scl;
import arc.scene.ui.layout.Table;
import arc.util.Align;
import br.com.roxcom.modustrybridge.ModustryBridge;
import mindustry.ui.dialogs.BaseDialog;

public class RequestPairingDialog extends BaseDialog {
    private static final float bestWidth = 500f;
    private static final float padding = 30f;

    public RequestPairingDialog(String pairCode) {
        super("@pairing.request.title", Core.scene.getStyle(DialogStyle.class));

        float width = Math.min(bestWidth, Core.graphics.getWidth() / Scl.scl(1) - padding);
        float maxHeight = Core.graphics.getHeight() / Scl.scl(1);

        Table table = new Table();

        table.add("@pairing.request.message")
                .wrap()
                .width(width)
                .pad(5f)
                .get().setAlignment(Align.center, Align.center);
        table.row();
        table.add(pairCode)
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

        keyDown(KeyCode.escape, this::hide);
        keyDown(KeyCode.enter, () -> {
            ModustryBridge.pairing.bridgeConfirm(pairCode);
            this.hide();
        });

        buttons.button("@pairing.request.confirm", () -> {
            ModustryBridge.pairing.bridgeConfirm(pairCode);
            this.hide();
        });
        buttons.button("@pairing.request.cancel", this::hide);

        this.show();
    }
}
