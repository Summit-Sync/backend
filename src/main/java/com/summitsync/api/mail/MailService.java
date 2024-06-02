package com.summitsync.api.mail;

import com.summitsync.api.course.Course;

public interface MailService {
    String sendMail(MailDetail mailDetail);
    String sendMailWithAttachment(MailDetail mailDetail);
    void sendCourseCancelMail(Course course, String jwt);
    void sendCourseChangeMail(Course course, String jwt);
    void sendCourseReminderMail(Course course, String jwt);
}
