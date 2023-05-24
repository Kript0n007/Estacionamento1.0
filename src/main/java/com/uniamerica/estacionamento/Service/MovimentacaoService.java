package com.uniamerica.estacionamento.Service;

import com.uniamerica.estacionamento.Entity.Condutor;
import com.uniamerica.estacionamento.Entity.Configuracao;
import com.uniamerica.estacionamento.Entity.Movimentacao;
import com.uniamerica.estacionamento.Entity.Veiculo;
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
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class MovimentacaoService {

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private ConfiguracaoRepository configuracaoRepository;

    @Autowired
    private CondutorRepository condutorRepository;

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
    @Transactional(rollbackOn = Exception.class)
    public void saida(final Long id) {

        final Movimentacao movimentacao = this.movimentacaoRepository.findById(id).orElse(null);

        // Registrar a data e hora de saída
        movimentacao.setSaida(LocalDateTime.now());

        // Calcular as horas utilizadas
        movimentacao.calcularHorasUtilizadas();

        // Calcular o valor a pagar
        movimentacao.calcularValorTotal();

        // Gerar o relatório da movimentação
        movimentacao.gerarRelatorio();


        // Salvar as alterações no banco de dados
        movimentacaoRepository.save(movimentacao);
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
