package fr.charlotte.arsweb.vessels;

import fr.charlotte.arsweb.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;

public class VesselForm extends FormLayout {

    private TextField name;
    private TextField vesselID;
    private TextField coID;
    private EmailField reportOfficerMail;

    //private ComboBox<Integer> region = new ComboBox<>("Region");

    private Button save = new Button("Save");
    private Button delete = new Button("Delete");
    private Binder<Vessel> binder = new Binder<>(Vessel.class);

    private MainView mainView;

    public VesselForm(MainView mainView) {
        this.mainView = mainView;
        VerticalLayout layout = new VerticalLayout();
        name = new TextField("Vessel Name");
        coID = new TextField("ID of the CO");
        vesselID = new TextField("Vessel ID");
        reportOfficerMail = new EmailField("Report Officer Email");
        vesselID.setEnabled(false);
        name.setValueChangeMode(ValueChangeMode.EAGER);
        name.addValueChangeListener(textFieldStringComponentValueChangeEvent -> {
            vesselID.setValue(name.getValue().toLowerCase().replace(" ", ""));
        });
        //region.setItems(IntStream.iterate(1, i -> i < 21, i -> i + 1).boxed());
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        layout.add(name, coID, vesselID, reportOfficerMail, new HorizontalLayout(save, delete));
        add(layout);


        binder.bind(name, Vessel::getName, Vessel::setName);
        binder.bind(reportOfficerMail, Vessel::getReportOfficerMail, Vessel::setReportOfficerMail);
        binder.bind(coID, Vessel::getCoID, Vessel::setCoID);
        binder.bind(vesselID, Vessel::getVesselid, Vessel::setVesselID);



    }

    public void setVessel(Vessel vessel) {
        binder.setBean(vessel);
        if (vessel == null)
            setVisible(false);
        else
            setVisible(true);
    }


}
