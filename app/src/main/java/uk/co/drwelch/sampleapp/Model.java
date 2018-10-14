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
    private ArrayList<Model.Presenter> presenters = new ArrayList<>();
    private Repository repo;
    private String errorMessage;
    private static final ArrayList<String> fieldKeys = new ArrayList<>();
    private static final int INVALID = -1;
    private static final int DEFAULT_PERSON = 0;
    private static Model myInstance = null;

    private Model() {
        repo = Repository.getInstance();
        dataLoaded = false;
    }

//    private void invalidateModelData(String message) {
//        currentPerson = Model.INVALID;
//        errorMessage = message;
//    }
//
//    private void validateModelData() {
//        currentPerson = Model.DEFAULT_PERSON;
//        errorMessage = "";
//    }
//
    public static synchronized Model getInstance() {  // sync to prevent re-entrant
        if (myInstance == null) {
            myInstance = new Model();
        }
        return myInstance;
    }

    public void attachPresenter(Model.Presenter newPresenter) {
        // if user navigates back, a new presenter is created while the old one persists
        // so detach the old one (TODO and destroy its object)
        for (Model.Presenter p:presenters) {
            if (p.getClass() == newPresenter.getClass() && p!=newPresenter) {
                detachPresenter(p);
            }
        }
        presenters.add(newPresenter);
    }

    public void detachPresenter(Model.Presenter thatPresenter) {
        presenters.remove(thatPresenter);
    }

//    public String getErrorMessage() {
//        return errorMessage;
//    }
//
    public void setCurrentPerson(String name) throws NoPersonDataException {
        if (dataLoaded) {
            currentPersonName = name;
        } else {
            throw new NoPersonDataException(AppStrings.NO_DATA);
        }
    }

    // TODO Keeping command for now ...
    public void refreshData(String command) {
        repo.fetch(this);
    }

//    public String findPersonByName(String name) throws NoPersonDataException {
//        if (currentPerson == Model.INVALID) {
//            throw new NoPersonDataException(AppStrings.NOT_LOADED);
//        }
//        for (Person person : allPeople) {
//            if (person.getName().equals(name)) {
//                return person.getID();
//            }
//        }
//        throw new NoPersonDataException(AppStrings.NAME_INVALID);
//    }

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
            if (person.getName().equals(currentPersonName)) {
                return person.getProperties();
            }
        }
        throw new NoPersonDataException(AppStrings.NONE_SELECTED);
    }

    private void initViewUpdate() {
        // Post event to 'windowmainloop' for each attached presenter
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                for (Model.Presenter p:presenters) {
                    p.updateView();
                }
            }
        });
    }

    private void sendError(String message) {
        for (Model.Presenter p:presenters) {
            p.onModelError(message);
        }
    }

    // Repo.Listener interface

    public void onFailure(Throwable t) {
        dataLoaded = false;
        if (t instanceof UnknownHostException) {
            sendError(AppStrings.NO_NETWORK);
        } else {
            sendError(AppStrings.NETWORK_ERROR);
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
        try {
            allPeople = repo.extractPeople(data);
            dataLoaded = true;
        } catch (NoPersonDataException e) {
            sendError(e.getMessage());
        }
//        currentPerson = repo.deserialise(data);
        initViewUpdate();
    }

    public interface Presenter {
        void updateView();
        void onModelError(String msg);
    }
}

