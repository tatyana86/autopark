package ru.krivonogova.autopark.dto;

import ru.krivonogova.autopark.models.Period;
import ru.krivonogova.autopark.models.TypeReport;

public class ReportRequestDTO {
	
	private int idVehicle;
	private TypeReport typeReport;
	private Period period;
	private String dateFrom;
	private String dateTo;
	
	public ReportRequestDTO(int idVehicle, TypeReport typeReport, Period period, String dateFrom, String dateTo) {
		this.idVehicle = idVehicle;
		this.typeReport = typeReport;
		this.period = period;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
	}

	public int getIdVehicle() {
		return idVehicle;
	}

	public void setIdVehicle(int idVehicle) {
		this.idVehicle = idVehicle;
	}

	public TypeReport getTypeReport() {
		return typeReport;
	}

	public void setTypeReport(TypeReport typeReport) {
		this.typeReport = typeReport;
	}

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}

	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}
	
}
