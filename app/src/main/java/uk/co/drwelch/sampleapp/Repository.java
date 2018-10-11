package uk.co.drwelch.sampleapp;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class Repository {

    private static Repository myInstance = null;
    private static OkHttpClient myClient = null;
    private static final String rootURL = "https://swapi.co/api/people/";

    private Repository() { // private constructor for singleton
        myClient = new OkHttpClient();
    }

    public static synchronized Repository getInstance() {  // sync to prevent re-entrant
        if (myInstance == null) {
            myInstance = new Repository();
        }
        return myInstance;
    }

    public void fetch(String command, final Repository.RepoListener modelListener) {
        String url = Repository.rootURL + command;
        Request request = new Request.Builder()
                .url(url)
                .build();
        Callback myCallback = new Callback() {
            @Override
            public void onFailure(final Call call, IOException e) {
                call.cancel();
                modelListener.onFailure(e);
            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                modelListener.onSuccess(response.body().string());
            }
        };
        myClient.newCall(request).enqueue(myCallback);
    }

    public interface RepoListener {
        void onSuccess(String data);
        void onFailure(Throwable t);
    }
}
