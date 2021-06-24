package com.epam.jwd_critics.util.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

public class MailSender {
    private static final String MAIL_PROPERTIES = "properties/mail.properties";
    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);
    private final Properties properties;
    private MimeMessage message;
    private String recipient;
    private String mailSubject;
    private String mailText;

    public MailSender() {
        properties = new Properties();
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream(MAIL_PROPERTIES));
        } catch (IOException e) {
            logger.error("Loading properties for mail sending error", e);
        }
    }

    public MailSender(String mailSubject, String mailText, String recipient) {
        this.recipient = recipient;
        this.mailSubject = mailSubject;
        this.mailText = mailText;
        properties = new Properties();
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream(MAIL_PROPERTIES));
        } catch (IOException e) {
            logger.error("Loading properties for mail sending error", e);
        }
    }

    public void send() {
        try {
            initMessage();
            Transport.send(message);
        } catch (AddressException e) {
            logger.error("Invalid address: " + recipient + " " + e);
        } catch (MessagingException e) {
            logger.error("Error generating or sending message: " + e);
        }
    }

    private void initMessage() throws MessagingException {
        Session mailSession = MailSessionFactory.getSession(properties);
        mailSession.setDebug(true);
        message = new MimeMessage(mailSession);
        message.setSubject(mailSubject);
        message.setContent(mailText, "text/plain");
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
    }
}
