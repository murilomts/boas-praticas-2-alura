package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.AprovacaoAdocaoDTO;
import br.com.alura.adopet.api.dto.ReprovacaoAdocaoDTO;
import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDTO;
import br.com.alura.adopet.api.model.*;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import br.com.alura.adopet.api.validacoes.ValidacaoSolicitacaoAdocao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class AdocaoServiceTest {

    @InjectMocks
    private AdocaoService adocaoService;

    @Mock
    private AdocaoRepository adocaoRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private EmailService emailService;

    @Spy
    private List<ValidacaoSolicitacaoAdocao> validacoes = new ArrayList<>();

    @Mock
    private ValidacaoSolicitacaoAdocao validador1;

    @Mock
    private ValidacaoSolicitacaoAdocao validador2;

    @Mock
    private Pet pet;

    @Mock
    private Tutor tutor;

    @Mock
    private Abrigo abrigo;

    @Mock
    private SolicitacaoAdocaoDTO solicitacaoAdocaoDTO;

    @Captor
    private ArgumentCaptor<Adocao> adocaoCaptor;

    @Mock
    private AprovacaoAdocaoDTO aprovacaoAdocaoDTO;

    @Mock
    private ReprovacaoAdocaoDTO reprovacaoAdocaoDTO;

    @Spy
    private Adocao adocao;


    @Test
    void salvarAdocaoAoSolicitar() {
        //ARRANGE
        given(petRepository.getReferenceById(solicitacaoAdocaoDTO.idPet())).willReturn(pet);
        given(tutorRepository.getReferenceById(solicitacaoAdocaoDTO.idTutor())).willReturn(tutor);
        given(pet.getAbrigo()).willReturn(abrigo);

        //ACT
        adocaoService.solicitar(solicitacaoAdocaoDTO);

        //ASSERT
        then(adocaoRepository).should().save(adocaoCaptor.capture());
        Adocao adocao = adocaoCaptor.getValue();
        assertEquals(pet, adocao.getPet());
        assertEquals(tutor, adocao.getTutor());
        assertEquals(adocao.getMotivo(), solicitacaoAdocaoDTO.motivo());
    }

    @Test
    void validacaoAdocaoAoSolicitar() {
        //ARRANGE
        given(petRepository.getReferenceById(solicitacaoAdocaoDTO.idPet())).willReturn(pet);
        given(tutorRepository.getReferenceById(solicitacaoAdocaoDTO.idTutor())).willReturn(tutor);
        given(pet.getAbrigo()).willReturn(abrigo);
        validacoes.add(validador1);
        validacoes.add(validador2);

        //ACT
        adocaoService.solicitar(solicitacaoAdocaoDTO);

        //ASSERT
        then(validador1).should().validar(solicitacaoAdocaoDTO);
        then(validador2).should().validar(solicitacaoAdocaoDTO);
    }

    @Test
    void enviarEmailAoSolicitarAdocao() {

        //ARRANGE
        given(petRepository.getReferenceById(solicitacaoAdocaoDTO.idPet())).willReturn(pet);
        given(tutorRepository.getReferenceById(solicitacaoAdocaoDTO.idTutor())).willReturn(tutor);
        given(pet.getAbrigo()).willReturn(abrigo);

        //ACT
        adocaoService.solicitar(solicitacaoAdocaoDTO);

        //ASSERT
        then(adocaoRepository).should().save(adocaoCaptor.capture());
        Adocao adocao = adocaoCaptor.getValue();
        then(emailService).should().enviarEmail(
                adocao.getPet().getAbrigo().getEmail(),
                "Solicitação de adoção",
                "Olá " +adocao.getPet().getAbrigo().getNome() +"!\n\nUma solicitação de adoção foi registrada hoje para o pet: " +adocao.getPet().getNome() +". \nFavor avaliar para aprovação ou reprovação."
        );
    }

    @Test
    void aprovarUmaAdocao(){

        //ARRANGE
        given(adocaoRepository.getReferenceById(aprovacaoAdocaoDTO.idAdocao())).willReturn(adocao);
        given(adocao.getPet()).willReturn(pet);
        given(pet.getAbrigo()).willReturn(abrigo);
        given(abrigo.getEmail()).willReturn("email@example.com");
        given(adocao.getTutor()).willReturn(tutor);
        given(tutor.getNome()).willReturn("Rodrigo");
        given(adocao.getData()).willReturn(LocalDateTime.now());

        //ACT
        adocaoService.aprovar(aprovacaoAdocaoDTO);

        //ASSERT
        then(adocao).should().marcarComoAprovado();
        assertEquals(StatusAdocao.APROVADO, adocao.getStatus());
    }

    @Test
    void enviarEmailAoAprovarUmaAdocao(){

        //ARRANGE
        given(adocaoRepository.getReferenceById(aprovacaoAdocaoDTO.idAdocao())).willReturn(adocao);
        given(adocao.getPet()).willReturn(pet);
        given(pet.getAbrigo()).willReturn(abrigo);
        given(abrigo.getEmail()).willReturn("email@example.com");
        given(adocao.getTutor()).willReturn(tutor);
        given(tutor.getNome()).willReturn("Rodrigo");
        given(adocao.getData()).willReturn(LocalDateTime.now());

        //ACT
        adocaoService.aprovar(aprovacaoAdocaoDTO);

        //ASSERT

        then(emailService).should().enviarEmail(
                adocao.getPet().getAbrigo().getEmail(),
                "Adoção aprovada",
                "Parabéns " +adocao.getTutor().getNome() +"!\n\nSua adoção do pet " +adocao.getPet().getNome() +", solicitada em " +adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) +", foi aprovada.\nFavor entrar em contato com o abrigo " +adocao.getPet().getAbrigo().getNome() +" para agendar a busca do seu pet."
        );
    }


    @Test
    void reprovarUmaAdocao(){
        //ARRANGE
        given(adocaoRepository.getReferenceById(aprovacaoAdocaoDTO.idAdocao())).willReturn(adocao);
        given(adocao.getPet()).willReturn(pet);
        given(pet.getAbrigo()).willReturn(abrigo);
        given(abrigo.getEmail()).willReturn("email@example.com");
        given(adocao.getTutor()).willReturn(tutor);
        given(tutor.getNome()).willReturn("Rodrigo");
        given(adocao.getData()).willReturn(LocalDateTime.now());

        //ACT
        adocaoService.reprovar(reprovacaoAdocaoDTO);


        //ASSERT
        then(adocao).should().marcarComoReprovado(reprovacaoAdocaoDTO.justificativa());
        assertEquals(StatusAdocao.REPROVADO,adocao.getStatus());
    }

    @Test
    void enviarEmailAoReprovarUmaAdocao(){
        //Arrange
        given(adocaoRepository.getReferenceById(aprovacaoAdocaoDTO.idAdocao())).willReturn(adocao);
        given(adocao.getPet()).willReturn(pet);
        given(pet.getAbrigo()).willReturn(abrigo);
        given(abrigo.getEmail()).willReturn("murilo@mail.com");
        given(adocao.getTutor()).willReturn(tutor);
        given(tutor.getNome()).willReturn("Murilo");
        given(adocao.getData()).willReturn(LocalDateTime.now());

        //Act
        adocaoService.reprovar(reprovacaoAdocaoDTO);

        //Assert

        then(emailService).should().enviarEmail(
                adocao.getPet().getAbrigo().getEmail(),
                "Solicitação de adoção",
                "Olá " +adocao.getTutor().getNome() +"!\n\nInfelizmente sua adoção do pet " +adocao.getPet().getNome() +", solicitada em " +adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) +", foi reprovada pelo abrigo " +adocao.getPet().getAbrigo().getNome() +" com a seguinte justificativa: " +adocao.getJustificativaStatus()
        );
    }

}