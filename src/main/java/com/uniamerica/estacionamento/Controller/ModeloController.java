package com.uniamerica.estacionamento.Controller;

import com.uniamerica.estacionamento.Entity.Modelo;
import com.uniamerica.estacionamento.Entity.Veiculo;
import com.uniamerica.estacionamento.Respository.ModeloRepository;
import com.uniamerica.estacionamento.Service.ModeloService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/modelo")
@CrossOrigin("http://localhost:3001")
public class ModeloController {

    @Autowired
    private ModeloRepository modeloRepository;

    @Autowired
    private ModeloService modeloService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") final Long id){

        return modeloService.buscarCondutor(id);
    }


    @GetMapping("/findAll")
    public ResponseEntity<?> listCompleta(){
        return ResponseEntity.ok(this.modeloRepository.findAll());
    }

    @PostMapping("/add")
    public ResponseEntity<?> cadastrar(@RequestBody final Modelo modelo){
        try{
            this.modeloService.cadastro(modelo);
            return ResponseEntity.ok("Registro realizado com sucesso");
        }catch (DataIntegrityViolationException erro){
            return ResponseEntity.internalServerError().body("Erro"+erro.getMessage());
        }catch (RuntimeException erro){
            return ResponseEntity.internalServerError().body("Erro"+erro.getMessage());
        }catch (Exception exception) {
            return ResponseEntity.badRequest().body("Error" + exception.getMessage());
        }
    }

    @PutMapping("/editar")
    public ResponseEntity<?> editar(@RequestParam("id") final Long id, @RequestBody final Modelo modelo){
        try{
            final Modelo modeloData = this.modeloRepository.findById(id).orElse(null);

            if(modeloData == null || !modeloData.getId().equals(modelo.getId())){
                throw new RuntimeException("Nao foi possivel identificar o registro no banco de dados");
            }

            this.modeloRepository.save(modelo);
            return ResponseEntity.ok("Registro atualizado");
        } catch(DataIntegrityViolationException erro){
            return ResponseEntity.internalServerError().body("Error" + erro.getCause().getCause().getMessage());
        } catch(RuntimeException erro){
            return ResponseEntity.internalServerError().body("Error" + erro.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") final Long id) {
        final Modelo modeloData = this.modeloRepository.findById(id).orElse(null);

        try {
            this.modeloService.deletar(modeloData);
            return ResponseEntity.ok("Registro deletado.");
        } catch (RuntimeException erro) {
            return ResponseEntity.internalServerError().body("Erro" + erro.getMessage());
        }
    }

}
