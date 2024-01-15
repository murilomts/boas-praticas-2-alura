package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastrarTutorDTO;
import br.com.alura.adopet.api.exception.CadastrarTutorException;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class TutorService {

    @Autowired
    private TutorRepository tutorRepository;

    public void cadastrar(CadastrarTutorDTO dto) {
        boolean telefoneJaCadastrado = tutorRepository.existsByTelefone(dto.telefone());
        boolean emailJaCadastrado = tutorRepository.existsByEmail(dto.email());

        try {
            if (telefoneJaCadastrado || emailJaCadastrado) {
                throw new CadastrarTutorException("Dados já cadastrados para outro tutor!");
            } else {
                Tutor tutor = new Tutor();
                tutor.setNome(dto.nome());
                tutor.setTelefone(dto.telefone());
                tutor.setEmail(dto.email());
            }
        } catch (CadastrarTutorException e) {
            e.getMessage();
        }
    }

}
