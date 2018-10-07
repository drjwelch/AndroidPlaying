package uk.co.drwelch.sampleapp;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Model {

    private String message;
    private MainActivityPresenter myPresenter;
    private Repository myRepo;

    public Model(MainActivityPresenter presenter) {
        message = "Error";
        myPresenter = presenter;
        myRepo = Repository.getInstance();
    }

    public void refreshData(String command) {

        Callback myCallback = new Callback() {
            @Override
            public void onFailure(final Call call, IOException e) {
                call.cancel();
            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // Extract results
                parseResponse(response.body().string());
                // Post event to 'windowmainloop'
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        myPresenter.refreshedData(Model.this.message);
                    }
                });
            }
        };
        myRepo.fetch(command, myCallback);
    }

    private void parseResponse(String data) {
        try {
            JSONObject Jobject = new JSONObject(data);
            message = Jobject.getString("name");
        } catch (JSONException e) {
            if (e.getMessage().contains("No value for name")) {
                message = "Too big";
            }
            e.printStackTrace();
        }
    }

}
