package com.uniamerica.estacionamento.Controller;

import com.uniamerica.estacionamento.Entity.Movimentacao;
import com.uniamerica.estacionamento.Entity.Veiculo;
import com.uniamerica.estacionamento.Respository.ModeloRepository;
import com.uniamerica.estacionamento.Respository.VeiculoRepository;
import com.uniamerica.estacionamento.Service.VeiculoService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/veiculo")
public class VeiculoController {

    @Autowired
    private VeiculoRepository veiculoRepository;
    @Autowired
    private VeiculoService veiculoService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findByIdParam(@PathVariable("id") final Long id){
        return veiculoService.buscarVeiculo(id);
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> listaCompleta(){
        return ResponseEntity.ok(this.veiculoRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody @Validated final Veiculo veiculo){

        try{
            this.veiculoService.cadastrar(veiculo);
            return ResponseEntity.ok("Registro realizado com sucesso");
        }catch (DataIntegrityViolationException erro){
            return ResponseEntity.internalServerError().body("Erro"+erro.getMessage());
        } catch (RuntimeException erro){
            return ResponseEntity.internalServerError().body("Erro"+erro.getMessage());
        } catch (Exception erro){
            return ResponseEntity.badRequest().body("Erro" + erro.getMessage());
        }
    }

     @PutMapping
    public ResponseEntity<?> editar(@RequestParam("id") final Long id, @RequestBody final Veiculo veiculo){
        try{
            final Veiculo veiculoBanco = this.veiculoRepository.findById(id).orElse(null);

            if(veiculoBanco == null || !veiculoBanco.getId().equals(veiculo.getId())){
                throw new RuntimeException("Nao foi possivel identificar o registro no banco de dados");
            }
            this.veiculoRepository.save(veiculo);
            return ResponseEntity.ok("Registro atualizado");
        }catch (DataIntegrityViolationException erro){
            return ResponseEntity.internalServerError().body("Erro" + erro.getCause().getCause().getMessage());
        }catch (RuntimeException erro){
            return ResponseEntity.internalServerError().body("Erro" + erro.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete (@PathVariable("id") final Long id){
        final Veiculo veiculoBanco = this.veiculoRepository.findById(id).orElse(null);

        try{
            this.veiculoService.deletar(veiculoBanco);
            return ResponseEntity.ok("Registro deletado");
        }catch (RuntimeException erro){
            return ResponseEntity.internalServerError().body("Registro nn exite ou ja foi deletado: "+erro.getMessage());
        }
    }

}
