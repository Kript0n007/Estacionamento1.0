package com.uniamerica.estacionamento.Service;

import com.uniamerica.estacionamento.Entity.Configuracao;
import com.uniamerica.estacionamento.Respository.ConfiguracaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.util.List;

@Service
public class ConfiguracaoService {

    @Autowired
    private ConfiguracaoRepository configuracaoRepository;



    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<String> cadastrar(final Configuracao config) {
        try {
            Assert.isTrue(config.getValorHora() != null, "Valor hora não informado");
            Assert.isTrue(config.getValorMinutoMulta() != null, "Valor minuto multa não informado");
            Assert.isTrue(config.getInicioExpediente() != null, "Inicio expediente não informado");
            Assert.isTrue(config.getFimExpediente() != null, "Fim expediente não informado");
            Assert.isTrue(config.getTempoParaDesconto() != null, "Tempo desconto não informado");
            Assert.isTrue(config.getGerarDesconto() != null, "Gerar desconto não informado");
//            String RegexEx = "^([01]\d|2[0-3]):([0-5]\d):([0-5]\d)$";

//            Assert.isTrue(config.getInicioExpediente().equals(RegexEx));

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.internalServerError().body("Erro ao atualizar configuração: " + e.getRootCause().getMessage());
        }
        this.configuracaoRepository.save(config);
        return ResponseEntity.status(HttpStatus.CREATED).body("Cadastrado");
    }

    @Transactional(rollbackFor = Exception.class)
    public Configuracao editar(final Configuracao config) {

        final Configuracao configBanco = this.configuracaoRepository.findById(config.getId()).orElse(null);

            Assert.isTrue((configBanco != null), "Não foi possivel identificar o registro informado");
//        Assert.isTrue(!config.getId().equals(id), "Não foi possivel identificar o registro informado");
            Assert.isTrue(config.getValorHora() != null, "Valor hora não informado");
            Assert.isTrue(config.getValorMinutoMulta() != null, "Valor minuto multa não informado");
            Assert.isTrue(config.getInicioExpediente() != null, "Inicio expediente não informado");
            Assert.isTrue(config.getFimExpediente() != null, "Fim expediente não informado");
            Assert.isTrue(config.getTempoParaDesconto() != null, "Tempo desconto não informado");
            Assert.isTrue(config.getGerarDesconto() != null, "Gerar desconto não informado");
            Assert.isTrue(config.getVagasCarro() != null, "Vagas de carro não informado");
            Assert.isTrue(config.getVagasVan() != null, "Vagas de van não informado");
            Assert.isTrue(config.getVagasMoto() != null, "Vagas de moto não informado");



        return this.configuracaoRepository.save(config);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deletar(final Long id) {
        final Configuracao configBanco = this.configuracaoRepository.findById(id).orElse(null);

        if (configBanco == null) {
            throw new RuntimeException("Id nn encontradodo tenta outro ai");
        } else {
            configBanco.setAtivo(false);
            this.configuracaoRepository.delete(configBanco);
        }
    }

    public BigDecimal getValorHora() {
        Sort sort = Sort.by(Sort.Direction.DESC, "data");
        List<Configuracao> configuracoes = configuracaoRepository.findLatestConfiguracao(sort);
        if (!configuracoes.isEmpty()) {
            Configuracao configuracao = configuracoes.get(0);
            return configuracao.getValorHora();
        }
        return BigDecimal.ZERO;
    }

}

