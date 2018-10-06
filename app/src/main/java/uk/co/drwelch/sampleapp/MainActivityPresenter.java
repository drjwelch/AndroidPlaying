package uk.co.drwelch.sampleapp;

import android.view.View;

public class MainActivityPresenter {

    private Model model;
    private View view;

    public MainActivityPresenter(View view) {
        this.model = new Model(this);
        this.view = view;
    }

    public void fetchClicked() {
        model.refreshData(view.getEntryValue());
    }

    public void refreshedData(String message) {
        view.setInputLabel(message);
    }

    public View getView() {
        return view;
    }

    public interface View {
        public void setInputLabel(String msg);
        public String getEntryValue();
    }
}