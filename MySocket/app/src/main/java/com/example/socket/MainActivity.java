package com.example.socket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    EditText input1;
    TextView output1;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input1 = findViewById(R.id.input1);
        output1= findViewById(R.id.output1);

        Button sendButton = findViewById(R.id.sendbutton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String data = input1.getText().toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        send(data);
                    }
                });

                //네트워크는 1.인터넷 권한설정 2.스레드사용(핸들러)
            }
        });

        Button button2 = findViewById(R.id.startServerButton);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        startServer();
                    }
                }).start();

            }
        });

    }


    //클라이언트에서 보내는 함수
    public void  send(String data){

        int port = 5001;

        try {
            Socket sock = new Socket("localhost",port);

            //sock.getOutputStream()-> 보내는 통로, new ObjectOutputStream(sock.getOutputStream())-> 통로에 연결된 보내는 객체를 생성
            ObjectOutputStream outputStream = new ObjectOutputStream(sock.getOutputStream());
            outputStream.writeObject(data);
            outputStream.flush();

            //sock.getInputStream()->받는 통로, new ObjectInputStream(sock.getInputStream())받는 통로에 연결된 받는 객체를생성
            ObjectInputStream objectInputStream = new ObjectInputStream(sock.getInputStream());
            //readObject()-> 객체를 받는 함수.
            String input = String.valueOf(objectInputStream.readObject());
            sock.close();

        } catch (IOException e) {
            e.printStackTrace();
    }


    // 요청을 받아서 서버를 시작
    public void startServer(){
        int port =5001;

        try {
            ServerSocket socket  = new ServerSocket(port);

            //클라이언트 요청에 대한 처리
            while (true){
                //클라이언트의 요청을 받는다
                Socket sock = socket.accept();
                InetAddress clientHost = sock.getLocalAddress();
                int clientPort = sock.getPort();

                println("클라이언트 연결됨: " + clientHost + ", " +clientPort);


                ObjectInputStream inputStream = new ObjectInputStream(sock.getInputStream());
                String input = (String) inputStream.readObject();
                println("데이터 받음: " + input);

                ObjectOutputStream outputStream = new ObjectOutputStream(sock.getOutputStream());
                outputStream.writeObject(input + "from Server.");
                outputStream.flush();
                println("데이터 보냄");

                sock.close();

            }

        } catch (IOException e) {
            e.printStackTrace();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    public  void println(final String data){


        handler.post(new Runnable() {
            @Override
            public void run() {
                output1.append((data + "\n"));
            }
        });

    }

}