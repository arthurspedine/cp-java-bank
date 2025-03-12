package br.com.fiap.bankcp.model;

import br.com.fiap.bankcp.controller.model.ETipo;

import java.time.LocalDate;

public class Conta {

    private Integer numeroAgencia;

    private String nomeTitular;

    private String cpf;

    private LocalDate dataAbertura;

    private long saldo;

    private boolean ativa;

    private ETipo tipo;

    public Conta(Integer numeroAgencia, String nomeTitular, String cpf, LocalDate dataAbertura, long saldo, boolean ativa, ETipo tipo) {
        this.numeroAgencia = numeroAgencia;
        this.nomeTitular = nomeTitular;
        this.cpf = cpf;
        this.dataAbertura = dataAbertura;
        this.saldo = saldo;
        this.ativa = ativa;
        this.tipo = tipo;
    }

    public Integer getNumeroAgencia() {
        return numeroAgencia;
    }

    public String getNomeTitular() {
        return nomeTitular;
    }

    public String getCpf() {
        return cpf;
    }

    public LocalDate getDataAbertura() {
        return dataAbertura;
    }

    public long getSaldo() {
        return saldo;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public ETipo getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return "Conta{" +
                "numeroAgencia=" + numeroAgencia +
                ", nomeTitular='" + nomeTitular + '\'' +
                ", cpf='" + cpf + '\'' +
                ", dataAbertura=" + dataAbertura +
                ", saldo=" + saldo +
                ", ativa=" + ativa +
                ", tipo=" + tipo +
                '}';
    }
}
