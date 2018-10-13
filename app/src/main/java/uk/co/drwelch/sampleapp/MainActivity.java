package uk.co.drwelch.sampleapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainActivityPresenter.View {

    private MainActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        attachPresenter();
        // see what user clicked on
        Intent intent = getIntent();
        if (intent != null) {
            String personID = intent.getStringExtra(PersonListActivity.PERSONID);
            if (personID != null) {
                presenter.setPersonID(personID);
            }
        }
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

    private void attachPresenter() {
        presenter = (MainActivityPresenter) getLastCustomNonConfigurationInstance();
        if (presenter == null) {
            presenter = new MainActivityPresenter();
        }
        presenter.attachView(this);
    }

    public void fetchButtonClicked(View view) {

        Intent intent = new Intent(this, PersonListActivity.class);
        startActivity(intent);

        //        presenter.fetchClicked();
    }

    // MainActivityPresenter.View interface

    public String getEntryValue() {
        TextView myInput = findViewById(R.id.inputField);
        return myInput.getText().toString();
    }

    public void setEntryValue(String value) {
        TextView myInput = findViewById(R.id.inputField);
        myInput.setText(value);
    }

    public void setFieldLabels(ArrayList<String> labels) {
        ((TextView) findViewById(R.id.field0Label)).setText(labels.get(0));
        ((TextView) findViewById(R.id.field1Label)).setText(labels.get(1));
        ((TextView) findViewById(R.id.field2Label)).setText(labels.get(2));
        ((TextView) findViewById(R.id.field3Label)).setText(labels.get(3));
    }

    public void setFieldValues(ArrayList<String> values) {
        ((TextView) findViewById(R.id.field0TextView)).setText(values.get(0));
        ((TextView) findViewById(R.id.field1TextView)).setText(values.get(1));
        ((TextView) findViewById(R.id.field2TextView)).setText(values.get(2));
        ((TextView) findViewById(R.id.field3TextView)).setText(values.get(3));
    }

    public void showSpinner() {
        ProgressBar spinner = findViewById(R.id.spinner);
        spinner.setVisibility(View.VISIBLE);
    }

    public void hideSpinner() {
        ProgressBar spinner = findViewById(R.id.spinner);
        spinner.setVisibility(View.GONE);
    }

    public void showErrorText() {
        TextView errorText = findViewById(R.id.errorLabel);
        errorText.setVisibility(View.VISIBLE);
    }

    public void hideErrorText() {
        TextView errorText = findViewById(R.id.errorLabel);
        errorText.setVisibility(View.GONE);
    }

    public void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        try {
            if (imm.isAcceptingText()) { // verify if the soft keyboard is open
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        } catch (NullPointerException e) {
            // wasn't open or focused - meh
        }
    }
}

