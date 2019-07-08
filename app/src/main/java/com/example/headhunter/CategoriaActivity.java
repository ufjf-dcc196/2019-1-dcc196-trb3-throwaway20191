package com.example.headhunter;

import android.content.ContentValues;
import android.content.Context;
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
import android.widget.TextView;

import com.example.headhunter.models.Categoria;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CategoriaActivity extends AppCompatActivity {

    private HeadHunterDBHelper dbHelper;
    private ArrayList<Categoria> items;
    private Categoria selectedCategoria = null;
    private CategoriaAdapter adapter = new CategoriaAdapter();

    private void queryCategorias() {
        dbHelper = new HeadHunterDBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {
                Contract.Categoria._ID,
                Contract.Categoria.COLUMN_TITULO,
        };
        Cursor c;
        c = db.query(Contract.Categoria.TABLE_NAME, columns,  "", null, null, null, Contract.Categoria._ID + " DESC");
        items = new ArrayList<>();
        while (c.moveToNext()) {
            Categoria item = Categoria.fromCursor(c);
            items.add(item);
        }
        adapter.notifyDataSetChanged();
    }

    private void selectCategoria(Categoria categoria) {
        selectedCategoria = categoria;
        ((TextView)findViewById(R.id.editCategoriaTitulo)).setText(selectedCategoria.titulo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);

        queryCategorias();

        RecyclerView rv = findViewById(R.id.rvCategorias);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.buttonAddCategoria).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView view = findViewById(R.id.editCategoriaTitulo);
                String titulo = view.getText().toString();
                if (titulo.isEmpty()) {
                    return;
                }
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(Contract.Categoria.COLUMN_TITULO, titulo);
                if (selectedCategoria == null) {
                    db.insert(Contract.Categoria.TABLE_NAME, null, values);
                }
                else {
                    String where = Contract.Categoria._ID + " = ?";
                    String[] args = { selectedCategoria.id };
                    db.update(Contract.Categoria.TABLE_NAME, values, where, args);
                    selectedCategoria = null;
                }
                queryCategorias();
                view.setText("");
            }
        });
    }

    public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            Context context = viewGroup.getContext();
            LayoutInflater infl = LayoutInflater.from(context);
            View view = infl.inflate(R.layout.item_categoria, viewGroup, false);
            ViewHolder holder = new ViewHolder(view);
            view.findViewById(R.id.textCategoria).setOnClickListener(holder);
            view.findViewById(R.id.buttonCategoriaRemove).setOnClickListener(holder);
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
            Categoria item;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                viewTitulo = itemView.findViewById(R.id.textCategoria);
            }

            void setData(Categoria item) {
                this.item = item;
                viewTitulo.setText(item.titulo);
            }

            @Override
            public void onClick(View v) {
                if (getAdapterPosition() == RecyclerView.NO_POSITION) {
                    return;
                }
                switch(v.getId()) {
                    case R.id.buttonCategoriaRemove:
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        String selection = Contract.Categoria._ID + " = ?";
                        String[] args = { item.id };
                        db.delete(Contract.Categoria.TABLE_NAME, selection, args);
                        queryCategorias();
                        break;
                    case R.id.textCategoria:
                        selectCategoria(this.item);
                        break;
                }
            }
        }
    }
}
