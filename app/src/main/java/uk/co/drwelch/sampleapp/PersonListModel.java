package uk.co.drwelch.sampleapp;

import android.os.Handler;
import android.os.Looper;

import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

public class PersonListModel implements Repository.RepoListener {

    private PersonListActivityPresenter presenter;
    private String[] data;
    private Repository myRepo;

    public PersonListModel(PersonListActivityPresenter presenter) {
        this.presenter = presenter;
        myRepo = Repository.getInstance();
        this.data = new String[] {"Waiting for server"};
    }

    public void refreshData(String URL) {
        myRepo.fetch("", this);
        // nothing it's a static array right now
        //return this.data;
    }

    public String[] getData() {
        return this.data;
    }

    private void initViewUpdate() {
        // Post event to 'windowmainloop'
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                presenter.updateView();
            }
        });
    }

    // Repo.Listener interface

    public void onFailure(Throwable t) {
        if (t instanceof UnknownHostException) {
            // internet is off
            this.data = new String[] {"No network"};
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
        this.data = myRepo.extractPeople(data);
        initViewUpdate();
    }

}
