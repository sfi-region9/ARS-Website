package fr.charlotte.arsweb.units;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;

import java.util.Arrays;

public class ButtonFeature extends Feature {


    public ButtonFeature(boolean whatIsIt, String title, String feature, String image, Button... buttons) {
        super(whatIsIt, title, feature, image);
        Arrays.stream(buttons).forEach(button -> {
            button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            content.add(button);
        });
    }
}
