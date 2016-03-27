package cz.cvut.fel.matyapav.afandroid.parsers;

import com.tomscz.afrest.rest.dto.AFClassInfo;

/**
 * Created by Pavel on 17.12.2015.
 */
public interface JSONParser {

    AFClassInfo parse(String jsonStrToBeParsed, boolean parsingInnerClass);
}
