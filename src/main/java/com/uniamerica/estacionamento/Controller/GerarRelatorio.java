package com.uniamerica.estacionamento.Controller;

import com.uniamerica.estacionamento.Entity.Movimentacao;
import com.uniamerica.estacionamento.Respository.MovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


    @RestController
    @RequestMapping("/relatorio")
    @CrossOrigin("http://localhost:3000")
    public class GerarRelatorio {

        @Autowired
        private final MovimentacaoRepository movimentacaoRepository;

        @Autowired
        public GerarRelatorio(MovimentacaoRepository movimentacaoRepository) {
            this.movimentacaoRepository = movimentacaoRepository;
        }

        @GetMapping("/gerar")
        public ResponseEntity<String> gerarRelatorio() {
            try {
                // Apenas como exemplo, obtendo a primeira movimentação.
                // Você precisa ajustar isso de acordo com sua lógica de negócios.
                Movimentacao movimentacao = movimentacaoRepository.findAll().get(0);
                return new ResponseEntity<>(movimentacao.gerarRelatorio(), HttpStatus.OK);
            } catch (Exception e) {
                // Log do erro
                e.printStackTrace();
                // Retornar uma resposta de erro
                return new ResponseEntity<>("Erro ao gerar relatório", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

