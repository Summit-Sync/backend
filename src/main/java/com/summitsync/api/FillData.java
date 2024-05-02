package com.summitsync.api;

import com.summitsync.api.course.Course;
import com.summitsync.api.course.CourseMapper;
import com.summitsync.api.course.CourseService;
import com.summitsync.api.course.dto.CoursePostDTO;
import com.summitsync.api.coursetemplate.CourseTemplate;
import com.summitsync.api.coursetemplate.CourseTemplateService;
import com.summitsync.api.location.Location;
import com.summitsync.api.location.LocationService;
import com.summitsync.api.price.Price;
import com.summitsync.api.price.PriceService;
import com.summitsync.api.qualification.Qualification;
import com.summitsync.api.qualification.QualificationService;
import com.summitsync.api.trainer.TrainerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/dummyData")
@AllArgsConstructor
public class FillData {
    private final CourseService courseService;
    private final CourseTemplateService courseTemplateService;
    private final QualificationService qualificationService;
    private final PriceService priceService;
    private final LocationService locationService;
    private final CourseMapper courseMapper;


    private CourseTemplate createTestDataTemplate1(Location location, Set<Price> prices, Set<Qualification> qualifications) {
        var courseTemplate = new CourseTemplate();
        courseTemplate.setTitle("Einstiegskurs");
        courseTemplate.setAcronym("EK");
        courseTemplate.setDuration(180);
        courseTemplate.setDescription("Kurs zum Erlangen der Sicherungskompetenz im Toprope");
        courseTemplate.setMeetingPoint("Lampenschirmecke");
        courseTemplate.setNumberOfDates(2);
        courseTemplate.setNumberParticipants(6);
        courseTemplate.setNumberTrainer(1);
        courseTemplate.setNumberWaitlist(3);
        courseTemplate.setPrices(prices);
        courseTemplate.setQualifications(qualifications);
        courseTemplate.setLocation(location);
        return this.courseTemplateService.createCourse(courseTemplate);
    }

    private CourseTemplate createTestDataTemplate2(Location location, Set<Price> prices, Set<Qualification> qualifications) {
        var courseTemplate = new CourseTemplate();
        courseTemplate.setTitle("Sturz- und Sicherungstraining");
        courseTemplate.setAcronym("WS");
        courseTemplate.setDuration(180);
        courseTemplate.setDescription("Sturz- und Sicherungstraining mit Flo");
        courseTemplate.setMeetingPoint("Kinderspieleparadies");
        courseTemplate.setNumberOfDates(3);
        courseTemplate.setNumberParticipants(6);
        courseTemplate.setNumberTrainer(1);
        courseTemplate.setNumberWaitlist(5);
        courseTemplate.setPrices(prices);
        courseTemplate.setQualifications(qualifications);
        courseTemplate.setLocation(location);
        return this.courseTemplateService.createCourse(courseTemplate);
    }

    private Set<Price> createTestDataPrices() {
        var price1 = new Price();
        price1.setName("Sektion");
        price1.setPrice(BigDecimal.valueOf(79));
        var price2 = new Price();
        price2.setName("DAV");
        price2.setPrice(BigDecimal.valueOf(84));
        var price3 = new Price();
        price3.setName("Gast");
        price3.setPrice(BigDecimal.valueOf(99));
        price1 = priceService.create(price1);
        price2 = priceService.create(price2);
        price3 = priceService.create(price3);
        return Set.of(price1, price2, price3);
    }

    private Set<Qualification> createTestDataQualifications() {
        var qualification1 = new Qualification();
        qualification1.setName("Kletterbetreuer");
        var qualification2 = new Qualification();
        qualification2.setName("Gruppeneinweisung");
        var qualification3 = new Qualification();
        qualification3.setName("BodyAndSoul");
        var qualification4 = new Qualification();
        qualification4.setName("Sturzkompetenz");
        qualification1 = qualificationService.saveQualification(qualification1);
        qualification2 = qualificationService.saveQualification(qualification2);
        qualification3 = qualificationService.saveQualification(qualification3);
        qualification4 = qualificationService.saveQualification(qualification4);
        return Set.of(qualification1, qualification2, qualification3, qualification4);
    }

    private Location createTestDataLocation() {
        Location location = new Location();
        location.setCountry("Deutschland");
        location.setEmail("florian.wicke@kletterzentrum-bremen.de");
        location.setPhone("0123456789");
        location.setRoom("KEZ Bremen");
        location.setStreet("Robert-Hooke-Straße 19");
        location.setPostCode("28359");
        return locationService.createLocation(location);
    }

    private Course createFromTemplate(CourseTemplate template) {
        var courseDto = new CoursePostDTO();
        courseDto.setAcronym(template.getAcronym());
        courseDto.setNotes("Privater Kurs");
        courseDto.setLocation(template.getLocation().getLocationId());
        courseDto.setDuration(template.getDuration());
        courseDto.setDescription(template.getDescription());
        courseDto.setTitle(template.getTitle());
        List<Long> prices = new ArrayList<>();
        for (var price : template.getPrices()) {
            prices.add(price.getCourseTemplatePriceId());
        }
        courseDto.setPrices(prices);
        courseDto.setMeetingPoint(template.getMeetingPoint());
        courseDto.setNumberParticipants(template.getNumberParticipants());
        courseDto.setNumberTrainers(template.getNumberTrainer());
        courseDto.setNumberWaitlist(template.getNumberWaitlist());
        List<Long> qualifications = new ArrayList<>();
        for (var qualification : template.getQualifications()) {
            qualifications.add(qualification.getQualificationId());
        }
        courseDto.setRequiredQualifications(qualifications);
        List<LocalDateTime> dates = new ArrayList<>();
        dates.add(LocalDateTime.of(2024, 5, 17, 18, 0));
        dates.add(LocalDateTime.of(2024, 5, 18, 10, 0));
        courseDto.setDates(dates);
        return courseService.create(courseMapper.mapCoursePostDTOToCourse(courseDto));
    }

    @PostMapping
    public void fillWithDummyData() {
        var template1 = createTestDataTemplate1(createTestDataLocation(), createTestDataPrices(), createTestDataQualifications());
        var template2 = createTestDataTemplate2(createTestDataLocation(), createTestDataPrices(), createTestDataQualifications());

        var course1 = createFromTemplate(template1);
        var course2 = createFromTemplate(template2);
    }
}
