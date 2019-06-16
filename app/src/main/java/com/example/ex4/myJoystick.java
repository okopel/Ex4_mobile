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


    private int STICK_ALPHA = 200;
    private int LAYOUT_ALPHA = 200;
    private int OFFSET = 0;

    private Context mContext;
    private ViewGroup mLayout;
    private LayoutParams params;
    private int stick_width, stick_height;

    private int position_x = 0, position_y = 0, min_distance = 0;
    private float distance = 0, angle = 0;

    private DrawCanvas draw;
    private Paint paint;
    private Bitmap stick;

    private boolean touch_state = false;

    myJoystick(Context context, ViewGroup layout, int stick_res_id) {
        mContext = context;

        stick = BitmapFactory.decodeResource(mContext.getResources(), stick_res_id);

        stick_width = stick.getWidth();
        stick_height = stick.getHeight();

        draw = new DrawCanvas(mContext);
        paint = new Paint();
        mLayout = layout;
        params = mLayout.getLayoutParams();
    }

    void drawStick(MotionEvent e) {
        position_x = (int) (e.getX() - (params.width / 2f));
        position_y = (int) (e.getY() - (params.height / 2f));
        distance = (float) Math.sqrt(Math.pow(position_x, 2f) + Math.pow(position_y, 2));
        angle = (float) cal_angle(position_x, position_y);


        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            if (distance <= (params.width / 2f) - OFFSET) {
                draw.position(e.getX(), e.getY());
                draw();
                touch_state = true;
            }
        } else if (e.getAction() == MotionEvent.ACTION_MOVE && touch_state) {
            if (distance <= (params.width / 2f) - OFFSET) {
                draw.position(e.getX(), e.getY());
                draw();
            } else if (distance > (params.width / 2f) - OFFSET) {
                float x = (float) (Math.cos(Math.toRadians(cal_angle(position_x, position_y))) * ((params.width / 2f) - OFFSET));
                float y = (float) (Math.sin(Math.toRadians(cal_angle(position_x, position_y))) * ((params.height / 2f) - OFFSET));
                x += (params.width / 2f);
                y += (params.height / 2f);
                draw.position(x, y);
                draw();
            } else {
                mLayout.removeView(draw);
            }
        } else if (e.getAction() == MotionEvent.ACTION_UP) {
            mLayout.removeView(draw);
            touch_state = false;
        }
    }


    int getX() {
        if (distance > min_distance && touch_state) {
            return position_x;
        }
        return 0;
    }

    int getY() {
        if (distance > min_distance && touch_state) {
            return position_y;
        }
        return 0;
    }

    float getAngle() {
        if (distance > min_distance && touch_state) {
            return angle;
        }
        return 0;
    }

    float getDistance() {
        if (distance > min_distance && touch_state) {
            return distance;
        }
        return 0;
    }

    void setMinimumDistance(int minDistance) {
        min_distance = minDistance;
    }

    void drawBasicStick() {
        draw.position(params.width / 2f, params.height / 2f);
        draw();
    }

    /*
        public int get8Direction() {
            if (distance > min_distance && touch_state) {
                if (angle >= 247.5 && angle < 292.5) {
                    return STICK_UP;
                } else if (angle >= 292.5 && angle < 337.5) {
                    return STICK_UPRIGHT;
                } else if (angle >= 337.5 || angle < 22.5) {
                    return STICK_RIGHT;
                } else if (angle >= 22.5 && angle < 67.5) {
                    return STICK_DOWNRIGHT;
                } else if (angle >= 67.5 && angle < 112.5) {
                    return STICK_DOWN;
                } else if (angle >= 112.5 && angle < 157.5) {
                    return STICK_DOWNLEFT;
                } else if (angle >= 157.5 && angle < 202.5) {
                    return STICK_LEFT;
                } else if (angle >= 202.5 && angle < 247.5) {
                    return STICK_UPLEFT;
                }
            } else if (distance <= min_distance && touch_state) {
                return STICK_NONE;
            }
            return 0;
        }

    */
    void setOffset(int offset) {
        OFFSET = offset;
    }


    void setStickAlpha(int alpha) {
        STICK_ALPHA = alpha;
        paint.setAlpha(alpha);
    }


    void setLayoutAlpha(int alpha) {
        LAYOUT_ALPHA = alpha;
        mLayout.getBackground().setAlpha(alpha);
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
        if (x >= 0 && y >= 0)
            return Math.toDegrees(Math.atan(y / x));
        else if (x < 0 && y >= 0)
            return Math.toDegrees(Math.atan(y / x)) + 180;
        else if (x < 0 && y < 0)
            return Math.toDegrees(Math.atan(y / x)) + 180;
        else if (x >= 0 && y < 0)
            return Math.toDegrees(Math.atan(y / x)) + 360;
        return 0;
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
