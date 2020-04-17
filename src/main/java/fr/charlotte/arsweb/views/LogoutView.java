package fr.charlotte.arsweb.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServletService;
import com.vaadin.flow.server.VaadinSession;
import fr.charlotte.arsweb.utils.Session;

@Route("logout")
public class LogoutView extends Div {

    public LogoutView(){
        Session session = Session.getSession(VaadinSession.getCurrent().getBrowser().getAddress());
        session.destroy();
        System.out.println("Session Destroyed");
        UI.getCurrent().getPage().executeJs("document.location = \"" + VaadinServletService.getCurrentServletRequest().getContextPath() + "\"");
    }
}
