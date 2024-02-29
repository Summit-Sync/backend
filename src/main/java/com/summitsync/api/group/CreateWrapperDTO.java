package com.summitsync.api.group;

import com.summitsync.api.grouptemplate.GroupTemplateDTO;
import lombok.Data;

@Data
public class CreateWrapperDTO {
    private GroupTemplateDTO template;
    private GroupDTO group;
}
