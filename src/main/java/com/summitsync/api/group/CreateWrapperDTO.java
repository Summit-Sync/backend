package com.summitsync.api.group;

import com.summitsync.api.grouptemplate.dto.GroupTemplatePostDTO;
import lombok.Data;
@Data
public class CreateWrapperDTO {
    private GroupTemplatePostDTO template;
    private GroupDTO group;
}
