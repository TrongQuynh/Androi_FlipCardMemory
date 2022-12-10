package com.example.gamecenter;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.gamecenter.R;
import com.example.gamecenter.games.CardInfo;
import com.example.gamecenter.games.ModeGame;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MemoryCard extends AppCompatActivity {

    private int row = 4;
    private int colmn = 3;
    private ModeGame modeGame;
    private int number_of_pair = (row * colmn) / 2;
    private ArrayList<Integer> card_flip_list = new ArrayList<Integer>();// Store ID card
    private ArrayList<CardInfo> cardInfo_list = new ArrayList<CardInfo>();
    private ArrayList<Integer> pair = new ArrayList<Integer>(); // Store ID card
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_card);

        this.modeGame = getIntent().getParcelableExtra("ModeGameInfo");
        row = modeGame.getAmountRow();
        colmn = modeGame.getAmountColumn();
        number_of_pair = (row * colmn) / 2;

        initBoard();
    }

    // Init board layout
    private void initBoard(){
        TableLayout board = (TableLayout) findViewById(R.id.board_layout);

        for(int i = 0;i<row;i++){
            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(280, 280,1.0f);
//            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 300);
            TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);

            lp.gravity = Gravity.CENTER;
//            row.setBackgroundColor(Color.YELLOW);
            row.setLayoutParams(tableRowParams);

            for(int y = 0;y<colmn;y++){
                ImageView imageView = new ImageView(this);

                imageView.setBackgroundColor(Color.rgb(50,50,50));
                imageView.setLayoutParams(lp);
                imageView.setId(View.generateViewId());
                int pair_number = generatePairNumber();
                int imageCard = generateCardImage(pair_number);
                CardInfo cardInfo = new CardInfo(imageView.getId(),pair_number, imageCard);
                cardInfo_list.add(cardInfo);
                imageView.setImageResource(R.drawable.dot);
//                imageView.setImageResource(imageCard);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        handleGameMode_Sample(view);
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
        if(this.modeGame.getThemeName() == "number_theme"){
            all_image = getResources().getStringArray(R.array.number_theme);
        }else if(this.modeGame.getThemeName() == "food_theme"){
            all_image = getResources().getStringArray(R.array.food_theme);
        }

        String url = "drawable/"+all_image[pair_number];
        int imageKey = getResources().getIdentifier(url, "drawable", getPackageName());
        return imageKey;
    }

    
    // Check whether this card is flip up or down
    boolean isFlipUp(int cardID){
        for(int i=0;i<card_flip_list.size();i++){
            if(cardID == card_flip_list.get(i)){
                return true;
            }
        }
        return false;
    }

    // remove index card from card flip up list
    private void removeFromFlipUpList(int cardID){
        try {
            card_flip_list.remove(Integer.valueOf(cardID));
        }catch (Exception e){

        }
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

    private void handleGameMode_Sample(View view){
        if(this.isFlipUp(view.getId())){
            // if this card is flip then not do anything
            return;
        }

        if(this.pair.size() == 2){
            if(this.isAPair()){
                // Add ID card into list store card which flip up
                for(int ID : this.pair){
                    this.card_flip_list.add(ID);
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

        if(this.checkWin_Sample()){
            showDialog_CompleteGame();
            return;
        }
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
                    ((ImageView)view).setImageResource(R.drawable.dot);
                    ((ImageView)view).setBackgroundColor(Color.rgb(50,50,50));

//                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 300,1.0f);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(280, 280,1.0f);
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

        TableRow.LayoutParams lp = new TableRow.LayoutParams(280, 280,1.0f);
        ((ImageView)view).setLayoutParams(lp);
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

    private boolean checkWin_Sample(){

        if((colmn * row) == this.card_flip_list.size() || (this.card_flip_list.size() == ((colmn*row) - 2) && this.pair.size() == 2)){
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
        dialog.setCancelable(true);

        dialog.setContentView(R.layout.complete_game_dialog);

        // init view for dialog
        final Button btn_Replay = dialog.findViewById(R.id.btn_Replay) ;

        btn_Replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initBoard();
            }
        });

        dialog.show();
    }

}


//https://stackoverflow.com/questions/46111262/card-flip-animation-in-android

//https://stackoverflow.com/questions/38118945/how-to-set-tablerow-width-to-max-in-android