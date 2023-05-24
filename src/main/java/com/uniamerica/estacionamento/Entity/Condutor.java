package com.uniamerica.estacionamento.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Table(name = "tb_condutores", schema = "public")

public class Condutor extends Abstract{
    @Getter @Setter
    //@Size(min = 2, max = 50, message = "O nome deve ter entre 2 a 50 caracteres.")
    @Column(name = "nome", nullable = true)
    private String nome;
    @Getter @Setter
    //@Size(min = 2, max = 20, message = "O CPF deve ter entre 2 a 50 caracteres.")
    @Column(name = "cpf", nullable = true, unique = true)
    private String cpf;
    @Getter @Setter
    //@Size(min = 2, max = 20, message = "O telefone deve ter entre 2 a 20 caracteres.")
    @Column(name = "telefone", nullable = true)
    private String telefone;
    @Getter @Setter
    @Column(name = "tempo_pago")
    private LocalTime tempoPago;
    @Getter @Setter
    @Column(name = "tempo_desconto")
    private LocalTime tempoDesconto;

    @PrePersist
    public void preencher(){
        this.tempoPago = LocalTime.now();
        this.tempoDesconto = LocalTime.now();
    }
}
