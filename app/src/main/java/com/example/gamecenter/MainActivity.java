package com.example.gamecenter;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    boolean isFront = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        }catch (Exception e){

        }
    }
    private void SwitchActivity(){
        try {
            Intent i = new Intent(this, FlipCardMemory_Menu.class);
            startActivity(i);
        }catch (Exception e ){

        }
    }
}