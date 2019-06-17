package com.example.ex4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

class myJoystick {

    private ViewGroup mLayout;
    private LayoutParams params;
    private int stick_width, stick_height, min_distance = 50, offset = 90;
    private float distance = 0, angle = 0;
    private DrawCanvas draw;
    private Paint paint;
    private Bitmap stick;
    private boolean isTouched = false;

    myJoystick(Context context, ViewGroup layout, int stick_res_id) {
        stick = BitmapFactory.decodeResource(context.getResources(), stick_res_id);
        stick_width = stick.getWidth();
        stick_height = stick.getHeight();
        draw = new DrawCanvas(context);
        paint = new Paint();
        mLayout = layout;
        params = mLayout.getLayoutParams();
        mLayout.getBackground().setAlpha(150);
        paint.setAlpha(100);
    }

    double getStickX() {
        double x = (Math.min(distance, (params.width / 2f) - offset) * Math.cos(Math.toRadians(angle)));
        x /= (params.width / 2f) - offset; //normalize
        return x;

    }

    double getStickY() {
        double y = (Math.min(distance, (params.height / 2f) - offset) * Math.sin(Math.toRadians(angle)));
        y /= (params.height / 2f) - offset;
        return y;

    }

    void drawStick(MotionEvent e) {
        int position_x = (int) (e.getX() - (params.width / 2f));
        int position_y = (int) (e.getY() - (params.height / 2f));
        distance = (float) Math.sqrt(Math.pow(position_x, 2f) + Math.pow(position_y, 2));
        angle = (float) cal_angle(position_x, position_y);

        float centerW = (params.width / 2f) - offset;
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            if (distance <= centerW) {
                draw.position(e.getX(), e.getY());
                draw();
                isTouched = true;
            }
        } else if (e.getAction() == MotionEvent.ACTION_MOVE && isTouched) {
            if (distance <= centerW) {
                draw.position(e.getX(), e.getY());
                draw();
            } else if (distance > centerW) {
                float x = (float) (Math.cos(Math.toRadians(cal_angle(position_x, position_y))) * centerW);
                float y = (float) (Math.sin(Math.toRadians(cal_angle(position_x, position_y))) * ((params.height / 2f) - offset));

                x += (params.width / 2f);
                y += (params.height / 2f);
                draw.position(x, y);
                draw();
            } else {
                mLayout.removeView(draw);
            }
        } else if (e.getAction() == MotionEvent.ACTION_UP) {
            mLayout.removeView(draw);
            isTouched = false;
        }
    }

    float getAngle() {
        if (isTouched && (distance > min_distance)) {
            return angle;
        }
        return 0;
    }

    float getDistance() {
        if (isTouched && (distance > min_distance)) {
            return distance;
        }
        return 0;
    }

    void drawBasicStick() {
        draw.position(params.width / 2f, params.height / 2f);
        draw();
    }

    void setStickSize(int width, int height) {
        stick = Bitmap.createScaledBitmap(stick, width, height, false);
        stick_width = stick.getWidth();
        stick_height = stick.getHeight();
    }


    void setLayoutSize(int width, int height) {
        params.width = width;
        params.height = height;
    }


    private double cal_angle(float x, float y) {
        double deg = Math.toDegrees(Math.atan(y / x));
        if (x >= 0) {
            if (y >= 0) {
                return deg;
            } else {
                return deg + 360;
            }
        } else {
            return deg + 180;
        }

    }

    private void draw() {
        try {
            mLayout.removeView(draw);
        } catch (Exception ignored) {
        }
        mLayout.addView(draw);
    }

    private class DrawCanvas extends View {
        float x, y;

        private DrawCanvas(Context mContext) {
            super(mContext);
        }

        public void onDraw(Canvas canvas) {
            canvas.drawBitmap(stick, x, y, paint);
        }

        private void position(float pos_x, float pos_y) {
            x = pos_x - (stick_width / 2f);
            y = pos_y - (stick_height / 2f);
        }
    }
}
