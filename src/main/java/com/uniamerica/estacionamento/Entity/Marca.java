package com.uniamerica.estacionamento.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_marcas", schema = "public")
public class Marca extends Abstract{
    @Getter @Setter
    @Column(name = "nome", nullable = true, unique = true)
    private String nome;
}
