using System;
using AFWinPhone.components.parts;
using Windows.Data.Json;

namespace AFWinPhone.parsers
{
    /// <summary>
    /// Interface for JSON parsers of components information.
    /// </summary>
    public interface JSONParser
    {
        /// <summary>
        /// arses given string in JSON format. Parsing should be different if parsing inner class of component.
        /// </summary>
        /// <param name="jsonStrToBeParsed">string to be parsed</param>
        /// <param name="parsingInnerClass">true if parsing inner class of component</param>
        /// <returns>information wrapped in object</returns>
        AFClassInfo parse(String jsonStrToBeParsed, bool parsingInnerClass);
    }
}
