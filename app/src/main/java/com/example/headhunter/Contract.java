package com.example.headhunter;

import android.provider.BaseColumns;

public class Contract {

    public static final String SQL_CREATE_CANDIDATO = String.format(
      "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT);",
      Candidato.TABLE_NAME, Candidato._ID, Candidato.COLUMN_NOME, Candidato.COLUMN_NASCIMENTO,
            Candidato.COLUMN_PERFIL, Candidato.COLUMN_TELEFONE, Candidato.COLUMN_EMAIL
    );

    public static final String SQL_CREATE_CATEGORIA = String.format(
            "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT);",
            Categoria.TABLE_NAME, Categoria._ID, Categoria.COLUMN_TITULO
    );

    public static final String SQL_CREATE_PRODUCAO = String.format(
            "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT," +
                    "%s INTEGER, %s INTEGER," +
                    "FOREIGN KEY (%s) REFERENCES %s(%s), FOREIGN KEY (%s) REFERENCES %s(%s));",
            Producao.TABLE_NAME, Producao._ID, Producao.COLUMN_TITULO, Producao.COLUMN_DESCRICAO,
            Producao.COLUMN_DATA_INICIO, Producao.COLUMN_DATA_FIM,
            Producao.COLUMN_FK_CANDIDATO, Producao.COLUMN_FK_CATEGORIA,
            Producao.COLUMN_FK_CANDIDATO, Candidato.TABLE_NAME, Candidato._ID,
            Producao.COLUMN_FK_CATEGORIA, Categoria.TABLE_NAME, Categoria._ID
    );

    public static final String SQL_CREATE_ATIVIDADE = String.format(
            "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT," +
                    "%s INTEGER, FOREIGN KEY (%s) REFERENCES %s(%s));",
            Atividade.TABLE_NAME, Atividade._ID, Atividade.COLUMN_DESCRICAO, Atividade.COLUMN_DATA,
            Atividade.COLUMN_HORAS,
            Atividade.COLUMN_FK_PRODUCAO, Atividade.COLUMN_FK_PRODUCAO, Producao.TABLE_NAME, Producao._ID
    );

    public static final class Candidato implements BaseColumns {
        public static final String TABLE_NAME = "candidato";
        public static final String COLUMN_NOME = "nome";
        public static final String COLUMN_NASCIMENTO = "nascimento";
        public static final String COLUMN_PERFIL = "perfil";
        public static final String COLUMN_TELEFONE = "telefone";
        public static final String COLUMN_EMAIL = "email";
    }

    public static final class Producao implements BaseColumns {
        public static final String TABLE_NAME = "producao";
        public static final String COLUMN_TITULO = "titulo";
        public static final String COLUMN_DESCRICAO = "descricao";
        public static final String COLUMN_DATA_INICIO = "data_inicio";
        public static final String COLUMN_DATA_FIM = "data_fim";
        public static final String COLUMN_FK_CANDIDATO = "id_candidato";
        public static final String COLUMN_FK_CATEGORIA = "id_categoria";
    }

    public static final class Categoria implements BaseColumns {
        public static final String TABLE_NAME = "categoria";
        public static final String COLUMN_TITULO = "titulo";
    }

    public static final class Atividade implements BaseColumns {
        public static final String TABLE_NAME = "atividade";
        public static final String COLUMN_DESCRICAO = "descricao";
        public static final String COLUMN_DATA = "data";
        public static final String COLUMN_HORAS = "horas";
        public static final String COLUMN_FK_PRODUCAO = "id_producao";
    }
}
