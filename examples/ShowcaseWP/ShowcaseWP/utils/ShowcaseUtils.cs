using System;
using System.Collections.Generic;
using Windows.Storage;
using Windows.UI.Popups;
using AFWinPhone.utils;

namespace ShowcaseWP.utils
{
    /// <summary>
    /// Utils used in showcase application.
    /// </summary>
    internal class ShowcaseUtils
    {
        /// <summary>
        /// Sets user credentials in application preferences.
        /// </summary>
        /// <param name="username">user's username</param>
        /// <param name="password">user's password</param>
        public static void setUserInPreferences(string username, string password)
        {
            var localSettings = ApplicationData.Current.LocalSettings;
            localSettings.Values["username"] = username;
            localSettings.Values["password"] = password;
        }

        /// <summary>
        /// Removes user credentials from application preferences
        /// </summary>
        public static void clearUserInPreferences()
        {
            var localSettings = ApplicationData.Current.LocalSettings;
            localSettings.Values.Remove("username");
            localSettings.Values.Remove("password");
        }

        /// <summary>
        /// Gets user credentials from applicaiton preferences
        /// </summary>
        /// <returns>dictionary with user credentials</returns>
        public static Dictionary<string, string> getUserCredentials()
        {
            var localSettings = ApplicationData.Current.LocalSettings;
            var username = (string) localSettings.Values["username"];
            var password = (string) localSettings.Values["password"];
            if (username != null && password != null)
            {
                var result = new Dictionary<string, string>();
                result.Add("username", username);
                result.Add("password", password);
                return result;
            }
            return null;
        }

        /// <summary>
        /// Gets username of user from application preferences
        /// </summary>
        /// <returns>user's login</returns>
        public static string getUserLogin()
        {
            var localSettings = ApplicationData.Current.LocalSettings;
            var username = (string) localSettings.Values["username"];
            return username;
        }

        /// <summary>
        /// Shows dialog with build failed message
        /// </summary>
        public static async void showComponentBuildFailedDialog()
        {
            await new MessageDialog(Localization.translate("build.failed")).ShowAsync();
        }
    }
}