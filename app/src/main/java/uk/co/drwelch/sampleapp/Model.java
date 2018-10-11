package uk.co.drwelch.sampleapp;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Model implements Repository.RepoListener {

    private Person currentPerson;
    private MainActivityPresenter myPresenter;
    private Repository myRepo;
    private static final ArrayList<String> fieldKeys = new ArrayList<String>();

    public Model(MainActivityPresenter presenter) {
        myPresenter = presenter;
        myRepo = Repository.getInstance();
        currentPerson = new Person("Not selected", "", "","");
        fieldKeys.add("name");
        fieldKeys.add("height");
        fieldKeys.add("mass");
        fieldKeys.add("created");
    }

    public void refreshData(String command) {
        myRepo.fetch(command, this);
    }

    public Person getCurrentPerson() {
        return currentPerson;
    }

    public String getFieldKey(int i) {
        return fieldKeys.get(i);
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

            currentPerson = new Person(Jobject.getString(fieldKeys.get(0)),
                    Jobject.getString(fieldKeys.get(1)),
                    Jobject.getString(fieldKeys.get(2)),
                    Jobject.getString(fieldKeys.get(3)));
        } catch (JSONException e) {
            if (e.getMessage().contains("No value for name")) {
                currentPerson = new Person("Not found", "", "","");
            }
            e.printStackTrace();
        }
    }

}
