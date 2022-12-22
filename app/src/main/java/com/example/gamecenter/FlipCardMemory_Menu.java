package com.example.gamecenter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.gamecenter.games.ModeGame;

public class FlipCardMemory_Menu extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private int currentTheme;
    private String currentSize;
    private String currentModeGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_card_memory_menu);

        getSupportActionBar().setTitle("Memory Game");
        getSupportActionBar().hide();

        this.currentTheme = R.id.number_theme;
        this.currentSize = "3x2";
        this.currentModeGame = "Sample";
        setColorCurrentThemeForText(R.id.txt_number_theme);

        eventChooseTheme();
        eventChooseSize();
        init_spinner();
    }

    private void init_spinner(){
        Spinner spinner_ModeGame = (Spinner) findViewById(R.id.spinner_ModeGame);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.mode_game, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner_ModeGame.setAdapter(adapter);

        spinner_ModeGame.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        String mode = parent.getItemAtPosition(position).toString();
        currentModeGame = mode;
        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
        ((TextView) parent.getChildAt(0)).setTypeface(((TextView) parent.getChildAt(0)).getTypeface(), Typeface.ITALIC);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void eventChooseTheme(){

        // Number Theme
        (findViewById(R.id.number_theme)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setColorCurrentThemeForText(R.id.txt_number_theme);
                currentTheme = R.id.number_theme;
            }
        });

        // Food Theme
        (findViewById(R.id.food_theme)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setColorCurrentThemeForText(R.id.txt_food_theme);
                currentTheme = R.id.food_theme;
            }
        });

        // Word Theme
        (findViewById(R.id.letter_theme)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setColorCurrentThemeForText(R.id.txt_letter_theme);
                currentTheme = R.id.letter_theme;
            }
        });

        // Word Theme
        (findViewById(R.id.animal_theme)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setColorCurrentThemeForText(R.id.txt_animal_theme);
                currentTheme = R.id.animal_theme;
            }
        });

        ((HorizontalScrollView) findViewById(R.id.theme_list)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    Log.d("DEBUG", "setColorCurrentThemeForText: " + view.getScrollX());
                    if(view.getScrollX() >= 450 && view.getScrollX() <= 1700){
                        ((HorizontalScrollView) findViewById(R.id.theme_list)).scrollTo(930,0);
                    }
                }

//                else if(view.getScrollX() < 930){
//                    ((HorizontalScrollView) findViewById(R.id.theme_list)).scrollTo(0,0);
//                }
//                else if(view.getScrollX() >1700){
//                    ((HorizontalScrollView) findViewById(R.id.theme_list)).scrollTo(2000,0);
//                }
                return false;
            }


        });
    }

    private void eventChooseSize(){

        int amount_tbl_row = ((TableLayout)findViewById(R.id.box_size)).getChildCount();
        for(int row=0;row < amount_tbl_row;row++){
            int amount_size = ((TableRow)((TableLayout)findViewById(R.id.box_size)).getChildAt(row)).getChildCount();
            for(int i=0;i<amount_size;i++){
                ((ImageView) ((TableRow)((TableLayout)findViewById(R.id.box_size)).getChildAt(row)).getChildAt(i)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String sizeBoard = getResources().getResourceName(view.getId()).replaceAll("com.example.gamecenter:id/","");
                        String theme = getResources().getResourceName(currentTheme).replaceAll("com.example.gamecenter:id/","");
                        currentModeGame = ((Spinner) findViewById(R.id.spinner_ModeGame)).getSelectedItem().toString();
                        ModeGame modeGame = new ModeGame(currentModeGame,sizeBoard, theme);
                        Log.d("DEBUG", "eventChooseSize: " + sizeBoard);
                        eventSendModeGameInfo(modeGame);
                    }
                });

            }
        }
    }

    private void eventSendModeGameInfo(ModeGame modeGame){
        Intent intent = new Intent(this,MemoryCard.class);
        intent.putExtra("ModeGameInfo",modeGame);
        startActivity(intent);
    }

    private void setColorCurrentThemeForText(int themeID){
        ((TextView)findViewById(R.id.txt_number_theme)).setTextColor(Color.WHITE);
        ((TextView)findViewById(R.id.txt_letter_theme)).setTextColor(Color.WHITE);
        ((TextView)findViewById(R.id.txt_food_theme)).setTextColor(Color.WHITE);

        ((TextView)findViewById(themeID)).setTextColor(Color.RED);
        if(themeID == R.id.txt_number_theme){
            ((HorizontalScrollView) findViewById(R.id.theme_list)).scrollTo(0,0);
        }else if(themeID == R.id.txt_letter_theme){
            ((HorizontalScrollView) findViewById(R.id.theme_list)).scrollTo(930,0);
        }else if(themeID == R.id.txt_food_theme){
            ((HorizontalScrollView) findViewById(R.id.theme_list)).scrollTo(2000,0);
        }

        // Width and Height of screen
        int width  = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;
//        float centerPoint_X = ((float) width/2) - ((ImageView)findViewById(themeID)).getDrawable().getIntrinsicWidth();
        int x, y;
        x = ((TextView) findViewById(themeID)).getLeft();
        y = ((TextView) findViewById(themeID)).getTop();
//        Log.d("DEBUG", "setColorCurrentThemeForText: " + ((HorizontalScrollView) findViewById(R.id.theme_list)).getScrollX());

    }


}

//https://stackoverflow.com/questions/18304460/how-to-get-the-center-coordinates-of-the-screen-in-android