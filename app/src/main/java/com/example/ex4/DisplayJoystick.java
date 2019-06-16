package com.example.ex4;

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
    private RelativeLayout layout_joystick;
    //ImageView image_joystick, image_border;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private TextView textView6;
    private myJoystick js;

    private void sendArgs(float aileron, float elavator) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_joystick);
        Intent intent = getIntent();
        String ip = intent.getStringExtra("ip");
        int port = intent.getIntExtra("port", 0);
        this.tcpClient = new TcpClient(ip, port);
        this.tcpClient.run();
        createJoystick();
    }

    private void createJoystick() {
        textView1 = findViewById(R.id.x_view);
        textView2 = findViewById(R.id.y_view);
        textView3 = findViewById(R.id.angle_view);
        textView4 = findViewById(R.id.distance_view);
        textView5 = findViewById(R.id.stickX);
        textView6 = findViewById(R.id.stickY);
        Runnable r = new Runnable() {
            @Override
            public void run() {

            }
        };
        layout_joystick = findViewById(R.id.layout_joystick);

        js = new myJoystick(getApplicationContext(), layout_joystick, R.drawable.image_button);
        js.setStickSize(150, 150);
        js.setLayoutSize(500, 500);
        js.setLayoutAlpha(150);
        js.setStickAlpha(100);
        js.setOffset(90);
        js.setMinimumDistance(50);
        OnTouchListener otl = new OnTouchListener() {

            public boolean onTouch(View arg0, MotionEvent e) {
                js.drawStick(e);
                if (e.getAction() == MotionEvent.ACTION_DOWN || e.getAction() == MotionEvent.ACTION_MOVE) {
                    textView1.setText("X : " + js.getX());
                    textView2.setText("Y : " + js.getY());
                    textView3.setText("Angle : " + js.getAngle());
                    textView4.setText("Distance : " + js.getDistance());
                    textView5.setText("stickX : " + js.getStickX());
                    textView6.setText("stickY : " + js.getStickY());
                } else if (e.getAction() == MotionEvent.ACTION_UP) {

                    textView1.setText("X :");
                    textView2.setText("Y :");
                    textView3.setText("Angle :");
                    textView4.setText("Distance :");
                    textView5.setText("stickX : ");
                    textView6.setText("stickY : ");
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
