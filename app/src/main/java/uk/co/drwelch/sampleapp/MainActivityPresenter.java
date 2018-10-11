package uk.co.drwelch.sampleapp;

import java.util.ArrayList;

public class MainActivityPresenter {

    private Model model;
    private View view;

    public MainActivityPresenter() {
        this.model = new Model(this);
    }

    public void attachView(View view) {
        this.view = view;
        view.setFieldLabels(model.getFieldLabels());
        updateView();
    }

    public void detachView() {
        this.view = null;
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
    }

    public void updateView() {
        view.hideSpinner();
        view.setFieldValues(model.getFieldsFromObject());
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
    }
}