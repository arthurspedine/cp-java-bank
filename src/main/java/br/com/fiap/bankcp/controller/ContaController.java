package br.com.fiap.bankcp.controller;

import br.com.fiap.bankcp.model.Conta;
import br.com.fiap.bankcp.model.TransacaoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/contas")
public class ContaController {

    private static final Logger log = LoggerFactory.getLogger(ContaController.class);
    private final List<Conta> repository = new ArrayList<>();

    @PostMapping
    public Conta registerConta(@RequestBody Conta conta) {
        validarConta(conta);
        normalizarCpf(conta);
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

    @PostMapping("/deposito")
    public Conta depositarValor(@RequestBody TransacaoDTO dto) {
        validarTransacaoSimples(dto);
        Conta conta = buscarContaPorId(dto.id());
        conta.depositar(dto.valor());
        log.info("Foi depositado o valor de {} na conta de id: {}", dto.valor(), dto.id());
        return conta;
    }

    @PostMapping("/saque")
    public Conta sacarValor(@RequestBody TransacaoDTO dto) {
        validarTransacaoSimples(dto);
        Conta conta = buscarContaPorId(dto.id());
        validarSaldoSuficiente(dto.valor(), conta);
        conta.sacar(dto.valor());
        log.info("Saque realizado na conta id: {}! Valor saque {} | Valor atual {}", dto.id(), dto.valor(), conta.getSaldo());
        return conta;
    }

    @PostMapping("/pix")
    public Conta transacaoPix(@RequestBody TransacaoDTO dto) {
        validarTransacaoPix(dto);
        Conta contaOrigem = buscarContaPorId(dto.id());
        Conta contaDestino = buscarContaPorId(dto.idDestino());
        validarSaldoSuficiente(dto.valor(), contaOrigem);

        realizarTransferencia(contaOrigem, contaDestino, dto.valor());

        log.info("Transferência pix realizada! Valor transação: {} | Conta origem: {} => Conta destino: {}",
                dto.valor(), contaOrigem.getNomeTitular(), contaDestino.getNomeTitular());
        return contaOrigem;
    }
    private Conta buscarContaPorId(Long id) {
        return repository.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada."));
    }

    private Conta buscarContaPorCpf(String cpf) {
        String cpfNormalizado = normalizarCpf(cpf);
        return repository.stream()
                .filter(c -> c.getCpf().equals(cpfNormalizado))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada."));
    }

    private void validarConta(Conta conta) {
        if (conta.getNomeTitular() == null || conta.getNomeTitular().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome da conta é obrigatório.");
        }
        if (conta.getCpf() == null || conta.getCpf().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O CPF da conta é obrigatório.");
        }
        if (conta.getDataAbertura() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A data de abertura da conta é obrigatória.");
        }
        if (conta.getDataAbertura().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A data de abertura da conta não pode ser numa data futura.");
        }
        if (conta.getSaldo() == null || conta.getSaldo().signum() == -1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O saldo da conta não pode ser negativo.");
        }
        if (conta.getTipo() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O tipo da conta é obrigatório.");
        }
    }

    private void validarTransacaoSimples(TransacaoDTO dto) {
        if (dto.id() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O Id da conta é obrigatório.");
        }
        if (dto.valor() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O valor da transação é obrigatório.");
        }
        if (dto.valor().signum() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O valor da transação deve ser positivo.");
        }
    }

    private void validarTransacaoPix(TransacaoDTO dto) {
        validarTransacaoSimples(dto);

        if (dto.idDestino() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O Id da conta destino é obrigatório.");
        }
        if (dto.idDestino().equals(dto.id())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Não é permitido pix para a mesma conta.");
        }
    }

    private void validarSaldoSuficiente(BigDecimal valorSaque, Conta conta) {
        if (conta.getSaldo().compareTo(valorSaque) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O valor do saque excede o saldo atual da conta.");
        }
    }

    private void normalizarCpf(Conta conta) {
        conta.setCpf(normalizarCpf(conta.getCpf()));
    }

    private String normalizarCpf(String cpf) {
        return cpf.replace(".", "")
                .replace("/", "")
                .replace("-", "");
    }

    private void realizarTransferencia(Conta origem, Conta destino, BigDecimal valor) {
        origem.sacar(valor);
        destino.depositar(valor);
    }

}

