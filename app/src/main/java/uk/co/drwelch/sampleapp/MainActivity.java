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

    public String getEntryValue() {
        TextView myInput = (TextView) findViewById(R.id.editText);
        return myInput.getText().toString();
    }

    public void fetchButtonClicked(View view) {
        presenter.fetchClicked();
    }

    public void setInputLabel(String msg) {
        TextView mylabel = (TextView) findViewById(R.id.textView2);
        mylabel.setText(msg);
    }
}

