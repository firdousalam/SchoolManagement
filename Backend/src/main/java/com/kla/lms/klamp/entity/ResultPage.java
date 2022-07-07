package com.kla.lms.klamp.entity;

import java.util.ArrayList;
import java.util.List;
@lombok.Data
@lombok.EqualsAndHashCode(callSuper = false)
public class ResultPage<T> {
	int totalPages = 1;
	long totalElements = 1;
	int pageNumber = 0;
	int pageSize = 10;	
	List<T> content = new ArrayList<T>();
}