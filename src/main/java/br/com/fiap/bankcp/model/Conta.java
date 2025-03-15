package br.com.fiap.bankcp.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

public class Conta {

    private Long id;

    private Integer numeroAgencia;

    private String nomeTitular;

    private String cpf;

    private LocalDate dataAbertura;

    private BigDecimal saldo;

    private boolean ativa;

    private ETipo tipo;

    public Conta(Long id, Integer numeroAgencia, String nomeTitular, String cpf, LocalDate dataAbertura, BigDecimal saldo, boolean ativa, ETipo tipo) {
        this.id = (id == null) ? Math.abs(new Random().nextLong()) : id;
        this.numeroAgencia = numeroAgencia;
        this.nomeTitular = nomeTitular;
        this.cpf = cpf;
        this.dataAbertura = dataAbertura;
        this.saldo = saldo;
        this.ativa = ativa;
        this.tipo = tipo;
    }

    public void desativar() {
        this.ativa = false;
    }

    public Long getId() {
        return id;
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

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataAbertura() {
        return dataAbertura;
    }

    public BigDecimal getSaldo() {
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
