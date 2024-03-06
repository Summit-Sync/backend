package com.summitsync.api.qualification;

import com.summitsync.api.qualification.dto.AddQualificationDto;
import com.summitsync.api.qualification.dto.QualificationDto;
import org.springframework.stereotype.Service;

@Service
public class QualificationMapper {
    public Qualification mapAddQualificationDtoToQualification(AddQualificationDto addQualificationDto) {
        var qualification = new Qualification();
        qualification.setName(addQualificationDto.getName());

        return qualification;
    }

    public QualificationDto mapQualificationToQualificationDto(Qualification qualification) {
        return new QualificationDto(qualification.getQualificationId(), qualification.getName());
    }
}
