package uk.co.drwelch.sampleapp;

public class PersonListModel {

    private PersonListActivityPresenter presenter;
    private String[] data;

    public PersonListModel(PersonListActivityPresenter presenter) {
        this.presenter = presenter;
        this.data = new String[] {"Bill", "Ben", "Bob", "Bev", "Deb", "Dib", "Dob", "Dub", "Rab", "Reb", "Rib", "Rub", "Rob"};
    }

    public String[] refreshData(String URL) {
        // nothing it's a static array right now
        return this.data;
    }

}
