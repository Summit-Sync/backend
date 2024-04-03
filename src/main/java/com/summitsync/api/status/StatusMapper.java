package com.summitsync.api.status;

import com.summitsync.api.status.dto.StatusGetDto;
import com.summitsync.api.status.dto.StatusPostDto;
import org.springframework.stereotype.Service;

@Service
public class StatusMapper {
    public Status mapStatusPostDtoToStatus(StatusPostDto statusPostDto) {
        return Status
                .builder()
                .text(statusPostDto.getText())
                .build();
    }

    public StatusGetDto mapStatusToStatusGetDto(Status status) {
        return StatusGetDto
                .builder()
                .statusId(status.getStatusId())
                .text(status.getText())
                .build();
    }
}
