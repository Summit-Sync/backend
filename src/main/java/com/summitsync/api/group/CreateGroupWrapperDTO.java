package com.summitsync.api.group;

import com.summitsync.api.group.dto.GroupGetDTO;
import com.summitsync.api.group.dto.GroupPostDTO;
import lombok.Data;
@Data
public class CreateGroupWrapperDTO {
    private long templateId;
    private GroupPostDTO group;
}
