package com.example.headhunter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.headhunter.models.Candidato;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    final int RESULT_CANDIDATO_ACTIVITY = 1;

    public HeadHunterDBHelper dbHelper;
    public List<Candidato> candidatos;
    private final CandidatoAdapter candidatoAdapter = new CandidatoAdapter();

    private void queryCandidatos() {
        dbHelper = new HeadHunterDBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {
                Contract.Candidato._ID,
                Contract.Candidato.COLUMN_NOME,
                Contract.Candidato.COLUMN_NASCIMENTO
        };
        Cursor c;
        c = db.query(Contract.Candidato.TABLE_NAME, columns,  "", null, null, null, null);
        candidatos = new ArrayList<>();
        while (c.moveToNext()) {
            Candidato candidato = Candidato.fromCursor(c);
            candidatos.add(candidato);
        }
        candidatoAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queryCandidatos();

        RecyclerView rv = findViewById(R.id.rvCandidatos);
        rv.setAdapter(candidatoAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.buttonNovoCandidato).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, CandidatoActivity.class);
            startActivityForResult(intent, RESULT_CANDIDATO_ACTIVITY);
            }
        });

        findViewById(R.id.buttonCategoria).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, CategoriaActivity.class);
            startActivity(intent);
            }
        });

        findViewById(R.id.buttonRanking).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, RankingActivity.class);
            startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (Activity.RESULT_OK == resultCode){
            switch (requestCode){
                case RESULT_CANDIDATO_ACTIVITY:
                    queryCandidatos();
                    break;
            }
        }
    }


    public class CandidatoAdapter extends RecyclerView.Adapter<CandidatoAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            Context context = viewGroup.getContext();
            LayoutInflater infl = LayoutInflater.from(context);
            View view = infl.inflate(R.layout.item_candidato, viewGroup, false);
            ViewHolder holder = new ViewHolder(view);
            view.findViewById(R.id.buttonCandidatoRemove).setOnClickListener(holder);
            view.findViewById(R.id.buttonCandidatoEdit).setOnClickListener(holder);
            view.findViewById(R.id.textCandidatoNome).setOnClickListener(holder);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull CandidatoAdapter.ViewHolder viewHolder, int i) {
            viewHolder.setData(candidatos.get(i));
        }

        @Override
        public int getItemCount() {
            return candidatos.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            Candidato candidato;
            TextView viewNome;
            TextView viewNascimento;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                viewNome = itemView.findViewById(R.id.textCandidatoNome);
                viewNascimento = itemView.findViewById(R.id.textCandidatoNascimento);
            }

            void setData(Candidato candidato){
                this.candidato = candidato;
                viewNome.setText(candidato.nome);
                viewNascimento.setText(candidato.nascimento);
            }

            @Override
            public void onClick(View v) {
                Intent intent;
                switch(v.getId()) {
                    case R.id.buttonCandidatoRemove:
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        String selection = Contract.Candidato._ID + " = ?";
                        String[] args = { candidato.id };
                        db.delete(Contract.Candidato.TABLE_NAME, selection, args);
                        queryCandidatos();
                        Toast.makeText(getApplicationContext(), "Candidato removido!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.buttonCandidatoEdit:
                        intent = new Intent(MainActivity.this, CandidatoActivity.class);
                        intent.putExtra("id", candidato.id);
                        startActivityForResult(intent, RESULT_CANDIDATO_ACTIVITY);
                        break;
                    case R.id.textCandidatoNome:
                        intent = new Intent(MainActivity.this, ProducaoActivity.class);
                        intent.putExtra("id", candidato.id);
                        startActivity(intent);
                        break;
                }
            }
        }
    }
}
