package com.mid.ui.control.converter;

import com.mid.util.StringUtil;
import java.util.function.Consumer;
import javafx.util.StringConverter;

/**
 * @author
 */
public class SizeConverter extends StringConverter<Double> {

    private final Consumer<Boolean> callback;
    private final String suffix;

    public SizeConverter(String suffix, Consumer<Boolean> callback) {
	this.suffix = suffix;
	this.callback = callback;
    }

    @Override
    public String toString(Double object) {
	if (object == null) {
	    if (callback != null) {
		callback.accept(Boolean.FALSE);
	    }
	    return "";
	}
	else {
	    if (callback != null) {
		callback.accept(object != 0);
	    }
	    return (object == 0 ? "" : StringUtil.DoubletoString(object) + suffix);
	}
    }

    @Override
    public Double fromString(String string) {
	if (string == null || string.equals("")) {
	    return 0D;
	}
	String trim = string.trim();
	String str = trim.substring(0, trim.length() - suffix.length());
	return Double.parseDouble(str);
    }
}
