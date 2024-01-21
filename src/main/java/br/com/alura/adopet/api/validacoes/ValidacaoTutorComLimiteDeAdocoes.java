package br.com.alura.adopet.api.validacoes;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDTO;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ValidacaoTutorComLimiteDeAdocoes implements ValidacaoSolicitacaoAdocao {

    @Autowired
    private AdocaoRepository adocaoRepository;

    @Override
    public void validar(SolicitacaoAdocaoDTO dto) {
        List<Adocao> adocoes = adocaoRepository.findByTutorIdAndStatus(dto.idTutor(), StatusAdocao.APROVADO);
        if (!adocoes.isEmpty() && adocoes.size() > 4) {
            throw new ValidacaoException("Tutor chegou ao limite máximo de 5 adoções!");
        }
    }

}
