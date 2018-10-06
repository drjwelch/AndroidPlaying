package uk.co.drwelch.sampleapp;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Model {

    private String message;
    private MainActivityPresenter myPresenter;

    public Model(MainActivityPresenter presenter) {

        this.message = "Donkey";
        this.myPresenter = presenter;
    }

    public void refreshData(String userData) {

//        Response response = null;
//        String jsonData = "{\"Name\":\"Plonkey\"}";

        String url = "https://swapi.co/api/people/"+userData;
        OkHttpClient client = new OkHttpClient();   // singleton better?
         Request request = new Request.Builder()
            .url(url)
            .build();
         client.newCall(request)
              .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                            call.cancel();
                        }
                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        String jsonData = response.body().string();
                        try {
                            JSONObject Jobject = new JSONObject(jsonData);
                            Model.this.message = Jobject.getString("name");
                        } catch (JSONException e) {
                            if (e.getMessage().contains("No value for name")) {
                                Model.this.message = "Too big";
                            }
                            e.printStackTrace();
                        }
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                myPresenter.refreshedData(Model.this.message);
                            }
                        });
                    }
              });
    }
}
