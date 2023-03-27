package scm.cbsarkar2.floatball;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private int bestScore=0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Removal of title bar copied from https://stackoverflow.com/questions/2591036/how-to-hide-the-title-bar-for-an-activity-in-xml-with-existing-custom-theme
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        this.setContentView(R.layout.activity_main);

        GameLevel.mainActivity=this;

        //Saving the "Best Score" method code copied from https://stackoverflow.com/questions/23024831/android-shared-preferences-for-creating-one-time-activity-example
        SharedPreferences prefs = getSharedPreferences("Best Score", MODE_PRIVATE);
        bestScore = prefs.getInt("idScore", bestScore);

        TextView tvHighScoreNum = findViewById(R.id.tvHighScoreNum);
        tvHighScoreNum.setText("Best Score: " + bestScore);

        //the following code is copied and modified from ActivityDemo - Topic 3

        Button btnPlay = findViewById(R.id.btn_play);
        Button btnHowToPlay = findViewById(R.id.btn_how);

        //replaced "new View.OnClickListener()" with lambda because Android Studio suggested it
        btnPlay.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,GameLevel.class);
            startActivity(intent);
        });

        btnHowToPlay.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,HowToPlay.class);
            startActivity(intent);
        });

    }

    @SuppressLint("SetTextI18n")
    public void updateScore(int score){
        if(score>bestScore){
            bestScore=score;

            TextView tvHighScoreNum = findViewById(R.id.tvHighScoreNum);
            tvHighScoreNum.setText("Best Score: " + bestScore);

            SharedPreferences.Editor editor = getSharedPreferences("Best Score", MODE_PRIVATE).edit();
            editor.putInt("idScore", bestScore);
            editor.apply();

        }
    }

}