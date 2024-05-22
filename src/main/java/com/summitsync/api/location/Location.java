package com.summitsync.api.location;

import com.summitsync.api.course.Course;
import com.summitsync.api.coursetemplate.CourseTemplate;
import com.summitsync.api.group.Group;
import com.summitsync.api.grouptemplate.GroupTemplate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "SS_Location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long locationId;
    private String street;
    private String postCode;
    private String country;
    private String email;
    private String phone;
    private String mapsUrl;
    private String title;
    private String city;
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Course> courses;
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CourseTemplate> courseTemplates;
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Group> groups;
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GroupTemplate> groupTemplates;
}
