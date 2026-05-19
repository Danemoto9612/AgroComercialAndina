package com.comercializadora.modelo.servicios;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import java.util.Map;

@Service
public class EmailServicio {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String remitente;

    public void enviarConPlantillaYAdjunto(String destino, String asunto,
            Map<String, Object> vars, MultipartFile archivo) throws Exception {

        Context ctx = new Context();
        if (vars != null) {
            ctx.setVariables(vars);
        }
        String html = templateEngine.process("correo", ctx);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(destino);
        helper.setSubject(asunto);
        helper.setText(html, true);
        helper.setFrom(remitente);

        if (archivo != null && !archivo.isEmpty()) {
            helper.addAttachment(archivo.getOriginalFilename(),
                    new ByteArrayResource(archivo.getBytes()));
        }
        mailSender.send(message);
    }
}
