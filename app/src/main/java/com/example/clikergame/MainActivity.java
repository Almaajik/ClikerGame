package com.example.clikergame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.TypedArray;
import android.media.AsyncPlayer;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    //initialisation des objets
    private ConstraintLayout layout;
    TextView t_time,t_restant,t_clicks,t_result,t_record;
    ImageButton b_click;
    private ObjectAnimator objectAnimatorY;
    private ObjectAnimator objectAnimatorAlpha;
    private ObjectAnimator objectAnimatorScaleY;
    private ObjectAnimator objectAnimatorsScaleX;
    private ImageView i_pokeball;
    public final String filename="scores.xml";
    Button b_start;

    //Timer
    CountDownTimer timer;
    int time = 15;

    //nb clicks
    int clicks = 0;

    //niveau
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

    //musik
    private MediaPlayer mediaPlayer;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instanciation des objets
        if(!createNewFile()){
            readAncientRecord();
        }
        t_time = (TextView) findViewById(R.id.t_time);
        t_restant = (TextView) findViewById(R.id.t_restant);
        b_click = (ImageButton) findViewById(R.id.b_clic);
        b_start = (Button) findViewById(R.id.b_start);
        t_clicks = (TextView) findViewById(R.id.t_clicks);
        t_result = (TextView) findViewById(R.id.t_result);
        t_record = (TextView) findViewById(R.id.t_record);
        this.i_pokeball = (ImageView) findViewById(R.id.i_pokeball);
        t_record.setText("Record : " + record);
        this.mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.poke_chill);
        b_start.setEnabled(true);
        b_click.setEnabled(false);
        layout = findViewById(R.id.c_layout);
        mediaPlayer.start();

        //redirection page des infos
        i_pokeball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent otherActivity = new Intent(getApplicationContext(), page_info.class);
                startActivity(otherActivity);
                finish();
            }
        });

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
                    // id_pokemon
                    id_pokemon= id_pokemon + value ;
                    b_click.setImageResource(resID);
                    b_click.setTag(resID);

                    niveau++;
                    if (clicks>record){
                        record = clicks;
                        saveFile();
                        t_record.setText("Record : " + record);
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


        //bouton pour cliquer sur l oeuf
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

                //changement de niveau
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

    //annimation

        b_click.setOnTouchListener(new View.OnTouchListener() {
        private TextView plus;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    plus = new TextView(getApplicationContext());
                    plus.setText(" +1 ");
                    plus.setX((float) (motionEvent.getX() + ((layout.getWidth() - b_click.getWidth()) / 2 ) ));
                    plus.setY(motionEvent.getY()+10);
                    plus.bringToFront();
                    layout.addView(plus);
                    String msg = String.valueOf("X : " + motionEvent.getX() + "      Y : " + motionEvent.getY());
                    Log.e("Activity", msg);
                    oneAnimation(plus);
                }
                return false;
            }
            public void oneAnimation(TextView plus) {
                AnimatorSet plus1 = new AnimatorSet();

                plus1.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        layout.removeView(plus);
                        Log.e("Suppresion","plus de plus");
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

                objectAnimatorY= ObjectAnimator.ofFloat(plus, "y", -50);
                objectAnimatorY.setDuration(1000);
                objectAnimatorY.setStartDelay(200);

                objectAnimatorAlpha=ObjectAnimator.ofFloat(plus, "alpha", 10, 0);
                objectAnimatorAlpha.setDuration(500);
                objectAnimatorAlpha.setStartDelay(200);

                objectAnimatorsScaleX=ObjectAnimator.ofFloat(plus, "scaleX", 1, 0);
                objectAnimatorsScaleX.setDuration(500);
                objectAnimatorsScaleX.setStartDelay(500);

                objectAnimatorScaleY=ObjectAnimator.ofFloat(plus, "scaleY", 1, 0);
                objectAnimatorScaleY.setDuration(500);
                objectAnimatorScaleY.setStartDelay(500);

                plus1.play(objectAnimatorY).with(objectAnimatorAlpha).with(objectAnimatorsScaleX).with(objectAnimatorScaleY);
                plus1.start();

            }
        });

    }

    public void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

    public void onPause() {
        super.onPause();
        mediaPlayer.stop();
    }

    //stockage local
    private boolean createNewFile() {
        File file = new File(getApplicationContext().getCacheDir(), filename);
        if(!file.exists()) {
            try {
                file.createNewFile();
                Log.e("test","File didn't exist, so I created it !");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void saveFile() {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(record);
            Log.e("json",json);
            write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(String json) throws IOException{
        File file = new File(getApplicationContext().getCacheDir(), filename);
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(json);
        bw.close();
    }

    private void readAncientRecord() {
        Gson gson = new Gson();
        try {
            Reader reader = new FileReader(getApplicationContext().getCacheDir()+"/"+filename);
            record = gson.fromJson(reader,int.class);
        /*if (gson.fromJson(reader,ScoresList.class) != null){
            scoresSaved = gson.fromJson(reader,ScoresList.class);
        }
        if (scoresSaved == null){
            scoresSaved = new ScoresList();
        }*/
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }



}