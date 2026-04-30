package Util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class EmailSender {

    private Properties mailProps;
    private Session session;

    public EmailSender() {
        this.mailProps = loadMailProperties();
        this.session = createSession(mailProps);
    }

    private Properties loadMailProperties() {
        Properties props = new Properties();
        try (InputStream input = EmailSender.class.getClassLoader()
                .getResourceAsStream("email.properties")) {

            if (input == null) {
                throw new RuntimeException("Không tìm thấy file email.properties");
            }
            props.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Lỗi load file properties", ex);
        }
        return props;
    }

    private Session createSession(Properties mailProps) {

        String fromEmail = mailProps.getProperty("mail.from");
        String password = mailProps.getProperty("mail.password");

        Properties props = new Properties();
        props.put("mail.smtp.auth", mailProps.getProperty("mail.smtp.auth"));
        props.put("mail.smtp.starttls.enable", mailProps.getProperty("mail.smtp.starttls.enable"));
        props.put("mail.smtp.host", mailProps.getProperty("mail.smtp.host"));
        props.put("mail.smtp.port", mailProps.getProperty("mail.smtp.port"));

        props.put("mail.smtp.connectiontimeout", "5000");
        props.put("mail.smtp.timeout", "5000");
        props.put("mail.smtp.writetimeout", "5000");

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });
    }


    public void sendVerificationEmail(String toEmail, String title,
                                      String username,
                                      String verificationLink,
                                      String content,
                                      String thanks) {

        new Thread(() -> {
            try {
                String fromEmail = mailProps.getProperty("mail.from");

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(fromEmail, "SÁCH THIẾU NHI CHO BÉ"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
                message.setSubject(title);

                String htmlContent =
                        "<div style='font-family:Arial'>" +
                                "<h2>Xin chào " + username + "</h2>" +
                                "<p>" + thanks + "!</p>" +
                                "<p>" + content + ":</p>" +
                                "<div style='margin-top:10px;padding:12px;background:#007bff;color:#fff;border-radius:6px;display:inline-block;'>" +
                                verificationLink +
                                "</div>" +
                                "</div>";

                message.setContent(htmlContent, "text/html; charset=utf-8");

                Transport.send(message);
                System.out.println("Gửi email thành công: " + toEmail);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    public void sendSimpleEmail(String toEmail, String title, String content) {

        new Thread(() -> {
            try {
                String fromEmail = mailProps.getProperty("mail.from");

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(fromEmail, "SÁCH THIẾU NHI CHO BÉ"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
                message.setSubject(title);

                String htmlContent =
                        "<div style='font-family:Arial'>" +
                                "<h2>" + title + "</h2>" +
                                "<p>" + content + "</p>" +
                                "</div>";

                message.setContent(htmlContent, "text/html; charset=utf-8");

                Transport.send(message);
                System.out.println("Gửi email thành công: " + toEmail);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}