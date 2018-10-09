package uk.co.drwelch.sampleapp;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivityPresenter {

    private Model model;
    private View view;

    public MainActivityPresenter() {
        this.model = new Model(this);
    }

    public void attachView(View view) {
        this.view = view;
        updateView();
    }

    public void detachView() {
        this.view = null;
    }

    public void fetchClicked() {
        String value = view.getEntryValue();
        if (!value.isEmpty()) {
            view.showSpinner();
            model.refreshData(value);
        } else {
            // error feedback on screen
        }
    }

    // TODO: take formatting elsewhere - but where? Presenter is getting tied to Model

    public void updateView() {
        Person person = model.getCurrentPerson();

        HashMap<String,String> viewData = new HashMap<String,String>();

        viewData.put("name",person.getName());

        String mass = person.getMassAsString();
        try {
            int mass_kg = Integer.parseInt(mass);
            mass = mass + " kg";
        } catch (NumberFormatException e) {
            // mass is text
        }
        viewData.put("mass",mass);

        String height = person.getHeightAsString();
        try {
            float height_m = (float) Integer.parseInt(height) / 100;
            height = Float.toString(height_m) + " m";
        } catch (NumberFormatException  e) {
            // height is text
        }
        viewData.put("height",height);

        String date = person.getCreatedAtDate();
        String displayDate;
        if (date.length()<10) {
            displayDate = "Unknown";
        } else {
            displayDate = date.substring(8,10) + "/" + date.substring(5,7) + "/" + date.substring(0,4)
                        + " at " + date.substring(11,19);
        }
        viewData.put("created",displayDate);

        view.hideSpinner();
        view.setLabels(viewData);
    }

    public interface View {
        void setLabels(HashMap<String,String> viewData);
        void showSpinner();
        void hideSpinner();
        String getEntryValue();
    }
}