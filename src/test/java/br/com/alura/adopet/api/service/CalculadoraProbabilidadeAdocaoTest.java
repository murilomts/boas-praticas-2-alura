package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastrarAbrigoDTO;
import br.com.alura.adopet.api.dto.CadastrarPetDTO;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.ProbabilidadeAdocao;
import br.com.alura.adopet.api.model.TipoPet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CalculadoraProbabilidadeAdocaoTest {

    @Test
    @DisplayName("Probabilidade alta para gatos jovens com peso baixo")
    void probabilidadeAltaParaGatos() {
        //idade 4 anos e 4kg - ALTA

        Abrigo abrigo = new Abrigo(new CadastrarAbrigoDTO(
                "Abrigo feliz",
                "94999999999",
                "abrigofeliz@email.com.br"
        ));
        Pet pet = new Pet(new CadastrarPetDTO(
                TipoPet.GATO,
                "Miau",
                "Siames",
                4,
                "Cinza",
                4.0f
        ), abrigo);

        CalculadoraProbabilidadeAdocao calculadora = new CalculadoraProbabilidadeAdocao();
        ProbabilidadeAdocao probabilidade = calculadora.calcular(pet);

        Assertions.assertEquals(ProbabilidadeAdocao.ALTA, probabilidade);
    }

    @Test
    @DisplayName("Probabilidade média para gatos idosos com peso baixo")
    void probabilidadeMediaparaGatos() {

        //idade 15 anos e 4kg - MEDIA

        Abrigo abrigo = new Abrigo(new CadastrarAbrigoDTO(
                "Abrigo feliz",
                "94999999999",
                "abrigofeliz@email.com.br"
        ));

        Pet pet = new Pet(new CadastrarPetDTO(
                TipoPet.GATO,
                "Miau",
                "Siames",
                15,
                "Cinza",
                4.0f
        ), abrigo);

        CalculadoraProbabilidadeAdocao calculadora = new CalculadoraProbabilidadeAdocao();
        ProbabilidadeAdocao probabilidade = calculadora.calcular(pet);

        Assertions.assertEquals(ProbabilidadeAdocao.MEDIA, probabilidade);
    }

}