package uk.co.drwelch.sampleapp;

public class MainActivityPresenter {

    private Model model;
    private View view;

    public MainActivityPresenter(View view) {
        this.model = new Model(this);
        this.view = view;
    }

    public void fetchClicked() {
        String value = view.getEntryValue();
        if (!value.isEmpty()) {
            model.refreshData(value);
        } else {
            // error feedback on screen
        }
    }

    public void showData(Person person) {
        view.setNameLabel(person.getName());

        String mass = person.getMassAsString();
        try {
            int mass_kg = Integer.parseInt(mass);
            mass = mass + " kg";
        } catch (NumberFormatException e) {
            // mass is text
        }
        view.setMassLabel(mass);

        String height = person.getHeightAsString();
        try {
            float height_m = (float) Integer.parseInt(height) / 100;
            height = Float.toString(height_m) + " m";
        } catch (NumberFormatException  e) {
            // height is text
        }
        view.setHeightLabel(height);

        String date = person.getCreatedAtDate();
        String displayDate;
        if (date.length()<10) {
            displayDate = "Unknown";
        } else {
            displayDate = date.substring(8,10) + "/" + date.substring(5,7) + "/" + date.substring(0,4);
        }
        view.setCreatedAtLabel(displayDate);
    }

    public View getView() {
        return view;
    }

    public interface View {
        void setNameLabel(String msg);
        void setMassLabel(String msg);
        void setHeightLabel(String msg);
        void setCreatedAtLabel(String msg);
        String getEntryValue();
    }
}