package com.mid.data.model;

import com.mid.data.common.ProductType;
import com.mid.data.common.StateType;
import java.time.LocalDate;

/**
 * @author
 */
public class FullContractModel {

    int id;
    int number;
    int bindedWoker;

    String catalog;
    ProductType productType;
    double width;
    double height;
    double price;

    String ownerName;
    String ownerNumber;

    LocalDate limitStart;
    LocalDate limitEnd;

    double sourceAmound;
    double agreedAmound;
    double prepaidAmound;
    double remaindAmound;

    private String note;

    private double squarePrice;
    private double welderSqrPay;
    private double painterSqrPay;
    private double welderPay;
    private double painterPay;

    private StateType state;
    private boolean fuse;

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public int getNumber() {
	return number;
    }

    public void setNumber(int number) {
	this.number = number;
    }

    public void setBindedWoker(int bindedWoker) {
        this.bindedWoker = bindedWoker;
        System.out.println("binset: " + bindedWoker);
    }

    public int getBindedWoker() {
        System.out.println("binget: " + bindedWoker);
        return bindedWoker;
    }
    
    public String getCatalog() {
	return catalog;
    }

    public void setCatalog(String catalog) {
	this.catalog = catalog;
    }

    public ProductType getProductType() {
	return productType;
    }

    public void setProductType(ProductType productType) {
	this.productType = productType;
    }

    public double getWidth() {
	return width;
    }

    public void setWidth(double width) {
	this.width = width;
    }

    public double getHeight() {
	return height;
    }

    public void setHeight(double height) {
	this.height = height;
    }

    public double getPrice() {
	return price;
    }

    public void setPrice(double price) {
	this.price = price;
    }

    public String getOwnerName() {
	return ownerName;
    }

    public void setOwnerName(String ownerName) {
	this.ownerName = ownerName;
    }

    public String getOwnerNumber() {
	return ownerNumber;
    }

    public void setOwnerNumber(String ownerNumber) {
	this.ownerNumber = ownerNumber;
    }
    public LocalDate getLimitStart() {
	return limitStart;
    }

    public void setLimitStart(LocalDate limitStart) {
	this.limitStart = limitStart;
    }

    public LocalDate getLimitEnd() {
	return limitEnd;
    }

    public void setLimitEnd(LocalDate limitEnd) {
	this.limitEnd = limitEnd;
    }

    public double getSourceAmound() {
	return sourceAmound;
    }

    public void setSourceAmound(double sourceAmound) {
	this.sourceAmound = sourceAmound;
    }

    public double getAgreedAmound() {
	return agreedAmound;
    }

    public void setAgreedAmound(double agreedAmound) {
	this.agreedAmound = agreedAmound;
    }

    public double getPrepaidAmound() {
	return prepaidAmound;
    }

    public void setPrepaidAmound(double prepaidAmound) {
	this.prepaidAmound = prepaidAmound;
    }

    public double getRemaindAmound() {
	return remaindAmound;
    }

    public void setRemaindAmound(double remaindAmound) {
	this.remaindAmound = remaindAmound;
    }

    public String getNote() {
	return note;
    }

    public void setNote(String note) {
	this.note = note;
    }

    public double getSquarePrice() {
	return squarePrice;
    }

    public void setSquarePrice(double squarePrice) {
	this.squarePrice = squarePrice;
    }

    public double getWelderPay() {
	return welderPay;
    }

    public void setWelderPay(double welderPay) {
	this.welderPay = welderPay;
    }

    public double getWelderSqrPay() {
	return welderSqrPay;
    }

    public void setWelderSqrPay(double welderSqrPay) {
	this.welderSqrPay = welderSqrPay;
    }

    public double getPainterPay() {
	return painterPay;
    }

    public void setPainterPay(double painterPay) {
	this.painterPay = painterPay;
    }

    public double getPainterSqrPay() {
	return painterSqrPay;
    }

    public void setPainterSqrPay(double painterSqrPay) {
	this.painterSqrPay = painterSqrPay;
    }

    public StateType getState() {
	return state;
    }

    public void setState(StateType state) {
	this.state = state;
    }

    public boolean isFuse() {
	return fuse;
    }

    public void setFuse(boolean fuse) {
	this.fuse = fuse;
    }
}
