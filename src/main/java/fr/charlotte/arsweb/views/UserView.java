package fr.charlotte.arsweb.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServletService;
import com.vaadin.flow.server.VaadinSession;
import fr.charlotte.arsweb.MainView;
import fr.charlotte.arsweb.login.NotLoggedException;
import fr.charlotte.arsweb.sdk.User;
import fr.charlotte.arsweb.sdk.UserDontExistException;
import fr.charlotte.arsweb.sdk.vessels.Vessel;
import fr.charlotte.arsweb.services.VesselServices;
import fr.charlotte.arsweb.utils.Session;

import java.util.Random;

@Route("user")
public class UserView extends VerticalLayout implements BeforeEnterListener {
    Session session = Session.getSession(VaadinSession.getCurrent().getBrowser().getAddress());

    String[] randomReport = {"Rapport", "NTR", "Je vais bien", "Le confinement c'est chiant n'empêche :("};

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (session.getValue("logged") == null)
            beforeEnterEvent.rerouteTo("home");
    }

    Button synchronize;
    Button postRandomReport;
    Button switchToARandomVessel;
    Random random = new Random();
    User user;

    public UserView() {
        if (session.getValue("name").equals("null")) {
            System.out.println("User not logged");
            UI.getCurrent().getPage().executeJs("document.location = \"" + VaadinServletService.getCurrentServletRequest().getContextPath() + "\"");
            return;
        }
        Paragraph p = new Paragraph("");
        p.addClassName("super-paragraph");
        try {
            user = User.loadUserFromSession(session);
            p.setText(MainView.GSON.toJson(user));
            synchronize = new Button("Synchronize now", buttonClickEvent -> {
                try {
                    synchronize.setEnabled(false);
                    User newUser = user.synchronize();
                    newUser.pushUserIntoSession(session);
                    user = newUser;
                    p.setText(MainView.GSON.toJson(newUser));
                    synchronize.setEnabled(true);
                } catch (UserDontExistException e) {
                    UI.getCurrent().getPage().executeJs("document.location = \"" + VaadinServletService.getCurrentServletRequest().getContextPath() + "\"");
                }
            });
        } catch (NotLoggedException e) {
            UI.getCurrent().getPage().executeJs("document.location = \"" + VaadinServletService.getCurrentServletRequest().getContextPath() + "\"");
        }

        postRandomReport = new Button("Post a random report", buttonClickEvent -> {
            postRandomReport.setEnabled(false);
            synchronize.setEnabled(false);
            String newReport = randomReport[random.nextInt(randomReport.length)];
            user.setReport(newReport);
            user.postReport();
            postRandomReport.setEnabled(true);
            synchronize.setEnabled(true);
        });
        switchToARandomVessel = new Button("Switch to a random vessel",buttonClickEvent -> {
            Vessel v = VesselServices.getInstance().findAll()[random.nextInt(3)];
            user.setVesselID(v.getVesselid());
            user.switchVessel();
        });

        add(p, synchronize, postRandomReport, switchToARandomVessel);

    }
}
