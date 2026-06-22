package br.com.roxcom.modustrybridge.dialogs;

import arc.util.Align;
import br.com.roxcom.modustrybridge.ModustryBridge;
import mindustry.gen.Tex;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

public class RequestPairingDialog extends BaseDialog {
    public RequestPairingDialog(String pairCode) {
        super("Confirm pairing");

        this.background(Styles.none);
        cont.background(Tex.pane);

        cont.margin(20);

        cont.add("The website wants to connect.")
                .center()
                .get()
                .setAlignment(Align.center);
        cont.row();

        cont.add(pairCode);
        cont.row();

        cont.button("Confirm", () -> {
            ModustryBridge.pairing.bridgeConfirm(pairCode);
            this.hide();
        }).size(100f, 50f);
        cont.button("Cancel", this::hide).size(100f, 50f);

        this.show();
    }
}
