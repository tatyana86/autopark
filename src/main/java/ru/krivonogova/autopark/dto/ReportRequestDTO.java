package ru.krivonogova.autopark.dto;

import ru.krivonogova.autopark.models.Period;
import ru.krivonogova.autopark.models.TypeReport;

public class ReportRequestDTO {
	
	private final int idVehicle;
	private final TypeReport typeReport;
	private final Period period;
	private final String dateFrom;
	private final String dateTo;
	
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
