package com.example.devoir2_ift2905;

import android.content.Intent;

import android.graphics.Point;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

// TRAVAIL PAR mathieu perron 20076170 philippe auclair 0991331 n'goc phong
public class MainActivity extends AppCompatActivity {


    private Button b1;
    private int screenWidth;
    private int screenHeight;
    private int screenPlusPetit;
    private ConstraintLayout.LayoutParams layoutParams;

    //pour loi de FITTS
    int nombreEssais;
    double[] indiceDiff;
    // int[] tableauY;
    int[] tableauTailles;
    float[] tempsReaction;

    //pour le temps
    float tempClick;

    //max clics
    int essaisCourants = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombreEssais = getResources().getInteger(R.integer.nombreEssais);
        tempsReaction = new float[nombreEssais];
        tableauTailles = new int[nombreEssais];
        indiceDiff = new double[nombreEssais];

        // trouve la taille du ActionBar pour que l'on puisse le soustraire a la valeur de Y
        TypedValue tv = new TypedValue();
        this.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
        int actionBarHeight = getResources().getDimensionPixelSize(tv.resourceId);

        //maintenant on trouve la taille de l'ecran
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        // hauteur utilisable
        screenHeight = metrics.heightPixels - actionBarHeight;

        //on va chercher le bouton
        b1 = findViewById(R.id.bouton);

        b1.setText(R.string.start);
        //on impose un layout absolu au bouton
        layoutParams = (ConstraintLayout.LayoutParams)b1.getLayoutParams();

        //on enlève la restriction minimale pour le bouton
        // (regle le probleme de bouton rectangulaire lorsque petit)
        b1.setMinimumHeight(0);
        b1.setMinimumWidth(0);

        //on met un listener sur le bouton
        b1.setOnTouchListener(b1_listener);

        //determine le coté le plus petit de l'ecran
        screenPlusPetit = (screenWidth < screenHeight)? screenWidth : screenHeight;

    }

    //Quand on appui sur le bouton
    View.OnTouchListener b1_listener = new View.OnTouchListener() {
        public boolean onTouch(View view, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                if (essaisCourants <= getResources().getInteger(R.integer.nombreEssais)) {
                    if(essaisCourants == 0){
                        b1.setText(" ");//enleve le text apres
                    }else {
                        //On trouve le temps de reaction de l'utilisateur
                        tempsReaction[essaisCourants - 1] = (SystemClock.uptimeMillis() - tempClick);
                    }
                    if (essaisCourants == getResources().getInteger(R.integer.nombreEssais)) {
                        nouvelleActivite();
                    }
                }
            }
            if (event.getAction() == MotionEvent.ACTION_UP && essaisCourants < getResources().getInteger(R.integer.nombreEssais)) {
                tempClick = SystemClock.uptimeMillis(); //On store le temps ou l'action c'est declanche
                essaisCourants++;
                placerBouton();
            }
            return true;
        }
    };

    // cette methode gèrera le placeage du bouton
    private void placerBouton(){

        int tailleBouton = genererTaille();
        tableauTailles[essaisCourants-1] = (tailleBouton);
        layoutParams.width = tailleBouton;
        layoutParams.height = tailleBouton;

        Point point = genererPoint(tailleBouton);
        indiceDiff[essaisCourants-1] = Math.log((Math.sqrt(Math.pow(point.x, 2) + Math.pow(point.y, 2)) / tailleBouton) + 1);
        layoutParams.leftMargin = point.x;
        layoutParams.topMargin = point.y;
        
        b1.setLayoutParams(layoutParams);

    }

    private Point genererPoint(int tailleBouton) {
        Point coordonnees = new Point();
        int x = (int)(Math.random() * (screenWidth - tailleBouton));
        int y = (int)(Math.random() * (screenHeight - tailleBouton));
        coordonnees.set(x,y);
        return coordonnees;
    };

    private int genererTaille(){
        int min = screenPlusPetit /getResources().getInteger(R.integer.ratioPlusPetit);
        int max = screenPlusPetit /getResources().getInteger(R.integer.ratioPlusGrand);

        return (int)(Math.random() * (max - min) + min);
    }

    private void nouvelleActivite() {
        Bundle bundle = new Bundle();
        bundle.putIntArray("tableauTailles", tableauTailles);
        bundle.putFloatArray("tempsReaction", tempsReaction);
        bundle.putDoubleArray("indiceDiff", indiceDiff);
        // bundle.putIntArray("tableauY", tableauY);
        Intent intent = new Intent(this, Main2Activity.class);
        intent.putExtra("indiceDiff", bundle);
        startActivity(intent);
    }
}