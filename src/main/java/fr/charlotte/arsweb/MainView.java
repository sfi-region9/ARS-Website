package fr.charlotte.arsweb;

import fr.charlotte.arsweb.units.CookieDialog;
import fr.charlotte.arsweb.units.Feature;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;

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
        enableInstallPrompt = true,
        backgroundColor = "grey",
        offlineResources = "./assets/")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends AppLayout {

    public static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    public static final Gson GSON = new Gson();

    public MainView() throws IOException {
        Tab home = new Tab("Home");
        Tab doc = new Tab("Documentation");
        Tab login = new Tab("Login");
        Tab register = new Tab("Register");
        register.setEnabled(false);
        Tabs tabs = new Tabs(home, doc, login, register);

        tabs.addSelectedChangeListener(selectedChangeEvent -> {
            if (selectedChangeEvent.getSelectedTab().getLabel().equalsIgnoreCase("Logins")) {
                System.out.println("Login Overlay was triggered by user");
                LoginOverlay loginOverlay = new LoginOverlay();
                loginOverlay.setTitle("ARS Login");
                loginOverlay.setDescription("Login into your ARS Account");
                loginOverlay.setForgotPasswordButtonVisible(false);
                loginOverlay.setAction("login");
                loginOverlay.setOpened(true);
            }
        });

        byte[] b = getClass().getResourceAsStream("/assets/logo.png").readAllBytes();
        Image icon = new Image(new StreamResource("logo.png", () -> new ByteArrayInputStream(b)), "logo");

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setPadding(false);
        content.setSpacing(false);
        Feature dummyFeature = new Feature(false, "Messenger Management", "You can do almost everything with the messenger bot ! You can do a light management directly into the Messenger Interface without login and linking your accounts", "help.png");
        Feature loadFeature = new Feature(true, "Easy Subscribing", "You're just a command away from subscribing to our system, the most advanced and automatic way to report to your chain-of-command", "sub.png");
        Feature thirdFeature = new Feature(false, "Lorem", "Ipsum", "logo.png");
        Feature fourthFeature = new Feature(true, "Lorem", "Ipsum", "logo.png");
        content.add(dummyFeature, loadFeature, thirdFeature, fourthFeature);
        content.addClassName("redborder");

        icon.setHeight("44px");
        addToNavbar(icon, tabs);
        setContent(content);

        CookieDialog cookieDialog = new CookieDialog();
        cookieDialog.setOpened(true);
    }


}
