package scm.cbsarkar2.floatball;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.Log;

//this part is my own contribution

public class LoseWall extends Wall{

    private static final int  color = Color.YELLOW;
    public LoseWall(Canvas canvas, PointF topLeft, PointF bottomRight) {
        super(canvas, topLeft, bottomRight, LoseWall.color);
    }


    @Override
    public void onCollision(TiltingView tiltingView) {
        tiltingView.getScore();
        tiltingView.initScore();
        tiltingView.loseVibrate();
        Log.d("Collision", "Collided with losing wall");
    }
}