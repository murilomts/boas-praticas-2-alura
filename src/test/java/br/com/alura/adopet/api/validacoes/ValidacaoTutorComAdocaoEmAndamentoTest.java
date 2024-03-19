package br.com.alura.adopet.api.validacoes;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDTO;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class ValidacaoTutorComAdocaoEmAndamentoTest {

    @InjectMocks
    private ValidacaoTutorComAdocaoEmAndamento validador;

    @Mock
    private AdocaoRepository adocaoRepository;

    @Mock
    private SolicitacaoAdocaoDTO solicitacaoAdocaoDTO;

    @Test
    void naoPermitirSolicitacaoTutorComAdocaoEmAndamento() {
        //ARRANGE
        given(adocaoRepository.existsByTutorIdAndStatus(
                solicitacaoAdocaoDTO.idTutor(),
                StatusAdocao.AGUARDANDO_AVALIACAO)
        ).willReturn(true);

        //ACT + ASSERT
        assertThrows(ValidacaoException.class, () -> validador.validar(solicitacaoAdocaoDTO));
    }

    @Test
    void permitirSolicitacaoTutorSemAdocaoEmAndamento() {
        //ARRANGE
        given(adocaoRepository.existsByTutorIdAndStatus(
                solicitacaoAdocaoDTO.idTutor(),
                StatusAdocao.AGUARDANDO_AVALIACAO)
        ).willReturn(false);

        //ACT + ASSERT
        assertDoesNotThrow(() -> validador.validar(solicitacaoAdocaoDTO));
    }
}