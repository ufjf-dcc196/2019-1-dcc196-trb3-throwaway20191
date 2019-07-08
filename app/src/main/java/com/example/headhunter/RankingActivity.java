package com.example.headhunter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.headhunter.models.Categoria;

import java.util.ArrayList;
import java.util.List;

public class RankingActivity extends AppCompatActivity {

    private HeadHunterDBHelper helper;
    private List<Rank> items;
    private RankingAdapter adapter;

    class Rank {
        String nome;
        String horas;

        public Rank(String nome, String horas) {
            this.nome = nome;
            this.horas = horas;
        }
    }

    List<Categoria> queryCategorias() {
        helper = new HeadHunterDBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {
                Contract.Categoria._ID,
                Contract.Categoria.COLUMN_TITULO,
        };
        Cursor c;
        c = db.query(Contract.Categoria.TABLE_NAME, columns,  "", null, null, null, null);
        List<Categoria> categoriaList = new ArrayList<>();
        while (c.moveToNext()) {
            Categoria item = Categoria.fromCursor(c);
            categoriaList.add(item);
        }
        return categoriaList;
    }

    void queryRanking(String categoria_id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        //String raw = "SELECT sum(a.horas) s, c.nome FROM atividade a INNER JOIN producao p ON p._id = a.id_producao INNER JOIN candidato c ON p.id_candidato = c._id ORDER BY s GROUP BY c._id";
        String raw = "SELECT sum(a.%s) s, c.%s FROM %s a INNER JOIN %s p ON p.%s = a.%s INNER JOIN %s c ON p.%s = c.%s WHERE p.%s = ? GROUP BY c.%s ORDER BY s DESC";
        String query = String.format(raw, Contract.Atividade.COLUMN_HORAS, Contract.Candidato.COLUMN_NOME, Contract.Atividade.TABLE_NAME, Contract.Producao.TABLE_NAME, Contract.Producao._ID, Contract.Atividade.COLUMN_FK_PRODUCAO,
        Contract.Candidato.TABLE_NAME, Contract.Producao.COLUMN_FK_CANDIDATO, Contract.Candidato._ID, Contract.Producao.COLUMN_FK_CATEGORIA, Contract.Candidato._ID);
        Cursor c = db.rawQuery(query, new String[] {categoria_id});
        items = new ArrayList<>();
        while(c.moveToNext()) {
            String total = c.getString(0);
            String nome = c.getString(1);
            items.add(new Rank(nome, total));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        helper = new HeadHunterDBHelper(this);
        final List<Categoria> categorias = queryCategorias();
        CategoriaSpinnerAdapter spinnerAdapter = new CategoriaSpinnerAdapter(this, categorias);

        Spinner spinner = findViewById(R.id.spinnerCategoria);
        spinner.setAdapter(spinnerAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Categoria cat = categorias.get(position);
                queryRanking(cat.id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapter = new RankingAdapter();
        RecyclerView rv = findViewById(R.id.rvRanking);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        items = new ArrayList<>();
    }

    public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            Context context = viewGroup.getContext();
            LayoutInflater infl = LayoutInflater.from(context);
            View view = infl.inflate(R.layout.item_ranking, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            viewHolder.setData(items.get(i));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            TextView viewNome;
            TextView viewHoras;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                viewNome = itemView.findViewById(R.id.textItemNome);
                viewHoras = itemView.findViewById(R.id.textItemHoras);
            }

            void setData(Rank item) {
                viewNome.setText(item.nome);
                viewHoras.setText(item.horas);
            }
        }
    }
}
