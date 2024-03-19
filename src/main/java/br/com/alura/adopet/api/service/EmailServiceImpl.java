package br.com.alura.adopet.api.service;

import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    public void enviarEmail(String to, String subject, String message) {
        System.out.println("Enviando email fake");
        System.out.println("Destinatario: " +to);
        System.out.println("Assunto: " +subject);
        System.out.println("Mensagem: " +message);
    }

}
