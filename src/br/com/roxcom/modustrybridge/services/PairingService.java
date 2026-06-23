package br.com.roxcom.modustrybridge.services;

import br.com.roxcom.modustrybridge.dialogs.WrongPairCodeHandler;

import java.util.Objects;
import java.util.Random;

public class PairingService {
    private String pairCode;
    private boolean websiteConfirmed = false;
    private boolean bridgeConfirmed = false;
    private boolean paired = false;

    public String pair() {
        Random random = new Random();
        String pairCode = String.format("%06d", random.nextInt(1000000));
        this.pairCode = pairCode;
        return pairCode;
    }

    public void bridgeConfirm(String pairCode) {
        if (Objects.equals(pairCode, this.pairCode)) {
            this.bridgeConfirmed = true;
            this.checkPaired();
        } else {
            new WrongPairCodeHandler();
        }
    }

    public void websiteConfirm(String pairCode) {
        if (Objects.equals(pairCode, this.pairCode)) {
            this.websiteConfirmed = true;
            this.checkPaired();
        } else {
            new WrongPairCodeHandler();
        }
    }

    private void checkPaired() {
        if (this.websiteConfirmed && this.bridgeConfirmed) this.paired = true;
    }

    public String getPairCode() {
        if (!this.paired) return null;
        return this.pairCode;
    }

    public boolean verify(String pairCode) {
        return pairCode.equals(this.pairCode);
    }

    public boolean isPaired() {
        return this.paired;
    }
}
