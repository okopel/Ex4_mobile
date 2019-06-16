package com.example.ex4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Connect onclick function which get port and ip, goto new intent of the joystick.
     *
     * @param view the connect screen.
     */
    public void connect(View view) {
        EditText ip = (EditText) findViewById(R.id.ip_box);
        EditText port = (EditText) findViewById(R.id.port_box);
        Intent intent = new Intent(this, DisplayJoystick.class);
        intent.putExtra("ip", ip.toString());
        intent.putExtra("port", Integer.parseInt(port.toString()));
        startActivity(intent);
    }
}
