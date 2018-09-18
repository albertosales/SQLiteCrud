package com.sales.sqlitecrud;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlbertoSales on 8/30/2018.
 */

public class EmpregadoActivity extends AppCompatActivity {

    List<Empregado> empregadoList;
    SQLiteDatabase mDados;
    ListView listViewEmpregados;
    EmpregadoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empregado);

        listViewEmpregados = (ListView) findViewById(R.id.listViewEmpregados);
        empregadoList = new ArrayList<>();

        //abrindo a base de dados
        mDados = openOrCreateDatabase(MainActivity.BASEDADOS_NOME, MODE_PRIVATE, null);

        // esse método exibirá os funcionários na lista
        mostrarEmpregados();
    }

    private void mostrarEmpregados() {
        // usamos rawQuery (sql, selectionargs) para buscar todos os funcionários
        Cursor cursorEmp = mDados.rawQuery("SELECT * FROM empregados", null);

        // se o cursor tiver alguns dados
        if (cursorEmp.moveToFirst()) {
            // looping através de todos os registros
            do {
                // colocando cada registro na lista de funcionários
                empregadoList.add(new Empregado(
                        cursorEmp.getInt(0),
                        cursorEmp.getString(1),
                        cursorEmp.getString(2),
                        cursorEmp.getString(3),
                        cursorEmp.getDouble(4)
                ));
            } while (cursorEmp.moveToNext());
        }
        // fechando o cursor
        cursorEmp.close();

        // criando o objeto adaptador
        adapter = new EmpregadoAdapter(this, R.layout.list_layout_empregado, empregadoList, mDados);

        // adicionando o adaptador ao listview
        listViewEmpregados.setAdapter(adapter);
    }

}
