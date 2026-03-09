package Util;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class EmailSender {

    private Properties loadMailProperties() {
        Properties props = new Properties();
        try (InputStream input = EmailSender.class.getClassLoader().getResourceAsStream("email.properties")) {
            if (input == null) {
                throw new RuntimeException("Không tìm thấy file mail.properties");
            }
            props.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Lỗi load file properties", ex);
        }
        return props;
    }

    public void sendVerificationEmail(String toEmail, String title, String username, String verificationLink, String content,String thanks) {
        Properties mailProps = loadMailProperties();

        String fromEmail = mailProps.getProperty("mail.from");
        String password = mailProps.getProperty("mail.password");

        Properties props = new Properties();
        props.put("mail.smtp.auth", mailProps.getProperty("mail.smtp.auth"));
        props.put("mail.smtp.starttls.enable", mailProps.getProperty("mail.smtp.starttls.enable"));
        props.put("mail.smtp.host", mailProps.getProperty("mail.smtp.host"));
        props.put("mail.smtp.port", mailProps.getProperty("mail.smtp.port"));

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail,"SÁCH THIẾU NHI CHO BÉ"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(title);

            String htmlContent = "<h2>Xin chào" + username + "</h2>"
                    + "<p>"+thanks+"!</p>"
                    + "<p>"+content+":</p>"
                    + "<p style=\"text-align: center;\">"
                    + "<div style=\"background-color: #007bff; color: white; padding: 15px 30px; text-decoration: none; border-radius: 5px;\">"+verificationLink+"</a>"
                    + "</p>";

            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("Gửi email thành công đến: " + toEmail);

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi gửi email", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
