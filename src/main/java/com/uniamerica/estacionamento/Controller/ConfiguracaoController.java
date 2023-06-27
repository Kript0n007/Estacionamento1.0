package com.uniamerica.estacionamento.Controller;

import com.uniamerica.estacionamento.Entity.Configuracao;
import com.uniamerica.estacionamento.Respository.ConfiguracaoRepository;
import com.uniamerica.estacionamento.Service.ConfiguracaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/configuracao")
@CrossOrigin("http://localhost:3000")
public class ConfiguracaoController {

    @Autowired
    private ConfiguracaoRepository configuracaoRepository;

    @Autowired
    private ConfiguracaoService configuracaoService;


    @GetMapping("/findAll")
    public ResponseEntity<?> listaCompleta() {

        return ResponseEntity.ok(this.configuracaoRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody final Configuracao configuracao) {
        try {
            this.configuracaoService.cadastrar(configuracao);
            return ResponseEntity.ok(configuracao);
        } catch (RuntimeException erro) {
            return ResponseEntity.badRequest().body("Erro ao cadastrar configuração: " + erro.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable("id") final Long id, @RequestBody Configuracao configuracao) {
        try {
            final Optional<Configuracao> configuracaoBancoOptional = this.configuracaoRepository.findById(id);

            if (configuracaoBancoOptional.isEmpty()) {
                throw new RuntimeException("Não foi possível identificar a configuração no banco de dados.");
            }

            Configuracao configuracaoBanco = configuracaoBancoOptional.get();

            // Atualizar os campos individualmente
            configuracaoBanco.setAtivo(configuracao.isAtivo());
            configuracaoBanco.setValorHora(configuracao.getValorHora());
            configuracaoBanco.setValorMinutoMulta(configuracao.getValorMinutoMulta());
            configuracaoBanco.setInicioExpediente(configuracao.getInicioExpediente());
            configuracaoBanco.setFimExpediente(configuracao.getFimExpediente());
            configuracaoBanco.setTempoParaDesconto(configuracao.getTempoParaDesconto());
            configuracaoBanco.setTempoDeDesconto(configuracao.getTempoDeDesconto());
            configuracaoBanco.setGerarDesconto(configuracao.getGerarDesconto());
            configuracaoBanco.setVagasMoto(configuracao.getVagasMoto());
            configuracaoBanco.setVagasVan(configuracao.getVagasVan());
            configuracaoBanco.setVagasCarro(configuracao.getVagasCarro());

            this.configuracaoRepository.save(configuracaoBanco);
            return ResponseEntity.ok("Registro atualizado.");
        } catch (DataIntegrityViolationException erro) {
            return ResponseEntity.internalServerError().body("Erro ao atualizar: " + erro.getCause().getCause().getMessage());
        } catch (RuntimeException erro) {
            return ResponseEntity.internalServerError().body("Erro ao atualizar: " + erro.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deletar(@Param("id") final Long id) {
        try {
            this.configuracaoService.deletar(id);
            return ResponseEntity.ok("Success");
        } catch (Exception error) {
            return new ResponseEntity<>("Erro ao deletar configuraçao: " + error.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
