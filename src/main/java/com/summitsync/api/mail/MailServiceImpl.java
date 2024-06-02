package com.summitsync.api.mail;

import com.summitsync.api.course.Course;
import com.summitsync.api.participant.Participant;
import com.summitsync.api.participant.ParticipantMapper;
import com.summitsync.api.participant.dto.ParticipantDto;
import com.summitsync.api.trainer.Trainer;
import com.summitsync.api.trainer.TrainerMapper;
import com.summitsync.api.trainer.dto.TrainerDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    @Autowired
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String sender;
    private final TrainerMapper trainerMapper;
    private final ParticipantMapper participantMapper;
    private static final String CANCEL_MAIL_COURSE_PARTICIPANT = """
            Hallo %s,

            Leider muss dein Kurs %s am %s um %s ausfallen.
            Solltest du deine Kursgebühren zurück erstattet haben wollen, melde dich bitte bei uns unter: kurse@kletterzentrum-bremen.de oder ruf uns an unter: +49421 51429053

            Andernfalls suche dir gerne einen neuen Kurstermin, für den wir dir deine aktuelle Kursgebühr anrechnen.

            Wir entschuldigen uns für die entstandenen Unannehmlichkeiten und hoffen dich bald wieder im Unterwegs Kletterzentrum Bremen begrüßen zu dürfen.

            Wir freuen uns auf dich!
            Dein Kletterzentrums Team""";
    private static final String CANCEL_MAIL_COURSE_TRAINER = """
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
    private static final String CANCEL_MAIL_SUBJECT_COURSE ="Absage Kurs %s am %s %s";
    private static final String REMINDER_MAIL_SUBJECT_COURSE = "Erinnerung für Kurs %s am %s %s";

    private static final String UPDATE_MAIL_SUBJECT_COURSE = "Änderungen bei Kurs %s am %s %s";




    @Override
    public String sendMail(MailDetail mailDetail) {
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
            System.out.println(e.getMessage());
            return "Error while Sending email!!!";
        }
    }

    @Override
    public String sendMailWithAttachment(MailDetail mailDetail) {
        // Creating a Mime Message
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {

            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(mailDetail.getRecipient());
            mimeMessageHelper.setSubject(mailDetail.getSubject());
            mimeMessageHelper.setText(mailDetail.getMsgBody());

            // Adding the file attachment
            FileSystemResource file = new FileSystemResource(new File(mailDetail.getAttachment()));

            mimeMessageHelper.addAttachment(file.getFilename(), file);

            // Sending the email with attachment
            mailSender.send(mimeMessage);
            return "Email has been sent successfully...";

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void sendCourseCancelMail(Course course, String jwt){
        LocalDateTime start = course.getDates().getFirst().getStartTime();
        String startTime = start.format(DateTimeFormatter.ofPattern("HH:mm"));
        String startDate = start.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        sendCancelMailForTrainers(course.getTrainers(), jwt, startDate, startTime, course.getAcronym());
        sendCancelMailForParticipants(course.getParticipants(),jwt, startDate, startTime, course.getAcronym());
    }

    @Override
    public void sendCourseChangeMail(Course course, String jwt){
        LocalDateTime start = course.getDates().getFirst().getStartTime();
        String startTime = start.format(DateTimeFormatter.ofPattern("HH:mm"));
        String startDate = start.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        sendChangeMailForTrainers(course.getTrainers(),jwt, startDate, startTime, course.getAcronym());
        sendChangeMailForParticipants(course.getParticipants(), jwt, startDate, startTime, course.getAcronym());
    }

    @Override
    public void sendCourseReminderMail(Course course, String jwt) {
        LocalDateTime start = course.getDates().getFirst().getStartTime();
        String startTime = start.format(DateTimeFormatter.ofPattern("HH:mm"));
        String startDate = start.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        sendCourseReminderMailForTrainers(course.getTrainers(), jwt, startDate, startTime, course.getAcronym());
        sendCourseReminderMailForParticipants(course.getParticipants(), jwt, startDate, startTime, course.getAcronym());
    }

    private void sendCourseReminderMailForTrainers(List<Trainer> trainerList, String jwt, String startDate, String startTime, String acronym){
        for (Trainer t: trainerList) {
            MailDetail detail = new MailDetail();
            TrainerDto trainerDto = trainerMapper.mapTrainerToTrainerDto(t, jwt);
            detail.setRecipient(trainerDto.getEmail());
            detail.setSubject(String.format(REMINDER_MAIL_SUBJECT_COURSE,acronym, startDate, startTime));
            detail.setMsgBody(String.format(REMINDER_MAIL_COURSE_TRAINER, trainerDto.getFirstName(),trainerDto.getLastName(), startDate, acronym, startTime));
            sendMail(detail);
        }
    }

    private void sendCourseReminderMailForParticipants(List<Participant> participantList, String jwt, String startDate, String startTime, String acronym) {
        for (Participant p: participantList) {
            MailDetail detail = new MailDetail();
            ParticipantDto participantDto = participantMapper.mapParticipantToParticipantDto(p, jwt);
            detail.setRecipient(participantDto.getEmail());
            detail.setSubject(String.format(REMINDER_MAIL_SUBJECT_COURSE,acronym,startDate,startTime));
            detail.setMsgBody(String.format(REMINDER_MAIL_COURSE_PARTICIPANT,participantDto.getName(),startDate,acronym,startTime));
            sendMail(detail);
        }
    }
    private void sendCancelMailForTrainers(List<Trainer> trainerList, String jwt, String startDate, String startTime, String acronym){
        for (Trainer t: trainerList) {
            MailDetail detail = new MailDetail();
            TrainerDto trainerDto = trainerMapper.mapTrainerToTrainerDto(t, jwt);
            detail.setRecipient(trainerDto.getEmail());
            detail.setSubject(String.format(CANCEL_MAIL_SUBJECT_COURSE,acronym,startDate,startTime));
            detail.setMsgBody(String.format(CANCEL_MAIL_COURSE_TRAINER,trainerDto.getFirstName(), trainerDto.getLastName(), acronym, startDate, startTime));
            sendMail(detail);
        }
    }

    private void sendCancelMailForParticipants(List<Participant>participantList, String jwt, String startDate, String startTime, String acronym){
        for (Participant p: participantList) {
            MailDetail detail = new MailDetail();
            ParticipantDto participantDto = participantMapper.mapParticipantToParticipantDto(p, jwt);
            detail.setRecipient(participantDto.getEmail());
            detail.setSubject(String.format(CANCEL_MAIL_SUBJECT_COURSE, acronym, startDate, startTime));
            detail.setMsgBody(String.format(CANCEL_MAIL_COURSE_PARTICIPANT, participantDto.getName(), acronym, startDate, startTime));
            sendMail(detail);
        }
    }

    private void sendChangeMailForTrainers(List<Trainer> trainerList, String jwt, String startDate, String startTime, String acronym){
        for (Trainer t: trainerList) {
            MailDetail detail = new MailDetail();
            TrainerDto trainerDto = trainerMapper.mapTrainerToTrainerDto(t, jwt);
            detail.setRecipient(trainerDto.getEmail());
            detail.setSubject(String.format(UPDATE_MAIL_SUBJECT_COURSE,acronym,startDate,startTime));
            detail.setMsgBody(String.format(UPDATE_MAIL_COURSE_TRAINER,trainerDto.getFirstName(),trainerDto.getLastName(),acronym,startDate,startTime));
            sendMail(detail);
        }
    }
    private void sendChangeMailForParticipants(List<Participant>participantList, String jwt, String startDate, String startTime, String acronym) {
        for (Participant p: participantList) {
            MailDetail detail = new MailDetail();
            ParticipantDto participantDto = participantMapper.mapParticipantToParticipantDto(p, jwt);
            detail.setRecipient(participantDto.getEmail());
            detail.setSubject(String.format(UPDATE_MAIL_SUBJECT_COURSE, acronym, startDate, startTime));
            detail.setMsgBody(String.format(UPDATE_MAIL_COURSE_PARTICIPANT, participantDto.getName(), acronym, startDate, startTime));
            sendMail(detail);
        }
    }
}
