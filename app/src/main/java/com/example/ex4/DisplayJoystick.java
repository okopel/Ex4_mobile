package com.example.ex4;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class DisplayJoystick extends AppCompatActivity implements Joystick.JoystickListener {

    TcpClient tcpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_joystick);
        Intent intent = getIntent();
        String ip = intent.getStringExtra("ip");
        int port = intent.getIntExtra("port", 0);
        this.tcpClient = new TcpClient(ip, port);
       // this.tcpClient.run();
        Joystick joystick = new Joystick(this);
        setContentView(R.layout.activity_display_joystick);


    }

    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int id) {
        switch (id) {
            case R.id.joystickRight:
                //         Log.d("Right Joystick", "X percent: " + xPercent + " Y percent: " + yPercent);
                break;
            case R.id.joystickLeft:
                //           Log.d("Left Joystick", "X percent: " + xPercent + " Y percent: " + yPercent);
                break;
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        this.tcpClient.stopClient();
    }


}
