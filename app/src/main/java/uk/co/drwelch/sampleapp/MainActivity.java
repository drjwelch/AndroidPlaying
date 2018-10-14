package uk.co.drwelch.sampleapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements MainActivityPresenter.View {

    private MainActivityPresenter presenter;
    private RecyclerView recycler;
    private boolean isComingBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createRecyclerView();

        attachPresenter();
        isComingBack = false;
    }

    @Override
    protected void onDestroy() {
        presenter.detachView(this.isComingBack);
        super.onDestroy();
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        this.isComingBack = true;
        return presenter;
    }

    private void attachPresenter() {
        presenter = (MainActivityPresenter) getLastCustomNonConfigurationInstance();
        if (presenter == null) {
            presenter = new MainActivityPresenter();
        }
        presenter.attachView(this);
    }

    private void createRecyclerView() {
        // recycler view itself
        recycler = findViewById(R.id.personListRecycler);
        recycler.setHasFixedSize(true); // gives a performance improvement
        // recycler view's layout manager
        recycler.setLayoutManager(new LinearLayoutManager(this));
    }

    public void retryButtonClicked(View view) {
        presenter.refreshAndUpdate();
    }


    // MainActivityPresenter.View interface

    public void setData(String[] data) {
        // recycler view's data adapter
        RecyclerView.Adapter recyclerListAdapter = new MainActivityRecyclerListAdapter(data, new MainActivityRecyclerListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String item) { // click handler for items in the layout
                presenter.itemClicked(item);
            }
        });
        recycler.setAdapter(recyclerListAdapter);
    }

    public void startDetailViewWith(String value) {
        Intent intent = new Intent(this, DetailViewActivity.class);
        intent.putExtra(presenter.getOutgoingExtraKey(), value);
        startActivity(intent);
    }

    public void showRetry() {
        Button retry = findViewById(R.id.retryButton);
        retry.setVisibility(View.VISIBLE);
    }

    public void hideRetry() {
        Button retry = findViewById(R.id.retryButton);
        retry.setVisibility(View.GONE);
    }

}


