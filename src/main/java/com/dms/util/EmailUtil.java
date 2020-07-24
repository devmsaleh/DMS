package com.dms.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;



public class EmailUtil {
	private String from;
	private String provider;
	private String subject;
	private Object emailText;
	private String to;
	protected String password;
	private static final String EMAIL_DISPLAY_NAME="Dawam";
	public EmailUtil( String subject, Object emailText, String to) {
		super();
		this.from = "dawamsa2017";
		this.provider = "gmail.com";
		this.subject = subject;
		this.emailText = emailText;
		this.to = to;
		this.password = "dawam@sa@2017";
	}
	public boolean send() {
		Properties props = new Properties();
		Session session = null;
//		System.out.println("PROVIDER:   " + this.provider);

		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		session = Session.getDefaultInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(EmailUtil.this.from, EmailUtil.this.password);
			}
		});
		session.setDebug(true);
		try {
			MimeMessage message = new MimeMessage(session);
			MimeBodyPart mbp1 = null;
			MimeMultipart mp = new MimeMultipart();
			InternetAddress from = new InternetAddress(this.from + "@" + this.provider, EMAIL_DISPLAY_NAME);
			message.setFrom(from);
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.to));
			message.setSubject(this.subject, "UTF-8");
			
			
			this.emailText = "<html><body><table width='100%' border='0'> " + "<tbody> "
					+ "	<tr> " + "		<td><font face='verdana' size='3'  color='#075653'>" + this.emailText
					+ "</font></td> " + "	</tr> " + "</tbody> "
					+ "</table> </body></html>";
			
			
			mbp1 = new MimeBodyPart();
			mbp1.setContent(this.emailText , "text/html; charset=UTF-8");
			mp.addBodyPart(mbp1);
			
			message.setContent(mp);
			Transport.send(message);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

public static void main(String[] args) {
	new EmailUtil("Test1", "test122", "mahmoud.samy@gmail.com").send();
}
}
