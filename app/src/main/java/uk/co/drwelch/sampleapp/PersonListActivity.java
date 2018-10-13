package uk.co.drwelch.sampleapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PersonListActivity extends AppCompatActivity implements PersonListActivityPresenter.View {

    public static final String PERSONID = "com.example.myfirstapp.PERSONID";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private PersonListActivityPresenter presenter;
    private String[] myDataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_list);
        createRecyclerView();
        attachPresenter();
    }

    private void createRecyclerView() {
        // recycler view itself
        mRecyclerView = findViewById(R.id.personListRecycler);
        mRecyclerView.setHasFixedSize(true); // gives a performance improvement
        // recycler view's layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
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
        presenter = (PersonListActivityPresenter) getLastCustomNonConfigurationInstance();
        if (presenter == null) {
            presenter = new PersonListActivityPresenter();
        }
        presenter.attachView(this);
    }

    // PersonListActivityPresenter.View interface

    public void setData(String[] data) { // TODO change to Person[]
        myDataset = data;
        // recycler view's data adapter
        mAdapter = new PersonListAdapter(myDataset, new PersonListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String item) { // click handler for items in the layout
                presenter.itemClicked(item); // TODO remove context - needed only for toast
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    public void startDetailViewWith(String value) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(PERSONID, value);
        startActivity(intent);
    }
}


