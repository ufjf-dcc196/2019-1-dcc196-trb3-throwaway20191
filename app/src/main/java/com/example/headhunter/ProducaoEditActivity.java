package com.example.headhunter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.headhunter.models.Categoria;
import com.example.headhunter.models.Producao;

import java.util.ArrayList;
import java.util.List;

public class ProducaoEditActivity extends AppCompatActivity {

    private HeadHunterDBHelper helper;
    private Producao producao = null;
    private List<Categoria> categoriaList;

    void queryCategorias() {
        helper = new HeadHunterDBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {
                Contract.Categoria._ID,
                Contract.Categoria.COLUMN_TITULO,
        };
        Cursor c;
        c = db.query(Contract.Categoria.TABLE_NAME, columns,  "", null, null, null, null);
        categoriaList = new ArrayList<>();
        while (c.moveToNext()) {
            Categoria item = Categoria.fromCursor(c);
            categoriaList.add(item);
        }
    }

    void restoreProducao(String id){
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {
                Contract.Producao._ID,
                Contract.Producao.COLUMN_TITULO,
                Contract.Producao.COLUMN_DESCRICAO,
                Contract.Producao.COLUMN_DATA_INICIO,
                Contract.Producao.COLUMN_DATA_FIM,
                Contract.Producao.COLUMN_FK_CATEGORIA
        };
        String selection = Contract.Producao._ID + " = ?";
        String[] args = { id };
        Cursor c = db.query(Contract.Producao.TABLE_NAME, columns, selection, args, null, null, null, null);
        if (!c.moveToNext()) {
            return;
        }
        producao = Producao.fromCursor(c);

        ((TextView)findViewById(R.id.editTextProdTitulo)).setText(producao.titulo);
        ((TextView)findViewById(R.id.editTextProdDescricao)).setText(producao.descricao);
        ((TextView)findViewById(R.id.editTextProdInicio)).setText(producao.inicio);
        ((TextView)findViewById(R.id.editTextProdFim)).setText(producao.fim);

        String[] columnsCat = {
            Contract.Categoria._ID,
            Contract.Categoria.COLUMN_TITULO,
        };
        String selectionCat = Contract.Categoria._ID + " = ?";
        String[] argsCat = { producao.categoria_id };
        c.close();
        c = db.query(Contract.Categoria.TABLE_NAME, columnsCat, selectionCat, argsCat, null, null, null, null);
        if (!c.moveToNext()){
            return;
        }
        Categoria categoria = Categoria.fromCursor(c);
        Spinner spinner = findViewById(R.id.spinnerCategoria);
        for(int i=0; i<categoriaList.size(); i++) {
            Categoria cat = categoriaList.get(i);
            if (cat.id.equals(categoria.id)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producaoedit);

        final String candidato_id = getIntent().getStringExtra("candidato_id");

        Spinner spinner = findViewById(R.id.spinnerCategoria);

        helper = new HeadHunterDBHelper(this);
        queryCategorias();

        CategoriaAdapter adapter = new CategoriaAdapter(this);
        spinner.setAdapter(adapter);

        final String producao_id = getIntent().getStringExtra("producao_id");
        if (producao_id != null) {
            restoreProducao(producao_id);
        }

        findViewById(R.id.buttonProducaoEditSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titulo = ((TextView)findViewById(R.id.editTextProdTitulo)).getText().toString();
                String descricao = ((TextView)findViewById(R.id.editTextProdDescricao)).getText().toString();
                String inicio = ((TextView)findViewById(R.id.editTextProdInicio)).getText().toString();
                String fim = ((TextView)findViewById(R.id.editTextProdFim)).getText().toString();
                String id_categoria = ((Categoria)((Spinner)findViewById(R.id.spinnerCategoria)).getSelectedItem()).id;
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put(Contract.Producao.COLUMN_TITULO, titulo);
                values.put(Contract.Producao.COLUMN_DESCRICAO, descricao);
                values.put(Contract.Producao.COLUMN_DATA_INICIO, inicio);
                values.put(Contract.Producao.COLUMN_DATA_FIM, fim);
                values.put(Contract.Producao.COLUMN_FK_CATEGORIA, id_categoria);
                values.put(Contract.Producao.COLUMN_FK_CANDIDATO, candidato_id);

                String feedback;
                if (producao_id == null){
                    db.insert(Contract.Producao.TABLE_NAME, null, values);
                    feedback = "adicionada";
                }
                else {
                    String where = Contract.Producao._ID + " = ?";
                    String[] args = { producao_id };
                    db.update(Contract.Producao.TABLE_NAME, values, where, args);
                    feedback = "atualizada";
                }
                Toast.makeText(ProducaoEditActivity.this,"Tarefa " + feedback + " com sucesso!", Toast.LENGTH_LONG).show();
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }

    class CategoriaAdapter extends BaseAdapter implements SpinnerAdapter {
        Context context;

        public CategoriaAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return categoriaList.size();
        }

        @Override
        public Object getItem(int position) {
            return categoriaList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            /*
            View view =  View.inflate(context, android.R.layout.simple_list_item_1, null);
            TextView textView = (TextView) view.findViewById(R.id.main);
            textView.setText(company[position]);
            return textView;
            */
            TextView view = new TextView(context);
            view.setText(categoriaList.get(position).titulo);
            return view;
        }
    }
}
