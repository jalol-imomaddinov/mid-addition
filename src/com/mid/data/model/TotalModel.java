package com.mid.data.model;

import com.mid.util.DateUtil;
import com.mid.util.StringUtil;
import java.time.LocalDate;

/**
 * @author
 */
public class TotalModel {

    private int id;
    private int workerId;
    private int type;
    private String note;
    private LocalDate date;
    private double amound;

    private String amoundStr;
    private String dateStr;

    public int getId() {
	return id;
    }

    public int getType() {
	return type;
    }

    public void setType(int type) {
	this.type = type;
    }

    public void setId(int id) {
	this.id = id;
    }

    public void setNote(String note) {
	this.note = note;
    }

    public String getNote() {
	return note;
    }

    public LocalDate getDate() {
	return date;
    }

    public void setDate(LocalDate date) {
	this.date = date;
    }

    public double getAmound() {
	return amound;
    }

    public void setAmound(double amound) {
	this.amound = amound;
    }

    public void setWorkerId(int workerId) {
	this.workerId = workerId;
    }

    public int getWorkerId() {
	return workerId;
    }

    public String getAmoundStr() {
	return amoundStr;
    }

    public String getDateStr() {
	return dateStr;
    }

    public void setAmoundStr(String amoundStr) {
	this.amoundStr = amoundStr;
    }

    public void setDateStr(String dateStr) {
	this.dateStr = dateStr;
    }

    public void init() {
	dateStr = DateUtil.viewFormat(date);
	amoundStr = StringUtil.toAmound(amound);
    }
}
