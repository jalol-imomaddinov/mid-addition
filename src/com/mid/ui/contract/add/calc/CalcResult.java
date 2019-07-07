package com.mid.ui.contract.add.calc;

/**
 * @author
 */
public class CalcResult {

    private double amount;
    private double price;
    private double painter;
    private double welder;

    private double painterSqrPrice;
    private double welderSqrPrice;
    private boolean block;

    public double getAmount() {
	return amount;
    }

    public void setAmount(double amount) {
	this.amount = amount;
    }

    public double getPrice() {
	return price;
    }

    public void setPrice(double price) {
	this.price = price;
    }

    public double getPainter() {
	return painter;
    }

    public void setPainter(double painter) {
	this.painter = painter;
    }

    public void setPainterSqrPrice(double painterSqrPrice) {
	this.painterSqrPrice = painterSqrPrice;
    }

    public void setWelderSqrPrice(double welderSqrPrice) {
	this.welderSqrPrice = welderSqrPrice;
    }

    public double getWelder() {
	return welder;
    }

    public void setWelder(double welder) {
	this.welder = welder;
    }

    public double getPainterSqrPrice() {
	return painterSqrPrice;
    }

    public double getWelderSqrPrice() {
	return welderSqrPrice;
    }
    public void clear() {
	if (block) {
	    return;
	}
	amount = 0;
	painter = 0;
	price = 0;
	welder = 0;

//	System.out.println("-> result:clear");
    }

    public void blockResult() {
	block = true;
    }

    public void unblockResult() {
	block = false;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("amount: ").append(amount)
                .append("price: ").append(price)
                .append("welder: ").append(welder)
                .append("painter: ").append(painter);
        return buffer.toString();
    }
}
