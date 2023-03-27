package scm.cbsarkar2.floatball;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;

//the following code is copied and modified from Tilting Test - Topic 7

public class TiltingView extends View {

    private final Paint circlePaint = new Paint();
    private final PointF center = new PointF(10, 10);
    private final float radius = 50;
    public Vibrator vibrator;
    private ArrayList<Float> xPos;

    //background and bitmap code copied and modified from Assignment 1
    private Bitmap bmpSpace = null;
    private Bitmap bmpRocket = null;
    private RectF rectBg;
    private RectF rectR;

    int bg_width = Resources.getSystem().getDisplayMetrics().widthPixels;
    int bg_height = Resources.getSystem().getDisplayMetrics().heightPixels;

    int level;

    boolean mustUpdateWalls;
    private int score;
    private final ArrayList<Wall> walls = new ArrayList<>();
    public GameLevel gameLevel;

    public TiltingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public TiltingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TiltingView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        gameLevel = (GameLevel)context;
        circlePaint.setColor(Color.RED);
        score=0;
        level = 0;
        xPos = new ArrayList<>();
        mustUpdateWalls = true;

        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        bmpSpace = BitmapFactory.decodeResource(getResources(), R.drawable.background_space);
        rectBg= new RectF(0,0, bg_width, bg_height);

        bmpRocket = BitmapFactory.decodeResource(getResources(), R.drawable.rocket);
        rectR = new RectF(bg_width-200,bg_height/2f-150,bg_width-10,bg_height/2f+150);

    }

    float vx = 0;
    float vy = 0;
    float scale = 0.5f;

    public void update(float ax, float ay) {
        // integrate the acceleration over time to find the velocity
        vx += ax * scale;
        vy += ay * scale;

        // integrate the velocity over time to determine the distance traveled
        center.x += vx;
        center.y += vy;

        if (center.x < radius) {
            center.x = radius;
            vx = 0;
        } else if (center.x > this.getWidth() - radius) {
            center.x = this.getWidth() - radius;
            vx = 0;
        }

        if (center.y < radius) {
            center.y = radius;
            vy = 0;
        } else if (center.y > this.getHeight() - radius) {
            center.y = this.getHeight() - radius;
            vy = 0;
        }

        for(int i = 0; i<walls.size();i++){
            if(walls.get(i).hasCollided(center,radius)){
                walls.get(i).onCollision(this);
                center.x = 10;
                center.y = getHeight()/2f;
                mustUpdateWalls =true;
            }
        }
        invalidate();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(bmpSpace,null,rectBg,null);

        canvas.drawBitmap(bmpRocket,null, rectR,null);

        canvas.drawCircle(center.x, center.y, radius, circlePaint);

        //SuppressLint automatically added after following Android Studio suggestion
        @SuppressLint("DrawAllocation") Paint scoreText = new Paint();
        scoreText.setColor(Color.WHITE);
        scoreText.setTextSize(50);
        canvas.drawText("Score: " + score, 20, 50, scoreText);

        xPos.clear();
        xPos.add((float) (getWidth()/2));
        xPos.add((float) (getWidth()/2-250));
        xPos.add((float) (getWidth()/2-500));
        xPos.add((float) (getWidth()/2-750));
        xPos.add((float) (getWidth()/2+250));
        xPos.add((float) (getWidth()/2+500));
        xPos.add((float) (getWidth()/2+750));


        if(mustUpdateWalls){
            updateWalls(canvas);
            mustUpdateWalls =false;
        }else{
            for(Wall wall:walls){
                wall.draw(canvas);
            }
        }

    }

    public void updateWalls(Canvas canvas){
        walls.clear();
        Collections.shuffle(xPos); //this is copied from https://stackoverflow.com/questions/1519736/random-shuffling-of-an-array

        for(int i = 0 ; i<level+1;i++){

            float lowerBound =200;
            float upperBound=getHeight()-200;
            float gapPos = (float)Math.floor(Math.random()*(upperBound-lowerBound+1)+lowerBound); //this is copied and modified from https://stackoverflow.com/questions/33167339/generate-random-numbers-in-specified-range-various-cases-int-float-inclusiv

            walls.add(new LoseWall(canvas,new PointF(xPos.get(i),0),new PointF(xPos.get(i)+20,gapPos-100)));
            walls.add(new LoseWall(canvas,new PointF(xPos.get(i),gapPos+100),new PointF(xPos.get(i)+20,getHeight())));

            walls.add(new WinWall(canvas,new PointF(getWidth()-150,getHeight()/2f-150),new PointF(getWidth(),getHeight()/2f+150)));
        }

    }
    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        center.x = 10;
        center.y = getHeight()/2f;

        super.onSizeChanged(w, h, oldW, oldH);
    }

    public void initScore(){
        score =0;
        level=0;
    }

    public void updateScore() {
        this.score++;
        if (this.score < 6) {
            this.level = 0;
        } else if (this.score < 11) {
            this.level = 1;
        } else if (this.score < 16){
            this.level = 2;
        } else if (this.score < 21){
            this.level = 3;
        } else if (this.score < 26){
            this.level = 4;
        } else if (this.score < 31){
            this.level = 5;
        } else {
            this.level = 6;
        }
    }

    public void getScore() {
        gameLevel.updateScore(score);
    }

    public void winVibrate() {
        vibrator.vibrate(50);
    }

    public void loseVibrate() {
        vibrator.vibrate(500);
    }

}
