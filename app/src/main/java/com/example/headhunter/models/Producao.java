package com.example.headhunter.models;

import android.database.Cursor;

import com.example.headhunter.Contract;

public class Producao {

    public String id;
    public String titulo;
    public String descricao;
    public String inicio;
    public String fim;
    public String categoria_id;

    public Producao(String id, String titulo, String descricao, String inicio, String fim, String categoria_id) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.inicio = inicio;
        this.fim = fim;
        this.categoria_id = categoria_id;
    }

    public static Producao fromCursor(Cursor cursor) {
        String id = cursor.getString(cursor.getColumnIndex(Contract.Producao._ID));
        String titulo = cursor.getString(cursor.getColumnIndex(Contract.Producao.COLUMN_TITULO));

        int index;
        index = cursor.getColumnIndex(Contract.Producao.COLUMN_DESCRICAO);
        String decricao = index == -1 ? "" : cursor.getString(index);

        index = cursor.getColumnIndex(Contract.Producao.COLUMN_DATA_INICIO);
        String inicio = index == -1 ? "" : cursor.getString(index);

        index = cursor.getColumnIndex(Contract.Producao.COLUMN_DATA_INICIO);
        String fim = index == -1 ? "" : cursor.getString(index);

        index = cursor.getColumnIndex(Contract.Producao.COLUMN_FK_CATEGORIA);
        String categoria_id = index == -1 ? "" : cursor.getString(index);

        return new Producao(id, titulo, decricao, inicio, fim, categoria_id);
    }
}
