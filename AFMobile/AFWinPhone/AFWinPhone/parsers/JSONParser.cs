using System;
using AFWinPhone.components.parts;
using Windows.Data.Json;

namespace AFWinPhone.parsers
{
    public interface JSONParser
    {
        ClassDefinition parse(String jsonStrToBeParsed, bool parsingInnerClass);
    }
}
