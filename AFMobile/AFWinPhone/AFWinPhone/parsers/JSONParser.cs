using System;
using AFWinPhone.components.parts;
using Windows.Data.Json;

namespace AFWinPhone.parsers
{
    public interface JSONParser
    {
        AFClassInfo parse(String jsonStrToBeParsed, bool parsingInnerClass);
    }
}
