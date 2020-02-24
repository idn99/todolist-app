package com.idn99.myapp.densnote;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    ListView lvTodos;
    FloatingActionButton fabAdd;
    EditText edtTodo;

    ArrayList<String> data = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        createTodos();

        lvTodos = findViewById(R.id.list_catatan);

        showSP();
        adapter = new ArrayAdapter<String>(this, R.layout.todo_content,R.id.tvTodo, data);
        lvTodos.setAdapter(adapter);

        fabAdd = findViewById(R.id.fa_button);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addTask();

            }
        });

        lvTodos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                deleteItem(position);
                return true;
            }
        });

        lvTodos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClickItemEdit(position);
            }
        });

    }

//    private void createTodos(){
//        data.add("Haikyuu");
//        data.add("Boku no Hero Acadamia");
//        data.add("Shingeki no Kyojin");
//        data.add("Darwin Games");
//    }


    private void addTask(){
        View view = View.inflate(this, R.layout.dialog_add_view, null);

        edtTodo = view.findViewById(R.id.edt_dialog);

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Ada Apaan ?");
        dialog.setView(view);
        dialog.setPositiveButton("Tambah", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int newKey = data.size();
                String input = edtTodo.getText().toString();
                Boolean isEmptyInput = false;
                if(TextUtils.isEmpty(edtTodo.getText().toString())) {
                    isEmptyInput = true;
                    edtTodo.setError("Field tidak boleh kosong");
                }
                else {
                    data.add(newKey, input);
                    addToSh(newKey, input);
                    adapter.notifyDataSetChanged();
                    //succesDialog();
                }
            }
        });
        dialog.create().show();
    }

    private void onClickItemEdit(final int position){

        final int index = position;

        View view = View.inflate(this, R.layout.dialog_add_view, null);

        edtTodo = view.findViewById(R.id.edt_dialog);
        edtTodo.setText(data.get(index));

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Mau Diubah ?");
        dialog.setView(view);
        dialog.setPositiveButton("Ubah", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                data.remove(index); // index didapat position parameter
                sortSP();

                String item = edtTodo.getText().toString();

                data.add(index, item);
                addToSh(index, item);

                adapter.notifyDataSetChanged();
            }
        });
        dialog.setNegativeButton("Batal",null);
        dialog.create();
        dialog.show();
    }

    private void sortSP(){
        SharedPreferences sp = getSharedPreferences("todo",MODE_PRIVATE);
        SharedPreferences.Editor editor= sp.edit();
        editor.clear();
        editor.apply();
        for(int i = 0; i < data.size();i++){
            editor.putString(String.valueOf(i),data.get(i));
        }
        editor.apply();

    }

    private void showSP(){
        SharedPreferences sh = getSharedPreferences("todo", MODE_PRIVATE);
        if (sh.getAll().size() > 0){
            for (int i=0 ; i< sh.getAll().size(); i++){
                String key = String.valueOf(i);
                data.add(sh.getString(key, null));
            }
        }
    }

    private void addToSh(int key, String item){
        SharedPreferences sh = getSharedPreferences("todo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sh.edit();
        String k = String.valueOf(key);
        editor.putString(k, item);
        editor.apply();
    }

    private void deleteItem(int position){

        final int index = position;

        //Buat alert dialog
        AlertDialog.Builder dialog  = new AlertDialog.Builder(this);
        dialog.setTitle("Yakin akan dihapus ?");
        dialog.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                data.remove(index); // index didapat position parameter
                sortSP();
                adapter.notifyDataSetChanged();
            }
        });
        dialog.setNeutralButton("Hapus Semua", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                data.clear();
                sortSP();
                adapter.notifyDataSetChanged();
            }
        });
        dialog.setNegativeButton("Batal",null);
        dialog.create().show();


    }


}
