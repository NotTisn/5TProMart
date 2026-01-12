package com.fivetpromart.presentation.dto.query;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StaffSearchQueryDto {
    private String search;
    private String accountType;
}
