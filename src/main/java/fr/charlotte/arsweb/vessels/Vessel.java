package fr.charlotte.arsweb.vessels;

public class Vessel {

    private String name;
    private String vesselID;
    private String coID;
    private String template = "%name%";
    private String defaultReport = "#nothing to report";
    private String reportOfficerMail;


    public Vessel() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVesselID(String vesselID) {
        this.vesselID = vesselID;
    }

    public void setCoID(String coID) {
        this.coID = coID;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public void setDefaultReport(String defaultReport) {
        this.defaultReport = defaultReport;
    }

    public void setReportOfficerMail(String reportOfficerMail) {
        this.reportOfficerMail = reportOfficerMail;
    }

    public String getVesselid() {
        return vesselID;
    }

    public String getCoID() {
        return coID;
    }

    public String getTemplate() {
        return template;
    }

    public String getDefaultReport() {
        return defaultReport;
    }

    public String getReportOfficerMail() {
        return reportOfficerMail;
    }

    public String getName() {
        name = name == null ? name : name.replace("_", " ");
        return name;
    }

    public String nameToVesselId() {
        return getName().toLowerCase().replace(" ", "");
    }
}
