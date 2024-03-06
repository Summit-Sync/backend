package com.summitsync.api.group;

import com.summitsync.api.group.dto.GroupGetDTO;
import com.summitsync.api.grouptemplate.dto.GroupTemplateGetDTO;
import lombok.Data;
@Data
public class CreateGroupWrapperDTO {
    private GroupTemplateGetDTO template;
    private GroupGetDTO group;
}
