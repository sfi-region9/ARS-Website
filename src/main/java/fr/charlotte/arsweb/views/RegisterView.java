package fr.charlotte.arsweb.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinServletService;
import com.vaadin.flow.server.VaadinSession;
import fr.charlotte.arsweb.MainView;
import fr.charlotte.arsweb.register.Register;
import fr.charlotte.arsweb.services.AuthServices;
import fr.charlotte.arsweb.services.VesselServices;
import fr.charlotte.arsweb.vessels.Vessel;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;


@Route("register")
public class RegisterView extends VerticalLayout {
    VerticalLayout formContent;
    TextField username = new TextField("Username");
    TextField name = new TextField("Name");
    PasswordField password = new PasswordField("Password");
    EmailField email = new EmailField("Email");
    IntegerField scc = new IntegerField("SCC");
    ComboBox<Vessel> vessels = new ComboBox<>("Vessel");
    Binder<Register> registerBinder = new Binder<>(Register.class);
    Label infoLabel = new Label("");
    Button register;
    RouterLink login;
    H1 h1;

    public RegisterView() {
        form();

        prepare();

        bind();

        //COMBOBOX For Vessel Choice

        register = new Button("Register", buttonClickEvent -> register());
        login = new RouterLink("Already an account? Login!", MainView.class);
        register.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        infoLabel.setVisible(false);
        formContent.add(h1, infoLabel, username, password, name, email, scc, vessels, register, login);
        add(formContent);
        addClassName("background");
        setSizeFull();
    }

    private void form() {
        formContent = new VerticalLayout();
        formContent.addClassName("background");
        formContent.setAlignItems(Alignment.CENTER);
        formContent.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        h1 = new H1("Register");
        h1.setSizeUndefined();
        h1.addClassName("super-paragraph");
        formContent.setSizeFull();
    }

    private void prepare() {
        infoLabel.setClassName("super-paragraph");
        infoLabel.setClassName("error-paragraph");

        password.setClearButtonVisible(true);
        password.setRequired(true);
        password.setRequiredIndicatorVisible(true);
        password.setRevealButtonVisible(true);
        password.setValueChangeMode(ValueChangeMode.EAGER);

        email.setPlaceholder("username");
        email.setRequiredIndicatorVisible(true);
        email.setPlaceholder("user@domain.tld");
        email.setValueChangeMode(ValueChangeMode.EAGER);

        name.setClearButtonVisible(true);
        name.setRequired(true);
        name.setRequiredIndicatorVisible(true);
        name.setPlaceholder("NAME Firstname");
        name.setValueChangeMode(ValueChangeMode.EAGER);

        username.setClearButtonVisible(true);
        username.setRequired(true);
        username.setRequiredIndicatorVisible(true);
        username.setValueChangeMode(ValueChangeMode.EAGER);

        scc.setMax(100000);
        scc.setRequiredIndicatorVisible(true);
        scc.setStep(1);
        scc.setValueChangeMode(ValueChangeMode.EAGER);

        vessels.setItems(VesselServices.getInstance().findAll());
        vessels.setRequired(true);
        vessels.setValue(VesselServices.getInstance().findAll()[0]);
        vessels.setRequiredIndicatorVisible(true);
    }

    private void bind() {
        Binder.Binding<Register, String> nameBinding = registerBinder.forField(name).withValidator(new StringLengthValidator("Please add your name", 4, null)).bind(Register::getName, Register::setName);
        Binder.Binding<Register, String> emailBinding = registerBinder.forField(email).withValidator(new StringLengthValidator("Please add your email", 1, null)).withValidator(new EmailValidator("Incorrect email address")).bind(Register::getEmail, Register::setEmail);
        Binder.Binding<Register, String> usernameBinding = registerBinder.forField(username).withValidator(new StringLengthValidator("Please add your username", 4, null)).bind(Register::getUsername, Register::setUsername);
        Binder.Binding<Register, String> passwordBinding = registerBinder.forField(password).withValidator(new StringLengthValidator("Please add your password", 1, null)).bind(Register::getPassword, Register::setPassword);
        Binder.Binding<Register, String> sccBinding = registerBinder.forField(scc).withValidator(new IntegerRangeValidator("Please set the scc between 0 and 1000000", 0, 1000000)).withConverter(Register::translateScc, Integer::valueOf).bind(Register::getScc, Register::setScc);
        Binder.Binding<Register, String> vesselBinding = registerBinder.forField(vessels).withConverter(Vessel::getVesselid, Vessel::vesselFromID).bind(Register::getVessel, Register::setVessel);

        name.addValueChangeListener(event -> nameBinding.validate());
        email.addValueChangeListener(event -> emailBinding.validate());
        username.addValueChangeListener(event -> usernameBinding.validate());
        password.addValueChangeListener(event -> passwordBinding.validate());
        scc.addValueChangeListener(event -> sccBinding.validate());
    }


    private void register() {
        register.setEnabled(false);
        Register r = new Register();
        boolean valid = registerBinder.writeBeanIfValid(r);
        if (!valid)
            return;
        try {
            String[] s = AuthServices.getInstance().register(r);
            if (Boolean.parseBoolean(s[0])) {
                Notification.show("You're now registred, you can login in the main page !", 10000, Notification.Position.BOTTOM_CENTER);
                UI.getCurrent().getPage().executeJs("   window.setTimeout(function(){\n" +
                        "\n" +
                        "        // Move to a new location or you can do something else\n" +
                        "        window.location.href = \"" + VaadinServletService.getCurrentServletRequest().getContextPath() + "\";\n" +
                        "\n" +
                        "    }, 3000);");
                return;
            }
            infoLabel.setText("There are an error : " + s[1]);
            infoLabel.setVisible(true);
            register.setEnabled(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
