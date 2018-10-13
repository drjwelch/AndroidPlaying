package uk.co.drwelch.sampleapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class Repository {

    private static Repository myInstance = null;
    private static OkHttpClient myClient = null;
    private static final String rootURL = "https://swapi.co/api/people/";
    private static final String[] KEYS = {"name","height","mass","created"};

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

    public Person deserialise(String data) {

        Person currentPerson;

        try {
            JSONObject Jobject = new JSONObject(data);

            // responses not guaranteed to be those types so store as strings
            // handle conversion in presenter - it's business logic

            currentPerson = new Person(Jobject.getString(KEYS[0]),
                    Jobject.getString(KEYS[1]),
                    Jobject.getString(KEYS[2]),
                    Jobject.getString(KEYS[3]));
        } catch (JSONException e) {
            if (e.getMessage().contains("No value for name")) { // proper test?
                // get strings from resources is hacky if not got a Context ... hmmm
                currentPerson = new Person("Not found", "", "","");
            } else {
                currentPerson = new Person("Error", "", "","");
                // log error
            }
            e.printStackTrace();
        }
        return currentPerson;
    }

    public String[] extractPeople(String data) {

        ArrayList<String> result = new ArrayList<>();

        try {
            JSONObject Jobject = new JSONObject(data);
            JSONArray peoplelist = Jobject.getJSONArray("results");
            for (int i=0; i<peoplelist.length(); i++) {
                result.add(peoplelist.getJSONObject(i).getString("name"));
            }
        } catch (JSONException e) {
                e.printStackTrace();
        }
        // bonkers mechanism to cast from ArrayList<String> to String[]
        return Arrays.copyOf(result.toArray(),result.size(),String[].class);
    }

    public interface RepoListener {
        void onSuccess(String data);
        void onFailure(Throwable t);
    }
}
