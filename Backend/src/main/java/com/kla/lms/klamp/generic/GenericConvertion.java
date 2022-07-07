package com.kla.lms.klamp.generic;

import java.util.List;

import org.springframework.data.domain.Page;

import com.kla.lms.klamp.entity.ResultPage;

public class GenericConvertion<T> implements GetPageable{
	
	public  ResultPage<T> convertPageToResultData(Page<T> page) {
		ResultPage<T> responseData = new ResultPage<>();
		if (page != null) {
			responseData.setContent(page.getContent());
			responseData.setPageNumber(page.getPageable().getPageNumber());
			responseData.setPageSize(page.getPageable().getPageSize());
			responseData.setTotalElements(page.getTotalElements());
			responseData.setTotalPages(page.getTotalPages());
		}
		return responseData;
	};
	
	public ResultPage<T> convertToResultData(Page<T> page, List<T> dataList){
		ResultPage<T> resultPage = new ResultPage<T>();
		
		resultPage.setContent(dataList);
		resultPage.setContent(page.getContent());
		resultPage.setPageNumber(page.getPageable().getPageNumber());
		resultPage.setPageSize(page.getPageable().getPageSize());
		resultPage.setTotalElements(page.getTotalElements());
		resultPage.setTotalPages(page.getTotalPages());
		
		return resultPage;
	}
}
