package com.watermelon.manager.common.manager.common.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public abstract class MailSender extends Authenticator {
	private String username;
	private String userpasswd;
	protected BodyPart messageBodyPart;
	protected Multipart multipart;
	protected MimeMessage mailMessage;
	protected Session mailSession;
	protected InternetAddress mailToAddress;

	protected MailSender(String smtpHost, String username, String password) {
		this(smtpHost, 25, username, password);
	}

	protected MailSender(String smtpHost, int smtpPort, String username, String password) {
		this.username = null;
		this.userpasswd = null;
		this.messageBodyPart = null;
		this.multipart = new MimeMultipart("related");
		this.mailMessage = null;
		this.mailSession = null;
		this.mailToAddress = null;
		this.username = username;
		this.userpasswd = password;
		Properties mailProperties = System.getProperties();
		if (smtpHost != null) {
			mailProperties.put("mail.smtp.host", smtpHost);
		}

		if (smtpPort > 0 && smtpPort != 25) {
			mailProperties.put("mail.smtp.port", String.valueOf(smtpPort));
		}

		mailProperties.put("mail.smtp.auth", "true");
		this.mailSession = Session.getDefaultInstance(mailProperties, this);
		this.mailMessage = new MimeMessage(this.mailSession);
		this.messageBodyPart = new MimeBodyPart();
	}

	public static MailSender getTextMailSender(String smtpHost, String username, String password) {
		return getTextMailSender(smtpHost, 25, username, password);
	}

    public static MailSender getTextMailSender(final String smtpHost, final int smtpPort, final String username, final String password) {
        return new MailSender(smtpHost, smtpPort, username, password) {
            @Override
            public void setMailContent(final String mailContent) throws MessagingException {
                this.messageBodyPart.setText(mailContent);
                this.multipart.addBodyPart(this.messageBodyPart);
            }
        };
    }

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(this.username, this.userpasswd);
	}

	public void setSubject(String mailSubject) throws MessagingException {
		this.mailMessage.setSubject(mailSubject);
	}

	public abstract void setMailContent(String arg0) throws MessagingException;

	public void setSendDate(Date sendDate) throws MessagingException {
		this.mailMessage.setSentDate(sendDate);
	}

	public void setAttachments(String attachmentName) throws MessagingException {
		this.messageBodyPart = new MimeBodyPart();
		FileDataSource source = new FileDataSource(attachmentName);
		this.messageBodyPart.setDataHandler(new DataHandler(source));
		int index = attachmentName.lastIndexOf(File.separator);
		String attachmentRealName = attachmentName.substring(index + 1);
		this.messageBodyPart.setFileName(attachmentRealName);
		this.multipart.addBodyPart(this.messageBodyPart);
	}

	public void setMailFrom(String mailFrom, String sender) throws UnsupportedEncodingException, MessagingException {
		if (sender != null) {
			this.mailMessage.setFrom(new InternetAddress(mailFrom, sender));
		} else {
			this.mailMessage.setFrom(new InternetAddress(mailFrom));
		}

	}

	public void setMailTo(String[] mailTo, String mailType) throws Exception {
		for (int i = 0; i < mailTo.length; ++i) {
			this.mailToAddress = new InternetAddress(mailTo[i]);
			if (mailType.equalsIgnoreCase("to")) {
				this.mailMessage.addRecipient(RecipientType.TO, this.mailToAddress);
			} else if (mailType.equalsIgnoreCase("cc")) {
				this.mailMessage.addRecipient(RecipientType.CC, this.mailToAddress);
			} else {
				if (!mailType.equalsIgnoreCase("bcc")) {
					throw new Exception("Unknown mailType: " + mailType + "!");
				}

				this.mailMessage.addRecipient(RecipientType.BCC, this.mailToAddress);
			}
		}

	}

	public void sendMail() throws MessagingException, SendFailedException {
		if (this.mailToAddress == null) {
			throw new MessagingException("The recipient is required.");
		} else {
			this.mailMessage.setContent(this.multipart);
			Transport.send(this.mailMessage);
		}
	}

	public MimeMessage getMimeMessage() throws MessagingException {
		if (this.mailToAddress == null) {
			throw new MessagingException("The recipient is required.");
		} else {
			this.mailMessage.setContent(this.multipart);
			return this.mailMessage;
		}
	}

	public static void main(String[] args) {
		String mailHost = "smtp.163.com";
		String mailUser = "mtsj9yu";
		String mailPassword = "mtsj@9yu";
		String[] toAddress = new String[]{"56886035@qq.com", "powercqy@163.com"};
		MailSender sendmail = getTextMailSender(mailHost, mailUser, mailPassword);

		try {
			sendmail.setSubject("邮件发送测试");
			sendmail.setSendDate(new Date());
			String ex = "测试";
			sendmail.setMailContent(ex);
			sendmail.setMailFrom("mtsj9yu@163.com", "发送者");
			sendmail.setMailTo(toAddress, "to");
			System.out.println("正在发送邮件，请稍候.......");
			sendmail.sendMail();
			System.out.println("恭喜你，邮件已经成功发送!");
		} catch (Exception arg6) {
			arg6.printStackTrace();
		}

	}
}