package com.fivetpromart.application.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DisposalBatchCommand {
    private String reason;
    private String note;
    private List<DisposalItemCommand> items;
    private List<String> image;
    private String staffId; // Will be set from security context
}
