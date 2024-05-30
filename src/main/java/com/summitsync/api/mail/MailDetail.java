package com.summitsync.api.mail;

import lombok.Data;

@Data
public class MailDetail {
    private String recipient;
    private String subject;
    private String msgBody;
    private String attachment;

}