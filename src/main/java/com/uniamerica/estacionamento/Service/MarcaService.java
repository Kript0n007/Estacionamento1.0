package com.uniamerica.estacionamento.Service;

import com.uniamerica.estacionamento.Entity.Condutor;
import com.uniamerica.estacionamento.Entity.Marca;
import com.uniamerica.estacionamento.Entity.Modelo;
import com.uniamerica.estacionamento.Entity.Movimentacao;
import com.uniamerica.estacionamento.Respository.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class MarcaService {

    @Autowired
    private MarcaRepository marcaRepository;

    @Transactional(rollbackFor = Exception.class)
        public void cadastrar(final Marca marca){
            Assert.isTrue(!marca.getNome().isBlank(), "Erro, nome vazio");
            Assert.isTrue(this.marcaRepository.findNome(marca.getNome()).isEmpty(), "Marca já existente.");

            this.marcaRepository.save(marca);
    }

    @Transactional(rollbackFor = Exception.class)
    public void editar(final Marca marca){
        final Marca marcaBanco = this.marcaRepository.findById(marca.getId()).orElse(null);

        Assert.isTrue(marca.getNome() != null, "Erro, digite um nome.");

        Assert.isTrue(marcaBanco != null || !marcaBanco.getId().equals(marca.getId()),"Marca já existente.");

        this.marcaRepository.save(marca);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deletar(final Long id) {
        final Marca marcaBanco = this.marcaRepository.findById(id).orElse(null);

        List<Modelo> modeloLista = this.marcaRepository.findModeloByMarca(marcaBanco);

        if (modeloLista.isEmpty()) {
            this.marcaRepository.delete(marcaBanco);
        } else {
            marcaBanco.setAtivo(Boolean.FALSE);
            this.marcaRepository.save(marcaBanco);
        }
    }

    public ResponseEntity<?> buscarMarca(long id) {
        Optional<Marca> marca;
        try {
            Optional<Marca> MarcaOptinal = marcaRepository.findById(id);
            marca = MarcaOptinal;

            Assert.isTrue(MarcaOptinal.isPresent(), "Nao existe marca com esse id");
        } catch (Exception error) {
            error.printStackTrace();
            return new ResponseEntity(error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(marca);
    }
}
