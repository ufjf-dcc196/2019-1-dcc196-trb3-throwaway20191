package com.example.headhunter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.headhunter.models.Candidato;
import com.example.headhunter.models.Producao;

import java.util.ArrayList;
import java.util.List;

public class ProducaoActivity extends AppCompatActivity {

    private static final int RESULT_PRODUCAO_EDIT_ACTIVITY = 1;

    private HeadHunterDBHelper helper;
    private List<Producao> items;
    private ProducaoAdapter adapter = new ProducaoAdapter();
    private Candidato candidato;

    void restoreCandidato(String id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {
                Contract.Candidato._ID,
                Contract.Candidato.COLUMN_NOME,
        };
        String selection = Contract.Candidato._ID + " = ?";
        String[] args = { id };
        Cursor c = db.query(Contract.Candidato.TABLE_NAME, columns, selection, args, null, null, null, null);
        c.moveToNext();
        candidato = Candidato.fromCursor(c);
    }

    void restoreProducoes() {
        SQLiteDatabase db = helper.getReadableDatabase();
        String raw = "SELECT p.%s, p.%s, p.%s, p.%s, p.%s, p.%s FROM %s p INNER JOIN %s c ON p.%s = c.%s WHERE c.%s = ?";
        String query = String.format(raw, Contract.Producao._ID, Contract.Producao.COLUMN_TITULO, Contract.Producao.COLUMN_DESCRICAO,
                Contract.Producao.COLUMN_DATA_INICIO, Contract.Producao.COLUMN_DATA_FIM, Contract.Producao.COLUMN_FK_CATEGORIA,
                Contract.Producao.TABLE_NAME, Contract.Candidato.TABLE_NAME, Contract.Producao.COLUMN_FK_CANDIDATO, Contract.Candidato._ID, Contract.Candidato._ID);
        Cursor c = db.rawQuery(query, new String[]{String.valueOf(candidato.id)});
        items = new ArrayList<>();
        while (c.moveToNext()) {
            String id = c.getString(0);
            String titulo = c.getString(1);
            String descricao = c.getString(2);
            String inicio = c.getString(3);
            String fim = c.getString(4);
            String cat_id = c.getString(5);
            Producao p = new Producao(id, titulo, descricao, inicio, fim, cat_id);
            items.add(p);
        }
        c.close();
        adapter.notifyDataSetChanged();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producao);
        String id = getIntent().getStringExtra("id");
        helper = new HeadHunterDBHelper(this);

        restoreCandidato(id);
        restoreProducoes();

        ((TextView)findViewById(R.id.textProdCandidato)).setText(candidato.nome);

        RecyclerView rv = findViewById(R.id.rvProducao);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));


        findViewById(R.id.buttonProdNovaProducao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProducaoActivity.this, ProducaoEditActivity.class);
                intent.putExtra("candidato_id", candidato.id);
                startActivityForResult(intent, RESULT_PRODUCAO_EDIT_ACTIVITY);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (Activity.RESULT_OK == resultCode){
            switch (requestCode){
                case RESULT_PRODUCAO_EDIT_ACTIVITY:
                    restoreProducoes();
                    break;
            }
        }
    }

    public class ProducaoAdapter extends RecyclerView.Adapter<ProducaoAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            Context context = viewGroup.getContext();
            LayoutInflater infl = LayoutInflater.from(context);
            View view = infl.inflate(R.layout.item_producao, viewGroup, false);
            ViewHolder holder = new ViewHolder(view);
            view.findViewById(R.id.buttonProducaoRemove).setOnClickListener(holder);
            view.findViewById(R.id.buttonProducaoEdit).setOnClickListener(holder);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            viewHolder.setData(items.get(i));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView viewTitulo;
            TextView viewInicio;
            TextView viewFim;
            Producao item;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                viewTitulo = itemView.findViewById(R.id.textItemTitulo);
                viewInicio = itemView.findViewById(R.id.textItemInicio);
                viewFim = itemView.findViewById(R.id.textItemFim);
            }

            void setData(Producao item) {
                this.item = item;
                viewTitulo.setText(item.titulo);
                viewInicio.setText(item.inicio);
                viewFim.setText(item.fim);
            }

            @Override
            public void onClick(View v) {
                if (getAdapterPosition() == RecyclerView.NO_POSITION) {
                    return;
                }
                Intent intent;
                switch(v.getId()) {
                    case R.id.buttonProducaoEdit:
                        intent = new Intent(ProducaoActivity.this, ProducaoEditActivity.class);
                        intent.putExtra("candidato_id", candidato.id);
                        intent.putExtra("producao_id", this.item.id);
                        startActivityForResult(intent, RESULT_PRODUCAO_EDIT_ACTIVITY);
                        break;
                    case R.id.buttonProducaoRemove:
                    default:
                        intent = new Intent(ProducaoActivity.this, AtividadeActivity.class);
                        intent.putExtra("id", this.item.id);
                        startActivity(intent);
                        break;
                }

            }
        }
    }
}
