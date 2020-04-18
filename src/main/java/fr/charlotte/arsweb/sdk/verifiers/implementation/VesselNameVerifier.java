package fr.charlotte.arsweb.sdk.verifiers.implementation;

import fr.charlotte.arsweb.sdk.verifiers.Verifier;

public class VesselNameVerifier extends Verifier {
    String text;
    public VesselNameVerifier(String coID, String vesselID, String text) {
        super(coID, vesselID, "update_name");
        this.text = text;
    }
}
