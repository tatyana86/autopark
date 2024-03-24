package ru.krivonogova.autopark.models;

import java.util.List;

public class Report {
	
	private final TypeReport typeReport;
	private final Period period;
	private final String dateFrom;
	private final String dateTo;
	private List<ReportResult> result;
	
	public Report(TypeReport typeReport, Period period, String dateFrom, String dateTo, List<ReportResult> result) {
		this.typeReport = typeReport;
		this.period = period;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.result = result;
	}

	public List<ReportResult> getResult() {
		return result;
	}

	public void setResult(List<ReportResult> result) {
		this.result = result;
	}

	public TypeReport getTypeReport() {
		return typeReport;
	}

	public Period getPeriod() {
		return period;
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}
	
}
