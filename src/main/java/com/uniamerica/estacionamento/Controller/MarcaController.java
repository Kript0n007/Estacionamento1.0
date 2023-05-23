package com.uniamerica.estacionamento.Controller;

import com.uniamerica.estacionamento.Entity.Condutor;
import com.uniamerica.estacionamento.Entity.Marca;
import com.uniamerica.estacionamento.Entity.Modelo;
import com.uniamerica.estacionamento.Respository.MarcaRepository;
import com.uniamerica.estacionamento.Service.MarcaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/marca")
public class MarcaController {

    @Autowired
    public MarcaRepository marcaRepository;

    @Autowired
    public MarcaService marcaService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") final Long id){
        return marcaService.buscarMarca(id);
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> listaCompleta() {
        return ResponseEntity.ok(this.marcaRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody final Marca marca){
        try{
            this.marcaService.cadastrar(marca);
            return ResponseEntity.ok("Registro realizado");
        }catch (Exception erro){
            return ResponseEntity.badRequest().body("Erro" + erro.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> editar (@RequestParam("id") final Long id, @RequestBody final Marca marca){
        try{
            final Marca marcaBanco = this.marcaRepository.findById(id).orElse(null);

            if(marcaBanco == null || !marcaBanco.getId().equals(marca.getId())){
                throw new RuntimeException("Registro nao identificado");
            }

            this.marcaRepository.save(marca);
            return ResponseEntity.ok("Registro atualizado.");
        }catch (DataIntegrityViolationException erro){
            return ResponseEntity.internalServerError().body("Error"+erro.getCause().getCause().getMessage());
        }catch (RuntimeException erro){
            return ResponseEntity.internalServerError().body("Erro"+erro.getMessage());
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletar(@PathVariable("id") final Long id) {
        try {
            this.marcaService.deletar(id);
            return ResponseEntity.ok("Success");
        } catch (Exception error) {
            return new ResponseEntity<>("Erro ao deletar marca: " + error.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
