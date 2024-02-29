package com.summitsync.api.course;

import com.summitsync.api.coursetemplate.CourseTemplate;
import com.summitsync.api.date.EventDate;
import com.summitsync.api.location.Location;
import com.summitsync.api.participant.Participant;
import com.summitsync.api.qualification.Qualification;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CourseDTO {
    private long id;
    private CourseTemplate template;
    private String description;
    private String title;
    private List<Qualification> requiredQualifications;
    private List<Participant> participants;
    private String notes;
    private List<EventDate> dates;
    private BigDecimal actualPrice;
    private Location location;
}
