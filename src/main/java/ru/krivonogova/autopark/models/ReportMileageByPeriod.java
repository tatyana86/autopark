package ru.krivonogova.autopark.models;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportMileageByPeriod extends Report {

	public ReportMileageByPeriod(Period period, String dateFrom, String dateTo, List<Trip> trips) {
		super(TypeReport.MILEAGE_BY_PERIOD, period, dateFrom, dateTo);
		getResult(trips);
	}
	
	private void getResult(List<Trip> trips) {
		List<ReportResult> result = new ArrayList<>();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
	    LocalDateTime startDate = LocalDateTime.parse(getDateFrom(), formatter);
	    LocalDateTime endDate = LocalDateTime.parse(getDateTo(), formatter);
	    
	    switch (getPeriod()) {
		    case DAY: {
		        Map<String, Double> dailyDistance = new HashMap<>();
		        LocalDate currentDate = startDate.toLocalDate();
		        while (currentDate.isBefore(endDate.toLocalDate()) || currentDate.isEqual(endDate.toLocalDate())) {
		            dailyDistance.put(currentDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")), 0.0);
		            currentDate = currentDate.plusDays(1);
		        }
		        for (Trip trip : trips) {
		            LocalDate tripDate = startDate.toLocalDate();
		            String tripDateStr = tripDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
		            if (dailyDistance.containsKey(tripDateStr)) {
		                dailyDistance.put(tripDateStr, dailyDistance.get(tripDateStr) + trip.getDistance());
		            }
		        }
		        for (Map.Entry<String, Double> entry : dailyDistance.entrySet()) {
		            result.add(new ReportResult(entry.getKey(), entry.getValue().toString()));
		        }
		        break;
		    }
		    
		    case MONTH: {
		        Map<String, Double> monthlyDistance = new HashMap<>();
		        YearMonth currentMonth = YearMonth.from(startDate);
		        while (currentMonth.isBefore(YearMonth.from(endDate)) || currentMonth.equals(YearMonth.from(endDate))) {
		            monthlyDistance.put(currentMonth.format(DateTimeFormatter.ofPattern("yyyy/MM")), 0.0);
		            currentMonth = currentMonth.plusMonths(1);
		        }
		        for (Trip trip : trips) {
		            YearMonth tripMonth = YearMonth.from(startDate.toLocalDate());
		            String tripMonthStr = tripMonth.format(DateTimeFormatter.ofPattern("yyyy/MM"));
		            if (monthlyDistance.containsKey(tripMonthStr)) {
		                monthlyDistance.put(tripMonthStr, monthlyDistance.get(tripMonthStr) + trip.getDistance());
		            }
		        }
		        for (Map.Entry<String, Double> entry : monthlyDistance.entrySet()) {
		            result.add(new ReportResult(entry.getKey(), entry.getValue().toString()));
		        }
		        break;
		    }
	    }
	    
	    setResult(result);
		
	}
	
    /*private LocalDate toLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        return dateTime.toLocalDate();
    }*/
	
}
