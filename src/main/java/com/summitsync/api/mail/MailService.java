package com.summitsync.api.mail;

public interface MailService {
    String sendMail(MailDetail mailDetail);
    String sendMailWithAttachment(MailDetail mailDetail);
}
