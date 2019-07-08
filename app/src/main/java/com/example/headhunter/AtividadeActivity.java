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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.headhunter.models.Atividade;
import com.example.headhunter.models.Producao;

import java.util.ArrayList;

public class AtividadeActivity extends AppCompatActivity {

    private HeadHunterDBHelper helper;
    private ArrayList<Atividade> items;
    private Producao producao = null;
    private AtividadeAdapter adapter = new AtividadeAdapter();
    private final int RESULT_ATIVIDADE_EDIT_ACTIVITY = 1;

    void restoreProducao(String id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {
                Contract.Producao._ID,
        };
        String selection = Contract.Producao._ID + " = ?";
        String[] args = { id };
        Cursor c = db.query(Contract.Producao.TABLE_NAME, columns, selection, args, null, null, null, null);
        c.moveToNext();
        producao = Producao.fromCursor(c);
    }

    private void queryAtividades() {
        helper = new HeadHunterDBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {
                Contract.Atividade._ID,
                Contract.Atividade.COLUMN_DESCRICAO,
                Contract.Atividade.COLUMN_DATA,
                Contract.Atividade.COLUMN_HORAS,
        };
        Cursor c;
        String selection = Contract.Atividade.COLUMN_FK_PRODUCAO + " = ?";
        String[] args = { producao.id };
        c = db.query(Contract.Atividade.TABLE_NAME, columns, selection, args, null, null, null, null);
        items = new ArrayList<>();
        while (c.moveToNext()) {
            Atividade item = Atividade.fromCursor(c);
            items.add(item);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividade);
        String id = getIntent().getStringExtra("id");
        helper = new HeadHunterDBHelper(this);
        restoreProducao(id);
        queryAtividades();

        RecyclerView rv = findViewById(R.id.rvAtividades);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.buttonAddAtividade).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AtividadeActivity.this, AtividadeEditActivity.class);
                intent.putExtra("producao_id", producao.id);
                startActivityForResult(intent, RESULT_ATIVIDADE_EDIT_ACTIVITY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (Activity.RESULT_OK == resultCode){
            switch (requestCode){
                case RESULT_ATIVIDADE_EDIT_ACTIVITY:
                    queryAtividades();
                    break;
            }
        }
    }

    public class AtividadeAdapter extends RecyclerView.Adapter<AtividadeAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            Context context = viewGroup.getContext();
            LayoutInflater infl = LayoutInflater.from(context);
            View view = infl.inflate(R.layout.item_atividade, viewGroup, false);
            ViewHolder holder = new ViewHolder(view);
            view.findViewById(R.id.buttonAtividadeEdit).setOnClickListener(holder);
            view.findViewById(R.id.buttonAtividadeRemove).setOnClickListener(holder);
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
            TextView viewDescricao;
            TextView viewData;
            TextView viewHoras;
            Atividade item;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                viewDescricao = itemView.findViewById(R.id.textItemAtividadeDescricao);
                viewData = itemView.findViewById(R.id.textItemAtividadeData);
                viewHoras = itemView.findViewById(R.id.textItemAtividadeHoras);
            }

            void setData(Atividade item) {
                this.item = item;
                viewDescricao.setText(item.descricao);
                viewData.setText(item.data);
                viewHoras.setText(item.horas);
            }

            @Override
            public void onClick(View v) {
                if (getAdapterPosition() == RecyclerView.NO_POSITION) {
                    return;
                }
                switch(v.getId()) {
                    case R.id.buttonAtividadeRemove:
                        SQLiteDatabase db = helper.getWritableDatabase();
                        String selection = Contract.Atividade._ID + " = ?";
                        String[] args = { item.id };
                        db.delete(Contract.Atividade.TABLE_NAME, selection, args);
                        queryAtividades();
                        break;
                    case R.id.buttonAtividadeEdit:
                        Intent intent = new Intent(AtividadeActivity.this, AtividadeEditActivity.class);
                        intent.putExtra("atividade_id", this.item.id);
                        intent.putExtra("producao_id", producao.id);
                        startActivityForResult(intent, RESULT_ATIVIDADE_EDIT_ACTIVITY);
                        break;
                }
            }
        }
    }
}
