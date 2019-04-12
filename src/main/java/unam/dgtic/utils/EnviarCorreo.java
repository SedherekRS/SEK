/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unam.dgtic.utils;


import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author edher
 */
public class EnviarCorreo {
    
    public static void sendMail(String destinatario, String asunto, String pass) {
          
        String host="smtp.gmail.com";  
        final String user="arpi.project.ai@gmail.com";//change accordingly  
        final String password="010390rs";//change accordingly  
        
        String to= destinatario;
        
        Properties props = new Properties();  
        props.put("mail.smtp.host",host);  
        props.put("mail.smtp.port", "587"); 
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true"); 
        
        Session session = Session.getInstance(props,new javax.mail.Authenticator() {  
            
            protected PasswordAuthentication getPasswordAuthentication() {  return new PasswordAuthentication(user,password);}  });  
        
        try {  
     MimeMessage message = new MimeMessage(session);  
     message.setFrom(new InternetAddress(user));  
     message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
     message.setSubject(asunto);  
     message.setText("Se ha Solicitado una nueva contraseña:" + '\n' + "Su nueva contraseña es:"+pass);  
       
    //send the message  
     Transport.send(message);  
  
     System.out.println("message sent successfully...");  
   
     } catch (MessagingException e) {e.printStackTrace();}  

    }
    
}
