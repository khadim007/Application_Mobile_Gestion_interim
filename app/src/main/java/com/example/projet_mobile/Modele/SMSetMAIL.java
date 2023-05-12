package com.example.projet_mobile.Modele;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.example.projet_mobile.Controler.AccueilActivity;
import com.example.projet_mobile.Controler.PartageActivity;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SMSetMAIL {
    private static final int PERMISSION_REQUEST_CODE = 1;
    SmsManager smsManager;
    PartageActivity partage;
    Context context;
    String contact;
    String text;

    public SMSetMAIL(Context context, String contact, String text, PartageActivity partage){
        this.context = context;
        this.contact = contact;
        this.text = text;
        this.partage = partage;
    }


    //--------------------------------------------SMS--------------------------------------
    public void envoyerSMS() {
        smsManager = SmsManager.getDefault();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.SEND_SMS};
                partage.requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            } else {
                go();
            }
        }
    }

    public void go(){
        String message = text;
        smsManager.sendTextMessage(contact, null, message, null, null);

        if(partage.aller.equals("true")) {
            Toast.makeText(context, "Succes !!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, AccueilActivity.class);
            context.startActivity(intent);
        }
    }



    //--------------------------------------------EMAIL--------------------------------------
    public void envoyerEmail() {
        String senderEmail = "projet.mobile.kao@gmail.com";
        String password = "lacclrbagojmcupu";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(contact));
            message.setSubject("Message envoye par KAO Interim");

            String messageBody = text;
            message.setText(messageBody);

            SendMailTask sendMailTask = new SendMailTask();
            sendMailTask.execute(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class SendMailTask extends AsyncTask<Message, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(context, "Please wait", "Sending mail", true, false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

            if(partage.aller.equals("true")) {
                Toast.makeText(context, "Succes !!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, AccueilActivity.class);
                context.startActivity(intent);
            }
        }

        @Override
        protected Void doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
