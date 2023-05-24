package com.uniamerica.estacionamento.Service;

import com.uniamerica.estacionamento.Entity.Condutor;
import com.uniamerica.estacionamento.Entity.Modelo;
import com.uniamerica.estacionamento.Entity.Veiculo;
import com.uniamerica.estacionamento.Respository.MarcaRepository;
import com.uniamerica.estacionamento.Respository.ModeloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class ModeloService {

    @Autowired
    private ModeloRepository modeloRepository;
    @Autowired
    private MarcaRepository marcaRepository;

    @Transactional(rollbackFor = Exception.class)
    public void cadastro(final Modelo modelo){

        Assert.isTrue(modelo.getMarca() != null, "Erro, sem marca.");
        Assert.isTrue(!modelo.getNome().isBlank(), "Erro, nome vazio.");

        String regex = ".*\\d+.*";
        Assert.isTrue(!modelo.getNome().matches(regex), "Escreva so letras");

        this.modeloRepository.save(modelo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void editar(final Modelo modelo){
        final Modelo modeloBanco = this.modeloRepository.findById(modelo.getId()).orElse(null);

        Assert.isTrue(modelo.getMarca() != null, "Erro, sem marca.");
        Assert.isTrue(modelo.getNome() != null, "Erro, digite um nome.");

        this.modeloRepository.save(modelo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deletar(final Modelo modelo){
        final Modelo modeloBanco = this.modeloRepository.findById(modelo.getId()).orElse(null);

        List<Veiculo> modeloLista = this.modeloRepository.findVeiculoByModelo(modeloBanco);

        if(modeloLista.isEmpty()){
            this.modeloRepository.delete(modeloBanco);
        }else {
            modeloBanco.setAtivo(false);
            this.modeloRepository.save(modelo);
        }
    }

    public ResponseEntity<?> buscarCondutor(long id) {
        Optional<Modelo> modelo;
        try {
            Optional<Modelo> modeloOptinal = modeloRepository.findById(id);
            modelo = modeloOptinal;
            Assert.isTrue(modeloOptinal.isPresent(), "Nao existe modelo com esse id");
        } catch (Exception error) {
            error.printStackTrace();
            return new ResponseEntity(error.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(modelo);
    }



}
