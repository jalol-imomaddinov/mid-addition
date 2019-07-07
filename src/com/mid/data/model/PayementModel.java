package com.mid.data.model;

import com.mid.util.StringUtil;
import java.time.LocalDate;

/**
 * @author
 */
public class PayementModel {

    private int id;
    private LocalDate date;
    private String amound;

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public LocalDate getDate() {
	return date;
    }

    public void setDate(LocalDate date) {
	this.date = date;
    }

    public void setAmound(double amound) {
	this.amound = StringUtil.toAmound(amound);
    }

    public String getAmound() {
	return amound;
    }
}
