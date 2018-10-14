package uk.co.drwelch.sampleapp;

import java.util.ArrayList;

public class DetailViewActivityPresenter implements Model.Presenter {

    private Model model;
    private View view;

    public void handleIncomingChoice(String name) {
        setPersonIDfromName(name);
        fetchClicked();
    }

    public String getIncomingExtraKey() {
        return AppStrings.PERSONID;
    }

    public DetailViewActivityPresenter() {
        this.model = Model.getInstance();
        model.attachPresenter(this);
    }

    public void attachView(View view) {
        this.view = view;
        view.setFieldLabels(model.getFieldLabels());
        updateView();
    }

    public void detachView(boolean isComingBack) {
        this.view = null;
        if (!isComingBack) { // remove references to this presenter so it dies
            model.detachPresenter(this);
        }
    }

    public void fetchClicked() {
        view.hideSoftKeyboard();
        String value = view.getEntryValue();
        if (!value.isEmpty()) {
            view.hideErrorText();
            view.showSpinner();
            model.refreshData(value);
        } else {
            view.showErrorText();
        }
        try {
            model.setCurrentPerson(value);
        } catch (NoPersonDataException e) {
            view.showErrorText();
        }
    }

    private void setPersonIDfromName(String value) {
        view.setEntryValue(value);
    }

    private ArrayList<String> setTopFieldOnly(String error) {
        ArrayList<String> fields;
        fields = new ArrayList<>();
        fields.add(error);
        for (int i=1;i<model.getFieldLabels().size();i++) {
            fields.add("");
        }
        return fields;
    }

    public void onModelError(String message) {
        view.hideSpinner();
        ArrayList<String> fields = setTopFieldOnly(message);
        view.setFieldValues(fields);
    }

    public void updateView() {
        view.hideSpinner();
        ArrayList<String> fields;
        try {
            fields = model.getFieldsFromObject();
        } catch (NoPersonDataException e) {
            fields = setTopFieldOnly(e.getMessage());
        }
        view.setFieldValues(fields);
    }

    public interface View {
        void setFieldValues(ArrayList<String> values);
        void setFieldLabels(ArrayList<String> labels);
        void showSpinner();
        void hideSpinner();
        void showErrorText();
        void hideErrorText();
        void hideSoftKeyboard();
        String getEntryValue();
        void setEntryValue(String value);
    }
}