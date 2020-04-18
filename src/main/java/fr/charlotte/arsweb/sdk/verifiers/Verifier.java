package fr.charlotte.arsweb.sdk.verifiers;

public abstract class Verifier{
    protected final String coID;
    protected final String vesselID;
    protected transient String endpoint;

    public Verifier(String coID, String vesselID, String endpoint){
        this.coID = coID;
        this.vesselID = vesselID;
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }
}
