package com.example.headhunter.models;

import android.database.Cursor;

import com.example.headhunter.Contract;

public class Atividade {

    public String id;
    public String descricao;
    public String data;
    public String horas;

    public Atividade(String id, String descricao, String data, String horas) {
        this.id = id;
        this.descricao = descricao;
        this.data = data;
        this.horas = horas;
    }

    public static Atividade fromCursor(Cursor cursor) {
        String id = cursor.getString(cursor.getColumnIndex(Contract.Atividade._ID));

        int index;
        index = cursor.getColumnIndex(Contract.Atividade.COLUMN_DESCRICAO);
        String descricao = index == -1 ? "" : cursor.getString(index);

        index = cursor.getColumnIndex(Contract.Atividade.COLUMN_DATA);
        String data = index == -1 ? "" : cursor.getString(index);

        index = cursor.getColumnIndex(Contract.Atividade.COLUMN_HORAS);
        String horas = index == -1 ? "" : cursor.getString(index);

        return new Atividade(id, descricao, data, horas);
    }
}
