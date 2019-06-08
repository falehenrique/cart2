package br.com.exmart.rtdpjlite.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.mail.internet.MimeMessage;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

@Component
public class EmailService {

    @Autowired
    private Configuration freemarkerConfig;
    protected static Logger log= LoggerFactory.getLogger(EmailService.class);

    @Value(value = "${spring.mail.username}")
    String from;
    @Autowired
    public JavaMailSenderImpl emailSender;


    public void sendMessage(String to, String subject, String template, HashMap atributos) throws IOException, TemplateException {

        Template t = freemarkerConfig.getTemplate(template);
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, atributos);

//        log.debug("Enviando email:{} \nsubject:\n{} \nconteúdo \n{}", to,subject, text);
        //emailSender.getJavaMailProperties().setProperty("mail.debug", "true");
        sendMessageWithAttachment(to,subject,html,null);
    }


    public void sendMessageWithAttachment(String to,
                                          String subject,
                                          String text,
                                          String pathToAttachment) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);
            helper.setFrom(from);
            if(pathToAttachment!=null) {
                FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
                helper.addAttachment("Invoice", file);
            }
            //message.writeTo(new FileOutputStream(new File("email.txt")));
            emailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}