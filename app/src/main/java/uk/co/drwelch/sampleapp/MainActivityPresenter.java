package uk.co.drwelch.sampleapp;

public class MainActivityPresenter implements Model.DataChangeListener {

    private Model model;
    private View view;
    private boolean errorState;

    public MainActivityPresenter() {
        this.model = Model.getInstance();
        model.attachPresenter(this);
        this.errorState = false;
    }

    public String getOutgoingExtraKey() {
        return AppStrings.PERSONID;
    }

    public void attachView(View view) {
        this.view = view;
        refreshAndUpdate();
    }

    public void refreshAndUpdate() {
        model.refreshData();
//        view.setData(model.getAllNames());
//        view.setFieldLabels(model.getFieldLabels());
        updateView(AppStrings.SUCCESS);
    }

    public void detachView(boolean isComingBack) {
        this.view = null;
        if (!isComingBack) {
            model.detachPresenter(this);
        }
    }

    public void itemClicked(String value) {
        if(!errorState) {
            view.startDetailViewWith(value);
        }
    }
//        Toast.makeText(ctx, value, Toast.LENGTH_LONG).show();

//        String value = view.getItemValue();
//        if (!value.isEmpty()) {
//            view.hideErrorText();
//            view.showSpinner();
//            model.refreshData(value);
//        } else {
//            view.showErrorText();
//        }

    public void updateView(String message) {
//        view.hideSpinner();
        view.hideRetry();
        if (message.equals(AppStrings.SUCCESS)) {
            try {
                view.setData(model.getAllNames());
                errorState = false;
            } catch (NoPersonDataException e) {
                view.setData(new String[]{AppStrings.NO_DATA});
                errorState = true;
                view.showRetry();
            }
//        view.setFieldValues(model.getFieldsFromObject());
        } else {
            view.setData(new String[] {message});
            errorState = true;
            view.showRetry();
        }
    }

    public interface View {
        void setData(String[] data);
        void startDetailViewWith(String value);
        void showRetry();
        void hideRetry();
    }
}
