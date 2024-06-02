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

    private static String reminderMailParticipant= """
            Hallo %s

            am %s findet dein Kurs %s um %s statt.
            Bitte finde dich ein paar Minuten früher bei uns ein, damit wir pünktlich mit dem Kurs starten können.

            Wir freuen uns auf dich!
            Dein Kletterzentrums Team""";
    private static String reminderMailTrainer= """
            Hallo %s %s,

            am %s findet dein Kurs &s um %s statt.
            Viel Spaß dabei ;).

            sportliche Grüße,
            Dein Kursmanager""";
    private static String cancelMailParticipant = """
            Hallo %s,

            Leider muss dein Kurs %s am %s um %s ausfallen.
            Solltest du deine Kursgebühren zurück erstattet haben wollen, melde dich bitte bei uns unter: kurse@kletterzentrum-bremen.de oder ruf uns an unter: +49421 51429053

            Andernfalls suche dir gerne einen neuen Kurstermin, für den wir dir deine aktuelle Kursgebühr anrechnen.

            Wir entschuldigen uns für die entstandenen Unannehmlichkeiten und hoffen dich bald wieder im Unterwegs Kletterzentrum Bremen begrüßen zu dürfen.

            Wir freuen uns auf dich!
            Dein Kletterzentrums Team""";
    private static String cancelMailTrainer= """
            Hallo &s &s,

            Leider muss dein Kurs %s am %s um %s ausfallen.

            Wir entschuldigen uns für die entstandenen Unannehmlichkeiten und hoffen du freust dich schon auf deinen nächsten Kurs.

            sportliche Grüße,
            Dein Kursmanager""";
    private static String cancelMailSubjectCourse ="Absage Kurs %s am %s %s";




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
            System.out.println(e);
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

    private void sendCancelMailForTrainers(List<Trainer> trainerList, String jwt, String startDate, String startTime, String acronym){
        for (Trainer t: trainerList) {
            MailDetail detail = new MailDetail();
            TrainerDto trainerDto = trainerMapper.mapTrainerToTrainerDto(t, jwt);
            detail.setRecipient(trainerDto.getEmail());
            detail.setSubject(String.format(cancelMailSubjectCourse,acronym,startDate,startTime));
            detail.setMsgBody(String.format(cancelMailTrainer,trainerDto.getFirstName(),trainerDto.getLastName(),acronym,startDate,startTime));
            sendMail(detail);
        }
    }

    private void sendCancelMailForParticipants(List<Participant>participantList, String jwt, String startDate, String startTime, String acronym){
        for (Participant p: participantList) {
            MailDetail detail = new MailDetail();
            ParticipantDto participantDto = participantMapper.mapParticipantToParticipantDto(p, jwt);
            detail.setRecipient(participantDto.getEmail());
            detail.setSubject(String.format(cancelMailSubjectCourse, acronym, startDate, startTime));
            detail.setMsgBody(String.format(cancelMailParticipant, participantDto.getName(), acronym, startDate, startTime));
            sendMail(detail);
        }
    }
}
