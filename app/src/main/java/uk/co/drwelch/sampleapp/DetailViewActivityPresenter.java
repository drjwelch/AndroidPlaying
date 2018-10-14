package uk.co.drwelch.sampleapp;

import java.util.ArrayList;

public class DetailViewActivityPresenter implements Model.DataChangeListener {

    private Model model;
    private View view;

    public void handleIncomingChoice(String name) {
        setCurrentPerson(name);
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
        updateView(AppStrings.SUCCESS);
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
            model.refreshData();
            try {
                model.setCurrentPerson(value);
            } catch (NoPersonDataException e) {
                view.setErrorMessage(e.getMessage());
                view.showErrorText();
            }
        } else { // empty field
            view.setErrorMessage(AppStrings.NONE_SELECTED);
            view.showErrorText();
        }
}

    private void setCurrentPerson(String value) {
        view.setEntryValue(value);
    }

    private ArrayList<String> setEmpty() {
        ArrayList<String> fields;
        fields = new ArrayList<>();
        for (int i=0;i<model.getFieldLabels().size();i++) {
            fields.add("");
        }
        return fields;
    }

    public void updateView(String message) {
        view.hideSpinner();
        view.hideErrorText();
        ArrayList<String> fields;
        if (message.equals(AppStrings.SUCCESS)) {
            try {
                fields = model.getFieldsFromObject();
            } catch (NoPersonDataException e) {
                fields = setEmpty();
                view.setErrorMessage(e.getMessage());
                view.showErrorText();
            }
            view.setFieldValues(fields);
        } else {
            fields = setEmpty();
            view.setErrorMessage(message);
            view.setFieldValues(fields);
            view.showErrorText();
        }
    }

    public interface View {
        void setFieldValues(ArrayList<String> values);
        void setFieldLabels(ArrayList<String> labels);
        void showSpinner();
        void hideSpinner();
        void showErrorText();
        void hideErrorText();
        void setErrorMessage(String err);
        void hideSoftKeyboard();
        String getEntryValue();
        void setEntryValue(String value);
    }
}