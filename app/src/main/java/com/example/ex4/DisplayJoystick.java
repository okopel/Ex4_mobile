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
    private TextView angle_view;
    private TextView distance_view;
    private TextView stick_x;
    private TextView stick_y;
    private myJoystick js;

    /**
     * onCreate get the ip and port, open client, draw the joystick.
     * this func also define the listener of the joystick
     *
     * @param savedInstanceState the instance
     */
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

    /**
     * Create the joystick and define the listener
     */
    private void createJoystick() {
        //text boxes
        angle_view = findViewById(R.id.angle_view);
        distance_view = findViewById(R.id.distance_view);
        stick_x = findViewById(R.id.stickX);
        stick_y = findViewById(R.id.stickY);
        RelativeLayout layout_joystick;

        layout_joystick = findViewById(R.id.layout_joystick);

        js = new myJoystick(getApplicationContext(), layout_joystick, R.drawable.image_button);
        //set the inition port or land
        //  js.setPortrait(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
        OnTouchListener otl = new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent e) {
                js.drawStick(e);
                writeArgsInTextBosex(e);
                if (e.getAction() == MotionEvent.ACTION_UP) {
                    tcpClient.sendMessage(js.getStickX(), js.getStickY());
                    js.drawBasicStick();
                }
                return true;
            }
        };
        js.drawBasicStick();
        layout_joystick.setOnTouchListener(otl);
    }

    /**
     * Write the updated args in textView
     *
     * @param e the motion event
     */
    private void writeArgsInTextBosex(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN || e.getAction() == MotionEvent.ACTION_MOVE) {
            angle_view.setText("Angle : " + js.getAngle());
            distance_view.setText("Distance : " + js.getDistance());
            stick_x.setText("aileron : " + (float) js.getStickX());
            stick_y.setText("elevator: " + (float) js.getStickY());
        } else if (e.getAction() == MotionEvent.ACTION_UP) {
            angle_view.setText("Angle :");
            distance_view.setText("Distance :");
            stick_x.setText("aileron : " + js.getStickX());
            stick_y.setText("elevator: " + js.getStickY());
        }
    }

    /**
     * onDestroy close the client
     */
    protected void onDestroy() {
        super.onDestroy();
        this.tcpClient.closeClient();
    }
}
