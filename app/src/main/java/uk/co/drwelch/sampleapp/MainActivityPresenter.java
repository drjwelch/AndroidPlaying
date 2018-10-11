package uk.co.drwelch.sampleapp;

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
        view.setFieldValues(getFieldsFromObject(model.getCurrentPerson()));
    }

    private HashMap<String,String> getFieldsFromObject(Person person) {

        HashMap<String,String> viewData = new HashMap<String,String>();

        viewData.put("name",person.getName());
        viewData.put("mass",person.getMassFormatted());
        viewData.put("height",person.getHeightFormatted());
        viewData.put("created",person.getCreatedAtFormatted());

        return viewData;
    }

    public String getFieldKey(int i) {
        return model.getFieldKey(i);
    }

    public interface View {
        void setFieldValues(HashMap<String,String> viewData);
        void showSpinner();
        void hideSpinner();
        void showErrorText();
        void hideErrorText();
        void hideSoftKeyboard();
        String getEntryValue();
    }
}