package br.com.fiap.bankcp.controller;

import br.com.fiap.bankcp.model.Conta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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
        ajustarCpf(conta);
        repository.add(conta);
        log.info("Conta do {} foi salva com sucesso! Id: {}", conta.getNomeTitular(), conta.getId());
        return conta;
    }

    @GetMapping("/id/{id}")
    public Conta contaInfoId(@PathVariable("id") Long id) {
        log.info("Buscando conta com id: {}", id);
        return buscarContaPorId(id);
    }

    @GetMapping("/cpf/{cpf}")
    public Conta contaInfoCpf(@PathVariable("cpf") String cpf) {
        log.info("Buscando conta com cpf: {}", cpf);
        return buscarContaPorCpf(cpf);
    }

    @PatchMapping("/desativar/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativarConta(@PathVariable("id") Long id) {
        Conta conta = buscarContaPorId(id);
        conta.desativar();
        log.info("Conta do {} foi desativada com sucesso!", conta.getNomeTitular());
    }

    private Conta buscarContaPorId(Long id) {
        return repository.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst().orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada.")
                );
    }

    private Conta buscarContaPorCpf(String cpf) {
        return repository.stream()
                .filter(c -> c.getCpf().equals(cpf))
                .findFirst().orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada.")
                );
    }

    private void ajustarCpf(Conta conta) {
        conta.setCpf(conta.getCpf()
                .replace(".", "")
                .replace("/", "")
                .replace("-", ""));
    }

    private void validarConta(Conta conta) {
        if (conta.getNomeTitular() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome da conta é obrigatório.");
        }
        if (conta.getCpf() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O CPF da conta é obrigatório.");
        }
        if (conta.getDataAbertura() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A data de aberta da conta é obrigatório.");
        }
        if (conta.getDataAbertura().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A data de aberta da conta não pode ser numa data futura.");
        }
        if (conta.getSaldo().signum() == -1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O saldo da conta não pode ser negativo.");
        }
        if (conta.getTipo() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O tipo da conta é obrigatório.");
        }
    }

}

