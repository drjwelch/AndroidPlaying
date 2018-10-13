package uk.co.drwelch.sampleapp;

import android.widget.Toast;

public class PersonListActivityPresenter {

    private PersonListModel model;
    private View view;

    public PersonListActivityPresenter() {
        this.model = new PersonListModel(this);
    }

    public void attachView(View view) {
        this.view = view;
        model.refreshData("nothing");
        view.setData(model.getData());
//        view.setFieldLabels(model.getFieldLabels());
        updateView();
    }

    public void detachView() {
        this.view = null;
    }

    public void itemClicked(android.content.Context ctx, String value) {
//        String value = view.getItemValue();
        Toast.makeText(ctx, value, Toast.LENGTH_LONG).show();
//        if (!value.isEmpty()) {
//            view.hideErrorText();
//            view.showSpinner();
//            model.refreshData(value);
//        } else {
//            view.showErrorText();
//        }
    }

    public void updateView() {
//        view.hideSpinner();
        view.setData(model.getData());
//        view.setFieldValues(model.getFieldsFromObject());
    }

    public interface View {
        void setData(String[] data);
    }
}
