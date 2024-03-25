package ru.krivonogova.autopark.services;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.krivonogova.autopark.dto.ReportRequestDTO;
import ru.krivonogova.autopark.models.Period;
import ru.krivonogova.autopark.models.ReportMileageByPeriod;
import ru.krivonogova.autopark.models.ReportResult;
import ru.krivonogova.autopark.models.Trip;
import ru.krivonogova.autopark.models.TypeReport;

@Service
@Transactional(readOnly = true)
public class ReportsService {
	
	public List<ReportResult> getReport(ReportRequestDTO request, List<Trip> trips) {
		TypeReport typeReport = request.getTypeReport();
        switch(typeReport) {
	        default: {
	        	Period period = request.getPeriod();
	        	String dateFrom = request.getDateFrom();
	        	String dateTo = request.getDateTo();
	        	ReportMileageByPeriod report = new ReportMileageByPeriod(period, dateFrom, dateTo, trips);
	            return report.getResult();
	        }
        }
	}
	
}
