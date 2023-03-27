package scm.cbsarkar2.floatball;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

//this part is my own contribution

public abstract class Wall {
    private final PointF topLeft;
    private final PointF bottomRight;
    Paint paint;

    public Wall(Canvas canvas, PointF topLeft, PointF bottomRight,int color ){
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
        paint = new Paint();
        paint.setColor(color);
        Log.d("X-Pos",  Float.toString(topLeft.x));
        this.draw(canvas);
    }

    public void draw(Canvas canvas){
        RectF rect = new RectF(this.topLeft.x,this.topLeft.y,this.bottomRight.x,this.bottomRight.y);
        canvas.drawRect(rect,paint);
    }

    public boolean hasCollided(PointF center, float radius){
        if(center.x+radius>topLeft.x && center.x+radius<bottomRight.x && center.y+radius>topLeft.y&&center.y+radius<bottomRight.y)
            return true;
        if(center.x-radius>topLeft.x && center.x-radius<bottomRight.x && center.y-radius>topLeft.y&&center.y-radius<bottomRight.y)
            return true;
        if(center.x+radius>topLeft.x && center.x+radius<bottomRight.x &&center.y-radius>topLeft.y&&center.y-radius<bottomRight.y)
            return true;
        return center.x - radius > topLeft.x && center.x - radius < bottomRight.x && center.y + radius > topLeft.y && center.y + radius < bottomRight.y;
    }

    public abstract void onCollision(TiltingView tiltingView);


}
