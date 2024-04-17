package com.summitsync.api.course.dto;

import com.summitsync.api.location.dto.GetLocationDto;
import com.summitsync.api.participant.dto.ParticipantDto;
import com.summitsync.api.price.dto.PriceDto;
import com.summitsync.api.qualification.dto.QualificationDto;
import com.summitsync.api.trainer.dto.TrainerDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CourseGetDTO {
    private long id;
    private boolean visible;
    private boolean canceled;
    private boolean finished;
    private String courseNumber;
    private String acronym;
    private String description;
    private List<LocalDateTime> dates;
    private int duration;
    private int numberParticipants;
    private List<ParticipantDto> participants;
    private List<ParticipantDto> waitList;
    private int numberWaitlist;
    private List<PriceDto> coursePrices;
    private GetLocationDto location;
    private String meetingPoint;
    private List<QualificationDto> requiredQualifications;
    private int numberTrainers;
    private List<TrainerDto> trainerList;
    private String notes;
}

//CourseDTO: id (long), visible (boolean), canceled (boolean), finished (boolean), courseNumber (calculated String), acronym (string),
// description (String), dates (array von Date als String), duration(int), numberParticipants(int), participants (Array von ParticipantDTOs),
// waitList (Array of ParticipantDTOs), numberWaitlist(int), prices (PriceDto), location LocationDTO, meetingPoint (String),
// requiredQulaifications (Liste von QualifiationDTOs), numberTrainers (int), trainerList (Array of TrainerApplicationDTO), notes (String)
