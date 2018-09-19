package com.sales.sqlitecrud.dao;

/**
 * Created by AlbertoSales on 8/30/2018.
 */

public class Empregado {
    int id;
    String nome, depto, dataIngresso;
    double salario;

    public Empregado(int id, String nome, String depto, String dataIngresso, double salario) {
        this.id = id;
        this.nome = nome;
        this.depto = depto;
        this.dataIngresso = dataIngresso;
        this.salario = salario;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDepto() {
        return depto;
    }

    public String getDataIngresso() {
        return dataIngresso;
    }

    public double getSalario() {
        return salario;
    }
}
