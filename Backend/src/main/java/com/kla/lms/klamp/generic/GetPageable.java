package com.kla.lms.klamp.generic;

import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.util.StringUtils;

public interface GetPageable {
	
	default Pageable getPageable(Integer pageNumber, Integer pageSize, String sortDirection, String sortBy) {
		Direction directionEnum = sortDirection.equalsIgnoreCase("asc")? Direction.ASC: Direction.DESC;
		List<String> sortList;
		
		if (!StringUtils.isEmpty(sortBy)) {
			sortList = Arrays.asList(sortBy);
		} else {
			sortList = Arrays.asList("createdAt");
		}
		
		pageSize = (pageSize > -1) ? pageSize : 1;
		return PageRequest.of(pageNumber, pageSize, directionEnum, sortList.toArray(new String[0]));
	}
}
