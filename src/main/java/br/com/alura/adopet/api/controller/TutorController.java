package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.CadastrarTutorDTO;
import br.com.alura.adopet.api.exception.CadastrarTutorException;
import br.com.alura.adopet.api.service.TutorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tutores")
public class TutorController {

    @Autowired
    private TutorService service;

    @PostMapping
    @Transactional
    public ResponseEntity<String> cadastrar(@RequestBody @Valid CadastrarTutorDTO dto) {
        try {
            this.service.cadastrar(dto);
            return ResponseEntity.ok("Cadastro de Tutor realizada com sucesso.");
        } catch (CadastrarTutorException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping
    @Transactional
    public ResponseEntity<String> atualizar(@RequestBody @Valid CadastrarTutorDTO dto) {
        try {
            this.service.cadastrar(dto);
            return ResponseEntity.ok("Atualização de Tutor realizada com sucesso.");
        } catch (CadastrarTutorException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
