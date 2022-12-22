package com.example.gamecenter;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.gamecenter.DB.DB_Handle;
import com.example.gamecenter.DB.ScoreGame;
import com.example.gamecenter.games.CardInfo;
import com.example.gamecenter.games.ModeGame;

import java.util.ArrayList;
import java.util.List;


public class MemoryCard extends AppCompatActivity {

    private final int GAME_ID = 1;
    private boolean IS_GAME_WIN = false;

    private int row = 4;
    private int colmn = 3;
    private int number_of_pair = (row * colmn) / 2;

    private ModeGame modeGame;
    private String themeGame;
    private int sizeCard;
    private int marginCard;
    private int cardBehind;

    private ArrayList<Integer> pair_card_complete_list = new ArrayList<Integer>();// Store ID card of card complete
    private ArrayList<CardInfo> cardInfo_list = new ArrayList<CardInfo>();
    private ArrayList<Integer> pair = new ArrayList<Integer>(); // Store ID card

    private int timesTry = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_card);

        getSupportActionBar().setTitle("Memory Game");
        getSupportActionBar().hide();

        this.initSetting();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        this.showDialog_LeaveGame();
    }

    private void initSetting(){
        this.modeGame = getIntent().getParcelableExtra("ModeGameInfo");
        row = modeGame.getAmountRow();
        colmn = modeGame.getAmountColumn();
        number_of_pair = (row * colmn) / 2;
        Log.d("DEBUG", "Theme name: " + modeGame.getThemeName());
        Log.d("DEBUG", "Size Card: " + modeGame.getSizeCard());
        Log.d("DEBUG", "Mode Game: " + modeGame.getModeName());

        this.themeGame = modeGame.getThemeName();
        this.sizeCard = modeGame.getSizeCard();
        this.marginCard = 5;
        this.cardBehind = R.drawable.card_behind;
        initBoard();
    }

    // Init board layout
    private void initBoard(){

        // Reset timer
        resetTimer();

        // Clear List
        this.pair_card_complete_list.removeAll(this.pair_card_complete_list);
        this.pair.removeAll(this.pair);
        this.cardInfo_list.removeAll(this.cardInfo_list);

        TableLayout board = (TableLayout) findViewById(R.id.board_layout);

        // Clear old view
        board.removeAllViews();

        for(int i = 0;i<row;i++){
            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(this.sizeCard, this.sizeCard,1.0f);
            lp.setMargins(this.marginCard,this.marginCard,this.marginCard,this.marginCard);

//            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 300);
            TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);

            row.setLayoutParams(tableRowParams);

            for(int y = 0;y<colmn;y++){
                ImageView imageView = new ImageView(this);

                imageView.setImageResource(this.cardBehind);
                imageView.setLayoutParams(lp);
                imageView.setId(View.generateViewId());
                int pair_number = generatePairNumber();
                int imageCard = generateCardImage(pair_number);
                CardInfo cardInfo = new CardInfo(imageView.getId(),pair_number, imageCard);
                cardInfo_list.add(cardInfo);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(modeGame.getModeName().contains("Sample")){
                            ((TextView) findViewById(R.id.txt_TimeDown)).setVisibility(View.GONE);
                            handleGameMode_Sample(view);
                        }else if(modeGame.getModeName().contains("Time Challenge")){
                            ((Chronometer)findViewById(R.id.chronometer_TimeUp)).setVisibility(View.GONE);
                            handleGameMode_TimeChallenge(view);
                        }else if(modeGame.getModeName().contains("Limited Tries")){
                            ((TextView) findViewById(R.id.txt_TimeDown)).setVisibility(View.GONE);
                            handleGameMode_LimitedTries(view);
                        }
                    }
                });
                row.addView(imageView);
            }
            board.addView(row);
        }
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    // check if whether pair complete when create game
    private boolean isPairComplete(int index){
        int count = 0;
        for(CardInfo cardInfo : cardInfo_list){
            if(cardInfo.getPair_number() == index){
                count ++;
            }
        }
        return count == 2 ? true : false;
    }

    //    Return a pair number
    private int generatePairNumber(){
     while(true){
         int pair_index = getRandomNumber(0,number_of_pair);
         if(!isPairComplete(pair_index)){
          return pair_index;
         }
     }
    }

    // Return the image
    private int generateCardImage(int pair_number){
        String[] all_image = getResources().getStringArray(R.array.letter_theme);

        if(this.modeGame.getThemeName().contains("number_theme")){
            all_image = getResources().getStringArray(R.array.number_theme);
        }else if(this.modeGame.getThemeName().contains("food_theme") ){
            all_image = getResources().getStringArray(R.array.food_theme);
        }else if(this.modeGame.getThemeName().contains("animal_theme")){
            all_image = getResources().getStringArray(R.array.animal_theme);
        }


        String url = "drawable/"+all_image[pair_number];
        int imageKey = getResources().getIdentifier(url, "drawable", getPackageName());
        return imageKey;
    }

    // Check whether this card is flip up or down
    boolean isFlipUp(int cardID){
        for(int i = 0; i< pair_card_complete_list.size(); i++){
            if(cardID == pair_card_complete_list.get(i)){
                return true;
            }
        }
        for(int id : this.pair){
            if(cardID == id){
                return true;
            }
        }
        return false;
    }

    // remove index card from card flip up list
    private void removeFromFlipUpList(int cardID){
        try {
            pair_card_complete_list.remove(Integer.valueOf(cardID));
        }catch (Exception e){
        }
    }

    private void setTimesTry(){
        ((TextView)findViewById(R.id.txt_tries)).setText("" + this.timesTry);
    }

    private boolean isCountTimerRun = false;
    private void runCountUpTimer(){
        if(this.isCountTimerRun){
            ((Chronometer)findViewById(R.id.chronometer_TimeUp)).stop();
        }else {
            ((Chronometer)findViewById(R.id.chronometer_TimeUp)).start();
        }
        this.isCountTimerRun = !this.isCountTimerRun;

    }

    private CountDownTimer countDownTimer;
    private void runCountDownTimer(int time){
        this.isCountTimerRun = true;
        countDownTimer = new CountDownTimer(time, 1000) {
            public void onTick(long millisUntilFinished) {
                double minute = millisUntilFinished / 60000;
//                int second = (int)

                Log.d("DEBUG", "onTick: " + minute);

                ((TextView)findViewById(R.id.txt_TimeDown)).setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                isCountTimerRun = false;
                IS_GAME_WIN = false;
                if(checkWin()){
                    IS_GAME_WIN = true;
                }


                showDialog_CompleteGame();
                ((TextView)findViewById(R.id.txt_TimeDown)).setText("00:00");
            }
        }.start();
    }

    // Reset ALL Timer
    private void resetTimer(){
        ((TextView)findViewById(R.id.txt_TimeDown)).setText("00:00");
        ((Chronometer)findViewById(R.id.chronometer_TimeUp)).setBase(SystemClock.elapsedRealtime());
    }

    // Find the card by card ID
    private CardInfo findCardByID(int cardID){
        for(CardInfo cardInfo : this.cardInfo_list){
            if(cardInfo.getID() == cardID){
                return cardInfo;
            }
        }
        return null;
    }

    private void eventFlipDownCard_animation(View view){
        try {
            final ObjectAnimator oa1 = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f);
            final ObjectAnimator oa2 = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f);
            oa1.setInterpolator(new DecelerateInterpolator());
            oa2.setInterpolator(new AccelerateDecelerateInterpolator());
            oa1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    ((ImageView)view).setImageResource(cardBehind);
