package com.mid.ui.control.converter;

import java.util.function.Consumer;
import javafx.util.StringConverter;

/**
 * @author
 */
public class TextConverter extends StringConverter<String> {

    private final Consumer<Boolean> callback;

    public TextConverter(Consumer<Boolean> callback) {
	this.callback = callback;
    }

    @Override
    public String toString(String object) {
	if (object == null) {
	    if (callback != null) {
		callback.accept(Boolean.FALSE);
	    }
	    return "";
	}
	else {
	    if (callback != null) {
		callback.accept(!object.isEmpty());
	    }
	    return (object.isEmpty() ? "" : object);
	}
    }

    @Override
    public String fromString(String string) {
	return (string == null || string.equals("")) ? "" : string;
    }
}
