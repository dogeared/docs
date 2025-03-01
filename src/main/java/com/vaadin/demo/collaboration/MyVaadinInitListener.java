package com.vaadin.demo.collaboration;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.collaborationengine.CollaborationEngine;
import com.vaadin.collaborationengine.CollaborationEngineConfiguration;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServiceInitListener;

// tag::init-listener[]
public class MyVaadinInitListener implements VaadinServiceInitListener {
    // tag::logger[]
    private static final Logger LOGGER = LoggerFactory
            .getLogger(MyVaadinInitListener.class);
    // end::logger[]

    @Override
    public void serviceInit(ServiceInitEvent serviceEvent) {
        // tag::configuration[]
        VaadinService service = serviceEvent.getSource();

        CollaborationEngineConfiguration configuration = new CollaborationEngineConfiguration();
        configuration.setDataDir("/Users/steve/vaadin/collaboration-engine/");
        CollaborationEngine.configure(service, configuration);
        // end::configuration[]
    }

    // tag::send-email[]
    private void sendEmail(String subject, String content) {
        // Replace the following information:
        String from = "sender@gmail.com";
        String password = "*****"; // Read, for example, from encrypted config
                                   // file.
        String to = "receiver@gmail.com";
        String host = "smtp.gmail.com";

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));
            message.setSubject(subject);
            message.setText(content);
            Transport.send(message);
        } catch (MessagingException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    // end::send-email[]
}
// end::init-listener[]
