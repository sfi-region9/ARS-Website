package fr.charlotte.arsweb.services;

import fr.charlotte.arsweb.MainView;
import fr.charlotte.arsweb.sdk.vessels.Vessel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Logger;

public class VesselServices {

    private static VesselServices instance;
    private static final Logger LOGGER = Logger.getLogger(VesselServices.class.getName());
    private HashMap<String, Vessel> contact = new HashMap<>();

    private VesselServices() {

    }

    public static VesselServices getInstance() {
        if (instance == null) {
            instance = new VesselServices();
            instance.updateDataFromServer();
        }
        return instance;
    }

    private void updateDataFromServer() {
        OkHttpClient h = MainView.HTTP_CLIENT;
        Gson g = MainView.GSON;

        Request r = new Request.Builder().url("https://api.sfiars.eu/allvessels").get().build();
        String answer = null;
        try {
            answer = h.newCall(r).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (answer == null)
            updateDataFromServer();
        Type listV = new TypeToken<ArrayList<Vessel>>() {
        }.getType();
        ArrayList<Vessel> vessels = g.fromJson(answer, listV);
        HashMap<String, Vessel> vesselHashMap = new HashMap<>();
        Objects.requireNonNull(vessels).forEach(vessel -> vesselHashMap.put(vessel.getVesselid(), vessel));
        this.contact = vesselHashMap;
    }


    public Vessel[] findAll() {
        return contact.values().stream().toArray(Vessel[]::new);
    }


}