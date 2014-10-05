package com.tomscz.afswinx.layout.definitions;

public enum LabelPossition {
    LEFT("left"),
    RIGHT("right"),
    TOP("top"),
    BOT("bottom"),
    NONE("none");
    
    private final String name; 
    
    private LabelPossition(String name) {
        this.name = name;
    }
    
    public boolean equalsName(String otherName){
        return (otherName == null)? false:name.equals(otherName);
    }

    public String toString(){
       return name;
    }
}