package uk.co.drwelch.sampleapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements MainActivityPresenter.View {

    private MainActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MainActivityPresenter(this);
    }

    public void fetchButtonClicked(View view) {
        presenter.fetchClicked();
    }

    // MainActivityPresenter.View interface

    public String getEntryValue() {
        TextView myInput = findViewById(R.id.editText);
        return myInput.getText().toString();
    }

    // these should be one method called on an array of textviews

    public void setNameLabel(String msg) {
        TextView mylabel = findViewById(R.id.nameTextView);
        mylabel.setText(msg);
    }

    public void setMassLabel(String msg) {
        TextView mylabel = findViewById(R.id.massTextView);
        mylabel.setText(msg);
    }

    public void setHeightLabel(String msg) {
        TextView mylabel = findViewById(R.id.heightTextView);
        mylabel.setText(msg);
    }

    public void setCreatedAtLabel(String msg) {
        TextView mylabel = findViewById(R.id.createdAtTextView);
        mylabel.setText(msg);
    }
}

