using Windows.UI;
using Windows.UI.Xaml;
using AFWinPhone.builders.skins;

namespace ShowcaseWP.skins
{
    internal class MyAbsencesSkin : DefaultSkin
    {
        public override float getListBorderWidth()
        {
            return 0;
        }

        public override Color getListItemTextColor()
        {
            return Colors.Chocolate;
        }

        public override Color getListItemNameColor()
        {
            return Colors.Chocolate;
        }

        public override int getListItemNameSize()
        {
            return 28;
        }

        public override int getListItemsTextSize()
        {
            return 12;
        }

        public override int getListHeight()
        {
            return -1; //see vertical alignment
        }

        public override VerticalAlignment getListVerticalAlignment()
        {
            return VerticalAlignment.Top;
        }

        public override Color getListItemBackgroundColor()
        {
            return Colors.Azure;
        }
    }
}