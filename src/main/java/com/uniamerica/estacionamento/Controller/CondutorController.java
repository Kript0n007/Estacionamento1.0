package com.uniamerica.estacionamento.Controller;

import com.uniamerica.estacionamento.Entity.Condutor;
import com.uniamerica.estacionamento.Entity.Movimentacao;
import com.uniamerica.estacionamento.Respository.CondutorRepository;
import com.uniamerica.estacionamento.Respository.MovimentacaoRepository;
import com.uniamerica.estacionamento.Service.CondutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/condutor")
@CrossOrigin("http://localhost:3000")
public class CondutorController {

    @Autowired
    public CondutorRepository condutorRepository;

    @Autowired
    public CondutorService condutorService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") final Long id){
        return condutorService.buscarCondutor(id);
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll(){

        return ResponseEntity.ok(this.condutorRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<?> cadastrar (@RequestBody final Condutor condutor) {

        try{
            this.condutorService.cadastrar(condutor);
            return ResponseEntity.ok("Regitro realizado com sucesso.");
        } catch (DataIntegrityViolationException erro){
            return ResponseEntity.internalServerError().body("Erro"+erro.getMessage());
        } catch (RuntimeException erro){
            return ResponseEntity.internalServerError().body("Erro"+erro.getMessage());
        } catch (Exception erro){
            return ResponseEntity.badRequest().body("Erro"+erro.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar (@PathVariable("id") final Long id, @RequestBody final Condutor condutor){
        try{
            final Condutor condutorValue = this.condutorRepository.findById(id).orElse(null);

            if(condutorValue == null || !condutorValue.getId().equals(condutor.getId())){
                throw new RuntimeException("Registro nao identificado");
            }

            this.condutorRepository.save(condutor);
            return ResponseEntity.ok("Registro atualizado");
        }catch (DataIntegrityViolationException ex){
            return ResponseEntity.internalServerError().body("Error"+ex.getCause().getMessage());
        }catch (RuntimeException ex){
            return ResponseEntity.internalServerError().body("Erro"+ex.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deletar(@Param("id") final Long id) {
        try {
            this.condutorService.deletar(id);
            return ResponseEntity.ok("Success");
        } catch (Exception error) {
            return new ResponseEntity<>("Erro ao deletar condutor: " + error.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
