package com.sales.sqlitecrud;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by AlbertoSales on 8/30/2018.
 */


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String BASEDADOS_NOME = "dados";

    TextView textViewEmpregados;
    EditText editTextNome, editTextSalario;
    Spinner spinnerDepartamento;

    SQLiteDatabase mDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewEmpregados = findViewById(R.id.textViewEmpregados);
        editTextNome = findViewById(R.id.editTextNome);
        editTextSalario = findViewById(R.id.editTextSalario);
        spinnerDepartamento = findViewById(R.id.spinnerDepartamento);

        findViewById(R.id.buttonAdicionarEmpregado).setOnClickListener(this);
        textViewEmpregados.setOnClickListener(this);

        //criando a base de dados
        mDados = openOrCreateDatabase(BASEDADOS_NOME, MODE_PRIVATE, null);

        criaEmpregadoTable();
    }


    // este método irá criar a tabela
    // como vamos chamar esse método toda vez que lançarmos o aplicativo
    // foi adicionado IF NOT EXISTS ao SQL
    // então, só criará a tabela quando a tabela ainda não estiver criada
    private void criaEmpregadoTable() {
        mDados.execSQL(
                "CREATE TABLE IF NOT EXISTS empregados (\n" +
                        "    id INTEGER NOT NULL CONSTRAINT empregados_pk PRIMARY KEY AUTOINCREMENT,\n" +
                        "    nome varchar(200) NOT NULL,\n" +
                        "    departamento varchar(200) NOT NULL,\n" +
                        "    dataingresso datetime NOT NULL,\n" +
                        "    salario double NOT NULL\n" +
                        ");"
        );
    }

    // este método irá validar o nome e o salário
    // depto não precisa de validação pois é um spinner e não pode estar vazio
    private boolean entradas(String nome, String salario) {
        if (nome.isEmpty()) {
            editTextNome.setError("Por favor! Enter com um nome");
            editTextNome.requestFocus();
            return false;
        }

        if (salario.isEmpty() || Integer.parseInt(salario) <= 0) {
            editTextSalario.setError("Por favor! Entre com um salario");
            editTextSalario.requestFocus();
            return false;
        }
        return true;
    }

    // Neste método, faremos a operação de criação
    private void adicionarEmpregados() {

        String nome = editTextNome.getText().toString().trim();
        String salario = editTextSalario.getText().toString().trim();
        String depto = spinnerDepartamento.getSelectedItem().toString();

        // obtendo a hora atual para a data de entrada
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String dataIngresso = sdf.format(cal.getTime());

        //validando entradas
        if (entradas(nome, salario)) {

            String insertSQL = "INSERT INTO empregados \n" +
                    "(nome, departamento, dataingresso, salario)\n" +
                    "VALUES \n" +
                    "(?, ?, ?, ?);";

            // usando o mesmo método execsql para inserir valores
            // desta vez tem dois parâmetros
            // primeiro é a string sql e segundo são os parâmetros que devem ser vinculados à consulta

            mDados.execSQL(insertSQL, new String[]{nome, depto, dataIngresso, salario});

            Toast.makeText(this, "Empregado Adicionado com Sucesso!!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonAdicionarEmpregado:

                adicionarEmpregados();

                break;
            case R.id.textViewEmpregados:

                startActivity(new Intent(this, EmpregadoActivity.class));

                break;
        }
    }
}
