package scm.cbsarkar2.floatball;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.Log;


//this part is my own contribution

public class WinWall extends Wall{

    private static final int  color = Color.TRANSPARENT;
    public WinWall(Canvas canvas, PointF topLeft, PointF bottomRight) {
        super(canvas, topLeft, bottomRight,WinWall.color);
    }

    @Override
    public void onCollision(TiltingView tiltingView) {
        tiltingView.updateScore();
        tiltingView.winVibrate();
        Log.d("Collision", "Next level");
    }
}
