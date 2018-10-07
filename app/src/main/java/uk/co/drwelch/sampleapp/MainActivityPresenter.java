package uk.co.drwelch.sampleapp;

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
        void setInputLabel(String msg);
        String getEntryValue();
    }
}