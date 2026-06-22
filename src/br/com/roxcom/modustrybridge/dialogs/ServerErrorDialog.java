package br.com.roxcom.modustrybridge.dialogs;

import mindustry.ui.dialogs.BaseDialog;

public class ServerErrorDialog extends BaseDialog {
    public ServerErrorDialog () {
        super("Modustry Bridge");
        cont.add("Modustry Bridge failed to start.").row();
        cont.button("Ok", this::hide).size(100f, 50f);
    }
}
