package gos;

import java.io.*; 
import java.net.*; 
import java.security.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import java.util.Enumeration;
import java.util.Properties;

// This may well work but, if I am not able to upload to the
// google drive account, I probabl also can't connect to the
// google smtp server either, so this doesn't really help.
// Therefore it is currently not being used or compiled.
public final class EmailSender {
    private EmailSender () { }

    static String d_email = "systemsthinkingtest@gmail.com",
            d_password = "<zoids>",
            d_host = "smtp.gmail.com",
            d_port = "465",
            m_to = "systems.thinking.test@gmail.com",
            m_subject = "Testing",
            m_text = "Hey, this is the testing email.";
        
    public static void sendEmail() {
        send(d_email, d_password, d_host, d_port, m_to, m_subject, m_text);
    }

    // Those are the values that have the email information
    public static void send(String from, String pass, String host, String port, 
                     String to, String subject, String text) {

        Properties props = new Properties();

        // Read properties file.

        props.put("mail.smtp.user", from);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        SecurityManager security = System.getSecurityManager();

        try {
            Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getInstance(props, auth);
            MimeMessage msg = new MimeMessage(session);

            msg.setText(text);
            msg.setSubject(subject);
            msg.setFrom(new InternetAddress(from));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            Transport.send(msg);
        } catch (Exception mex) {
            mex.printStackTrace();
        }
    }

    public static class SMTPAuthenticator extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(d_email, d_password);
        }
    }
}