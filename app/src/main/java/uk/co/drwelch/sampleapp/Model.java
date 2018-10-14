package uk.co.drwelch.sampleapp;

import android.os.Handler;
import android.os.Looper;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Model implements Repository.RepoListener {

    private String currentPersonName;
    private boolean dataLoaded;
    private Person[] allPeople;
    private ArrayList<Model.DataChangeListener> presenters = new ArrayList<>();
    private Repository repo;
    private static Model modelInstance = null;

    private Model() {
        repo = Repository.getInstance();
        dataLoaded = false;
    }

    public static synchronized Model getInstance() {  // sync to prevent re-entrant
        if (modelInstance == null) {
            modelInstance = new Model();
        }
        return modelInstance;
    }

    public void attachPresenter(Model.DataChangeListener newPresenter) {
        // Retain only one listener/presenter of each class
        // On BACK, View does not re-establish connection to its presenter
        // so the presenter has to go, which means releasing all references to it
        // so that Java garbage-collects it
        for (Model.DataChangeListener p:presenters) {
            if (p.getClass() == newPresenter.getClass() && p!=newPresenter) {
                detachPresenter(p);
            }
        }
        presenters.add(newPresenter);
    }

    public void detachPresenter(Model.DataChangeListener thatPresenter) {
        try {
            tryDetachPresenter(thatPresenter);
        } catch (InvalidPresenterStateException e) {
            e.printStackTrace();
            // log error
        }
    }

    private void tryDetachPresenter(Model.DataChangeListener thatPresenter) throws InvalidPresenterStateException {
        boolean success = presenters.remove(thatPresenter);
        if (!success) {
            throw new InvalidPresenterStateException(thatPresenter.getClass().getName());
        }
    }

    public void setCurrentPerson(String name) throws NoPersonDataException {
        if (dataLoaded) {
            currentPersonName = name;
        } else {
            throw new NoPersonDataException(AppStrings.NO_DATA);
        }
    }

    public void refreshData() {
        repo.fetch(this);
    }

    public String[] getAllNames() throws NoPersonDataException {
        if (!dataLoaded) {
            throw new NoPersonDataException(AppStrings.NOT_LOADED);
        }
        ArrayList<String> result = new ArrayList<>();
        for (Person person : allPeople) {
            result.add(person.getName());
        }
        return Arrays.copyOf(result.toArray(),result.size(),String[].class);
    }

    public ArrayList<String> getFieldLabels() {
        return new ArrayList<>(Arrays.asList(Person.PROPERTIES));
    }

    public ArrayList<String> getFieldsFromObject() throws NoPersonDataException {
        if (!dataLoaded) {
            throw new NoPersonDataException(AppStrings.NOT_LOADED);
        }
        for (Person person : allPeople) {
            if (person.getName().equalsIgnoreCase(currentPersonName)) {
                return person.getProperties();
            }
        }
        throw new NoPersonDataException(AppStrings.NOT_FOUND);
    }

    private void initViewUpdate(final String msg) {
        // Post event to 'windowmainloop' for each attached presenter
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                for (Model.DataChangeListener p:presenters) {
                    p.updateView(msg);
                }
            }
        });
    }

    // Repo.Listener interface

    public void onFailure(Throwable t) {
        dataLoaded = false;
        String err;
        if (t instanceof UnknownHostException) {
            err = AppStrings.NO_NETWORK;
        } else {
            err = AppStrings.NETWORK_ERROR;
        }
        t.printStackTrace();
        try {
            TimeUnit.SECONDS.sleep(1); // just to make sure you see the spinner
        } catch (InterruptedException e) {
            // do nothing?
        }
        initViewUpdate(err);
    }

    public void onSuccess(String data) {
        // Cache and extract results
        String res = AppStrings.SUCCESS;
        try {
            allPeople = repo.extractPeople(data);
            dataLoaded = true;
        } catch (NoPersonDataException e) {
            res = e.getMessage();
        }
        initViewUpdate(res);
    }

    public interface DataChangeListener {
        void updateView(String message);
    }
}

