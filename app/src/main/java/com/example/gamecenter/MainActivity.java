package com.example.gamecenter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamecenter.DB.DB_Handle;
import com.example.gamecenter.DB.Game;
import com.example.gamecenter.DB.ScoreGame;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    boolean isFront = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createAlarmManagerChannel();

        int width  = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;
        Log.d("DEBUG", "URL: " + width + " - " + height);

        String url = "drawable/"+"number_1";
        int imageKey = getResources().getIdentifier(url, "drawable", getPackageName());
        ((ImageView) findViewById(R.id.imageView)).setImageResource(imageKey);
        ((ImageView) findViewById(R.id.imageView)).setX(((float) width/2) - ((ImageView) findViewById(R.id.imageView)).getDrawable().getIntrinsicWidth());
//        ((ImageView) findViewById(R.id.imageView)).setY((float) 0.2);


        try {
            (findViewById(R.id.card_font)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("DEBUG", "onClick: ");
                    final ObjectAnimator oa1 = ObjectAnimator.ofFloat(findViewById(R.id.card_font), "scaleX", 1f, 0f);
                    final ObjectAnimator oa2 = ObjectAnimator.ofFloat(findViewById(R.id.card_font), "scaleX", 0f, 1f);
                    oa1.setInterpolator(new DecelerateInterpolator());
                    oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa1.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            if(isFront){
                                ((TextView)findViewById(R.id.card_font)).setText("BACK");
                                ((TextView)findViewById(R.id.card_font)).setBackgroundColor(Color.BLUE);
                                isFront = false;
                            }else{
                                ((TextView)findViewById(R.id.card_font)).setText("FONT");
                                ((TextView)findViewById(R.id.card_font)).setBackgroundColor(Color.GRAY);
                                isFront = true;
                            }

                            oa2.start();
                        }
                    });
                    oa1.start();
                }
            });

            (findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("DEBUG", "onClick: ");
                    SwitchActivity();
                }
            });
            initData();


        }catch (Exception e){

        }

    }

    private final String MY_ACTION = "alarmAction";
    @Override
    protected void onDestroy() {
        super.onDestroy();

        Intent intent = new Intent(MainActivity.this, ReminderNotification.class);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,intent,PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        long timeAtCloseApp = System.currentTimeMillis();e
        long timeAtToReminder = 1000 * 5; // 10 seconds

        alarmManager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + timeAtToReminder, pendingIntent);
        Toast.makeText(this, "On Destroy", Toast.LENGTH_SHORT).show();
    }

    private void runNotification(){
        try {
            Intent intent = new Intent(MainActivity.this, ReminderNotification.class);


            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,intent,PendingIntent.FLAG_IMMUTABLE);
            Toast.makeText(this, "On Destroy", Toast.LENGTH_SHORT).show();
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            long timeAtCloseApp = System.currentTimeMillis();
            long timeAtToReminder = 1000 * 5; // 10 seconds

            alarmManager.set(AlarmManager.RTC_WAKEUP, timeAtCloseApp + timeAtToReminder, pendingIntent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void runNotification2(){
        String channelID = "AlarmManager";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this,channelID);
        builder.setContentTitle("My Title");
        builder.setContentText("Hello, This is my notification");
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
//                builder.setAutoCancel(true);
//        builder.setColor(getResources().getColor(R.color.color_es));

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
        managerCompat.notify(1,builder.build());
    }

    private void SwitchActivity(){
        try {
            Intent i = new Intent(this, FlipCardMemory_Menu.class);
            startActivity(i);
//            runNotification();
        }catch (Exception e ){

        }
    }

    private void createAlarmManagerChannel(){
        // This notification will be called after a few minute close app

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            String channelID = "AlarmManager";
            String description = "This notification will be called after a few minute close app";

            NotificationChannel channel = new NotificationChannel(channelID, "Game Center Channel", importance);
            channel.setDescription(description);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void initData(){
//        initDataGame();
//            initDataScore();
//                readDataGame();
                readDataScore();
//        DB_Handle handle = new DB_Handle(this);
//            List<ScoreGame> scoreGame =  handle.getScoreByGameID(1);
//            getHighestTries(scoreGame);
    }

    private void initDataGame(){
        DB_Handle db_handle = new DB_Handle(this);

        db_handle.addNewGame(new Game("Memory Card"));

    }

    private void initDataScore(){
        DB_Handle db_handle = new DB_Handle(this);
        db_handle.addNewScore(new ScoreGame(1,"Sample",5,3));
        db_handle.addNewScore(new ScoreGame(1,"Sample",10,5));
        db_handle.addNewScore(new ScoreGame(1,"Sample",10,7));
    }

    private void readDataGame(){
        DB_Handle handle = new DB_Handle(this);
        List<Game> gameList = handle.getListGame();

        for(Game game : gameList){
            Log.d("DEBUG DB", "readData GameName: " + game.getGameName());
            Log.d("DEBUG DB", "readData GameID: " + game.getID());
        }
    }

    private void readDataScore(){
        DB_Handle handle = new DB_Handle(this);
        List<ScoreGame> scoreGame =  handle.getScoreByGameID(1);

        for(ScoreGame game : scoreGame){
            Log.d("DEBUG DB Score", "readData ID: " + game.getID());
            Log.d("DEBUG DB Score", "readData GameID: " + game.getGameID());
            Log.d("DEBUG DB Score", "readData Time: " + game.getTime());
            Log.d("DEBUG DB Score", "readData Tries: " + game.getTries());
            Log.d("DEBUG DB Score", "readData GameMode: " + game.getGameMode());
        }
    }

    private int getHighestTries(List<ScoreGame> games){
        int max = games.get(0).getTries();
        for(ScoreGame game: games){
            if((max = game.getTries()) > max){

            }
        }
        Log.d("TAG", "getHighestScore: " + max);
        return max;
    }

}