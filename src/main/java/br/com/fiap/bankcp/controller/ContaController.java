package br.com.fiap.bankcp.controller;

import br.com.fiap.bankcp.model.Conta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/contas")
public class ContaController {

    private Logger log = LoggerFactory.getLogger(getClass());
    private List<Conta> repository = new ArrayList<>();

    @PostMapping
    public Conta registerConta(@RequestBody Conta conta) {
        validarConta(conta);
        repository.add(conta);
        log.info("Conta do {} foi salva com sucesso!", conta.getNomeTitular());
        return conta;
    }

    private void validarConta(Conta conta) {
        boolean tipoValido = conta.getTipo() != null;

        if (conta.getNomeTitular() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome da conta é obrigatório.");
        }
        if (conta.getCpf() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O CPF da conta é obrigatório.");
        }
        if (conta.getDataAbertura() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A data de aberta da conta é obrigatório.");
        }
        if (conta.getDataAbertura().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A data de aberta da conta não pode ser numa data futura.");
        }
        if (conta.getSaldo() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O saldo da conta não pode ser negativo.");
        }
//        if (conta.getTipo() == null)
    }

}

