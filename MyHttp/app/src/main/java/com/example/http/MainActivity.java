package com.example.http;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editTextTextPersonName);
        textView = findViewById(R.id.textView3);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String url = editText.getText().toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        request(url);
                   }
                }).start();
            }
        });

    }


    public void request(String urlstr){
        try {

            StringBuilder builder = new StringBuilder();

            URL url = new URL(urlstr);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if(connection !=null){
                connection.setConnectTimeout(10000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);

                int resCode = connection.getResponseCode();

                BufferedReader reader= new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line = null;

                while (true){
                    line = reader.readLine();
                    if(line ==null) {
                        break;
                    }

                    builder.append(line+"\n");
                }

                reader.close();
                connection.disconnect();
            }

            println("응답 :" + builder.toString());

        }catch (Exception e){

        }
    }

    public  void println(final String data){
        handler.post(new Runnable() {
            @Override
            public void run() {
                textView.append(data + "\n");
            }
        });
    }
}