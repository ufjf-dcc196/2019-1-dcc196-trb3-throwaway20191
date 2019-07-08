package com.example.headhunter.models;

import android.database.Cursor;

import com.example.headhunter.Contract;

public class Categoria {

    public String id;
    public String titulo;

    public Categoria(String id, String titulo) {
        this.id = id;
        this.titulo = titulo;
    }

    public static Categoria fromCursor(Cursor cursor){
        String id = cursor.getString(cursor.getColumnIndex(Contract.Categoria._ID));
        String titulo = cursor.getString(cursor.getColumnIndex(Contract.Categoria.COLUMN_TITULO));
        return new Categoria(id, titulo);
    }
}
