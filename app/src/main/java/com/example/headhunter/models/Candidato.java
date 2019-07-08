package com.example.headhunter.models;

import android.database.Cursor;

import com.example.headhunter.Contract;

public class Candidato {

    public String id = "";
    public String nome;
    public String nascimento;
    public String perfil;
    public String telefone;
    public String email;

    public Candidato(){

    }

    public Candidato(String id, String nome, String nascimento, String perfil, String telefone, String email) {
        this.id = id;
        this.nome = nome;
        this.nascimento = nascimento;
        this.perfil = perfil;
        this.telefone = telefone;
        this.email = email;
    }

    public static Candidato fromCursor(Cursor cursor) {
        String id = cursor.getString(cursor.getColumnIndex(Contract.Candidato._ID));
        String nome = cursor.getString(cursor.getColumnIndex(Contract.Candidato.COLUMN_NOME));

        int index;

        index = cursor.getColumnIndex(Contract.Candidato.COLUMN_NASCIMENTO);
        String nascimento =  index == -1 ? "" : cursor.getString(index);

        index = cursor.getColumnIndex(Contract.Candidato.COLUMN_PERFIL);
        String perfil = index == -1 ? "" : cursor.getString(index);

        index = cursor.getColumnIndex(Contract.Candidato.COLUMN_TELEFONE);
        String telefone = index == -1 ? "" : cursor.getString(index);

        index = cursor.getColumnIndex(Contract.Candidato.COLUMN_EMAIL);
        String email = index == -1 ? "" : cursor.getString(index);

        return new Candidato(id, nome, nascimento, perfil, telefone, email);
    }
}
