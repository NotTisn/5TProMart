package com.fivetpromart.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaginationMeta {
    private long totalItems;
    private int itemsPerPage;
    private int totalPages;
    private int startPage;
}