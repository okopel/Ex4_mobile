package com.example.ex4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

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
        EditText ip = findViewById(R.id.ip_box);
        String ip_txt = ip.getText().toString();
        EditText port = findViewById(R.id.port_box);
        int port_txt = Integer.parseInt(port.getText().toString());
        Intent intent = new Intent(this, DisplayJoystick.class);
        intent.putExtra("ip", ip_txt);
        intent.putExtra("port", port_txt);
        startActivity(intent);
    }
}




