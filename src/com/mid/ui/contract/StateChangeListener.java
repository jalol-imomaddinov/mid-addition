package com.mid.ui.contract;

public interface StateChangeListener<T> {

    public void change(T state);

    public void refused(boolean refused);
}
