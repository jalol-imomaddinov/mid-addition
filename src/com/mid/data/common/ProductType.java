package com.mid.data.common;

/**
 * @author
 */
public enum ProductType {

    RAILING(0, "Перила"),
    BENCH(1, "Скамейка"),
    SWING(2, "Качели"),
    CAPRICORN(3, "Козерог");

    private final int id;
    private final String name;

    private ProductType(int id, String name) {
	this.id = id;
	this.name = name;
    }

    public int getId() {
	return id;
    }

    public String getName() {
	return name;
    }

    @Override
    public String toString() {
	return getName();
    }

    public static ProductType productById(int id) {
	for (ProductType value : values()) {
	    if (value.id == id) {
		return value;
	    }
	}
	return null;
    }
}
