using System.Text;

namespace AFWinPhone.components.parts.validators
{
    interface AFValidator
    {
        bool validate(AFField field, StringBuilder errorMsgs, AFValidationRule rule);
    }
}
