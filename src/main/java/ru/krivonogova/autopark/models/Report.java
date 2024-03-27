package ru.krivonogova.autopark.models;

import java.util.List;

public class Report {
	
	private TypeReport typeReport;
	private Period period;
	private String dateFrom;
	private String dateTo;
	private List<ReportResult> result;
	
	public Report(TypeReport typeReport, Period period, String dateFrom, String dateTo) {
		this.typeReport = typeReport;
		this.period = period;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
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

	public void setTypeReport(TypeReport typeReport) {
		this.typeReport = typeReport;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}
	
}
