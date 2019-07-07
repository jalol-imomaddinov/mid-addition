package com.mid.ui.contract.add.calc;

import com.mid.data.database.DataAccessHelper;
import com.mid.data.model.ProductModel;

public class Calculator {

    private final CalcResult result;

    /// BOUNDS
    private double w;
    private double h;

    /// MODELS
    private ProductModel productModel;

    private final CalculateData manualData;

    private boolean manual;

    public Calculator() {
	result = new CalcResult();
	manualData = new CalculateData();
    }

    public void setProduct(ProductModel product) {
	this.productModel = product;
    }

    public void setSize(double w, double h) {
	this.w = w / 100;
	this.h = h / 100;
    }

    // CALCULATING
    public void calculate() {
        result.clear();
        
	if (productModel == null) {
	    return;
	}

        System.out.println("calc::switch: " + productModel);
        System.out.println("w: " + w);
        
        switch(productModel.getProductType()) {
            case RAILING:
            case BENCH: {
                result.setPrice(productModel.getPrice());
                result.setAmount(w * productModel.getPrice());
                result.setPainter(w * productModel.getPainter());
                result.setWelder(w * productModel.getWelder());
                result.setWelderSqrPrice(productModel.getWelder());
                result.setPainterSqrPrice(productModel.getPainter());
                break;
            }
            case CAPRICORN:
            case SWING: {
                double sqr = w * h;
                result.setPrice(productModel.getPrice());
                result.setAmount(sqr * productModel.getPrice());
                result.setPainter(sqr * productModel.getPainter());
                result.setWelder(sqr * productModel.getWelder());
                result.setWelderSqrPrice(productModel.getWelder());
                result.setPainterSqrPrice(productModel.getPainter());
                break;
            }
            default: {
                throw new RuntimeException("CALCULATE: productModel not tries;");
            }
        }
    }

    public CalcResult getResult() {
	return result;
    }

    public CalculateData getManualData() {
	return manualData;
    }

    public void clear() {
	w = 0;
	h = 0;
    }
}
