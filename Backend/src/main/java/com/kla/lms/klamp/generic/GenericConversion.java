package com.kla.lms.klamp.generic;

import java.util.List;

import org.springframework.data.domain.Page;

import com.kla.lms.klamp.entity.ResultPage;

public class GenericConversion<T> implements GetPageable{
	
	public  ResultPage<T> convertToResultData(Page<T> page) {
		ResultPage<T> responseData = new ResultPage<>();
		if (page != null) {
			responseData.setContent(page.getContent());
			responseData.setPageNumber(page.getPageable().getPageNumber());
			responseData.setPageSize(page.getPageable().getPageSize());
			responseData.setTotalElements(page.getTotalElements());
			responseData.setTotalPages(page.getTotalPages());
		}
		return responseData;
	}
	
	public ResultPage<T> convertToResultData(Page<T> page, List<T> dataList){
		ResultPage<T> resultPage = new ResultPage<>();
		
		resultPage.setContent(dataList);
		resultPage.setPageNumber(page.getPageable().getPageNumber());
		resultPage.setPageSize(page.getPageable().getPageSize());
		resultPage.setTotalElements(page.getTotalElements());
		resultPage.setTotalPages(page.getTotalPages());
		
		return resultPage;
	}

	public ResultPage<T> convertToResultData(List<T> dataList){
		ResultPage<T> resultPage = new ResultPage<>();
		
		resultPage.setContent(dataList);
		resultPage.setPageNumber(0);
		resultPage.setPageSize(0);
		resultPage.setTotalElements(dataList.size());
		resultPage.setTotalPages(0);
		
		return resultPage;
	}
}
