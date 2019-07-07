package com.mid.ui.control.converter;

import com.mid.util.StringUtil;
import java.util.function.Consumer;
import javafx.util.StringConverter;

/**
 * @author
 */
public class IntegerConverter extends StringConverter<Integer> {

    private final Consumer<Boolean> callback;

    public IntegerConverter(Consumer<Boolean> callback) {
	this.callback = callback;
    }

    @Override
    public String toString(Integer object) {
	if (object == null) {
	    if (callback != null) {
		callback.accept(Boolean.FALSE);
	    }
	    return "";
	}
	else {
	    if (callback != null) {
		callback.accept(true);
	    }
	    return (object == 0 ? "" : object.toString());
	}
    }

    @Override
    public Integer fromString(String string) {
	return (string == null || string.equals("")) ? 0 : Integer.parseInt(string);
    }
}
