package fr.charlotte.arsweb.views;


import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("login")
public class LoginView extends Div implements BeforeEnterListener{
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(VaadinSession.getCurrent().getAttribute("username") != null)
            beforeEnterEvent.rerouteTo("home");
    }

}
