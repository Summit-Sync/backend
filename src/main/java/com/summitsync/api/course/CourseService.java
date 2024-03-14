package com.summitsync.api.course;

import com.summitsync.api.coursetemplate.CourseTemplate;
import com.summitsync.api.coursetemplate.CourseTemplateService;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final Logger log = LoggerFactory.getLogger(CourseService.class);
    private final CourseRepository repository;
    private final CourseTemplateService templateService;

    public Course create(Course course) { return this.repository.save(course); }

    public Course createFromTemplate(long templateId, Course course) {
        CourseTemplate template = this.templateService.get(templateId);
        course.setTemplate(template);
        return create(course);
    }

    public Course update(Course course, Long id) {
        Optional<Course> data = this.findById(id);
        if(data.isEmpty()) {
            log.info("Course with id {} does not exist", id);
            throw new RuntimeException("Course with id " + id + " does not exist");
        }
        Course dbCourse = data.get();
        dbCourse.setDescription(course.getDescription());
        dbCourse.setDates(course.getDates());
        dbCourse.setAcronym(course.getAcronym());
        dbCourse.setDuration(course.getDuration());
        dbCourse.setNotes(course.getNotes());
        dbCourse.setActualPrice(course.getActualPrice());
        dbCourse.setLengthOfWaitList(course.getLengthOfWaitList());
        dbCourse.setNumberOfDates(course.getNumberOfDates());
        dbCourse.setLocation(course.getLocation());
        dbCourse.setDates(course.getDates());
        dbCourse.setPriceList(course.getPriceList());
        dbCourse.setTemplate(course.getTemplate());
        dbCourse.setTitle(course.getTitle());
        dbCourse.setTrainerList(course.getTrainerList());
        dbCourse.setWaitList(course.getWaitList());
        dbCourse.setNumber(course.getNumber());
        dbCourse.setParticipants(course.getParticipants());
        dbCourse.setRequiredQualifications(course.getRequiredQualifications());
        dbCourse.setNumberOfTrainers(course.getNumberOfTrainers());
        return this.repository.save(dbCourse);
    }

    public Course deleteById(long id) {
        if (this.findById(id).isEmpty()) {
            log.info("Course with id {} does not exist", id);
            throw new RuntimeException("Course with id " + id + " does not exist");
        }
        Course course = this.findById(id).get();
        this.repository.deleteById(id);
        return course;
    }

    public Course get(long id) {
        if (this.findById(id).isEmpty()) {
            log.info("Course with id {} does not exist", id);
            throw new RuntimeException("Course with id " + id + " does not exist");
        }
        return this.findById(id).get();
    }

    public List<Course> getAll() {
        List<Course> all = this.repository.findAll();
        if (all.isEmpty()) {
            log.info("CourseList is empty");
        }
        return all;
    }

    private Optional<Course> findById(Long id) {
        return this.repository.findById(id);
    }
}
