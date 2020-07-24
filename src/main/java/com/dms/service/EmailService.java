package com.dms.service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private Environment environment;

    @Autowired
    private MessageSource messageSource;

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private String convertTemplateToString(String templateName, Map<String, String> parametersMap, Locale locale) {
        if (locale == null)
            locale = new Locale("ar");
        Context context = new Context(locale);
        if (parametersMap != null) {
            for (Map.Entry<String, String> entry : parametersMap.entrySet()) {
                context.setVariable(entry.getKey(), entry.getValue());
            }
        }
        return templateEngine.process(templateName, context);
    }

    public void sendTestEmail(String toEmail, String subject, Map<String, String> parametersMap, Locale locale) throws Exception {
        String message = convertTemplateToString("mailTemplate", parametersMap, locale);
        sendMail(toEmail, subject, message);
    }

    @Async
    public void sendRegistrationActivationEmail(String toEmail, String uuid, Locale locale) {
        String subject = messageSource.getMessage("registration.activation", null, locale);
        Map<String, String> parametersMap = new HashMap<String, String>();
        parametersMap.put("logoUrl", environment.getProperty("email.logo.url"));
        parametersMap.put("activationUrl", environment.getProperty("application.url") + "/activate.xhtml?c=" + uuid);
        String message = convertTemplateToString("registrationActivationEmailTemplate", parametersMap, locale);
        sendMail(toEmail, subject, message);
    }

    @Async
    public void sendRegistrationApprovedEmail(String toEmail, Locale locale) {
        String subject = messageSource.getMessage("registration.approved", null, locale);
        Map<String, String> parametersMap = new HashMap<String, String>();
        parametersMap.put("appUrl", environment.getProperty("application.url"));
        parametersMap.put("acceptedImageUrl", environment.getProperty("email.accepted.url"));
        parametersMap.put("logoUrl", environment.getProperty("email.logo.url"));
        String message = convertTemplateToString("registrationAcceptedEmailTemplate", parametersMap, locale);
        sendMail(toEmail, subject, message);
    }

    @Async
    public void sendRegistrationRejectedEmail(String toEmail, String rejectReason, Locale locale) {
        String subject = messageSource.getMessage("registration.rejected", null, locale);
        Map<String, String> parametersMap = new HashMap<String, String>();
        parametersMap.put("appUrl", environment.getProperty("application.url"));
        parametersMap.put("rejectedImageUrl", environment.getProperty("email.rejected.url"));
        parametersMap.put("logoUrl", environment.getProperty("email.logo.url"));
        parametersMap.put("appUrl", environment.getProperty("application.url"));
        parametersMap.put("rejectReasonParam", rejectReason);
        String message = convertTemplateToString("registrationRejectedEmailTemplate", parametersMap, locale);
        sendMail(toEmail, subject, message);
    }

    public void sendResetPasswordEmail(String toEmail, String token, Locale locale) {
        String subject = messageSource.getMessage("resetPassword", null, locale);
        Map<String, String> parametersMap = new HashMap<String, String>();
        parametersMap.put("url", environment.getProperty("application.url") + "/resetPassword?token=" + token);
        parametersMap.put("imageUrl", environment.getProperty("email.logo.url"));
        String message = convertTemplateToString("resetPasswordEmailTemplate", parametersMap, locale);
        sendMail(toEmail, subject, message);
    }

    public void sendMail(String toEmail, String subject, String message) {
        try {
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            String testEmail = environment.getProperty("test.email");
            if (StringUtils.isNotEmpty(testEmail)) {
                toEmail = testEmail;
            }
            System.out.println("####### send email to: " + toEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(message, true);
            helper.setFrom(environment.getProperty("spring.mail.username"));
            javaMailSender.send(mail);
            System.out.println("####### AFTER SEND EMAIL ###########");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("######## Exception in sendMail", e);
        }
    }

}
