package com.summitsync.api.status.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class StatusGetDto {
    long statusId;
    String text;
}
