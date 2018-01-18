package com.example.ibrahim.game_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class MaBaseHelper extends SQLiteOpenHelper {

    public final static String DB_NAME = "ScoresDB";//nom de ma base
    public final static int VERSION = 1;//version de la base
    public final static String TABLE_NAME = "BestScores";//nom de ma table
    final static String _ID = "_id";//column
    final static String SCORE = "score";//column
    final static String _DATETIME = "time";//column
    final static String[] columns = {_ID,SCORE,_DATETIME};


    Context context;

    //Constructeur de la classe
    public MaBaseHelper(Context context){
        super(context,DB_NAME,null,VERSION);
        this.context = context;
    }

    /**
     * Création de la table en utilisant SQL
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+"("
                +_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +SCORE+" INTEGER NOT NULL, "
                +_DATETIME+" DATE); ");
    }

    /**
     * Upgrade of the database
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //nothing for the moment
    }

    //méthode pour ajouter un score dans notre base
    public void addScore(int score){
        //1.get reference to writable database
        SQLiteDatabase sqlitedb = this.getWritableDatabase();
        //2.create ContentValues to add key "column"/value
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm:ss a");
        date.setTimeZone(TimeZone.getTimeZone("GMT+2:00"));
        String localtime = date.format(currentLocalTime);
        ContentValues values = new ContentValues();
        values.put(SCORE,score);
        values.put(_DATETIME,localtime);
        //3.insert in the DB
        sqlitedb.insert(TABLE_NAME,null,values);
        //4.on ferme notre base
        sqlitedb.close();
    }

    //méthode pour récupérer tous les scores stockés dans notre base
    public void getAllScores(){

    }

}
