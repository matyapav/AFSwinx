package cz.cvut.fel.matyapav.afandroid.parsers;

import org.json.JSONObject;

import cz.cvut.fel.matyapav.afandroid.components.parts.ClassDefinition;

/**
 * Created by Pavel on 17.12.2015.
 */
public interface JSONParser {

    ClassDefinition parse(String jsonStrToBeParsed, boolean parsingInnerClass);
}
