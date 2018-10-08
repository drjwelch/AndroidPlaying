package uk.co.drwelch.sampleapp;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONException;
import org.json.JSONObject;

public class Model implements Repository.RepoListener {

    private Person message;
    private MainActivityPresenter myPresenter;
    private Repository myRepo;

    public Model(MainActivityPresenter presenter) {
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
        // Cache and extract results
        parseResponse(data);
        // Post event to 'windowmainloop'
        new Handler(Looper.getMainLooper()).post(new Runnable() {
             @Override
             public void run() {
                 myPresenter.showData(Model.this.message);
             }
        });
    }

    private void parseResponse(String data) {
        try {
            JSONObject Jobject = new JSONObject(data);

            // responses not guaranteed to be those types so store as strings
            // handle conversion in presenter - it's business logic

            message = new Person(Jobject.getString("name"),
                    Jobject.getString("height"),
                    Jobject.getString("mass"),
                    Jobject.getString("created"));
        } catch (JSONException e) {
            if (e.getMessage().contains("No value for name")) {
                message = new Person("Not found", "", "","");
            }
            e.printStackTrace();
        }
    }

}
