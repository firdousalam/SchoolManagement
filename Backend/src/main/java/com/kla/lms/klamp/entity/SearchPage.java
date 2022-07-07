package com.kla.lms.klamp.entity;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
@lombok.Data
@lombok.EqualsAndHashCode(callSuper = false)
public class SearchPage {
    private int pageNumber = 0;
    private int pageSize = 10;
    private Sort.Direction sortDirection = Direction.ASC;
    private String sortBy = "id";
}
