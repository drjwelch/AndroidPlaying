package uk.co.drwelch.sampleapp;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Model implements Repository.RepoListener {

    private ArrayList<String> message;
    private MainActivityPresenter myPresenter;
    private Repository myRepo;

    public Model(MainActivityPresenter presenter) {
        message = new ArrayList<>();
        message.add("Error");
        message.add("0 kg");
        myPresenter = presenter;
        myRepo = Repository.getInstance();
    }

    public void refreshData(String command) {
        myRepo.fetch(command, this);
    }

    // Repo.Listener interface

    public void onFailure(Throwable t) {
        t.printStackTrace();
    }

    public void onSuccess(String data) {
        // Extract results
        parseResponse(data);
        // Post event to 'windowmainloop'
        new Handler(Looper.getMainLooper()).post(new Runnable() {
             @Override
             public void run() {
                 myPresenter.refreshedData(Model.this.message);
             }
        });
    }

    private void parseResponse(String data) {
        try {
            JSONObject Jobject = new JSONObject(data);
            String message0 = Jobject.getString("name");
            String message1 = Jobject.getString("mass");
            message.clear();
            message.add(message0);
            message.add(message1+" kg");
        } catch (JSONException e) {
            if (e.getMessage().contains("No value for name")) {
                message.clear();
                message.add("Not found");
                message.add("N/A");
            }
            e.printStackTrace();
        }
    }

}
