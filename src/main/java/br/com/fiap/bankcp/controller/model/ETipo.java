package br.com.fiap.bankcp.controller.model;

public enum ETipo {

    CORRENTE("Corrente"),
    POUPANCA("Poupança"),
    SALARIO("Salário");

    private final String description;


    ETipo(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
