package com.summitsync.api.mail;

import com.summitsync.api.contact.Contact;
import com.summitsync.api.course.Course;
import com.summitsync.api.date.EventDate;
import com.summitsync.api.group.Group;
import com.summitsync.api.participant.Participant;
import com.summitsync.api.trainer.Trainer;
import com.summitsync.api.trainer.TrainerMapper;
import com.summitsync.api.trainer.dto.TrainerDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private static final Logger log = LoggerFactory.getLogger(MailServiceImpl.class);
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String sender;
    @Value("${summitsync.mail.enabled}")
    private boolean enabled;
    private final TrainerMapper trainerMapper;
    private static final String CANCEL_MAIL_COURSE_PARTICIPANT = """
            Hallo %s,

            Leider muss dein Kurs %s am %s um %s ausfallen.
            Solltest du deine Kursgebühren zurück erstattet haben wollen, melde dich bitte bei uns unter: kurse@kletterzentrum-bremen.de oder ruf uns an unter: +49421 51429053

            Andernfalls suche dir gerne einen neuen Kurstermin, für den wir dir deine aktuelle Kursgebühr anrechnen.

            Wir entschuldigen uns für die entstandenen Unannehmlichkeiten und hoffen dich bald wieder im Unterwegs Kletterzentrum Bremen begrüßen zu dürfen.

            Wir freuen uns auf dich!
            Dein Kletterzentrums Team"""; private static final String CANCEL_MAIL_COURSE_TRAINER = """
            Hallo %s %s,

            Leider muss dein Kurs %s am %s um %s ausfallen.

            Wir entschuldigen uns für die entstandenen Unannehmlichkeiten und hoffen du freust dich schon auf deinen nächsten Kurs.

            Sportliche Grüße
            Dein Kursmanager""";

    private static final String REMINDER_MAIL_COURSE_TRAINER = """
            Hallo %s %s
            
            am %s findet dein Kurs %s um %s statt.
            Viel Spaß dabei ;).

            Sportliche Grüße
            Dein Kursmanager""";

    private static final String REMINDER_MAIL_COURSE_PARTICIPANT = """
            Hallo %s,

            am %s findet dein Kurs %s um %s statt.
            Bitte finde dich ein paar Minuten früher bei uns ein, damit wir pünktlich mit dem Kurs starten können.

            Wir freuen uns auf dich!
            Dein Kletterzentrums Team""";

    private static final String UPDATE_MAIL_COURSE_TRAINER = """
            Hallo %s %s,
            
            Bei deinem Kurs %s hat sich etwas verändert.
            Der Kurs findet nun am %s um %s statt.
            
            Viel Spaß dabei ;)
            
            Sportliche Grüße
            Dein Kursmanager
            """;
    private static final String UPDATE_MAIL_COURSE_PARTICIPANT = """
            Hallo %s,
            
            bei deinem Kurs %s hat sich etwas verändert.
            Der Kurs findet nun am %s um %s statt.
            
            Bitte finde dich ein paar Minuten früher bei uns ein, damit wir pünktlich mit dem Kurs starten können.
            
            Wir freuen uns auf dich
            Dein Kletterzentrum Team""";
    private static final String CANCEL_MAIL_GROUP_CONTACT = """
            Hallo %s %s,

            Leider muss deine Gruppe %s am %s um %s ausfallen.
            Solltest du deine Gruppengebühren zurück erstattet haben wollen, melde dich bitte bei uns unter: kurse@kletterzentrum-bremen.de oder ruf uns an unter: +49421 51429053

            Andernfalls suche dir gerne einen neuen Gruppentermin, für den wir dir deine aktuelle Gruppengebühr anrechnen.

            Wir entschuldigen uns für die entstandenen Unannehmlichkeiten und hoffen dich bald wieder im Unterwegs Kletterzentrum Bremen begrüßen zu dürfen.

            Wir freuen uns auf euch!
            Dein Kletterzentrums Team""";
    private static final String CANCEL_MAIL_GROUP_TRAINER = """
            Hallo %s %s,

            Leider muss deine Gruppe %s am %s um %s ausfallen.

            Wir entschuldigen uns für die entstandenen Unannehmlichkeiten und hoffen du freust dich schon auf deine nächste Gruppe.

            Sportliche Grüße
            Dein Kursmanager""";

    private static final String REMINDER_MAIL_GROUP_TRAINER = """
            Hallo %s %s
            
            am %s findet deine Gruppe %s um %s statt.
            Viel Spaß dabei ;).

            Sportliche Grüße
            Dein Kursmanager""";

    private static final String REMINDER_MAIL_GROUP_CONTACT = """
            Hallo %s %s,

            am %s findet deine Gruppe %s um %s statt.
            Bitte finde dich ein paar Minuten früher bei uns ein, damit wir pünktlich mit der Gruppe starten können.

            Wir freuen uns auf euch!
            Dein Kletterzentrums Team""";

    private static final String UPDATE_MAIL_GROUP_TRAINER = """
            Hallo %s %s,
            
            Bei deiner Gruppe %s hat sich etwas verändert.
            Die Gruppe findet nun am %s um %s statt.
            
            Viel Spaß dabei ;)
            
            Sportliche Grüße
            Dein Kursmanager
            """;
    private static final String UPDATE_MAIL_GROUP_CONTACT = """
            Hallo %s %s,
            
            bei deiner Gruppe %s hat sich etwas verändert.
            Die Gruppe findet nun am %s um %s statt.
            
            Bitte finde dich ein paar Minuten früher bei uns ein, damit wir pünktlich mit der Gruppe starten können.
            
            Wir freuen uns auf euch!
            Dein Kletterzentrum Team""";

    private static final String OPEN_TRAINER_SPOT = """
            Hallo %s %s,
            
            es werden noch Trainer für folgende Kurse und Gruppen in der nächsten Woche gesucht.
            
            %s
            
            Wenn du Interesse es, melde dich bitte bei mir.
            
            Sportliche Grüße
            Dein Kursmanager
            """;
    private static final String CANCEL_MAIL_SUBJECT_COURSE ="Absage Kurs %s am %s %s";
    private static final String REMINDER_MAIL_SUBJECT_COURSE = "Erinnerung für Kurs %s am %s %s";
    private static final String UPDATE_MAIL_SUBJECT_COURSE = "Änderungen bei Kurs %s am %s %s";
    private static final String CANCEL_MAIL_SUBJECT_GROUP ="Absage Gruppe %s am %s %s";
    private static final String REMINDER_MAIL_SUBJECT_GROUP = "Erinnerung für Gruppe %s am %s %s";

    private static final String UPDATE_MAIL_SUBJECT_GROUP = "Änderungen bei Gruppe %s am %s %s";

    private static final String OPEN_SPORT_MAIL_SUBJECT = "Offene Trainerstellen";




    @Override
    public String sendMail(MailDetail mailDetail) {
        if (!enabled) {
            return "";
        }
        // Try block to check for exceptions handling
        try {

            // Creating a simple mail message object
            SimpleMailMessage emailMessage = new SimpleMailMessage();

            // Setting up necessary details of mail
            emailMessage.setFrom(sender);
            emailMessage.setTo(mailDetail.getRecipient());
            emailMessage.setSubject(mailDetail.getSubject());
            emailMessage.setText(mailDetail.getMsgBody());

            // Sending the email
            mailSender.send(emailMessage);
            return "Email has been sent successfully...";
        }

        // Catch block to handle the exceptions
        catch (Exception e) {
            log.warn(e.getMessage());
            return "Error while Sending email!!!";
        }
    }
    @Override
    public void sendCourseCancelMail(Course course, String jwt){
        LocalDateTime start = course.getDates().getFirst().getStartTime();
        String startTime = start.format(DateTimeFormatter.ofPattern("HH:mm"));
        String startDate = start.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        sendCourseCancelMailForTrainers(course.getTrainers(), jwt, startDate, startTime, course.getAcronym());
        sendCourseCancelMailForParticipants(course.getParticipants(), startDate, startTime, course.getAcronym());
    }

    @Override
    public void sendCourseChangeMail(Course course, String jwt){
        LocalDateTime start = course.getDates().getFirst().getStartTime();
        String startTime = start.format(DateTimeFormatter.ofPattern("HH:mm"));
        String startDate = start.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        sendCourseChangeMailForTrainers(course.getTrainers(),jwt, startDate, startTime, course.getAcronym());
        sendCourseChangeMailForParticipants(course.getParticipants(), startDate, startTime, course.getAcronym());
    }

    @Override
    public void sendCourseReminderMail(Course course, String jwt) {
        LocalDateTime start = course.getDates().getFirst().getStartTime();
        String startTime = start.format(DateTimeFormatter.ofPattern("HH:mm"));
        String startDate = start.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        sendCourseReminderMailForTrainers(course.getTrainers(), jwt, startDate, startTime, course.getAcronym());
        sendCourseReminderMailForParticipants(course.getParticipants(), startDate, startTime, course.getAcronym());
    }

    @Override
    public void sendGroupCancelMail(Group group, String jwt) {
        LocalDateTime start = group.getDates().getFirst().getStartTime();
        String startTime = start.format(DateTimeFormatter.ofPattern("HH:mm"));
        String startDate = start.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        sendGroupCancelMailForTrainers(group.getTrainers(), jwt, startDate, startTime, group.getAcronym());
        sendGroupCancelMailForContact(group.getContact(), startDate, startTime, group.getAcronym());
    }

    @Override
    public void sendGroupReminderMail(Group group, String jwt) {
        LocalDateTime start = group.getDates().getFirst().getStartTime();
        String startTime = start.format(DateTimeFormatter.ofPattern("HH:mm"));
        String startDate = start.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        sendGroupReminderMailForTrainers(group.getTrainers(), jwt, startDate, startTime, group.getAcronym());
        sendGroupReminderMailForContact(group.getContact(), startDate, startTime, group.getAcronym());
    }

    @Override
    public void sendGroupChangeMail(Group group, String jwt) {
        LocalDateTime start = group.getDates().getFirst().getStartTime();
        String startTime = start.format(DateTimeFormatter.ofPattern("HH:mm"));
        String startDate = start.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        sendGroupUpdateMailForTrainers(group.getTrainers(), jwt, startDate, startTime, group.getAcronym());
        sendGroupUpdateMailForContact(group.getContact(), startDate, startTime, group.getAcronym());
    }

    @Override
    public void sendOpenTrainerSpotTrainerMail(List<Trainer> trainerList, Map<Trainer, List<Course>> courseMap, Map<Trainer, List<Group>> groupMap, String jwt) {
        for(Trainer trainer: trainerList){
            MailDetail detail=new MailDetail();
            detail.setSubject(OPEN_SPORT_MAIL_SUBJECT);
            TrainerDto trainerDto = trainerMapper.mapTrainerToTrainerDto(trainer, jwt);
            detail.setRecipient(trainerDto.getEmail());
            StringBuilder mailBody=new StringBuilder();
            for(Course course:courseMap.get(trainer)){
                mailBody.append("- ").append(course.getAcronym()).append(course.getCourseNumber()).append(": ").append(getMailDatesForCourse(course.getDates())).append(System.lineSeparator());
            }
            for(Group group: groupMap.get(trainer)){
                mailBody.append("- ").append(group.getAcronym()).append(group.getGroupNumber()).append(": ").append(getMailDatesForGroup(group.getDates())).append(System.lineSeparator());
            }
            detail.setMsgBody(String.format(OPEN_TRAINER_SPOT,trainerDto.getFirstName(), trainerDto.getLastName(), mailBody));
            sendMail(detail);
        }
    }

    private void sendGroupReminderMailForTrainers(List<Trainer> trainerList, String jwt, String startDate, String startTime, String acronym){
        for (Trainer t: trainerList) {
            MailDetail detail = generateTrainerMailDetails(t, jwt, REMINDER_MAIL_SUBJECT_GROUP, acronym, startDate, startTime, REMINDER_MAIL_GROUP_TRAINER);
            sendMail(detail);
        }
    }

    private void sendGroupReminderMailForContact(Contact contact, String startDate, String startTime, String acronym){
        MailDetail detail = generateContactMailDetails(contact, REMINDER_MAIL_SUBJECT_GROUP, acronym, startDate, startTime, REMINDER_MAIL_GROUP_CONTACT);
        if (detail == null) return;
        sendMail(detail);
    }

    private static MailDetail generateContactMailDetails(Contact contact, String reminderMailSubjectGroup, String acronym, String startDate, String startTime, String reminderMailGroupContact) {
        if(StringUtils.hasLength(contact.getEmail())){
            log.warn("Contact could not be informed, because they did not enter an email");
            return null;
        }
        MailDetail detail = new MailDetail();
        detail.setRecipient(contact.getEmail());
        detail.setSubject(String.format(reminderMailSubjectGroup, acronym, startDate, startTime));
        detail.setMsgBody(String.format(reminderMailGroupContact, contact.getFirstName(), contact.getLastName(), acronym, startDate, startTime));
        return detail;
    }

    private void sendGroupCancelMailForTrainers(List<Trainer> trainerList, String jwt, String startDate, String startTime, String acronym){
        for (Trainer t: trainerList) {
            MailDetail detail = generateTrainerMailDetails(t, jwt, CANCEL_MAIL_SUBJECT_GROUP, acronym, startDate, startTime, CANCEL_MAIL_GROUP_TRAINER);
            sendMail(detail);
        }
    }

    private void sendGroupCancelMailForContact(Contact contact, String startDate, String startTime, String acronym){
        MailDetail detail = generateContactMailDetails(contact, CANCEL_MAIL_SUBJECT_GROUP, acronym, startDate, startTime, CANCEL_MAIL_GROUP_CONTACT);
        if (detail == null) return;
        sendMail(detail);
    }
    private void sendGroupUpdateMailForTrainers(List<Trainer> trainerList, String jwt, String startDate, String startTime, String acronym){
        for (Trainer t: trainerList) {
            MailDetail detail = generateTrainerMailDetails(t, jwt, UPDATE_MAIL_SUBJECT_GROUP, acronym, startDate, startTime, UPDATE_MAIL_GROUP_TRAINER);
            sendMail(detail);
        }
    }

    private void sendGroupUpdateMailForContact(Contact contact, String startDate, String startTime, String acronym){
        MailDetail detail = generateContactMailDetails(contact, UPDATE_MAIL_SUBJECT_GROUP, acronym, startDate, startTime, UPDATE_MAIL_GROUP_CONTACT);
        if (detail == null) return;
        sendMail(detail);
    }

    private void sendCourseReminderMailForTrainers(List<Trainer> trainerList, String jwt, String startDate, String startTime, String acronym){
        for (Trainer t: trainerList) {
            MailDetail detail = generateTrainerMailDetails(t, jwt, REMINDER_MAIL_SUBJECT_COURSE, acronym, startDate, startTime, REMINDER_MAIL_COURSE_TRAINER);
            sendMail(detail);
        }
    }
    private void sendCourseReminderMailForParticipants(List<Participant> participantList, String startDate, String startTime, String acronym) {
        for (Participant p: participantList) {
            MailDetail detail = generateParticipantMailDetails(p, REMINDER_MAIL_SUBJECT_COURSE, acronym, startDate, startTime, REMINDER_MAIL_COURSE_PARTICIPANT);
            sendMail(detail);
        }
    }
    private void sendCourseCancelMailForTrainers(List<Trainer> trainerList, String jwt, String startDate, String startTime, String acronym){
        for (Trainer t: trainerList) {
            MailDetail detail = generateTrainerMailDetails(t, jwt, CANCEL_MAIL_SUBJECT_COURSE, acronym, startDate, startTime, CANCEL_MAIL_COURSE_TRAINER);
            sendMail(detail);
        }
    }

    private void sendCourseCancelMailForParticipants(List<Participant>participantList, String startDate, String startTime, String acronym){
        for (Participant p: participantList) {
            MailDetail detail = generateParticipantMailDetails(p, CANCEL_MAIL_SUBJECT_COURSE, acronym, startDate, startTime, CANCEL_MAIL_COURSE_PARTICIPANT);
            sendMail(detail);
        }
    }

    private void sendCourseChangeMailForTrainers(List<Trainer> trainerList, String jwt, String startDate, String startTime, String acronym){
        for (Trainer t: trainerList) {
            MailDetail detail = generateTrainerMailDetails(t, jwt, UPDATE_MAIL_SUBJECT_COURSE, acronym, startDate, startTime, UPDATE_MAIL_COURSE_TRAINER);
            sendMail(detail);
        }
    }
    private void sendCourseChangeMailForParticipants(List<Participant>participantList, String startDate, String startTime, String acronym) {
        for (Participant p: participantList) {
            MailDetail detail = generateParticipantMailDetails(p, UPDATE_MAIL_SUBJECT_COURSE, acronym, startDate, startTime, UPDATE_MAIL_COURSE_PARTICIPANT);
            sendMail(detail);
        }
    }

    private MailDetail generateParticipantMailDetails(Participant p, String updateMailSubjectCourse, String acronym, String startDate, String startTime, String updateMailCourseParticipant) {
        MailDetail detail = new MailDetail();
        detail.setRecipient(p.getEmail());
        detail.setSubject(String.format(updateMailSubjectCourse, acronym, startDate, startTime));
        detail.setMsgBody(String.format(updateMailCourseParticipant, p.getName(), acronym, startDate, startTime));
        return detail;
    }

    private MailDetail generateTrainerMailDetails(Trainer t, String jwt, String cancelMailSubjectGroup, String acronym, String startDate, String startTime, String cancelMailGroupTrainer) {
        MailDetail detail = new MailDetail();
        TrainerDto trainerDto = trainerMapper.mapTrainerToTrainerDto(t, jwt);
        detail.setRecipient(trainerDto.getEmail());
        detail.setSubject(String.format(cancelMailSubjectGroup, acronym, startDate, startTime));
        detail.setMsgBody(String.format(cancelMailGroupTrainer,trainerDto.getFirstName(), trainerDto.getLastName(), acronym, startDate, startTime));
        return detail;
    }

    private String getMailDatesForCourse(List<EventDate>dates){
        StringBuilder sb=new StringBuilder();
        LocalDate start=LocalDate.now().plusDays(6);
        LocalDate end=LocalDate.now().plusDays(15);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu HH:mm:ss", Locale.GERMAN);
        for(EventDate date:dates){
            if(date.getStartTime().toLocalDate().isBefore(end) && date.getStartTime().toLocalDate().isAfter(start)){
                if(sb.isEmpty()){
                    sb.append(date.getStartTime().format(formatter));
                }else {
                    sb.append(", ").append(date.getStartTime().format(formatter));
                }
            }
        }
        return sb.toString();
    }

    private String getMailDatesForGroup(List<EventDate> dates){
        StringBuilder sb= new StringBuilder();
        LocalDate start=LocalDate.now().plusDays(6);
        LocalDate end=LocalDate.now().plusDays(15);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu HH:mm:ss", Locale.GERMAN);
        for(EventDate date:dates){
            if(date.getStartTime().toLocalDate().isBefore(end) && date.getStartTime().toLocalDate().isAfter(start)){
                if(sb.isEmpty()){
                    sb.append(date.getStartTime().format(formatter)).append("(").append(date.getDurationInMinutes()).append(" Minuten)");
                }else {
                    sb.append(", ").append(date.getStartTime().format(formatter)).append("(").append(date.getDurationInMinutes()).append(" Minuten)");
                }
            }
        }
        return sb.toString();
    }
}
