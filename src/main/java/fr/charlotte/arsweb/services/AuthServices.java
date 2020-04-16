package fr.charlotte.arsweb.services;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.vaadin.flow.component.login.AbstractLogin;
import fr.charlotte.arsweb.MainView;
import fr.charlotte.arsweb.login.Login;

import java.io.IOException;

public class AuthServices {

    private static AuthServices instance;

    private AuthServices() {
    }

    ;

    public static AuthServices getInstance() {
        if (instance == null) {
            instance = new AuthServices();
        }
        return instance;
    }

    public String[] login(AbstractLogin.LoginEvent loginEvent) throws IOException {
        String password = loginEvent.getPassword();
        String username = loginEvent.getUsername();

        Login login = new Login();
        login.setPassword(password);
        login.setUsername(username);

        String jsonLogin = MainView.GSON.toJson(login);
        Request r = new Request.Builder().post(RequestBody.create(MediaType.parse("application/json"), jsonLogin)).url("https://auth.sfiars.eu/login").build();
        String answer = MainView.HTTP_CLIENT.newCall(r).execute().body().string();

        if (answer.contains("Error while login, ")) {
            return new String[]{"false", answer};
        } else {
            return new String[]{"true", answer};
        }

    }


    ;
}
