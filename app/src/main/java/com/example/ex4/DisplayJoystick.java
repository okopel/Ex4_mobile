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
    private myJoystick js;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_joystick);
        Intent intent = getIntent();
        String ip = intent.getStringExtra("ip");
        int port = intent.getIntExtra("port", 0);
        this.tcpClient = new TcpClient(ip, port);
        // this.tcpClient.run();
        createJoystick();
    }

    private void createJoystick() {
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);

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

                    /*int direction = js.get8Direction();
                    if (direction == myJoystick.STICK_UP) {
                        textView5.setText("Direction : Up");
                    } else if (direction == myJoystick.STICK_UPRIGHT) {
                        textView5.setText("Direction : Up Right");
                    } else if (direction == myJoystick.STICK_RIGHT) {
                        textView5.setText("Direction : Right");
                    } else if (direction == myJoystick.STICK_DOWNRIGHT) {
                        textView5.setText("Direction : Down Right");
                    } else if (direction == myJoystick.STICK_DOWN) {
                        textView5.setText("Direction : Down");
                    } else if (direction == myJoystick.STICK_DOWNLEFT) {
                        textView5.setText("Direction : Down Left");
                    } else if (direction == myJoystick.STICK_LEFT) {
                        textView5.setText("Direction : Left");
                    } else if (direction == myJoystick.STICK_UPLEFT) {
                        textView5.setText("Direction : Up Left");
                    } else if (direction == myJoystick.STICK_NONE) {
                        textView5.setText("Direction : Center");
                    }*/
                } else if (e.getAction() == MotionEvent.ACTION_UP) {
                    textView1.setText("X :");
                    textView2.setText("Y :");
                    textView3.setText("Angle :");
                    textView4.setText("Distance :");

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
