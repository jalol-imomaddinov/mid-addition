package com.mid.data.common;

import java.util.HashMap;

/**
 * @author
 */
public enum StateType {

    BEGIN(0),
    PAINT(1),
    STETUP(2),
    COMPLETE(3),
    NONE(4);

    private final static HashMap<Integer, StateType> STATE_MAP = new HashMap<>(5);

    static {
	STATE_MAP.put(BEGIN.getId(), BEGIN);
	STATE_MAP.put(PAINT.getId(), PAINT);
	STATE_MAP.put(STETUP.getId(), STETUP);
	STATE_MAP.put(COMPLETE.getId(), COMPLETE);
	STATE_MAP.put(NONE.getId(), NONE);
    }

    private final int id;

    private StateType(int id) {
	this.id = id;
    }

    public int getId() {
	return id;
    }

    public static StateType getStateById(int id) {
	return STATE_MAP.get(id);
    }
}
