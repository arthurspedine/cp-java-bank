package br.com.fiap.bankcp.model;

import java.math.BigDecimal;

public record TransacaoDTO(
        Long id,
        Long idDestino,
        BigDecimal valor

) {
}
