package fr.charlotte.arsweb.units;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class Feature extends Div {

    String image;
    String feature;

    public Feature(boolean whatIsIt, String title, String feature, String image) {

        this.image = image;
        this.feature = feature;
        addClassName("feature-div");

        Image loadedImage = null;

        byte[] loadImage;
        try {
            loadImage = getClass().getResourceAsStream("/assets/" + image).readAllBytes();
            StreamResource img = new StreamResource(image, () -> new ByteArrayInputStream(loadImage));
            loadedImage = new Image(img, image.split("\\.")[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (loadedImage == null) {
            return;
        }

        VerticalLayout superContent = new VerticalLayout();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Paragraph label = new Paragraph(feature);
        H2 h2 = new H2(title);
        h2.setClassName("super-title");

        VerticalLayout content = new VerticalLayout();
        content.add(h2, label);
        label.setClassName("super-paragraph");

        content.setSizeUndefined();

        horizontalLayout.addClassName("feature-h");
        horizontalLayout.setSizeFull();

        if (whatIsIt) {
            superContent.addClassName("pinkback");
            horizontalLayout.addClassName("pinkback");
            horizontalLayout.add(content, loadedImage);
        } else {
            superContent.addClassName("blueback");
            horizontalLayout.addClassName("blueback");
            horizontalLayout.add(loadedImage, content);
        }

        setWidthFull();
        superContent.add(horizontalLayout);
        add(superContent);

    }


}
