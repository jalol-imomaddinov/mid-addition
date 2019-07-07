package com.mid.data.model;

import com.mid.data.common.ProductType;
import com.mid.util.StringUtil;

/**
 * @author
 */
public class ProductModel {

    private int id;
    private String catalog;
    private ProductType productType;
    private double price;
    private double welder;
    private double painter;

    private String sizeStr;
    private String priceStr;
    private String typeStr;
    private String welderStr;
    private String painterStr;

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
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
        System.out.println("TYPE_AAA: "+productType);
    }

    public double getPrice() {
	return price;
    }

    public double getWelder() {
        return welder;
    }

    public void setWelder(double welder) {
        this.welder = welder;
    }

    public double getPainter() {
        return painter;
    }

    public void setPainter(double painter) {
        this.painter = painter;
    }
    
    public String getPriceStr() {
	return priceStr;
    }

    public void setPrice(double price) {
	this.price = price;
    }

   
    public String getSizeStr() {
	return sizeStr;
    }

    public void setSizeStr(String sizeStr) {
	this.sizeStr = sizeStr;
    }

    public String getTypeStr() {
	return typeStr;
    }

    public void setTypeStr(String typeStr) {
	this.typeStr = typeStr;
    }

    public void setPriceStr(String priceStr) {
	this.priceStr = priceStr;
    }

    public String getWelderStr() {
        return welderStr;
    }

    public void setWelderStr(String welderStr) {
        this.welderStr = welderStr;
    }

    public String getPainterStr() {
        return painterStr;
    }

    public void setPainterStr(String painterStr) {
        this.painterStr = painterStr;
    }

    public void init() {
                StringBuilder buffer = new StringBuilder();
        buffer.append("amount: ").append(catalog)
                .append("price: ").append(price)
                .append("welder: ").append(welder)
                .append("painter: ").append(painter);
        
        
//	sizeStr = StringUtil.DoubletoString(width) + " x " + StringUtil.DoubletoString(height);
	priceStr = StringUtil.toAmound(price);
	typeStr = productType.toString();
        painterStr = StringUtil.toAmound(painter);
        welderStr = StringUtil.toAmound(welder);
        
/*	if (productType == ProductType.SWING || productType == ProductType.CAPRICORN || productType == ProductType.LATTICE) {
	    typeStr += " " + productClass;
	}
  */  }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("catalog: ").append(catalog)
                .append("price: ").append(price)
                .append("welder: ").append(welder)
                .append("painter: ").append(painter);
        
        return buffer.toString();
    }
}
