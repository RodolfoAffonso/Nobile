package com.rodolfoafonso.nobile.service;


import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine emailTemplateEngine;

    public void sendPasswordResetEmail(String to, String token) throws MessagingException {
        String resetLink = "http://localhost:3000/reset-password?token=" + token;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject("Recuperação de senha - Nobile");

        Context context = new Context();
        context.setVariable("resetLink", resetLink);

        String htmlContent = emailTemplateEngine.process("password-reset", context);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}
