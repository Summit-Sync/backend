package com.summitsync.api.course;

import com.summitsync.api.calendar.CalendarEventDateSet;
import com.summitsync.api.course.dto.CourseGetDTO;
import com.summitsync.api.course.dto.CoursePostDTO;
import com.summitsync.api.date.EventDate;
import com.summitsync.api.location.LocationMapper;
import com.summitsync.api.location.LocationService;
import com.summitsync.api.participant.Participant;
import com.summitsync.api.participant.ParticipantMapper;
import com.summitsync.api.participant.ParticipantService;
import com.summitsync.api.price.Price;
import com.summitsync.api.price.PriceMapper;
import com.summitsync.api.price.PriceService;
import com.summitsync.api.qualification.QualificationMapper;
import com.summitsync.api.qualification.QualificationService;
import com.summitsync.api.trainer.Trainer;
import com.summitsync.api.trainer.TrainerMapper;
import com.summitsync.api.trainer.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseMapper {
    private final ParticipantMapper participantMapper;
    private final ParticipantService participantService;
    private final LocationMapper locationMapper;
    private final QualificationMapper qualificationMapper;
    private final TrainerService trainerService;
    private final TrainerMapper trainerMapper;
    private final LocationService locationService;
    private final QualificationService qualificationService;
    private final PriceMapper priceMapper;
    private final PriceService priceService;

    public CourseGetDTO mapCourseToCourseGetDTO(Course course, String jwt) {

        return CourseGetDTO.builder()
                .id(course.getCourseId())
                .visible(course.isVisible())
                .canceled(course.isCancelled())
                .finished(course.isFinished())
                .courseNumber(course.getCourseNumber())
                .acronym(course.getAcronym())
                .description(course.getDescription())
                .dates(course.getDates().stream().map(EventDate::getStartTime).toList())
                .duration(course.getDuration())
                .numberParticipants(course.getNumberParticipants())
                .participants(course.getParticipants().stream().map(this.participantMapper::mapParticipantToParticipantDto).toList())
                .waitList(course.getWaitList().stream().map(this.participantMapper::mapParticipantToParticipantDto).toList())
                .numberWaitlist(course.getNumberWaitlist())
                .prices(course.getCoursePrices().stream().map(this.priceMapper::mapPriceToPriceDto).toList())
                .location(this.locationMapper.mapLocationToGetLocationDto(course.getLocation()))
                .meetingPoint(course.getMeetingPoint())
                .requiredQualifications(course.getRequiredQualifications().stream().map(this.qualificationMapper::mapQualificationToQualificationDto).toList())
                .numberTrainers(course.getNumberTrainer())
                .trainers(course.getTrainers().stream().map(trainer -> this.trainerMapper.mapTrainerToTrainerDto(trainer, jwt)).toList())
                .notes(course.getNotes())
                .title(course.getTitle())
                .build();
    }

    public Course mapCoursePostDTOToCourse(CoursePostDTO dto) {
        List<EventDate> dates = dto.getDates().stream().map(d ->{
            EventDate eventDate=new EventDate();
            eventDate.setStartTime(d);
            eventDate.setDurationInMinutes(dto.getDuration());
            return eventDate;
        }).toList();


        List<Participant> participants = new ArrayList<>();
        List<Participant> waitList = new ArrayList<>();
        List<Trainer> trainers = new ArrayList<>();
        if (dto.getParticipants() != null) {
            for (var participant : dto.getParticipants()) {
                participants.add(this.participantService.newParticipant(participant));
            }
        }

        if (dto.getWaitList() != null) {
            for (var participant : dto.getWaitList()) {
                waitList.add(this.participantService.newParticipant(participant));
            }
        }

        if (dto.getTrainers() != null) {
            for (var trainer : dto.getTrainers()) {
                trainers.add(this.trainerService.findById(trainer));
            }
        }

        var prices = new ArrayList<Price>();

        if (dto.getPrices() != null) {
            for (var price : dto.getPrices()) {
                var mappedPrice = this.priceMapper.mapPostPriceDtoToPrice(price);
                var savedPrice = this.priceService.create(mappedPrice);
                prices.add(savedPrice);
            }
        }

        return Course.builder()
                .visible(false)
                .cancelled(false)
                .finished(false)
                .courseNumber("1")
                .acronym(dto.getAcronym())
                .description(dto.getDescription())
                .dates(dates)
                .duration(dto.getDuration())
                .numberParticipants(dto.getNumberParticipants())
                .participants(participants)
                .waitList(waitList)
                .numberWaitlist(dto.getNumberWaitlist())
                .coursePrices(prices)
                .location(this.locationService.getLocationById(dto.getLocation()))
                .meetingPoint(dto.getMeetingPoint())
                .requiredQualifications(dto.getRequiredQualifications().stream().map(this.qualificationService::findById).toList())
                .numberTrainer(dto.getNumberTrainers())
                .trainers(trainers)
                .title(dto.getTitle())
                .notes(dto.getNotes())
                .build();
    }

    public static List<CalendarEventDateSet> mapEventDatesListToCalendarEventDateSetList(List<EventDate> eventDates) {
        return eventDates.stream()
                .filter(eventDate -> eventDate.getStartTime() != null && eventDate.getDurationInMinutes() != 0)
                .map(CourseMapper::mapEventDateToCalendarEventDateSetList)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static CalendarEventDateSet mapEventDateToCalendarEventDateSetList(EventDate date) {
        var startTime = date.getStartTime();
        var endTime = startTime.plusMinutes(date.getDurationInMinutes());
        return CalendarEventDateSet.builder()
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }
}
