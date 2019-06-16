package com.example.ex4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.net.InetAddress;

public class DisplayJoystick extends AppCompatActivity {

    TcpClient tcpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_joystick);
        Intent intent = getIntent();
        String ip = intent.getStringExtra("ip");
        int port = intent.getIntExtra("port", 0);
        this.tcpClient = new TcpClient(ip, port);
        this.tcpClient.run();
    }

    protected void onDestroy() {
        super.onDestroy();
        this.tcpClient.stopClient();
    }

    private void openClient(String ip, int port) {


    }
}
