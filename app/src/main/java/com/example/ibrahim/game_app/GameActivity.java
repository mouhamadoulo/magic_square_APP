package com.example.ibrahim.game_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GameActivity extends AppCompatActivity {

    //Variables utiles au bon fonctionnement du programme
    int sumLINE1;
    int sumLINE2;
    int sumLINE3;
    int sumCOLUMN1;
    int sumCOLUMN2;
    int sumCOLUMN3;
    int[] numbersAllowed = {1,2,3,4,5,6,7,8,9};
    int[] tabGAME = new int[9];
    Random rnd = new Random();
    List indicesChoisis = new ArrayList();
    int indice;
    /*------------------------*/

    //Variables grâce auxquelles on va pouvoir récupérer les composants de notre GUI
    Button btn_submit;
    Button btn_continue;
    Button btn_newgame;
    Button btn_exitgame;
    /*------------------------*/
    EditText edtCELL1;
    EditText edtCELL2;
    EditText edtCELL3;
    EditText edtCELL4;
    EditText edtCELL5;
    EditText edtCELL6;
    EditText edtCELL7;
    EditText edtCELL8;
    EditText edtCELL9;
    /*------------------------*/
    TextView result_LINE1;
    TextView result_LINE2;
    TextView result_LINE3;
    TextView result_COLUMN1;
    TextView result_COLUMN2;
    TextView result_COLUMN3;
    /*------------------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //récupération des composants
        btn_submit = (Button) findViewById(R.id.btnSUBMIT);
        btn_continue = (Button) findViewById(R.id.btnCONTINUE);
        btn_newgame = (Button) findViewById(R.id.btnNEWGAME);
        btn_exitgame = (Button) findViewById(R.id.btnEXITGAME);
        /*----------------------------------------------------------*/
        edtCELL1 = (EditText) findViewById(R.id.edt1);
        edtCELL2 = (EditText) findViewById(R.id.edt2);
        edtCELL3 = (EditText) findViewById(R.id.edt3);
        edtCELL4 = (EditText) findViewById(R.id.edt4);
        edtCELL5 = (EditText) findViewById(R.id.edt5);
        edtCELL6 = (EditText) findViewById(R.id.edt6);
        edtCELL7 = (EditText) findViewById(R.id.edt7);
        edtCELL8 = (EditText) findViewById(R.id.edt8);
        edtCELL9 = (EditText) findViewById(R.id.edt9);
        /*----------------------------------------------------------*/
        result_LINE1 = (TextView) findViewById(R.id.resLINE1);
        result_LINE2 = (TextView) findViewById(R.id.resLINE2);
        result_LINE3 = (TextView) findViewById(R.id.resLINE3);
        result_COLUMN1 = (TextView) findViewById(R.id.resCOLUMN1);
        result_COLUMN2 = (TextView) findViewById(R.id.resCOLUMN2);
        result_COLUMN3 = (TextView) findViewById(R.id.resCOLUMN3);
        /*----------------------------------------------------------*/


        //Remplissage des positions des nombres dans notre Jeu
        for(int i=0;i<9;i++){
            int valeurIndex = genIndiceOnce();
            int valeur = numbersAllowed[valeurIndex];
            tabGAME[i] = valeur;
        }
        /*----------------------------------------------------------*/

        //Ajout d'événements sur les différents boutons
        btn_submit.setOnClickListener(new Soumettre());
        btn_continue.setOnClickListener(new Continuer());
        btn_newgame.setOnClickListener(new NouveauJeu());
        btn_exitgame.setOnClickListener(new ExitGame());
        /*----------------------------------------------------------*/
    }

    //Classe interne gérant le fait de cliquer sur le bouton SUBMIT
    class Soumettre implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }
    //Classe interne gérant le fait de cliquer sur le bouton SUBMIT
    class Continuer implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }
    //Classe interne gérant le fait de cliquer sur le bouton SUBMIT
    class NouveauJeu implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }
    //Classe interne gérant le fait de cliquer sur le bouton SUBMIT
    class ExitGame implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            finish();
        }
    }

    //méthode pour générer un nombre entier aléatoire entre 0 et 8 (inclus)
    public int genIndiceOnce(){
            int val = rnd.nextInt(9);
            if(indicesChoisis.contains(val)){
                genIndiceOnce();
            }else{
                indice = val;
                indicesChoisis.add(val);
            }
            return indice;
            //Log.i("INDEX",""+indice);
            //int valeur = numbersAllowed[indice_aleatoire];
            //int place = rnd.nextInt(9);
            //tabGAME[place] = valeur;
    }
}
