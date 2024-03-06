package com.summitsync.api.group;

import com.summitsync.api.group.dto.GroupGetDTO;
import com.summitsync.api.grouptemplate.dto.GroupTemplateGetDTO;
import com.summitsync.api.grouptemplate.dto.GroupTemplatePostDTO;
import lombok.Data;
@Data
public class CreateWrapperDTO {
    private GroupTemplateGetDTO template;
    private GroupGetDTO group;
}
