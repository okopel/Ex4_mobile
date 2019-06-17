package com.example.ex4;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DisplayJoystick extends AppCompatActivity {

    private TcpClient tcpClient;
    private TextView x_view;
    private TextView y_view;
    private TextView angle_view;
    private TextView distance_view;
    private TextView stick_x;
    private TextView stick_y;
    private myJoystick js;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_joystick);
        Intent intent = getIntent();
        final String ip = intent.getStringExtra("ip");
        final int port = intent.getIntExtra("port", 0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                tcpClient = new TcpClient(ip, port);
                tcpClient.run();
            }
        }).start();
        createJoystick();
    }


    private void createJoystick() {
        x_view = findViewById(R.id.x_view);
        y_view = findViewById(R.id.y_view);
        angle_view = findViewById(R.id.angle_view);
        distance_view = findViewById(R.id.distance_view);
        stick_x = findViewById(R.id.stickX);
        stick_y = findViewById(R.id.stickY);
        RelativeLayout layout_joystick;

        layout_joystick = findViewById(R.id.layout_joystick);

        js = new myJoystick(getApplicationContext(), layout_joystick, R.drawable.image_button);
        js.setStickSize(150, 150);
        js.setLayoutSize(500, 500);
        js.setLayoutAlpha(150);
        js.setStickAlpha(100);
        js.setOffset(90);
        js.setMinimumDistance(50);
        OnTouchListener otl = new OnTouchListener() {
            @SuppressLint("SetTextI18n")
            public boolean onTouch(View arg0, MotionEvent e) {
                js.drawStick(e);
                if (e.getAction() == MotionEvent.ACTION_DOWN || e.getAction() == MotionEvent.ACTION_MOVE) {

                    x_view.setText("X : " + js.getX());
                    y_view.setText("Y : " + js.getY());
                    angle_view.setText("Angle : " + js.getAngle());
                    distance_view.setText("Distance : " + js.getDistance());
                    stick_x.setText("stickX : " + (float) js.getStickX());
                    stick_y.setText("stickY : " + (float) js.getStickY());
                } else if (e.getAction() == MotionEvent.ACTION_UP) {
                    tcpClient.sendMessage(js.getStickX(), js.getStickY());
                    x_view.setText("X :");
                    y_view.setText("Y :");
                    angle_view.setText("Angle :");
                    distance_view.setText("Distance :");
                    stick_x.setText("stickX : " + js.getStickX());
                    stick_y.setText("stickY : " + js.getStickY());
                    js.drawBasicStick();
                }
                return true;
            }
        };
        js.drawBasicStick();
        layout_joystick.setOnTouchListener(otl);
    }

    protected void onDestroy() {
        super.onDestroy();
        this.tcpClient.stopClient();
    }


}
