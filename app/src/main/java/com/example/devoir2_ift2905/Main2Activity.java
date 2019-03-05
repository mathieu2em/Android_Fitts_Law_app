package com.example.devoir2_ift2905;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.Arrays;

public class Main2Activity extends AppCompatActivity {


    double[] indiceDiff;
    int[] tableauTailles;
    float[] tempsReaction;
    Button exporter;
    double R2;
    double A;
    double B;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // pour le bouton
        exporter = findViewById(R.id.exporter);
        exporter.setOnTouchListener(exporter_listener);
        // pour le bouton retour en arriere
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Pour le nombre de decimals
        DecimalFormat numberFormat = new DecimalFormat("#.000000");

        Bundle bundle = getIntent().getExtras().getBundle("indiceDiff");
        tableauTailles = bundle.getIntArray("tableauTailles");
        tempsReaction = bundle.getFloatArray("tempsReaction");
        indiceDiff = bundle.getDoubleArray("indiceDiff");
        System.out.println("indiceDiff: " + Arrays.toString(indiceDiff));

        calculerLoiFitts();
        DecimalFormat formatter = new DecimalFormat("#.######");
        TextView texteA = (TextView) findViewById(R.id.a);
        texteA.setText("a = " + formatter.format(A));
        TextView texteB = (TextView) findViewById(R.id.b);
        texteB.setText("b = " + formatter.format(B));
        TextView texteR2 = (TextView) findViewById(R.id.r2);
        texteR2.setText("r2 = " + formatter.format(R2));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyc);
        recyclerView.addItemDecoration(new DividerItemDecoration(Main2Activity.this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        int nbrElems = indiceDiff.length;
        String[] resultats = new String[nbrElems];
        String[] resultats2 = new String[nbrElems];

        for (int i = 0; i < nbrElems; i++) {
            resultats[i] = "Essai " + (i + 1);
        }

        for (int i = 0; i < nbrElems; i++) {
            resultats2[i] ="Difficulté: " + formatter.format(indiceDiff[i]) + ", " + "Durée: " + (int) tempsReaction[i] + " ms";
        }

        recyclerView.setAdapter(new Adapter(resultats, resultats2));

    }
    //Quand on appui sur le bouton
    View.OnTouchListener exporter_listener = new View.OnTouchListener() {
        public boolean onTouch(View view, MotionEvent event) {
            System.out.println("test");
            if (event.getAction() == MotionEvent.ACTION_UP) {
                String filename = "myfile";
                String fileContents = "test";
                FileOutputStream outputStream;

                try{
                    outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                    outputStream.write(fileContents.getBytes());
                    outputStream.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            return true;
        }
    };

    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        return true;
    }

    private void calculerLoiFitts() {

        double avgX = getAverage(indiceDiff);
        double avgY = getAverage(tempsReaction);

        int taille = indiceDiff.length;
        double[] tabX2 = new double[taille];
        double[] tabY2 = new double[taille];
        double[] tabXY = new double[taille];
        double[] covarianceNum = new double[taille];
        double[] varianceNum = new double[taille];

        for (int i = 0; i < indiceDiff.length; i++) {

            tabX2[i] = Math.pow(indiceDiff[i], 2);
            tabY2[i] = Math.pow(tempsReaction[i], 2);
            tabXY[i] = indiceDiff[i] * tempsReaction[i];
            covarianceNum[i] = (indiceDiff[i] - avgX) * (tempsReaction[i] - avgY);
            varianceNum[i] = Math.pow((indiceDiff[i] - avgX), 2);
        }
        /* Tableau des resultats de l'application de la loi de Fitts
         * Dans l'ordre, c'est alpha, beta et r2
         */
        float num = (float) getSum(covarianceNum);
        float den = (float) getSum(varianceNum);
        B = num/den;
        A = avgY - (B * avgX);
        //calcul de r
        double denominateur = 0;
        double numerateur = 0;
        // denominateur
        for ( int i =0; i<tempsReaction.length; i++){
            denominateur += Math.pow(tempsReaction[i] - avgY, 2);
            numerateur += Math.pow((tempsReaction[i] - (A + B*indiceDiff[i])),2);
        }
        R2 = (1 - numerateur/denominateur);
    }

    private int getSum(int[] tabEntiers) {
        int sum = 0;

        for (int i =0 ; i<tabEntiers.length;i++){
            sum += tabEntiers[i];
        }

        return sum;
    }

    private double getSum(double[] tabDoubles) {
        double sum = 0;

        for (int i = 0;i<tabDoubles.length; i++) {
            sum += tabDoubles[i];
        }

        return sum;
    }

    private float getSum(float[] tabFloat) {
        float sum = 0;

        for (int i = 0;i<tabFloat.length; i++) {
            sum += tabFloat[i];
        }

        return sum;
    }

    private float getAverage(float[] tabEntiers) {
        float sum = getSum(tabEntiers);
        return sum / tabEntiers.length;
    }

    private double getAverage(double[] tabDoubles) {
        double sum = getSum(tabDoubles);
        return sum / tabDoubles.length;
    }
}