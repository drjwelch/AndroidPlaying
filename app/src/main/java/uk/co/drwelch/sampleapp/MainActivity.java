package uk.co.drwelch.sampleapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
        attachPresenter();
        resourceIDs = new HashMap<>();
        resourceIDs.put(presenter.getFieldKey(0), R.id.nameTextView);
        resourceIDs.put(presenter.getFieldKey(1), R.id.massTextView);
        resourceIDs.put(presenter.getFieldKey(2), R.id.heightTextView);
        resourceIDs.put(presenter.getFieldKey(3), R.id.createdAtTextView);
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
        presenter.fetchClicked();
    }

    // MainActivityPresenter.View interface

    public String getEntryValue() {
        TextView myInput = findViewById(R.id.editText);
        return myInput.getText().toString();
    }



    public void setLabelValues(HashMap<String, String> viewLabels) {
        for (HashMap.Entry<String, Integer> entry : resourceIDs.entrySet()) {
            TextView mylabel = findViewById(entry.getValue());
            mylabel.setText(viewData.get(entry.getKey()));
        }
    }

    public void setFieldValues(HashMap<String, String> viewData) {
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
            // wasn't open or focused
        }
    }
}

