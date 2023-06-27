package com.uniamerica.estacionamento.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "tb_movimentacoes", schema = "public")
public class Movimentacao extends Abstract{
    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "veiculo", nullable = true, unique = true)
    private Veiculo veiculo;
    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "condutor", nullable = true)
    private Condutor condutor;
    @Getter @Setter
    @Column(name = "entrada", nullable = true)
    private LocalDateTime entrada;
    @Getter @Setter
    @Column(name = "saida")
    private LocalDateTime saida;
    @Getter @Setter
    @Column(name = "hora")
    private Integer hora;
    @Getter @Setter
    @Column(name = "minutos")
    private Integer minutos;
    @Getter @Setter
    @Column(name = "tempo_desconto")
    private LocalTime tempoDesconto;
    @Getter @Setter
    @Column(name = "tempo_multa")
    private LocalTime tempoMulta;
    @Getter @Setter
    @Column(name = "valor_desconto")
    private BigDecimal valorDesconto;
    @Getter @Setter
    @Column(name = "valor_multa")
    private BigDecimal valorMulta;
    @Getter @Setter
    @Column(name = "valor_total")
    private BigDecimal valorTotal;
    @Getter @Setter
    @Column(name = "valor_hora")
    private BigDecimal valorHora;
    @Getter @Setter
    @Column(name = "valor_hora_multa")
    private BigDecimal valorHoraMulta;

    public int calcularHorasUtilizadas() {
        Duration duracao = Duration.between(entrada,saida);
        long horas = duracao.toHours();
        return (int) horas;
    }

    public BigDecimal calcularValorTotal() {
        int horasUtilizadas = calcularHorasUtilizadas();
        BigDecimal resultado = valorHora.multiply(BigDecimal.valueOf(horasUtilizadas));
        resultado = resultado.subtract(BigDecimal.valueOf(10));
        return resultado;

    }

    public String gerarRelatorio() {
        // Crie a estrutura do relatório com as informações necessárias
        StringBuilder relatorio = new StringBuilder();
        relatorio.append("Data e Hora de Entrada: ").append(entrada).append("\n");
        relatorio.append("Data e Hora de Saída: ").append(saida).append("\n");
        relatorio.append("Condutor: ").append(condutor.getNome()).append("\n");
        relatorio.append("Veículo: ").append(veiculo.getModelo().getNome()).append("\n");
        relatorio.append("Placa: ").append(veiculo.getPlaca()).append("\n");
        relatorio.append("Quantidade de Horas Utilizadas: ").append(calcularHorasUtilizadas()).append("\n");
        relatorio.append("Valor a Pagar: ").append(calcularValorTotal()).append("\n");
        relatorio.append("Valor Desconto: ").append(valorDesconto);
        // Adicione outras informações relevantes ao relatório

        return relatorio.toString();
    }

    @PrePersist
    public void Carregar() {
        this.entrada = LocalDateTime.now();
    }
}
