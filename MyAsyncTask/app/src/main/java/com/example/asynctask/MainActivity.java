package com.example.asynctask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;

    int value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar2);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackgroundTask task = new BackgroundTask();
                task.execute();
            }
        });

    }

    class BackgroundTask extends AsyncTask<Integer,Integer,Integer> {

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0].intValue());

        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            while (isCancelled() == false){
                value +=1;
                if(value >=100){
                    break;
                }

                publishProgress(value);
                try {
                    Thread.sleep(1000);
                }catch (Exception e){

                }finally {

                }
            }

            return value;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            value = 0;
            progressBar.setProgress(value);

        }

        @Override
        protected void onPostExecute(Integer integer) {
            progressBar.setProgress(0);
        }


    }

}