package fr.charlotte.arsweb.units;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.VaadinService;

public class CookieDialog extends Dialog {

    public CookieDialog() {
        Paragraph paragraph = new Paragraph("Do you consent to the usage of cookies ?");
        paragraph.setClassName("super-paragraph");
        Button agreed = new Button("Heck Yeah!");
        Button nop = new Button("Nah");
        agreed.addClickListener(buttonClickEvent -> this.close());
        nop.addClickListener(buttonClickEvent -> VaadinService.getCurrentResponse().setHeader("Location:", "google.com"));
        agreed.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        nop.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout layout = new HorizontalLayout(agreed, nop);

        this.add(paragraph, layout);
    }

}
