package uk.co.drwelch.sampleapp;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

class Repository {

    private static Repository myInstance = null;
    private static OkHttpClient myClient = null;
    private static final String rootURL = "https://swapi.co/api/people/";

    private Repository() { // private constructor for singleton
        myClient = new OkHttpClient();
    }

    public static synchronized Repository getInstance() {
        if (myInstance == null) {
            myInstance = new Repository();
        }
        return myInstance;
    }

    public void fetch(String command, Callback myCallback) {
        String url = Repository.rootURL + command;
        Request request = new Request.Builder()
                .url(url)
                .build();
        myClient.newCall(request)
                .enqueue(myCallback);
    }
}
