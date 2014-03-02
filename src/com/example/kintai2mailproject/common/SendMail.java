package com.example.kintai2mailproject.common;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


import com.example.kintai2mailproject.dto.SendMailDto;



public class SendMail {


	public void sendMail(SendMailDto sendMailDto){

		Properties props = new Properties();
		props.setProperty("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.host", "smtp.gmail.com"); // SMTPサーバ名
		props.put("mail.host", "smtp.gmail.com");      // 接続するホスト名
		props.put("mail.smtp.port", "587");       // SMTPサーバポート
		props.put("mail.smtp.auth", "true");    // smtp auth
		props.put("mail.smtp.starttls.enable", "true");	// STTLS

		// セッション
		Session session = Session.getDefaultInstance(props);
		session.setDebug(true);

		MimeMessage msg = new MimeMessage(session);
		try {
			msg.setSubject("gmailでメール送信テスト(utf-8)", "utf-8");
			//送信元アドレス
			msg.setFrom(new InternetAddress("funnybunny0504@gmail.com"));
			//送信先アドレス
//			msg.setSender(new InternetAddress("funnybunny0504@gmail.com"));
			//送信先アドレス
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress("funnybunny0504@gmail.com"));
			//本文
			msg.setText("gmail経由でgmail.com向けメール送信テスト",	"utf-8");

			Transport t = session.getTransport("smtp");
			//ユーザー、PW
			t.connect("funnybunny0504@gmail.com", "****");

			//↓ここでおちる
			t.sendMessage(msg, msg.getAllRecipients());
		} catch (MessagingException e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}
}
