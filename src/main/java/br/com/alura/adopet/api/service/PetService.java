package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.RetornoPetDTO;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class PetService {

    @Autowired
    PetRepository petRepository;

    public List<RetornoPetDTO> consultarPetsDisponiveis() {
        List<Pet> pets = petRepository.findByAdotado(false);
        return pets.stream().map(p -> new RetornoPetDTO(p)).collect(Collectors.toList());
    }
}
