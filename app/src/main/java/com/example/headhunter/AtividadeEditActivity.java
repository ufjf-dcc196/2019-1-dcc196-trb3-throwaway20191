package com.example.headhunter;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.headhunter.models.Atividade;

public class AtividadeEditActivity extends AppCompatActivity {

    private HeadHunterDBHelper helper;

    void restoreAtividade(String id){
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {
                Contract.Atividade._ID,
                Contract.Atividade.COLUMN_DATA,
                Contract.Atividade.COLUMN_DESCRICAO,
                Contract.Atividade.COLUMN_HORAS,
        };
        String selection = Contract.Atividade._ID + " = ?";
        String[] args = { id };
        Cursor c = db.query(Contract.Atividade.TABLE_NAME, columns, selection, args, null, null, null, null);
        if (!c.moveToNext()) {
            return;
        }
        Atividade atividade = Atividade.fromCursor(c);

        ((TextView)findViewById(R.id.editTextAtivData)).setText(atividade.data);
        ((TextView)findViewById(R.id.editTextAtivDescricao)).setText(atividade.descricao);
        ((TextView)findViewById(R.id.editTextAtivHoras)).setText(atividade.horas);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividadeedit);

        helper = new HeadHunterDBHelper(this);

        final String producao_id = getIntent().getStringExtra("producao_id");
        final String atividade_id = getIntent().getStringExtra("atividade_id");
        if (atividade_id != null) {
            restoreAtividade(atividade_id);
        }

        findViewById(R.id.buttonAtividadeEditSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String descricao = ((TextView)findViewById(R.id.editTextAtivDescricao)).getText().toString();
                String data = ((TextView)findViewById(R.id.editTextAtivData)).getText().toString();
                String horas = ((TextView)findViewById(R.id.editTextAtivHoras)).getText().toString();
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put(Contract.Atividade.COLUMN_DATA, data);
                values.put(Contract.Atividade.COLUMN_DESCRICAO, descricao);
                values.put(Contract.Atividade.COLUMN_HORAS, horas);
                values.put(Contract.Atividade.COLUMN_FK_PRODUCAO, producao_id);

                String feedback;
                if (atividade_id == null){
                    db.insert(Contract.Atividade.TABLE_NAME, null, values);
                    feedback = "adicionada";
                }
                else {
                    String where = Contract.Atividade._ID + " = ?";
                    String[] args = { atividade_id };
                    db.update(Contract.Atividade.TABLE_NAME, values, where, args);
                    feedback = "atualizada";
                }
                Toast.makeText(AtividadeEditActivity.this,"Atividade " + feedback + " com sucesso!", Toast.LENGTH_LONG).show();
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }
}
