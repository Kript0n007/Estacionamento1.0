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
        Duration duracao = Duration.between(entrada, saida);
        long horas = duracao.toHours();
        return (int) horas;
    }

    public BigDecimal calcularValorTotal() {
        int horasUtilizadas = calcularHorasUtilizadas();
        return valorHora.multiply(BigDecimal.valueOf(horasUtilizadas));
    }

    public String gerarRelatorio() {
        // Crie a estrutura do relatório com as informações necessárias
        StringBuilder relatorio = new StringBuilder();
        relatorio.append("Data e Hora de Entrada: ").append(entrada).append("\n");
        relatorio.append("Data e Hora de Saída: ").append(saida).append("\n");
        relatorio.append("Condutor: ").append(condutor.getNome()).append("\n");
        relatorio.append("Veículo: ").append(veiculo.getModelo()).append("\n");
        relatorio.append("Quantidade de Horas Utilizadas: ").append(calcularHorasUtilizadas()).append("\n");
        relatorio.append("Valor a Pagar: ").append(calcularValorTotal()).append("\n");
        // Adicione outras informações relevantes ao relatório

        System.out.println("Relatório da Movimentação:");
        System.out.println("Data e Hora de Entrada: " + entrada);
        System.out.println("Data e Hora de Saída: " + saida);
        System.out.println("Condutor: " + condutor.getNome());
        System.out.println("Veículo: " + veiculo.getModelo().getNome());
        System.out.println("Placa: " + veiculo.getPlaca());
        System.out.println("Quantidade de Horas: " + calcularHorasUtilizadas());
        System.out.println("Valor a Pagar: " + valorTotal);
        System.out.println("Valor de Desconto: " + valorDesconto);

        return relatorio.toString();
    }
}
