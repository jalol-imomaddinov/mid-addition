package com.mid.data.model;

import com.mid.data.common.JobType;

/**
 * @author
 */
public class WorkerModel {

    private int id;
    private String name;
    private String state;
    private String phone;
    private JobType jobType;
    private boolean fired;

    private boolean binded;

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public JobType getJobType() {
	return jobType;
    }

    public void setJobType(JobType jobType) {
	this.jobType = jobType;
    }

    public boolean isFired() {
	return fired;
    }

    public void setFired(boolean fired) {
	this.fired = fired;
	this.setState(fired ? "Бушатилган" : "Ишлаяпди");
    }

    public void setBinded(boolean binded) {
	this.binded = binded;
    }

    public String getState() {
	return state;
    }

    public void setState(String state) {
	this.state = state;
    }

    public boolean isBinded() {
	return binded;
    }

    @Override
    public String toString() {
	return name + " " + jobType;
    }
}
