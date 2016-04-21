using AFWinPhone.builders.skins;

namespace ShowcaseWP.skins
{
    /// <summary>
    /// Defines skin for absence management components
    /// </summary>
    internal class AbsenceManagementSkin : DefaultSkin
    {
        public override int getLabelWidth()
        {
            return 75;
        }

        public override int getInputWidth()
        {
            return 120;
        }

        public override int getLabelFontSize()
        {
            return 18;
        }

        public override int getFieldFontSize()
        {
            return 18;
        }

        public override bool isListItemNameLabelVisible()
        {
            return false;
        }
    }
}