package com.uniamerica.estacionamento;

import com.uniamerica.estacionamento.Entity.Condutor;
import com.uniamerica.estacionamento.Entity.Veiculo;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public class Recibo {
    @Getter
    @Setter
    private LocalDateTime entrada;

    @Getter @Setter
    private LocalDateTime saida;

    @Getter @Setter
    private Condutor condutor;

    @Getter @Setter
    private Veiculo veiculo;

    @Getter @Setter
    private Long tempo;

    @Getter @Setter
    private Long tempoParaDesconto;

    @Getter @Setter
    private BigDecimal valorTotal;

    @Getter @Setter
    private Long desconto;

    public Recibo(LocalDateTime entrada, LocalDateTime saida, Condutor condutor, Veiculo veiculo, Long tempo, Long tempoParaDesconto, BigDecimal valorTotal, Long desconto) {
        this.entrada = entrada;
        this.saida = saida;
        this.condutor = condutor;
        this.veiculo = veiculo;
        this.tempo = tempo;
        this.tempoParaDesconto = tempoParaDesconto;
        this.valorTotal = valorTotal;
        this.desconto = desconto;
    }

    public Recibo() {
    }
}
