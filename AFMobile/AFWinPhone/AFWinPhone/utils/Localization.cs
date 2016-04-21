using AFWinPhone.enums;
using System;
using System.Diagnostics;
using Windows.ApplicationModel.Resources;
using Windows.Globalization;

namespace AFWinPhone.utils
{
    /// <summary>
    /// This class is responsible for translations of strings. Gives user also ability to change language.
    /// </summary>
    public class Localization
    {
        private static String currentLanguage;

        /// <summary>
        /// Translates given resource. Looks for the texts in Strings/{lang folder}/Resources.resw file in the root package of application.
        /// </summary>
        /// <param name="resource">key to be translated</param>
        /// <returns>Translation if key was found in localization texts, otherwise key is not translated and returned.</returns>
        public static String translate(String resource)
        {
            try
            {
                ResourceLoader loader = ResourceLoader.GetForCurrentView();
                String editedResource = resource.Replace('.', '/');
                if(String.IsNullOrEmpty(loader.GetString(editedResource))){
                    Debug.WriteLine("Localization text " + resource + " not found");
                    return resource;
                }
                Debug.WriteLine("Localization for is " + loader.GetString(editedResource));
                return loader.GetString(editedResource);
            }
            catch (Exception e)
            {
                Debug.WriteLine(e.StackTrace);
                return resource;
            }
        }

        /// <summary>
        /// Changes language of application.
        /// </summary>
        /// <param name="lang">Shortcut of language. Examples: English = en, Czech = cs etc.</param>
        public static void changeLanguage(String lang)
        {
            ApplicationLanguages.PrimaryLanguageOverride = lang.ToLower();
            currentLanguage = lang;
        }

        public static String getCurrentLanguage()
        {
            return currentLanguage;
        }
    }
}
