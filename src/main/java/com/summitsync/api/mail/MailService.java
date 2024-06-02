package com.summitsync.api.mail;

import com.summitsync.api.course.Course;
import com.summitsync.api.group.Group;

public interface MailService {
    String sendMail(MailDetail mailDetail);
    void sendCourseCancelMail(Course course, String jwt);
    void sendCourseChangeMail(Course course, String jwt);
    void sendCourseReminderMail(Course course, String jwt);
    void sendGroupCancelMail(Group group, String jwt);
    void sendGroupReminderMail(Group group, String jwt);
    void sendGroupChangeMail(Group group, String jwt);
}
