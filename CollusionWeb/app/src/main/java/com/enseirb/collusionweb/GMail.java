package com.enseirb.collusionweb;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import android.os.AsyncTask;
import android.util.Log;

public class GMail {
    final String emailPort = "587"; // gmail's smtp port
    final String smtpAuth = "true";
    final String starttls = "true";
    final String emailHost = "smtp.gmail.com";

    String fromEmail;
    String fromPassword;
    String toEmailList;
    String emailSubject;
    String emailBody;

    public GMail() {}

    public GMail(String fromEmail, String fromPassword,
                 String toEmailList, String emailSubject, String emailBody) {
        this.fromEmail = fromEmail;
        this.fromPassword = fromPassword;
        this.toEmailList = toEmailList;
        this.emailSubject = emailSubject;
        this.emailBody = emailBody;

        Properties props = new Properties();
        props.put("mail.smtp.auth", smtpAuth);
        props.put("mail.smtp.starttls.enable", starttls);
        props.put("mail.smtp.host", emailHost);
        props.put("mail.smtp.port", emailPort);

        Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, fromPassword);
                }
            });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmailList));
            message.setSubject(emailSubject);
            message.setText(emailBody);

            new SendMailTask().execute(message);

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

    }

    private class SendMailTask extends AsyncTask<Message,String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
                return "Success";
            }
            catch(Exception e)
            {
                Log.e("SendMailTask", e.getMessage());
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals("Success")) {
                super.onPostExecute(result);
            } else {
                Log.e("SendMailTask","Email failure");
            }
        }
    }
}

