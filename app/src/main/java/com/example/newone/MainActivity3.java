package com.example.newone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity3 extends AppCompatActivity {
    private static String Getturl ="http://192.168.1.63:8080/Mobile/checklist";
    //private static String Getturl ="http://192.168.1.106:8080/items";
    ListView listView;
    private static final String TAG = "LIST_TAG";
    OkHttpClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        listView = findViewById(R.id.list_view);
        //creating a new instance of the GetItemsTask class
        new GetItemsTask().execute();
    }
    //GetItemsTask imbeded class to avoid crashing the main thread
    private class GetItemsTask extends AsyncTask<Void, Void, ArrayList<item>> {
        //setting the function to work in the background
        @Override
        protected ArrayList<item> doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(Getturl)
                    .addHeader("Accept-Encoding", "br,deflate,gzip,x-gzip")
                    .build();
            try {
                //putting the response of the website in a Response obj and setting to a be a jsong obj then forming it to a ArrayList of items
                Response response = client.newCall(request).execute();
                Log.d(TAG, "response q: "+response);
                String jsonData = response.body().string();
                Log.d(TAG, "json data: " + jsonData);
                Gson gson = new Gson();
               //filling an arrayList of items with the data retreived from the server
                Type listType = new TypeToken<ArrayList<item>>(){}.getType();
                ArrayList<item> items = gson.fromJson(jsonData, listType);
                return items;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        //sets the view to the items using setAdapter function
        @Override
        protected void onPostExecute(ArrayList<item> items) {
            if (items != null) {
                //setting the view to the ListView with the list of items(adapter)
                ItemAdapter adapter = new ItemAdapter(MainActivity3.this, items);
                listView.setAdapter(adapter);
            }
            //here is thje showing no item message till we get it tested
            else {
                TextView emptyView = findViewById(R.id.empty_view);
                listView.setEmptyView(emptyView);
            }
        }
    }
}







/*
*   @Override
        protected ArrayList<item> doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(Getturl)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String jsonData = response.body().string();
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<item>>(){}.getType();
                ArrayList<item> items = gson.fromJson(jsonData, listType);
                return items;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<item> items) {
            if (items != null) {
                ItemAdapter adapter = new ItemAdapter(MainActivity3.this, items);
                listView.setAdapter(adapter);
            }
        }
    }*/



/* private static String Getturl ="http://192.168.1.106:8080/items";
    ListView listView;
    private static final String TAG = "LIST_TAG";
    OkHttpClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        listView = findViewById(R.id.list_view);
        client = new OkHttpClient();
        ArrayList<item> items;
        try {
            items = getItems();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ItemAdapter adapter = new ItemAdapter(this, items);
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);
    }
    private ArrayList<item> getItems() throws IOException {
         client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Getturl)
                .build();
        Response response = client.newCall(request).execute();
        String jsonData = response.body().string();
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<item>>(){}.getType();
        ArrayList<item> items = gson.fromJson(jsonData, listType);
        return items;
    }*/


