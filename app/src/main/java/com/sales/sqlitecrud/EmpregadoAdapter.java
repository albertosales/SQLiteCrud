package com.sales.sqlitecrud;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by AlbertoSales on 8/30/2018.
 */


public class EmpregadoAdapter extends ArrayAdapter<Empregado> {

    Context mCtx;
    int listLayoutRes;
    List<Empregado> empregadoList;
    SQLiteDatabase mDados;

    public EmpregadoAdapter(Context mCtx, int listLayoutRes, List<Empregado> empregadoList, SQLiteDatabase mDados) {
        super(mCtx, listLayoutRes, empregadoList);

        this.mCtx = mCtx;
        this.listLayoutRes = listLayoutRes;
        this.empregadoList = empregadoList;
        this.mDados = mDados;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(listLayoutRes, null);

        final Empregado empregado = empregadoList.get(position);


        TextView textViewNome = view.findViewById(R.id.textViewNome);
        TextView textViewDepto = view.findViewById(R.id.textViewDepartamento);
        TextView textViewSalario = view.findViewById(R.id.textViewSalario);
        TextView textViewDataIngresso = view.findViewById(R.id.textViewDataIngresso);


        textViewNome.setText(empregado.getNome());
        textViewDepto.setText(empregado.getDepto());
        textViewSalario.setText(String.valueOf(empregado.getSalario()));
        textViewDataIngresso.setText(empregado.getDataIngresso());


        Button btDeletar = view.findViewById(R.id.buttonDeletarEmpregado);
        Button btnEditar = view.findViewById(R.id.buttonEditEmpregado);

        // adicionando um listener de cliques ao botão
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                atualizarEmpregado(empregado);
            }
        });

        // a operação de exclusão
        btDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setTitle("Você tem Certeza?");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String sql = "DELETE FROM empregados WHERE id = ?";
                        mDados.execSQL(sql, new Integer[]{empregado.getId()});
                        atualizarEmpregadosDoBancoDados();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return view;
    }

    private void atualizarEmpregado(final Empregado empregado) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.dialog_atualiza_empregado, null);
        builder.setView(view);


        final EditText editTextNome = view.findViewById(R.id.editTextNome);
        final EditText editTextSalario = view.findViewById(R.id.editTextSalario);
        final Spinner spinnerDepartamento = view.findViewById(R.id.spinnerDepartamento);

        editTextNome.setText(empregado.getNome());
        editTextSalario.setText(String.valueOf(empregado.getSalario()));

        final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.buttonAtualizarEmpegado).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = editTextNome.getText().toString().trim();
                String salario = editTextSalario.getText().toString().trim();
                String depto = spinnerDepartamento.getSelectedItem().toString();

                if (nome.isEmpty()) {
                    editTextNome.setError("Nome em branco!!!");
                    editTextNome.requestFocus();
                    return;
                }

                if (salario.isEmpty()) {
                    editTextSalario.setError("Salário em branco");
                    editTextSalario.requestFocus();
                    return;
                }

                String sql = "UPDATE empregados \n" +
                        "SET nome = ?, \n" +
                        "departamento = ?, \n" +
                        "salario = ? \n" +
                        "WHERE id = ?;\n";

                mDados.execSQL(sql, new String[]{nome, depto, salario, String.valueOf(empregado.getId())});
                Toast.makeText(mCtx, "Empregado Atualizado", Toast.LENGTH_SHORT).show();
                atualizarEmpregadosDoBancoDados();

                dialog.dismiss();
            }
        });
    }

    private void atualizarEmpregadosDoBancoDados() {
        Cursor cursorEmp = mDados.rawQuery("SELECT * FROM empregados", null);
        if (cursorEmp.moveToFirst()) {
            empregadoList.clear();
            do {
                empregadoList.add(new Empregado(
                        cursorEmp.getInt(0),
                        cursorEmp.getString(1),
                        cursorEmp.getString(2),
                        cursorEmp.getString(3),
                        cursorEmp.getDouble(4)
                ));
            } while (cursorEmp.moveToNext());
        }
        cursorEmp.close();
        notifyDataSetChanged();
    }

}
