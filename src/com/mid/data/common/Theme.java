package com.mid.data.common;

/**
 * @author
 */
public enum Theme {

//	DARK("dark-theme", "Кора", "/resources/theme/dark-theme.css"),
//	LIGHT("light-theme", "Ок", "/resources/theme/light-theme.css"),
    BLUE("fiolet-theme", "Фиолетовый", "/resources/theme/fiolet-theme.css"),
    RED("red-theme", "Кизил", "/resources/theme/red-theme.css"),;

    private final String identifier;
    private final String name;
    private final String location;

    private Theme(String identifier, String name, String location) {
	this.identifier = identifier;
	this.name = name;
	this.location = location;
    }

    public String getLocation() {
	return location;
    }

    public String getName() {
	return name;
    }

    public String getIdentifier() {
	return identifier;
    }

    @Override
    public String toString() {
	return name;
    }

    public static Theme getTheme(String identifier) {
	for (Theme theme : values()) {
	    if (theme.getIdentifier().equals(identifier)) {
		return theme;
	    }
	}

	return BLUE;
    }
}
