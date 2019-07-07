package com.mid.ui.control.converter;

import com.mid.util.StringUtil;
import java.util.function.Consumer;
import javafx.util.StringConverter;

/**
 * @author
 */
public class DoubleConverter extends StringConverter<Double> {

    private final Consumer<Boolean> callback;

    public DoubleConverter(Consumer<Boolean> callback) {
	this.callback = callback;
    }

    @Override
    public String toString(Double object) {
	if (object == null) {
	    if (callback != null) {
		callback.accept(Boolean.FALSE);
	    }
	    return "0";
	}
	else {
	    if (callback != null) {
		callback.accept(true);
	    }
	    return (StringUtil.DoubletoString(object));
	}
    }

    @Override
    public Double fromString(String string) {
	return (string == null || string.equals("")) ? 0D : Double.parseDouble(string.replace(',', '.'));
    }
}
