package fr.charlotte.arsweb.sdk.verifiers.implementation;

import fr.charlotte.arsweb.sdk.verifiers.Verifier;

public class CommandingOfficerVerifier extends Verifier {
    public CommandingOfficerVerifier(String coID, String vesselID, String dummy) {
        super(coID, vesselID, "check_co");
    }
}
