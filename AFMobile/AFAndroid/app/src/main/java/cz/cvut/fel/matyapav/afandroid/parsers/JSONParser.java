package cz.cvut.fel.matyapav.afandroid.parsers;

/**
 * Interface for JSON parsers of components information.
 *
 * @author Pavel Matyáš (matyapav@fel.cvut.cz)
 *
 * @since 1.0.0.
 */
public interface JSONParser<T> {

    /**
     * Parses given string in JSON format. Parsing should be different if parsing inner class of component.
     *
     * @param jsonStrToBeParsed string to be parsed
     * @param parsingInnerClass true if parsing inner class of component
     * @return information wrapped in some object
     */
    T parse(String jsonStrToBeParsed, boolean parsingInnerClass);
}
