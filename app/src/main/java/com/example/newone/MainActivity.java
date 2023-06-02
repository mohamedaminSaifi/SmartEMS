package com.example.newone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static String Posturl ="http://192.168.1.106:8080/login";
    private static final String TAG = "MAIN2_TAG";
    private String res;
    OkHttpClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        client = new OkHttpClient();
        TextView user_id =(TextView) findViewById(R.id.login_id);
        TextView user_password =(TextView) findViewById(R.id.login_password);
        Button login_button =  findViewById(R.id.login_button);
        //1234 admin

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id=  user_id.getText().toString();
                String password = user_password.getText().toString();
                login(id,password);
            }
        });
    }
    //changini the activity and layout to the next page
    private void changeActivity(){
        Intent intent = new Intent(this , MainActivity2.class);
        startActivity(intent);
    }

    private void login(String id,String password){
        RequestBody requestBody = new FormBody.Builder()
                .add("id",id).add("password",password).build();
        Request request = new Request.Builder().url(Posturl).post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(MainActivity.this,"checking",Toast.LENGTH_SHORT).show();

                        try {
                            res = response.body().string();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        Log.d(TAG, "run: res : "+res);
                        if (res.equals("true")){
                            Log.d(TAG, "if: res=treu + res= "+res);
                            changeActivity();
                        }else if(res.equals("false")){
                            Toast.makeText(MainActivity.this,"wrong password",Toast.LENGTH_SHORT).show();
                        }else {
                            res=null;
                            Toast.makeText(MainActivity.this,"try again",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }












}