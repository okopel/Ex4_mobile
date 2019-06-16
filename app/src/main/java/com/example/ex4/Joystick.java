package com.example.ex4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class Joystick extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    private final int ratio = 5;
    private float x_pos;
    private float y_pos;
    private float radius;
    private float innerRadius;
    private JoystickListener joystickCallback;

    public Joystick(Context context) {
        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if (context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;

    }


    @Override
    public boolean onTouch(View v, MotionEvent e) {
        if (v.equals(this)) {
            if (e.getAction() != MotionEvent.ACTION_UP) {
                float displacement = (float) Math.sqrt((Math.pow(e.getX() - x_pos, 2)) + Math.pow(e.getY() - y_pos, 2));
                if (displacement < radius) {
                    draw(e.getX(), e.getY());
                    joystickCallback.onJoystickMoved((e.getX() - x_pos) / radius, (e.getY() - y_pos) / radius, getId());
                } else {
                    float ratio = radius / displacement;
                    float constrainedX = x_pos + (e.getX() - x_pos) * ratio;
                    float constrainedY = y_pos + (e.getY() - y_pos) * ratio;
                    draw(constrainedX, constrainedY);
                    joystickCallback.onJoystickMoved((constrainedX - x_pos) / radius, (constrainedY - y_pos) / radius, getId());
                }
            } else
                draw(x_pos, y_pos);
            joystickCallback.onJoystickMoved(0, 0, getId());
        }
        return true;
    }


    private void init() {
        x_pos = getWidth() / 2f;
        y_pos = getHeight() / 2f;
        radius = Math.min(getWidth(), getHeight()) / 3f;
        innerRadius = Math.min(getWidth(), getHeight()) / 5f;
    }

    private void draw(float x, float y) {
        if (!getHolder().getSurface().isValid()) {
            return;
        }
        Canvas myCanvas = this.getHolder().lockCanvas();
        Paint colors = new Paint();
        myCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        float hypotenuse = (float) Math.sqrt(Math.pow(x - x_pos, 2) + Math.pow(y - y_pos, 2));
        float sin = (y - y_pos) / hypotenuse;
        float cos = (x - x_pos) / hypotenuse;

        colors.setARGB(255, 100, 100, 100);
        myCanvas.drawCircle(x_pos, y_pos, radius, colors);
        for (int i = 1; i <= (int) (radius / ratio); i++) {
            colors.setARGB(150 / i, 255, 0, 0);
            myCanvas.drawCircle(x - cos * hypotenuse * (ratio / radius) * i,
                    y - sin * hypotenuse * (ratio / radius) * i, i * (innerRadius * ratio / radius), colors);
        }

        //Drawing the joystick hat
        for (int i = 0; i <= (int) (innerRadius / ratio); i++) {
            colors.setARGB(255, (int) (i * (255 * ratio / innerRadius)), (int) (i * (255 * ratio / innerRadius)), 255); //Change the joystick color for shading purposes
            myCanvas.drawCircle(x, y, innerRadius - (float) i * (ratio) / 2, colors); //Draw the shading for the hat
        }
        getHolder().unlockCanvasAndPost(myCanvas);

    }

    public void surfaceCreated(SurfaceHolder holder) {
        init();
        draw(x_pos, y_pos);

    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public interface JoystickListener {
        void onJoystickMoved(float xPercent, float yPercent, int id);
    }
}
