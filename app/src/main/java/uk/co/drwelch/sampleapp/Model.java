package uk.co.drwelch.sampleapp;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

public class Model implements Repository.RepoListener {

    private Person currentPerson;
    private MainActivityPresenter myPresenter;
    private Repository myRepo;

    public Model(MainActivityPresenter presenter) {
        myPresenter = presenter;
        myRepo = Repository.getInstance();
        currentPerson = new Person("Not selected", "", "","");
    }

    public void refreshData(String command) {
        myRepo.fetch(command, this);
    }

    public Person getCurrentPerson() {
        return currentPerson;
    }

    // Repo.Listener interface

    public void onFailure(Throwable t) {
        if (t instanceof UnknownHostException) {
            // internet is off
            currentPerson = new Person("No network", "", "","");
        }
        t.printStackTrace();
        try {
            TimeUnit.SECONDS.sleep(1); // just to make sure you see the spinner
        } catch (InterruptedException e) {
            // do nothing?
        }
        // Post event to 'windowmainloop'
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                myPresenter.updateView();
            }
        });
    }

    public void onSuccess(String data) {
        // Cache and extract results
        parseResponse(data);
        // Post event to 'windowmainloop'
        new Handler(Looper.getMainLooper()).post(new Runnable() {
             @Override
             public void run() {
                 myPresenter.updateView();
             }
        });
    }

    private void parseResponse(String data) {
        try {
            JSONObject Jobject = new JSONObject(data);

            // responses not guaranteed to be those types so store as strings
            // handle conversion in presenter - it's business logic

            currentPerson = new Person(Jobject.getString("name"),
                    Jobject.getString("height"),
                    Jobject.getString("mass"),
                    Jobject.getString("created"));
        } catch (JSONException e) {
            if (e.getMessage().contains("No value for name")) {
                currentPerson = new Person("Not found", "", "","");
            }
            e.printStackTrace();
        }
    }

}
