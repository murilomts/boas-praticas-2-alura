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
class ValidacaoTutorComLimiteDeAdocoesTest {

    @InjectMocks
    private ValidacaoTutorComLimiteDeAdocoes validador;

    @Mock
    private AdocaoRepository adocaoRepository;

    @Mock
    private SolicitacaoAdocaoDTO solicitacaoAdocaoDTO;

    @Test
    void naoPermitirSolicitacaoQuandoTutorAtingirLimite5Adocoes() {
        //ARRANGE
        given(adocaoRepository.findCountByTutorIdAndStatus(
                solicitacaoAdocaoDTO.idTutor(),
                StatusAdocao.APROVADO)).willReturn(5);

        //ACT + ASSERT
        assertThrows(ValidacaoException.class, () -> validador.validar(solicitacaoAdocaoDTO));
    }

    @Test
    void deveriaPermitirSolicitacaoDeAdocaoTutorNaoAtingiuLimiteDe5Adocoes() {
        //Arrange
        given(adocaoRepository.findCountByTutorIdAndStatus(
                solicitacaoAdocaoDTO.idTutor(),
                StatusAdocao.APROVADO)).willReturn(4);

        //Act + Assert
        assertDoesNotThrow(() -> validador.validar(solicitacaoAdocaoDTO));
    }
}