//                    ((ImageView)view).setBackgroundColor(Color.rgb(50,50,50));

//                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 300,1.0f);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(sizeCard, sizeCard,1.0f);
                    lp.setMargins(marginCard,marginCard,marginCard,marginCard);
                    ((ImageView)view).setLayoutParams(lp);
                    oa2.start();
                }
            });
            oa1.start();
        }catch (Exception e){

        }
    }

    private void eventFlipCard_animation(View view){
        try {
            final ObjectAnimator oa1 = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f);
            final ObjectAnimator oa2 = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f);
            oa1.setInterpolator(new DecelerateInterpolator());
            oa2.setInterpolator(new AccelerateDecelerateInterpolator());
            oa1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    setImageForCard(view);
                    oa2.start();
                }
            });
            oa1.start();
        }catch (Exception e){

        }
    }

    private void setImageForCard(View view){
        int image = findCardByID(view.getId()).getImage();
        ((ImageView)view).setImageResource(image);
//        ((ImageView)view).setBackgroundColor(Color.GREEN);

        TableRow.LayoutParams lp = new TableRow.LayoutParams(this.sizeCard, this.sizeCard,1.0f);
        lp.setMargins(this.marginCard,this.marginCard,this.marginCard,this.marginCard);
        ((ImageView)view).setLayoutParams(lp);
    }

    // Handle Mode: Sample
    private void handleGameMode_Sample(View view){

        // Run countUpTimer
        if(this.isCountTimerRun == false){
            this.runCountUpTimer();
        }

        if(this.isFlipUp(view.getId())){
            // if this card is flip then not do anything
            return;
        }

        if(this.pair.size() == 2){
            if(this.isAPair()){
                // Add ID card into list store card which flip up
                for(int ID : this.pair){
                    this.pair_card_complete_list.add(ID);
                }
            }else{
                for(int ID : this.pair){
                    this.eventFlipDownCard_animation(findViewById(ID));
                }
            }
            this.pair.removeAll(this.pair);
        }
        eventFlipCard_animation(view);
        this.pair.add(view.getId());

        if(this.pair.size() == 2){
            this.timesTry ++;
            setTimesTry();
        }

        if(this.checkWin()){
            this.runCountUpTimer();

            this.IS_GAME_WIN = true;
            showDialog_CompleteGame();
            return;
        }
    }

    // Handle Mode: TimeChallenge
    private int getTimeCountDown(){
        if(this.modeGame.getSizeBoard().contains("3x2")){
            return 15000;
        }else if(this.modeGame.getSizeBoard().contains("4x3")){
            return 30000;
        }else if(this.modeGame.getSizeBoard().contains("4x4")){
            return 55000;
        }else if(this.modeGame.getSizeBoard().contains("5x4")){
            return 61000;
        }else if(this.modeGame.getSizeBoard().contains("6x5")){
            return 115000;
        }else{
            // 8x5
            return 155000;
        }

    }

    private void handleGameMode_TimeChallenge(View view){

        // Run countUpTimer
        if(this.isCountTimerRun == false){
            this.runCountDownTimer(getTimeCountDown());
        }

        if(this.isFlipUp(view.getId())){
            // if this card is flip then not do anything
            return;
        }

        if(this.pair.size() == 2){
            if(this.isAPair()){
                // Add ID card into list store card which flip up
                for(int ID : this.pair){
                    this.pair_card_complete_list.add(ID);
                }
            }else{
                for(int ID : this.pair){
                    this.eventFlipDownCard_animation(findViewById(ID));
                }
            }
            this.pair.removeAll(this.pair);
        }
        eventFlipCard_animation(view);
        this.pair.add(view.getId());

        if(this.pair.size() == 2){
            this.timesTry ++;
            setTimesTry();
        }

        // GAME WIN
        if(this.checkWin()){
            // Stop timer
            countDownTimer.cancel();

            this.IS_GAME_WIN = true;

            showDialog_CompleteGame();
            return;
        }
    }

    // Handle Mode: Limited Tries
    private int getTimeCanTries(){
        if(this.modeGame.getSizeBoard().contains("3x2")){
            return 5;
        }else if(this.modeGame.getSizeBoard().contains("4x3")){
            return 20;
        }else if(this.modeGame.getSizeBoard().contains("4x4")){
            return 30;
        }else if(this.modeGame.getSizeBoard().contains("5x4")){
            return 40;
        }else if(this.modeGame.getSizeBoard().contains("6x5")){
            return 50;
        }else{
            // 8x5
            return 60;
        }
    }

    private void handleGameMode_LimitedTries(View view){
        // Run countUpTimer
        if(this.isCountTimerRun == false){
            this.runCountUpTimer();
            timesTry = getTimeCanTries();
            setTimesTry();
        }

        if(this.isFlipUp(view.getId())){
            // if this card is flip then not do anything
            return;
        }

        if(this.pair.size() == 2){
            if(this.isAPair()){
                // Add ID card into list store card which flip up
                for(int ID : this.pair){
                    this.pair_card_complete_list.add(ID);
                }
            }else{
                for(int ID : this.pair){
                    this.eventFlipDownCard_animation(findViewById(ID));
                }
            }
            this.pair.removeAll(this.pair);
        }
        eventFlipCard_animation(view);
        this.pair.add(view.getId());

        if(this.pair.size() == 2){
            this.timesTry--;
            setTimesTry();

            // If tries = 0 => GAME OVER
            if(this.timesTry == 0){
                this.runCountUpTimer();

                this.IS_GAME_WIN = this.checkWin();

                showDialog_CompleteGame();
                return;
            }
        }

        // GAME WIN
        if(this.checkWin()){
            this.runCountUpTimer();

            this.IS_GAME_WIN = true;

            showDialog_CompleteGame();
            return;
        }
    }

    // Event Back to menu game
    private void eventBackToMenu(){
        try {
            Intent i = new Intent(this,FlipCardMemory_Menu.class);
            startActivity(i);
        }catch (Exception e ){

        }
    }

    // Check win
    // Mode: Sample
    private boolean isAPair(){
        // check whether card in pair list is same
        int card1 = this.pair.get(0);
        int card2 = this.pair.get(1);
        if(this.findCardByID(card1).getPair_number() == this.findCardByID(card2).getPair_number()){
            return true;
        }
        return false;
    }

    private boolean checkWin(){

        if((colmn * row) == this.pair_card_complete_list.size() || (this.pair_card_complete_list.size() == ((colmn*row) - 2) && this.pair.size() == 2)){
            return true;
        }
        return false;
    }



    // Dialog
    private void showDialog_CompleteGame(){
        final Dialog dialog = new Dialog(this);
        // Disable default title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // close dialog by click any where on screen
        dialog.setCancelable(false);

        dialog.setContentView(R.layout.complete_game_dialog);

        // init view for dialog
        final TextView btn_Replay = dialog.findViewById(R.id.btn_Replay) ;
        final TextView btn_Continue = dialog.findViewById(R.id.btn_Continue);
        final TableLayout tbl_Score = dialog.findViewById(R.id.tbl_Score);

        final TextView txt_Tries = dialog.findViewById(R.id.txt_Tries_DL);
        final TextView txt_Time = dialog.findViewById(R.id.txt_Time_DL);

        final TextView txt_Result = dialog.findViewById(R.id.txt_Result_DL);
        final TextView txt_ModeGame = dialog.findViewById(R.id.txt_ModeGame_DL);

        txt_Tries.setText("Tries: " + this.timesTry);

        txt_Result.setText(this.IS_GAME_WIN ? "DONE!" : "GAME OVER");
        String str_Color = this.IS_GAME_WIN ? "#00FF00" : "#FF0000";
        txt_Result.setTextColor(Color.parseColor(str_Color));

        txt_ModeGame.setText("Mode: " + modeGame.getModeName());

        int totalTime = 0;
         if(modeGame.getModeName().contains("Time Challenge")){
             totalTime = Integer.parseInt(((TextView)findViewById(R.id.txt_TimeDown)).getText().toString());
             txt_Time.setText("Time: " + ((TextView)findViewById(R.id.txt_TimeDown)).getText() + "s");
        }else{
             txt_Time.setText("Time: " + ((Chronometer)findViewById(R.id.chronometer_TimeUp)).getText());
             totalTime = (int)( SystemClock.elapsedRealtime() - ((Chronometer)findViewById(R.id.chronometer_TimeUp)).getBase());
             totalTime = (Math.round(totalTime/1000));
             Log.d("DEBUG", "showDialog_CompleteGame: " + (Math.round(totalTime/1000)));
         }

        DB_Handle handle = new DB_Handle(this);
        List<ScoreGame> scoreGame =  handle.getScoreOfModeGameByID(GAME_ID, modeGame.getModeName());
        // Save Score
        if(scoreGame.size() > 2){
            ScoreGame s = handle.getHighestTries(scoreGame);
            int maxTries = s.getTries();

            Log.d("TAG", "showDialog_CompleteGame: " + maxTries);
            Log.d("TAG", "totalTime: " + totalTime);
            Log.d("TAG", "s.getTime(): " + s.getTime());
            if((this.timesTry <= maxTries) ||
                    (this.timesTry == maxTries && totalTime < s.getTime())){
                handle.deleteScore(s.getID());
                handle.addNewScore(new ScoreGame(GAME_ID, modeGame.getModeName(), ((int) totalTime), timesTry));
            }
        }else{
            handle.addNewScore(new ScoreGame(GAME_ID, modeGame.getModeName(), ((int) totalTime), timesTry));
        }


        scoreGame =  handle.getScoreOfModeGameByID(GAME_ID, modeGame.getModeName());
        int rowIndex = 0;
        for(ScoreGame game : scoreGame){
            TableRow.LayoutParams rowTableParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
            rowTableParams.setMarginStart(200);
            TableRow row = new TableRow(this);
            if(rowIndex % 2 != 0){
                row.setBackgroundColor(Color.GRAY);

            }

            row.setLayoutParams(rowTableParams);

            TextView txtIndex = new TextView(this);
            txtIndex.setLayoutParams(new TableRow.LayoutParams(20, TableRow.LayoutParams.WRAP_CONTENT,1.0f));
            txtIndex.setText("\t\t\t\t" + game.getID());
            txtIndex.setTextColor(Color.WHITE);

            TextView txtTries = new TextView(this);
            txtTries.setText(game.getTries()+"");
            txtTries.setTextColor(Color.WHITE);

            TextView txtTime = new TextView(this);
            txtTime.setText(game.getTime()+" s");
            txtTime.setTextColor(Color.WHITE);

            row.addView(txtIndex);
            row.addView(txtTime);
            row.addView(txtTries);

            tbl_Score.addView(row);

            rowIndex++;
        }

        btn_Replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("DEBUG", "onClick Dialog Replay: ");

                dialog.hide();
                // Rest Tries
                timesTry = 0;
                setTimesTry();

                // Create new Board
                initSetting();

            }
        });

        btn_Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("DEBUG", "onClick Dialog Continue: ");
                dialog.hide();
                eventBackToMenu();
            }
        });



        dialog.show();
    }

    private void showDialog_LeaveGame(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MemoryCard.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        builder.setTitle(Html.fromHtml("<font color='#009788'>PAUSED</font>"));
        builder.setMessage("Do you want to leave the game?");

        builder.setCancelable(true);
        builder.setNegativeButton("LEAVE GAME", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                eventBackToMenu();
            }
        });

        builder.setPositiveButton("RESUME", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}


//https://stackoverflow.com/questions/46111262/card-flip-animation-in-android

//https://stackoverflow.com/questions/38118945/how-to-set-tablerow-width-to-max-in-android