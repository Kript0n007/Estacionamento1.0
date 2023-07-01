package com.uniamerica.estacionamento.Controller;

import com.uniamerica.estacionamento.Entity.Configuracao;
import com.uniamerica.estacionamento.Entity.Movimentacao;
import com.uniamerica.estacionamento.Recibo;
import com.uniamerica.estacionamento.Respository.ModeloRepository;
import com.uniamerica.estacionamento.Respository.MovimentacaoRepository;
import com.uniamerica.estacionamento.Service.MovimentacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movimentacao")
@CrossOrigin("http://localhost:3000")
public class MovimentacaoController {

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private MovimentacaoService movimentacaoService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findByParam(@PathVariable("id") final Long id){
       return movimentacaoService.buscarVeiculo(id);
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> listaMovimentacao(){
        return ResponseEntity.ok(this.movimentacaoRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<?> cadastrar (@RequestBody final Movimentacao movimentacao){
        try{

            Configuracao configuracao = new Configuracao();
            movimentacao.setValorHora(configuracao.getValorHora());

            this.movimentacaoRepository.save(movimentacao);
            return ResponseEntity.ok("Registro realizado.");
        }catch (Exception erro){
            return ResponseEntity.badRequest().body("Erro" + erro.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar (@PathVariable("id") final Long id, @RequestBody final Movimentacao movimentacao){
        try{
            final Movimentacao movimentacaoBanco = this.movimentacaoRepository.findById(id).orElse(null);

            if (movimentacaoBanco == null || !movimentacaoBanco.getId().equals(movimentacao.getId())){
                throw new RuntimeException("Registro nao identificado");
            }

            this.movimentacaoRepository.save(movimentacao);
            return ResponseEntity.ok("Registro atualizado");
        }catch (DataIntegrityViolationException erro){
            return ResponseEntity.internalServerError().body("Erro" + erro.getCause().getCause().getMessage());
        }catch (RuntimeException erro){
            return ResponseEntity.internalServerError().body("Erro" + erro.getMessage());
        }
    }

//    @PutMapping("/saida/{id}")
//    public ResponseEntity<?> saida(@PathVariable("id") final Long id){
//        try{
//            Recibo recibo = this.movimentacaoService.saida(id);
//            return ResponseEntity.ok(recibo);
//        } catch (RuntimeException erro){
//            return ResponseEntity.badRequest().body("Não conseguio sair"+erro.getMessage());
//        }
//    }

    @PutMapping("/sair/{id}")
    public ResponseEntity<?> sair(@PathVariable("id") final Long id){
        try{
            this.movimentacaoService.sair(id);
            return ResponseEntity.ok("Registro realizado e relatorio gerado");
        } catch (RuntimeException erro){
            return ResponseEntity.badRequest().body("Não conseguio sair"+erro.getMessage());
        }
    }

     @DeleteMapping("/delete")
     public ResponseEntity<?> deletar(@RequestParam("id") final Long id) {
            try {
                this.movimentacaoService.deletar(id);
                return ResponseEntity.ok("Success");
            } catch (Exception error) {
                return new ResponseEntity<>("Erro ao deletar configuraçao: " + error.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
}
