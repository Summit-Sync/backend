package com.summitsync.api.mail;

import com.summitsync.api.course.Course;

public interface MailService {
    String sendMail(MailDetail mailDetail);
    String sendMailWithAttachment(MailDetail mailDetail);
    void sendCourseCancelMail(Course courser, String jwt);
}
