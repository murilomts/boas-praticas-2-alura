package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastrarPetDTO;
import br.com.alura.adopet.api.dto.RetornoPetDTO;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    public List<RetornoPetDTO> consultarPetsDisponiveis() {
        return petRepository.findAllByAdotadoFalse()
                .stream()
                .map(RetornoPetDTO::new)
                .toList();
    }

    public void cadastrarPet(Abrigo abrigo, CadastrarPetDTO dto) {
        petRepository.save(new Pet(dto, abrigo));
    }
}
