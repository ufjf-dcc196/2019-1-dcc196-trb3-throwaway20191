package com.example.headhunter;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.headhunter.models.Candidato;

public class CandidatoActivity extends AppCompatActivity {
    Candidato candidato;

    void restoreData(String id) {
        HeadHunterDBHelper helper = new HeadHunterDBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {
                Contract.Candidato._ID,
                Contract.Candidato.COLUMN_NOME,
                Contract.Candidato.COLUMN_NASCIMENTO,
                Contract.Candidato.COLUMN_PERFIL,
                Contract.Candidato.COLUMN_TELEFONE,
                Contract.Candidato.COLUMN_EMAIL,
        };
        String selection = Contract.Candidato._ID + " = ?";
        String[] args = { id };
        Cursor c = db.query(Contract.Candidato.TABLE_NAME, columns, selection, args, null, null, null, null);
        if (!c.moveToNext()) {
            Log.i("HEAD", "Could not find row with id " + id);
            return;
        }
        candidato = Candidato.fromCursor(c);

        ((TextView)findViewById(R.id.editTextNome)).setText(candidato.nome);
        ((TextView)findViewById(R.id.editTextNascimento)).setText(candidato.nascimento);
        ((TextView)findViewById(R.id.editTextPerfil)).setText(candidato.perfil);
        ((TextView)findViewById(R.id.editTextTelefone)).setText(candidato.telefone);
        ((TextView)findViewById(R.id.editTextEmail)).setText(candidato.email);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_candidato, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_candidato:
                String titulo = ((TextView)findViewById(R.id.editTextNome)).getText().toString();
                String nascimento = ((TextView)findViewById(R.id.editTextNascimento)).getText().toString();
                String perfil = ((TextView)findViewById(R.id.editTextPerfil)).getText().toString();
                String telefone = ((TextView)findViewById(R.id.editTextTelefone)).getText().toString();
                String email = ((TextView)findViewById(R.id.editTextEmail)).getText().toString();

                HeadHunterDBHelper dbHelper = new HeadHunterDBHelper(this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(Contract.Candidato.COLUMN_NOME, titulo);
                values.put(Contract.Candidato.COLUMN_NASCIMENTO, nascimento);
                values.put(Contract.Candidato.COLUMN_PERFIL, perfil);
                values.put(Contract.Candidato.COLUMN_TELEFONE, telefone);
                values.put(Contract.Candidato.COLUMN_EMAIL, email);

                String feedback;
                if (candidato.id == null || candidato.id.isEmpty()) {
                    long id = db.insert(Contract.Candidato.TABLE_NAME, null, values);
                    feedback = "adicionado";
                }
                else {
                    String where = Contract.Candidato._ID + " = ?";
                    String[] args = { candidato.id };
                    db.update(Contract.Candidato.TABLE_NAME, values, where, args);
                    feedback = "atualizado";
                }
                Toast.makeText(this,"Candidato " + feedback + " com sucesso!", Toast.LENGTH_LONG).show();
                setResult(Activity.RESULT_OK);
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidato);

        String id = getIntent().getStringExtra("id");
        if (id != null) {
            restoreData(id);
        }
        else {
            candidato = new Candidato();
        }
    }
}
