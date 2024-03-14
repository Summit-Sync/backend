package com.summitsync.api.group;

import com.summitsync.api.group.dto.GroupGetDTO;
import lombok.Data;
@Data
public class CreateGroupWrapperDTO {
    private long templateId;
    private GroupGetDTO group;
}
