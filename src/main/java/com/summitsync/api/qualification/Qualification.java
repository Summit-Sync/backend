package com.summitsync.api.qualification;

import com.summitsync.api.course.Course;
import com.summitsync.api.coursetemplate.CourseTemplate;
import com.summitsync.api.group.Group;
import com.summitsync.api.grouptemplate.GroupTemplate;
import com.summitsync.api.trainer.Trainer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "SS_Qualification")
@Builder
public class Qualification {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long qualificationId;
    private String name;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Trainer> trainers;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Course> courses;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Group> groups;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CourseTemplate> courseTemplates;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<GroupTemplate> groupTemplates;
}
