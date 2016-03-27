package com.tomscz.afrest.commons;

/**
 * This {@link Enum} hold supported components, which can be generated as UI in Swing application.
 * Components is for example Form, Table, etc. For supported widget see {@link SupportedWidgets}.
 * 
 * @author Martin Tomasek (martin@toms-cz.com)
 * 
 * @since 1.0.0.
 */
public enum SupportedComponents {

    FORM("FORM"),TABLE("TABLE"),LIST("LIST");

    private final String name;

    private SupportedComponents(String name) {
        this.name = name;
    }

    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : name.equals(otherName);
    }

    public String toString() {
        return name;
    }
    
}
