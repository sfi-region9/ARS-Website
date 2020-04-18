package fr.charlotte.arsweb.sdk;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletService;
import fr.charlotte.arsweb.MainView;
import fr.charlotte.arsweb.login.NotLoggedException;
import fr.charlotte.arsweb.sdk.verifiers.Verifier;
import fr.charlotte.arsweb.sdk.verifiers.implementation.CommandingOfficerVerifier;
import fr.charlotte.arsweb.sdk.verifiers.implementation.VesselNameVerifier;
import fr.charlotte.arsweb.sdk.verifiers.implementation.VesselTemplateVerifier;
import fr.charlotte.arsweb.utils.Session;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class User {

    private static final String authUrl = "https://auth.sfiars.eu";
    private static final String arsUrl = "https://api.sfiars.eu";

    private final String name;
    private final String scc;
    private String vesselID;
    private String report = "";
    private String uuid = "defaultuuid";
    private transient String messengerid;
    private transient String commandingOfficer;

    public User(String name, String scc, String vesselID, String report, String uuid) {
        this(name, scc, vesselID, report, uuid, "undefined");
    }

    public User(String name, String scc, String vesselID, String report, String uuid, String messengerid) {
        this.name = name;
        this.scc = scc;
        this.vesselID = vesselID;
        this.report = report;
        this.uuid = uuid;
        this.messengerid = messengerid;
    }

    public boolean isCommandingOfficer() {
        if (commandingOfficer == null) {
            try {
                commandingOfficer = String.valueOf(verify(CommandingOfficerVerifier.class, ""));
            } catch (IOException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                commandingOfficer = "false";
            }
        }
        return Boolean.parseBoolean(commandingOfficer);
    }

    public boolean changeTemplateOfVessel(String newTemplate) {
        if (!isCommandingOfficer())
            return false;
        else {
            try {
                return verify(VesselTemplateVerifier.class, newTemplate);
            } catch (IOException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                return false;
            }
        }
    }

    public boolean changeDefaultReportOfVessel(String newDefaultReport) {
        if (!isCommandingOfficer())
            return false;
        else {
            try {
                return verify(VesselNameVerifier.class, newDefaultReport);
            } catch (IOException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                return false;
            }
        }
    }

    public static User loadUserFromSession(Session session) throws NotLoggedException {
        if (session.getValue("logged").equals("null"))
            throw new NotLoggedException();
        return new User(session.getValue("name", String.class), session.getValue("scc", String.class), session.getValue("vesselid", String.class), session.getValue("report", String.class), session.getValue("uuid", String.class));
    }

    public User synchronize() throws UserDontExistException {
        String url = arsUrl + "/synchronize_user";
        String json = MainView.GSON.toJson(this);
        Request request = new Request.Builder().url(url).post(RequestBody.create(MainView.JSON, json)).build();
        User loadedUser;
        try {
            loadedUser = MainView.GSON.fromJson(MainView.HTTP_CLIENT.newCall(request).execute().body().string(), User.class);
            if (loadedUser == null || loadedUser.uuid.equalsIgnoreCase("invalidid"))
                throw new UserDontExistException();
        } catch (IOException e) {
            throw new UserDontExistException();
        }
        return loadedUser;
    }

    public void postReport() {
        basicRequest("submit");
    }

    public void destroyUser(Session session) {
        String url = arsUrl + "/destroy_user";
        String auth = authUrl + "/destroy_user";
        String json = MainView.GSON.toJson(this);
        Request arsRequest = new Request.Builder().url(url).post(RequestBody.create(MainView.JSON, json)).build();
        Request authRequest = new Request.Builder().url(auth).post(RequestBody.create(MainView.JSON, json)).build();
        try {
            MainView.HTTP_CLIENT.newCall(arsRequest).execute();
            MainView.HTTP_CLIENT.newCall(authRequest).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        session.destroy();
        UI.getCurrent().getPage().executeJs("document.location = \"" + VaadinServletService.getCurrentServletRequest().getContextPath() + "\"");
    }

    public void setReport(String report) {
        this.report = report;
    }

    public void pushUserIntoSession(Session session) {
        session.addKey("name", this.name, true);
        session.addKey("uuid", this.uuid, true);
        session.addKey("messengerid", this.messengerid, true);
        session.addKey("vesselid", this.vesselID, true);
        session.addKey("scc", this.scc, true);
        session.addKey("report", this.report, true);
    }


    private boolean verify(Class<? extends Verifier> v, String text) throws IOException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Verifier verifier = (Verifier) v.getConstructors()[0].newInstance(messengerid, vesselID, text);
        String url = arsUrl + "/" + verifier.getEndpoint();
        String json = MainView.GSON.toJson(verifier);
        System.out.println(json);
        Request request = new Request.Builder().post(RequestBody.create(MainView.JSON, json)).url(url).build();
        return Boolean.parseBoolean(MainView.HTTP_CLIENT.newCall(request).execute().body().string());
    }

    public void switchVessel() {
       basicRequest("switch_vessel");
    }

    public void setVesselID(String vesselID) {
        this.vesselID = vesselID;
    }

    private void basicRequest(String endpoint){
        String url = arsUrl + "/" +  endpoint;
        String json = MainView.GSON.toJson(this);
        Request request = new Request.Builder().url(url).post(RequestBody.create(MainView.JSON, json)).build();
        try {
            MainView.HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
