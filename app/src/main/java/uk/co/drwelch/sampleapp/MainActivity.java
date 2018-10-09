package uk.co.drwelch.sampleapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements MainActivityPresenter.View {

    private MainActivityPresenter presenter;
    private HashMap<String, Integer> resourceIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resourceIDs = new HashMap<String, Integer>();
        resourceIDs.put("name", R.id.nameTextView);
        resourceIDs.put("mass", R.id.massTextView);
        resourceIDs.put("height", R.id.heightTextView);
        resourceIDs.put("created", R.id.createdAtTextView);
        attachPresenter();
    }

    private void attachPresenter() {
        presenter = (MainActivityPresenter) getLastCustomNonConfigurationInstance();
        if (presenter == null) {
            presenter = new MainActivityPresenter();
        }
        presenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return presenter;
    }

    public void fetchButtonClicked(View view) {
        presenter.fetchClicked();
    }

    // MainActivityPresenter.View interface

    public String getEntryValue() {
        TextView myInput = findViewById(R.id.editText);
        return myInput.getText().toString();
    }

    public void setLabels(HashMap<String, String> viewData) {
        for (HashMap.Entry<String, Integer> entry : resourceIDs.entrySet()) {
            TextView mylabel = findViewById(entry.getValue());
            mylabel.setText(viewData.get(entry.getKey()));
        }
    }

    public void showSpinner() {
        ProgressBar spinner = findViewById(R.id.spinner);
        spinner.setVisibility(View.VISIBLE);
    }

    public void hideSpinner() {
        ProgressBar spinner = findViewById(R.id.spinner);
        spinner.setVisibility(View.GONE);
    }

}

