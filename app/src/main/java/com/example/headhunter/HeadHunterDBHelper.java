package com.example.headhunter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class HeadHunterDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 7;
    public static final String DATABASE_NAME = "headhunter.db";

    public HeadHunterDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Contract.SQL_CREATE_CATEGORIA);
        db.execSQL(Contract.SQL_CREATE_CANDIDATO);
        db.execSQL(Contract.SQL_CREATE_PRODUCAO);
        db.execSQL(Contract.SQL_CREATE_ATIVIDADE);
        Log.i("Head", "Created DB ok");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contract.Atividade.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.Producao.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.Categoria.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.Candidato.TABLE_NAME);
        onCreate(db);
    }
}
