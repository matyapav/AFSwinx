using AFWinPhone.enums;

namespace AFWinPhone.components.parts
{
    public class TopLevelLayout
    {
        private LayoutDefinitions layoutDefinition;
        private LayoutOrientation layoutOrientation;

        public TopLevelLayout()
        {
        }

        public LayoutDefinitions getLayoutDefinition()
        {
            return this.layoutDefinition;
        }

        public void setLayoutDefinition(LayoutDefinitions layoutDefinition)
        {
            this.layoutDefinition = layoutDefinition;
        }

        public LayoutOrientation getLayoutOrientation()
        {
            return this.layoutOrientation;
        }

        public void setLayoutOrientation(LayoutOrientation layoutOrientation)
        {
            this.layoutOrientation = layoutOrientation;
        }

    }
}
