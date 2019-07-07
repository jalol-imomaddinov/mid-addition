package com.mid.util;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * @author
 */
public abstract class AbstractChangeListener<T> implements ChangeListener<T> {

    @Override
    public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
	changed(newValue);
    }

    public abstract void changed(T value);
}
