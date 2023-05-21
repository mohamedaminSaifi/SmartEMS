package com.example.newone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView user_id =(TextView) findViewById(R.id.login_id);
        TextView user_password =(TextView) findViewById(R.id.login_password);
        Button login_button =  findViewById(R.id.login_button);
        //1234 admin

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user_id.getText().toString().equals("1234")&& user_password.getText().toString().equals("admin")){
                    //correct
                    changeActivity();
                }else
                    Toast.makeText(MainActivity.this,"Login Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //changini the activity and layout to the next page
    private void changeActivity(){
        Intent intent = new Intent(this , MainActivity2.class);
        startActivity(intent);
    }
}