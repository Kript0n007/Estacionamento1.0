package com.uniamerica.estacionamento.Service;

import com.uniamerica.estacionamento.Entity.Movimentacao;
import com.uniamerica.estacionamento.Respository.CondutorRepository;
import com.uniamerica.estacionamento.Respository.ConfiguracaoRepository;
import com.uniamerica.estacionamento.Respository.MovimentacaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MovimentacaoService {

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private ConfiguracaoRepository configuracaoRepository;

    @Autowired
    private CondutorRepository condutorRepository;

    @Autowired
    private ConfiguracaoService configuracaoService;

    @Transactional(rollbackOn = Exception.class)
    public void cadastrar(Movimentacao movimentacao){

        Assert.isTrue(movimentacao.getVeiculo() != null, "Veiculo nao informado");
        Assert.isTrue(movimentacao.getCondutor() != null, "Condutor nao informada");
        Assert.isTrue(movimentacao.getEntrada() != null, "Entradada nao informado");
        Assert.isTrue(movimentacao.getSaida() != null, "Saida nao informada");

        this.movimentacaoRepository.save(movimentacao);

    }

    @Transactional(rollbackOn = Exception.class)
    public void editar(final Long id, Movimentacao movimentacao){

        Assert.isTrue(movimentacao.getVeiculo() != null, "Veiculo nao informado");
        Assert.isTrue(movimentacao.getCondutor() != null, "Condutor nao informada");
        Assert.isTrue(movimentacao.getEntrada() != null, "Entradada nao informado");
        Assert.isTrue(movimentacao.getSaida() != null, "Saida nao informada");

        final Movimentacao moviBanco = this.movimentacaoRepository.findById(id).orElse(null);
        Assert.isTrue(moviBanco != null, "nao foi possivel encontrar o registro");

        movimentacao.setEntrada(moviBanco.getEntrada());
        movimentacao.setCadastro(moviBanco.getCadastro());

        this.movimentacaoRepository.save(movimentacao);
    }

    public ResponseEntity<String> saida(final Long id) {
        Movimentacao movimentacao = movimentacaoRepository.findById(id).orElse(null);

        if (movimentacao != null) {
            BigDecimal valorHora = configuracaoService.getValorHora();
            movimentacao.setValorHora(valorHora);
            movimentacao.setSaida(LocalDateTime.now());
            movimentacao.calcularHorasUtilizadas();
            movimentacao.calcularValorTotal();

            movimentacao.gerarRelatorio();

            movimentacaoRepository.save(movimentacao);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("erro na saida");
        }
        return ResponseEntity.ok().body("Deu certo");
    }
    @Transactional(rollbackOn = Exception.class)
    public void deletar(final Long id) {
        final Movimentacao moviBanco = this.movimentacaoRepository.findById(id).orElse(null);

        if (moviBanco == null) {
            throw new RuntimeException("Id nn encontradodo tenta outro ai");
        } else {
            moviBanco.setAtivo(false);
            this.movimentacaoRepository.delete(moviBanco);
        }
    }

    public ResponseEntity<?> buscarVeiculo(long id) {
        Optional<Movimentacao> movimentacao;
        try {
            Optional<Movimentacao> movimentacaoOptinal = movimentacaoRepository.findById(id);
            movimentacao = movimentacaoOptinal;
            Assert.isTrue(movimentacaoOptinal.isPresent(), "Nao existe movimentacao com esse id");
        } catch (Exception error) {
            error.printStackTrace();
            return new ResponseEntity(error.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(movimentacao);
    }


}
