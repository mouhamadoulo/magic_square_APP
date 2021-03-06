package com.example.ibrahim.game_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    int cptHELP = 5;//on n'a droit qu'à 5 aides
    Random rnd1 = new Random();
    Random rnd2 = new Random();
    List indicesChoisis = new ArrayList();
    List indicesBTNHELP = new ArrayList();
    int indice1,indice2;
    MediaPlayer mymedia1;
    int nbHELP = 0;
    int score;
    /*------------------------*/

    //Variables grâce auxquelles on va pouvoir récupérer les composants de notre GUI
    Button btn_submit;
    Button btn_newgame;
    Button btn_exitgame;
    Button btn_help;
    ImageView img_info;
    Button btn_scores;
    Chronometer mychrono;
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

    //Variables utiles pour manipuler notre base de données
    private SQLiteDatabase sqlitedb = null;
    private MaBaseHelper mybasedb = null;
    /*-----------------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //récupération des composants
        btn_submit = (Button) findViewById(R.id.btnSUBMIT);
        btn_newgame = (Button) findViewById(R.id.btnNEWGAME);
        btn_exitgame = (Button) findViewById(R.id.btnEXITGAME);
        btn_help = (Button) findViewById(R.id.btnHELP);
        img_info = (ImageView) findViewById(R.id.imgINFO);
        btn_scores = (Button) findViewById(R.id.btn_scores);
        mychrono = (Chronometer) findViewById(R.id.chrono);
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

        //on démarre le chrono
        mychrono.start();

        //Positions des nombres(1 à 9) dans notre Jeu
        for(int i=0;i<9;i++){
            int valeurIndex = genIndiceOnce();
            int valeur = numbersAllowed[valeurIndex];
            tabGAME[i] = valeur;
        }
        sumLINE1 = tabGAME[0]+tabGAME[1]+tabGAME[2];
        sumLINE2 = tabGAME[3]+tabGAME[4]+tabGAME[5];
        sumLINE3 = tabGAME[6]+tabGAME[7]+tabGAME[8];
        sumCOLUMN1 = tabGAME[0]+tabGAME[3]+tabGAME[6];
        sumCOLUMN2 = tabGAME[1]+tabGAME[4]+tabGAME[7];
        sumCOLUMN3 = tabGAME[2]+tabGAME[5]+tabGAME[8];
        result_LINE1.setText(""+sumLINE1);
        result_LINE2.setText(""+sumLINE2);
        result_LINE3.setText(""+sumLINE3);
        result_COLUMN1.setText(""+sumCOLUMN1);
        result_COLUMN2.setText(""+sumCOLUMN2);
        result_COLUMN3.setText(""+sumCOLUMN3);
        /*----------------------------------------------------------*/

        //au démarrage on rend le bouton "NEW GAME" non clickable
        btn_newgame.setEnabled(false);

        //Ajout d'événements sur les différents boutons de notre GUI
        btn_submit.setOnClickListener(new Soumettre());
        btn_newgame.setOnClickListener(new NouveauJeu());
        btn_exitgame.setOnClickListener(new ExitGame());
        btn_help.setOnClickListener(new HelpMePlease());
        img_info.setOnClickListener(new AboutTheGame());
        btn_scores.setOnClickListener(new ShowScores());
        /*----------------------------------------------------------*/
    }

    //Classe interne gérant le fait de cliquer sur le bouton SUBMIT
    class Soumettre implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //On vérifie que tous les cases ont bien été remplies
            if(!edtCELL1.getText().toString().equals("") && !edtCELL2.getText().toString().equals("") &&
                    !edtCELL3.getText().toString().equals("") && !edtCELL4.getText().toString().equals("") &&
                    !edtCELL5.getText().toString().equals("") && !edtCELL6.getText().toString().equals("") &&
                    !edtCELL7.getText().toString().equals("") && !edtCELL8.getText().toString().equals("") &&
                    !edtCELL9.getText().toString().equals("")){
                //On vérifie la conformité des nombres remplis par rapport aux règles
                if(checkNumbers() == 0){//Présence de 0
                    afficherDialbox("Chiffre Zéro","Le nombre 0 n'est pas autorisé");
                }else if(checkNumbers() == 1){//Présence de 2 valeurs identiques
                    afficherDialbox("Valeurs identiques","Il ne doit pas y avoir 2 valeurs identiques");
                }else {//checkNumbers() retourne 2 --> donc tt est OK
                    if(correctAnswers()){//Bonne Réponse
                        btn_newgame.setEnabled(true);
                        mychrono.stop();
                        score = calculScore();
                        mybasedb.addScore(score);
                        //Les félicitations sont de rigueur
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(GameActivity.this);
                        LayoutInflater inflater = GameActivity.this.getLayoutInflater();
                        View alertDialogView = inflater.inflate(R.layout.congrats_dialbox, null);
                        alertDialog.setView(alertDialogView);
                        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                afficherDialbox("Score","Votre score est de : "+score);
                            }
                        });
                        alertDialog.show();
                        btn_help.setEnabled(false);
                        mymedia1 = new MediaPlayer();
                        mymedia1 = MediaPlayer.create(GameActivity.this, R.raw.applause);
                        mymedia1.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mymedia1.start();


                    }else{//Mauvaise Réponse
                        afficherDialbox("Mauvaise Réponse","Vérifier bien la position des nombres");
                        btn_newgame.setEnabled(true);
                    }
                }
            }else{
                afficherDialbox("Chiffre manquant","Il faut remplir toutes les cases");
            }
        }
    }
    //Classe interne gérant le fait de cliquer sur l'image du trophée pour afficher les meilleurs scores
    class ShowScores implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }
    //Classe interne gérant le fait de cliquer sur le bouton NEW GAME
    class NouveauJeu implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(),GameActivity.class);
            startActivity(i);
            finish();
        }
    }
    //Classe interne gérant le fait de cliquer sur le bouton EXIT GAME
    class ExitGame implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            finish();
        }
    }
    //Classe interne gérant le fait de cliquer sur le bouton help
    class HelpMePlease implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            nbHELP++;
            int index = genIndiceForHelp();
            switch (index){
                case 0 :
                    edtCELL1.setText(""+tabGAME[0]);
                    edtCELL1.setEnabled(false);
                    edtCELL1.setBackgroundColor(getResources().getColor(R.color.green));
                    break;
                case 1 :
                    edtCELL2.setText(""+tabGAME[1]);
                    edtCELL2.setEnabled(false);
                    edtCELL2.setBackgroundColor(getResources().getColor(R.color.green));
                    break;
                case 2 :
                    edtCELL3.setText(""+tabGAME[2]);
                    edtCELL3.setEnabled(false);
                    edtCELL3.setBackgroundColor(getResources().getColor(R.color.green));
                    break;
                case 3 :
                    edtCELL4.setText(""+tabGAME[3]);
                    edtCELL4.setEnabled(false);
                    edtCELL4.setBackgroundColor(getResources().getColor(R.color.green));
                    break;
                case 4 :
                    edtCELL5.setText(""+tabGAME[4]);
                    edtCELL5.setEnabled(false);
                    edtCELL5.setBackgroundColor(getResources().getColor(R.color.green));
                    break;
                case 5 :
                    edtCELL6.setText(""+tabGAME[5]);
                    edtCELL6.setEnabled(false);
                    edtCELL6.setBackgroundColor(getResources().getColor(R.color.green));
                    break;
                case 6 :
                    edtCELL7.setText(""+tabGAME[6]);
                    edtCELL7.setEnabled(false);
                    edtCELL7.setBackgroundColor(getResources().getColor(R.color.green));
                    break;
                case 7 :
                    edtCELL8.setText(""+tabGAME[7]);
                    edtCELL8.setEnabled(false);
                    edtCELL8.setBackgroundColor(getResources().getColor(R.color.green));
                    break;
                case 8 :
                    edtCELL9.setText(""+tabGAME[8]);
                    edtCELL9.setEnabled(false);
                    edtCELL9.setBackgroundColor(getResources().getColor(R.color.green));
                    break;
                default:
                    Log.i("Erreur","Valeur non correspondante");
                    break;
            }
            cptHELP--;
            btn_help.setText("HELP : "+cptHELP);
            if(cptHELP == 0){
                btn_help.setEnabled(false);
            }
        }
    }
    //Classe interne gérant le fait de cliquer sur l'image ? pour comprendre le but du jeu
    class AboutTheGame implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(GameActivity.this);
            LayoutInflater inflater = GameActivity.this.getLayoutInflater();
            View alertDialogView = inflater.inflate(R.layout.about_dialbox, null);
            alertDialog.setView(alertDialogView);
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        }
    }

    //méthode pour persister les données dans une base de données SQLite du téléphone
    public void saveScore(int score){
        /*mybasedb = new MaBaseHelper(this);
        sqlitedb = mybasedb.getWritableDatabase();*/
    }

    //méthode pour générer un nombre entier aléatoire entre 0 et 8 (inclus)
    public int genIndiceOnce(){
            int val = rnd1.nextInt(9);
            if(indicesChoisis.contains(val)){
                genIndiceOnce();
            }else{
                indice1 = val;
                indicesChoisis.add(val);
            }
            return indice1;
    }

    //méthode pour générer un indice une seule fois (entre 0 et 8)
    public int genIndiceForHelp(){
        int val = rnd2.nextInt(9);
        if(indicesBTNHELP.contains(val)){
            genIndiceForHelp();
        }else{
            indice2 = val;
            indicesBTNHELP.add(val);
        }
        return indice2;
    }

    /*méthode pour vérifier que notre remplissage est conforme aux règles
    * return 0 s'il y'a le nombre 0
    * return 1 s'il y'a deux valeurs identiques
    * return 2 si tout est OK
    * */
    public int checkNumbers(){
        int numb1 = Integer.parseInt(edtCELL1.getText().toString());
        int numb2 = Integer.parseInt(edtCELL2.getText().toString());
        int numb3 = Integer.parseInt(edtCELL3.getText().toString());
        int numb4 = Integer.parseInt(edtCELL4.getText().toString());
        int numb5 = Integer.parseInt(edtCELL5.getText().toString());
        int numb6 = Integer.parseInt(edtCELL6.getText().toString());
        int numb7 = Integer.parseInt(edtCELL7.getText().toString());
        int numb8 = Integer.parseInt(edtCELL8.getText().toString());
        int numb9 = Integer.parseInt(edtCELL9.getText().toString());
        int[] tabValues = {numb1,numb2,numb3,numb4,numb5,numb6,numb7,numb8,numb9};
        //on vérifie la présence du nombre 0 dans ce qu'on a rempli
        for(int i=0;i<9;i++){
            if(tabValues[i] == 0){
                return 0;
            }
        }
        //on vérifie s'il y'a des valeurs dupliquées dans ce qu'on a rempli
        for(int i=0;i<(9-1);i++){
            for(int j=i+1;j<9;j++){
                if(tabValues[i] == tabValues[j]){
                    return 1;
                }
            }
        }
        return 2;
    }

    //méthode pour créer et afficher une boite de dialogue avec un titre et un message
    public void afficherDialbox(String titre,String message){
        AlertDialog alertDialog = new AlertDialog.Builder(GameActivity.this).create();
        alertDialog.setTitle(titre);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    //méthode pour vérifier que l'utilisateur a trouvé la bonne position pour chaque valeur du tableau
    public boolean correctAnswers(){
        int numb1 = Integer.parseInt(edtCELL1.getText().toString());
        int numb2 = Integer.parseInt(edtCELL2.getText().toString());
        int numb3 = Integer.parseInt(edtCELL3.getText().toString());
        int numb4 = Integer.parseInt(edtCELL4.getText().toString());
        int numb5 = Integer.parseInt(edtCELL5.getText().toString());
        int numb6 = Integer.parseInt(edtCELL6.getText().toString());
        int numb7 = Integer.parseInt(edtCELL7.getText().toString());
        int numb8 = Integer.parseInt(edtCELL8.getText().toString());
        int numb9 = Integer.parseInt(edtCELL9.getText().toString());
        if(numb1 == tabGAME[0] && numb2 == tabGAME[1] && numb3 == tabGAME[2]
                && numb4 == tabGAME[3] && numb5 == tabGAME[4] && numb6 == tabGAME[5]
                && numb7 == tabGAME[6] && numb8 == tabGAME[7] && numb9 == tabGAME[8]){
            return true;
        }else{
            return false;
        }
    }

    //méthode de calcul du score de l'utilisateur
    /**
     * Le score est calculé en fonction du chrono et du nombre de fois que l'utilisateur
     * clique sur le bouton HELP pour afficher un nombre
     */
    public int calculScore(){
        int thescore = 100;//par défaut quand on réussit le jeu on a déjà 100 points
        Character c1 = mychrono.getText().charAt(0);//minute de mon chrono
        Character c2 = mychrono.getText().charAt(1);//minute de mon chrono
        //Suivant le chrono et le nombre de fois qu'on clique sur le bouton HELP
        if(c1.charValue() == '0'){
            if(c2.charValue() == '0'){//si le jeu termine en moins de 1mn
                thescore = thescore + 750;
                if(nbHELP == 0){
                    thescore = thescore + 450;
                }
                if(nbHELP == 1){
                    thescore = thescore + 250;
                }
                if(nbHELP == 2){
                    thescore = thescore + 200;
                }
                if(nbHELP == 3){
                    thescore = thescore + 150;
                }
                if(nbHELP == 4){
                    thescore = thescore + 100;
                }
                if(nbHELP == 5){
                    thescore = thescore + 50;
                }
            }
            if(c2.charValue() == '1'){//si le jeu termine entre 1mn et 2mn
                thescore = thescore + 550;
                if(nbHELP == 0){
                    thescore = thescore + 350;
                }
                if(nbHELP == 1){
                    thescore = thescore + 250;
                }
                if(nbHELP == 2){
                    thescore = thescore + 200;
                }
                if(nbHELP == 3){
                    thescore = thescore + 150;
                }
                if(nbHELP == 4){
                    thescore = thescore + 100;
                }
                if(nbHELP == 5){
                    thescore = thescore + 50;
                }
            }
            if(c2.charValue() == '2'){//si le jeu termine entre 2mn et 3mn
                thescore = thescore + 350;
                if(nbHELP == 0){
                    thescore = thescore + 300;
                }
                if(nbHELP == 1){
                    thescore = thescore + 250;
                }
                if(nbHELP == 2){
                    thescore = thescore + 200;
                }
                if(nbHELP == 3){
                    thescore = thescore + 150;
                }
                if(nbHELP == 4){
                    thescore = thescore + 100;
                }
                if(nbHELP == 5){
                    thescore = thescore + 50;
                }
            }
            if(c2.charValue() == '3'){//si le jeu termine entre 3mn et 4mn
                thescore = thescore + 250;
                if(nbHELP == 0){
                    thescore = thescore + 250;
                }
                if(nbHELP == 1){
                    thescore = thescore + 250;
                }
                if(nbHELP == 2){
                    thescore = thescore + 200;
                }
                if(nbHELP == 3){
                    thescore = thescore + 150;
                }
                if(nbHELP == 4){
                    thescore = thescore + 100;
                }
                if(nbHELP == 5){
                    thescore = thescore + 50;
                }
            }
        }else{
            thescore = thescore + 200;
        }
        return thescore;
    }
}
