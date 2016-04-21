using Windows.UI;
using Windows.UI.Xaml;
using AFWinPhone.builders.skins;

namespace ShowcaseWP.skins
{
    /// <summary>
    /// Defines skin for login form
    /// </summary>
    internal class LoginFormSkin : DefaultSkin
    {
        public override int getLabelWidth()
        {
            return 200;
        }
    }
}