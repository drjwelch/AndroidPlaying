package uk.co.drwelch.sampleapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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
    private static final String MASTER_KEY = "results";

    private Repository() { // private constructor for singleton
        myClient = new OkHttpClient();
    }

    public static synchronized Repository getInstance() {  // sync to prevent re-entrant
        if (myInstance == null) {
            myInstance = new Repository();
        }
        return myInstance;
    }

    public void fetch(final Repository.RepoListener modelListener) {
        Request request = new Request.Builder()
                .url(Repository.rootURL)
                .build();
        Callback myCallback = new Callback() {
            @Override
            public void onFailure (final Call call, IOException e) {
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

    public Person[] extractPeople(String data) throws NoPersonDataException {

        // Get array of people from response
        ArrayList<Person> result = new ArrayList<>();
        JSONArray personsJArray;

        try {
            personsJArray = new JSONObject(data).getJSONArray(Repository.MASTER_KEY);
        } catch (JSONException e) {
            throw new NoPersonDataException(AppStrings.NO_RESULTS);
        }

        // Parse each element into Person object
        boolean success;
        JSONObject p = null;

        for (int i=0; i<personsJArray.length(); i++) {
            success = true;
            try {
                p = personsJArray.getJSONObject(i);
            } catch (JSONException e) {
                success = false;
                // ignore this person
            }
            // no 'finally' in Java to the dismay of us Pythonistas
            if (success) {
                try {
                    Person currentPerson = new Person(p.getString(KEYS[0]),
                            p.getString(KEYS[1]),
                            p.getString(KEYS[2]),
                            p.getString(KEYS[3]));
                    result.add(currentPerson);
                } catch (JSONException e) {
                    // log error
                    throw new NoPersonDataException(AppStrings.RESPONSE_ERROR);
                }
            }
        }
        // bonkers mechanism to cast from ArrayList<Person> to Person[]
        return Arrays.copyOf(result.toArray(),result.size(),Person[].class);
    }

    public interface RepoListener {
        void onSuccess(String data);
        void onFailure(Throwable t);
    }
}
