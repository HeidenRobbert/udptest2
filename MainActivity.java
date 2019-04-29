package com.example.robbertvanderheiden.udptest;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private UDPHandler nt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        EditText display = findViewById(R.id.tvLastRecieved);
        nt = new UDPHandler(display,getApplicationContext(),(EditText)findViewById(R.id.etToSend),this);
        nt.run();

        final Button button = findViewById(R.id.btnSend);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nt.sendRelay();
            }
        });

    }

    public void textSetter(String text){
        EditText display = findViewById(R.id.tvLastRecieved);
        display.setText("" + text);
    }
}
