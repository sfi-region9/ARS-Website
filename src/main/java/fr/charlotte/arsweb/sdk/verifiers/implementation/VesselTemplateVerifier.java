package fr.charlotte.arsweb.sdk.verifiers.implementation;

import fr.charlotte.arsweb.sdk.verifiers.Verifier;

public class VesselTemplateVerifier extends Verifier {
    String template;
    public VesselTemplateVerifier(String coID, String vesselID, String template) {
        super(coID, vesselID, "update_template");
        this.template = template;
    }
}
