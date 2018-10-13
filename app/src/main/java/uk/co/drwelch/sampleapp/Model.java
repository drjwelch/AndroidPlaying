package uk.co.drwelch.sampleapp;

import android.os.Handler;
import android.os.Looper;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Model implements Repository.RepoListener {

    private Person currentPerson;
    private MainActivityPresenter myPresenter;
    private Repository myRepo;
    private static final ArrayList<String> fieldKeys = new ArrayList<>();

    public Model(MainActivityPresenter presenter) {
        myPresenter = presenter;
        myRepo = Repository.getInstance();
        currentPerson = new Person("Not selected", "", "","");
    }

    // TODO decide if we need this command - if it's one model per presenter maybe not
    public void refreshData(String command) {
        myRepo.fetch(command, this);
    }

    public ArrayList<String> getFieldLabels() {
        return new ArrayList<>(Arrays.asList(Person.PROPERTIES));
    }

    public ArrayList<String> getFieldsFromObject() {
        return currentPerson.getProperties();
    }

    private void initViewUpdate() {
        // Post event to 'windowmainloop'
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                myPresenter.updateView();
            }
        });
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
        initViewUpdate();
    }

    public void onSuccess(String data) {
        // Cache and extract results
        currentPerson = myRepo.deserialise(data);
        initViewUpdate();
    }
}
