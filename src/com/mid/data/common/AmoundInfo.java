package com.mid.data.common;

public class AmoundInfo {

    private double agreed;
    private double prepaid;

    public AmoundInfo() {
    }

    public double getAgreed() {
	return agreed;
    }

    public double getPrepaid() {
	return prepaid;
    }
    
    public double getRemaind() {
	return agreed - prepaid;
    }

    public void setAgreed(double agreed) {
	this.agreed = agreed;
    }

    public void setPrepaid(double prepaid) {
	this.prepaid = prepaid;
    }
}