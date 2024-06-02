import com.summitsync.api.SummitSyncApplication;
import com.summitsync.api.course.CourseService;
import com.summitsync.api.date.EventDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SummitSyncApplication.class)
@ActiveProfiles("test")
public class CourseServiceTest {
    List<EventDate> oldDates;
    @Autowired
    private CourseService courseService;

    @BeforeEach
    public void fillLists(){
        oldDates=new ArrayList<>();
        oldDates.add(EventDate.builder().startTime(LocalDateTime.of(2024,2,3,12,0)).durationInMinutes(30).build());
        oldDates.add(EventDate.builder().startTime(LocalDateTime.of(2024,2,4,12,0)).durationInMinutes(30).build());
        oldDates.add(EventDate.builder().startTime(LocalDateTime.of(2024,2,5,12,0)).durationInMinutes(30).build());
        oldDates.add(EventDate.builder().startTime(LocalDateTime.of(2024,2,6,12,0)).durationInMinutes(30).build());
        oldDates.add(EventDate.builder().startTime(LocalDateTime.of(2024,2,7,12,0)).durationInMinutes(30).build());
        oldDates.add(EventDate.builder().startTime(LocalDateTime.of(2024,2,8,12,0)).durationInMinutes(30).build());
    }

    @Test
    public void testRemovingUnusedDatesFromOldDatesList(){
        List<EventDate>newEventDates=new ArrayList<>();
        System.out.println("List size before updating: "+oldDates.size());
        boolean updatedList=courseService.removeUnusedOrUpdatedDates(oldDates,newEventDates);
        System.out.println("List size after updating: "+oldDates.size());
        Assertions.assertEquals(oldDates.size(),0);
        Assertions.assertTrue(updatedList);
    }

    @Test
    public void testRemovingUpdatedDatesFromOldDates(){
        List<EventDate>newDates =new ArrayList<>();
        newDates.add(EventDate.builder().startTime(LocalDateTime.of(2024,2,3,12,1)).durationInMinutes(30).build());
        newDates.add(EventDate.builder().startTime(LocalDateTime.of(2024,2,4,12,2)).durationInMinutes(30).build());
        newDates.add(EventDate.builder().startTime(LocalDateTime.of(2024,2,5,12,3)).durationInMinutes(30).build());
        newDates.add(EventDate.builder().startTime(LocalDateTime.of(2024,2,6,12,4)).durationInMinutes(30).build());
        newDates.add(EventDate.builder().startTime(LocalDateTime.of(2024,2,7,12,5)).durationInMinutes(30).build());
        newDates.add(EventDate.builder().startTime(LocalDateTime.of(2024,2,8,12,6)).durationInMinutes(30).build());

        System.out.println("List size before updating: "+oldDates.size());
        boolean updatedList=courseService.removeUnusedOrUpdatedDates(oldDates,newDates);
        System.out.println("List size after updating: "+oldDates.size());
        Assertions.assertEquals(oldDates.size(),0);
        Assertions.assertTrue(updatedList);
    }

    @Test
    public void testRemovingDuplicateDateFromNewDatesList(){
        List<EventDate>newDates=new ArrayList<>();
        newDates.add(EventDate.builder().startTime(LocalDateTime.of(2024,2,3,12,0)).durationInMinutes(30).build());
        newDates.add(EventDate.builder().startTime(LocalDateTime.of(2024,2,4,12,0)).durationInMinutes(30).build());
        newDates.add(EventDate.builder().startTime(LocalDateTime.of(2024,2,5,12,0)).durationInMinutes(30).build());
        newDates.add(EventDate.builder().startTime(LocalDateTime.of(2024,2,6,12,0)).durationInMinutes(30).build());
        newDates.add(EventDate.builder().startTime(LocalDateTime.of(2024,2,7,12,0)).durationInMinutes(30).build());
        newDates.add(EventDate.builder().startTime(LocalDateTime.of(2024,2,8,12,0)).durationInMinutes(30).build());
        System.out.println("List size before updating: "+newDates.size());
        boolean updatedList=courseService.removeUnusedOrUpdatedDates(oldDates,newDates);
        System.out.println("List size after updating: "+newDates.size());
        Assertions.assertEquals(newDates.size(),0);
        Assertions.assertFalse(updatedList);
    }

    @Test
    public void testRemovingUnusedAndUpdatedDatesAndAddingNewDates(){
        List<EventDate>newDates=new ArrayList<>();
        //Updated date
        newDates.add(EventDate.builder().startTime(LocalDateTime.of(2024,2,3,12,1)).durationInMinutes(30).build());
        newDates.add(EventDate.builder().startTime(LocalDateTime.of(2024,2,4,12,0)).durationInMinutes(30).build());
        newDates.add(EventDate.builder().startTime(LocalDateTime.of(2024,2,5,12,0)).durationInMinutes(30).build());
        newDates.add(EventDate.builder().startTime(LocalDateTime.of(2024,2,6,12,0)).durationInMinutes(30).build());
        //updated date(duration)
        newDates.add(EventDate.builder().startTime(LocalDateTime.of(2024,2,7,12,0)).durationInMinutes(45).build());
        //new date
        newDates.add(EventDate.builder().startTime(LocalDateTime.of(2024,2,9,12,0)).durationInMinutes(30).build());
        System.out.println("List size before updating: "+oldDates.size());
        boolean updatedList=courseService.updateDatesList(oldDates,newDates);
        System.out.println("List size after updating: "+oldDates.size());
        Assertions.assertEquals(oldDates.size(),6);
        Assertions.assertTrue(updatedList);
    }
}
