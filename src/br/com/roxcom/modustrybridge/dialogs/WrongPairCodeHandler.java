package br.com.roxcom.modustrybridge.dialogs;

import mindustry.ui.dialogs.BaseDialog;

public class WrongPairCodeHandler extends BaseDialog {
    public WrongPairCodeHandler() {
        super("Wrong Pair Code");

        cont.add("Wrong Pair Code").row();
        cont.button("Ok", this::hide).size(100f, 50f);

        this.show();
    }
}
