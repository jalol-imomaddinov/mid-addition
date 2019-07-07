package com.mid.ui.contract.add.calc;

/**
 * @author
 */
public class CalculateData {

    private double price;
    private double welderPay;
    private double painterPay;

    private boolean enable;

    public void setPainterPay(double painterPay) {
	this.painterPay = painterPay;
    }

    public void setPrice(double price) {
	this.price = price;
    }

    public void setWelderPay(double welderPay) {
	this.welderPay = welderPay;
    }

    public double getPrice() {
	return price;
    }

    public double getWelderPay() {
	return welderPay;
    }

    public double getPainterPay() {
	return painterPay;
    }

    public boolean isEnable() {
	return enable;
    }

    public void setEnable(boolean enable) {
	this.enable = enable;
    }

    public void clear() {
	enable = false;
	painterPay = 0;
	price = 0;
	welderPay = 0;
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder("ManualData: \n");
	builder.append("\nenable: ").append(enable);
	builder.append("\nsqare-price: ").append(price);
	builder.append("\npainter: ").append(painterPay);
	builder.append("\nwelder: ").append(welderPay);

	return builder.toString();
    }
}
