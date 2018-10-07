package uk.co.drwelch.sampleapp;

import java.util.ArrayList;

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

    public void refreshedData(ArrayList<String> message) {
        view.setNameLabel(message.get(0));
        view.setMassLabel(message.get(1));
    }

    public View getView() {
        return view;
    }

    public interface View {
        void setNameLabel(String msg);
        void setMassLabel(String msg);
        String getEntryValue();
    }
}