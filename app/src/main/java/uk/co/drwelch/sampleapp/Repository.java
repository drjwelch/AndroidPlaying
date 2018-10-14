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

//    public Person deserialise(String data) {
//
//        Person currentPerson;
//
//        try {
//            JSONObject Jobject = new JSONObject(data);
//
//            // responses not guaranteed to be those types so store as strings
//            // handle conversion in presenter - it's business logic
//
//            String[] temp = Jobject.getString("url").split("/");
//            String personID = temp[temp.length - 1];
//
//            currentPerson = new Person(Jobject.getString(KEYS[0]),
//                    personID,
//                    Jobject.getString(KEYS[1]),
//                    Jobject.getString(KEYS[2]),
//                    Jobject.getString(KEYS[3]));
//        } catch (JSONException e) {
//            if (e.getMessage().contains("No value for name")) { // proper test?
//                // get strings from resources - but is hacky if not got a Context ... hmmm
//                currentPerson = new Person("Not found", "","", "","");
//            } else {
//                currentPerson = new Person("Error", "","", "","");
//                // log error
//            }
//            e.printStackTrace();
//        }
//        return currentPerson;
//    }

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
//        String personID;
        String[] temp;
        JSONObject p = null;

        for (int i=0; i<personsJArray.length(); i++) {
            success = true;
//            personID = "";
            try {
                p = personsJArray.getJSONObject(i);
//                temp = p.getString("url").split("/");
//                personID = temp[temp.length - 1];
            } catch (JSONException e) {
                success = false;
                // ignore this person if ANY PROBLEM no ID
            }
            // no 'finally' in Java to the dismay of us Pythonistas
            if (success) {
                try {
                    Person currentPerson = new Person(p.getString(KEYS[0]),
//                            personID,
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
