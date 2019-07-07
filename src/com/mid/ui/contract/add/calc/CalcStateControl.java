package com.mid.ui.contract.add.calc;

import java.util.function.Consumer;

/**
 * @author
 */
public class CalcStateControl {

    private boolean catalogFlag;
    private boolean widthFlag;
    private boolean heightFlag;

    private Consumer<Boolean> callback;

    public CalcStateControl() {
	clear();
    }

    public void setCatalogFlag(boolean catalogFlag) {
	this.catalogFlag = catalogFlag;
	check();
    }

    public void setHeightFlag(boolean heightFlag) {
	this.heightFlag = heightFlag;
	check();
    }

    public void setWidthFlag(boolean widthFlag) {
	this.widthFlag = widthFlag;
	check();
    }

    public boolean isSuccess() {
	return (catalogFlag && widthFlag && heightFlag);
    }

    private void check() {
	callback.accept(catalogFlag && widthFlag && heightFlag);
    }

    public void setCallback(Consumer<Boolean> callback) {
	this.callback = callback;
    }

    public void clear() {
	this.catalogFlag = false;
	this.heightFlag = false;
	this.widthFlag = false;

	if (callback != null) {
	    callback.accept(false);
	}
    }
}
