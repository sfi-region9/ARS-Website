package fr.charlotte.arsweb;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import fr.charlotte.arsweb.services.AuthServices;
import fr.charlotte.arsweb.services.VesselServices;
import fr.charlotte.arsweb.units.ButtonFeature;
import fr.charlotte.arsweb.units.CookieDialog;
import fr.charlotte.arsweb.units.Feature;
import fr.charlotte.arsweb.utils.Session;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.stream.IntStream;

/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and
 * use @Route annotation to announce it in a URL as a Spring managed
 * bean.
 * Use the @PWA annotation make the application installable on phones,
 * tablets and some desktop browsers.
 * <p>
 * A new instance of this class is created for every new user and every
 * browser tab/window.
 */
@Route("")
@RouteAlias("home")
@PageTitle("ARS Home")
@PWA(name = "Vaadin Application",
        shortName = "Vaadin App",
        description = "This is an example Vaadin application.",
        backgroundColor = "grey",
        enableInstallPrompt = false,
        offlineResources = "./assets/")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends AppLayout {

    public static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    public static final Gson GSON = new Gson();
    public static final MediaType JSON = MediaType.parse("application/json");

    private static String[] SESSION = new String[]{"username", "scc", "vesselid", "name", "messengerid", "uuid"};

    private AuthServices authServices = AuthServices.getInstance();

    public MainView() throws IOException {
        Tab home = new Tab("Home");
        Tab doc = new Tab("Documentation");
        Tab login = new Tab("Login");
        Tab register = new Tab("Register");
        Tabs tabs = new Tabs(home, doc);

        byte[] b = getClass().getResourceAsStream("/assets/logo.png").readAllBytes();
        Image icon = new Image(new StreamResource("logo.png", () -> new ByteArrayInputStream(b)), "logo");
        icon.setHeight("44px");

        Session session = Session.getSession(VaadinSession.getCurrent().getBrowser().getAddress());

        if (session.getValue("logged").equals("null")) {
            tabs.add(login, register);
            addToNavbar(icon, tabs);
        } else {
            Label welcomeLabel = new Label("Welcome " + session.getValue("name"));
            Tab tab = new Tab("Log Out");
            Tab user = new Tab("User page");
            tabs.add(user, tab);
            addToNavbar(icon, tabs, welcomeLabel);
        }

        tabs.addSelectedChangeListener(selectedChangeEvent -> {
            if (selectedChangeEvent.getSelectedTab().getLabel().equalsIgnoreCase("Login")) {
                System.out.println("Login Overlay was triggered by user");
                LoginOverlay loginOverlay = new LoginOverlay();
                loginOverlay.setTitle("ARS Login");
                loginOverlay.setDescription("Login into your ARS Account");
                loginOverlay.setForgotPasswordButtonVisible(false);
                loginOverlay.setOpened(true);
                loginOverlay.addLoginListener(loginEvent -> {
                    String[] answer = null;
                    try {
                        answer = authServices.login(loginEvent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (answer == null) {
                        loginOverlay.setError(true);
                        return;
                    }

                    boolean isAuthenticated = Boolean.parseBoolean(answer[0]);
                    String lig = answer[1];
                    if (isAuthenticated) {
                        String[] sessionParsing = lig.split("}_}");

                        IntStream.iterate(1, i -> i < sessionParsing.length, i -> i + 1).forEach(i -> session.addKey(SESSION[i], sessionParsing[i]));
                        session.addKey("logged", true);
                        loginOverlay.close();
                        tabs.setSelectedTab(home);
                        UI.getCurrent().getPage().reload();
                    } else {
                        loginOverlay.setError(true);
                    }
                });
            } else if (selectedChangeEvent.getSelectedTab().getLabel().equalsIgnoreCase("Log Out")) {
                UI.getCurrent().navigate("logout");
            } else if (selectedChangeEvent.getSelectedTab().getLabel().equalsIgnoreCase("Register")) {
                UI.getCurrent().navigate("register");
            }else if(selectedChangeEvent.getSelectedTab().getLabel().equalsIgnoreCase("User page")){
                if(session.getValue("logged") == null)
                    return;
                UI.getCurrent().navigate("user");
            }
        });


        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setPadding(false);
        content.setSpacing(false);

        Button downloadClient = new Button("Download Android Client!", buttonClickEvent -> {
            Notification.show("Coming Soon ;)", 10000, Notification.Position.BOTTOM_CENTER);
        });
        downloadClient.addThemeVariants(ButtonVariant.LUMO_SUCCESS);


        Button request = new Button("Request a plugin", buttonClickEvent -> Notification.show("Describe your plugin and send the description to the administrator at : charlotte@sfiars.eu!", 10000, Notification.Position.BOTTOM_CENTER));
        request.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        Feature dummyFeature = new Feature(false, "Messenger Management", "You can do almost everything with the messenger bot ! You can do a light management directly into the Messenger Interface without login and linking your accounts", "help.png");
        Feature loadFeature = new Feature(true, "Easy Subscription", "You're just a command away from subscribing to our system, the most advanced and automatic way to report to your chain-of-command", "sub.png");
        Feature thirdFeature = new ButtonFeature(false, "Clients", "You can use our web client if you're on a pc, or our Android client on your mobile!", "", downloadClient);
        Feature fourthFeature = new ButtonFeature(true, "Flexible System", "The system include a system of plugins, you can add your own commands or custom processing of reports with the API, you can request a custom-made plugin too", "",
                request);
        content.add(dummyFeature, loadFeature, thirdFeature, fourthFeature);
        content.addClassName("redborder");


        setContent(content);

        if (VaadinSession.getCurrent().getAttribute("cookie") == null) {
            CookieDialog cookieDialog = new CookieDialog();
            cookieDialog.setOpened(true);
            VaadinSession.getCurrent().setAttribute("cookie", true);
        }


    }


}
