package com.example.clikergame;

import androidx.appcompat.app.AppCompatActivity;


import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    //initialisation des objets
    TextView t_time,t_restant,t_clicks,t_result,t_record;
    ImageButton b_click;
    Button b_start;

    //Timer
    CountDownTimer timer;
    int time = 15;

    //nb clicks
    int clicks = 0;

    //nb clicks par niveau
    int niveau = 1;

    //nb clicks necessaire par niveau
    int c_niveau = 15;

    //affichage du record
    int record = 0;

    //piece
    int piece = 0;

    //id_pokemon
    TypedArray img_pokemon;
    int min = 1;
    int max = 898;
    String id_pokemon = "_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instanciation des objets
        t_time = (TextView) findViewById(R.id.t_time);
        t_restant = (TextView) findViewById(R.id.t_restant);
        b_click = (ImageButton) findViewById(R.id.b_clic);
        b_start = (Button) findViewById(R.id.b_start);
        t_clicks = (TextView) findViewById(R.id.t_clicks);
        t_result = (TextView) findViewById(R.id.t_result);
        t_record = (TextView) findViewById(R.id.t_record);

        b_start.setEnabled(true);
        b_click.setEnabled(false);




        //methode du timer
        timer = new CountDownTimer(14000, 1000) {
            @Override
            public void onTick(long l) {
                time--;
                t_time.setText("Time: " + time);
            }

            @Override
            public void onFinish() {
                b_start.setEnabled(true);
                b_click.setEnabled(false);
                t_time.setText("Time: 0");
                if(clicks >= c_niveau){
                //affichage des résultats
                    t_result.setText("Bravo");
                    t_result.setVisibility(View.VISIBLE);



                    img_pokemon = getResources().obtainTypedArray(R.array.img_pokemon);
                    Random random = new Random();

                    int value = random.nextInt(img_pokemon.length());
                    int resID = img_pokemon.getResourceId(value,0);
                    //TODO id_pokemon
                    id_pokemon= id_pokemon + value ;
                    b_click.setImageResource(resID);
                    b_click.setTag(resID);

                    niveau++;
                    if (clicks>record){
                        record = clicks;
                        t_record.setText("Reccord : " + record);
                    }
                    if(niveau == 5){
                        niveau = 1;
                    }

                            if (niveau==1){
                                c_niveau = 15;
                            }

                            if (niveau==2){
                                c_niveau = 30;
                            }

                            if (niveau==3){
                                c_niveau = 45;
                            }

                            if (niveau==4){
                            c_niveau = 60;
                            }

                            if (niveau==5){
                                c_niveau = 75;
                            }


                 }else {
                    t_result.setText("Perdu");
                    t_result.setVisibility(View.VISIBLE);
                }
            }
        };

        b_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicks++;
                t_restant.setText("Réalisé: " + clicks);

            }
        });

        b_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.start();
                b_start.setEnabled(false);
                b_click.setEnabled(true);
                clicks = 0;
                time = 15;
                t_time.setText("Time: " + time);
                t_clicks.setText("Clicks :" + c_niveau);
                t_restant.setText("Réalisé: " + clicks);
                t_clicks.setText("Clicks :" + c_niveau);
                if (niveau==1){
                    b_click.setImageResource(R.drawable.oeuf1);
                }

                if (niveau==2){
                    b_click.setImageResource(R.drawable.oeuf2);
                }

                if (niveau==3){
                    b_click.setImageResource(R.drawable.oeuf3);
                }

                if (niveau==4){
                    b_click.setImageResource(R.drawable.oeuf4);
                }

                if (niveau==5){
                    b_click.setImageResource(R.drawable.oeuf5);
                }

                t_result.setVisibility(View.INVISIBLE);
            }
        });



    }
}