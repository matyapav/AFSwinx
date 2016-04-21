using ShowcaseWP.Common;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading.Tasks;
using Windows.Foundation;
using Windows.Foundation.Collections;
using Windows.Graphics.Display;
using Windows.UI.Popups;
using Windows.UI.ViewManagement;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Data;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Navigation;
using AFWinPhone;
using AFWinPhone.builders.widgets;
using AFWinPhone.components;
using AFWinPhone.components.types;
using AFWinPhone.enums;
using AFWinPhone.utils;
using ShowcaseWP.skins;
using ShowcaseWP.utils;

// The Basic Page item template is documented at http://go.microsoft.com/fwlink/?LinkID=390556

namespace ShowcaseWP.pages
{
    /// <summary>
    /// An empty page that can be used on its own or navigated to within a Frame.
    /// </summary>
    public sealed partial class LoginPage : Page
    {
        private NavigationHelper navigationHelper;
        private ObservableDictionary defaultViewModel = new ObservableDictionary();

        public LoginPage()
        {
            this.InitializeComponent();

            this.navigationHelper = new NavigationHelper(this);
            this.navigationHelper.LoadState += this.NavigationHelper_LoadState;
            this.navigationHelper.SaveState += this.NavigationHelper_SaveState;

            var progressbar = StatusBar.GetForCurrentView().ProgressIndicator;
            progressbar.Text = Localization.translate("please.wait");
            progressbar.ShowAsync();

            var commandBar = new CommandBar();
            var cz = new AppBarButton();
            var en = new AppBarButton();
            cz.Label = "Čeština";
            cz.Click += async (sender, args) =>
            {
                await progressbar.ShowAsync();
                var previousCacheSize = Frame.CacheSize;
                Frame.CacheSize = 0;
                Localization.changeLanguage("cs");
                await Task.Delay(100);
                Frame.Navigate(GetType());
                if (Frame.CanGoBack)
                {
                    Frame.BackStack.RemoveAt(0);
                }
                Frame.CacheSize = previousCacheSize;
                await progressbar.HideAsync();
            };
            en.Label = "English";
            en.Click += async (sender, args) =>
            {
                await progressbar.ShowAsync();
                var previousCacheSize = Frame.CacheSize;
                Frame.CacheSize = 0;
                Localization.changeLanguage("en");
                await Task.Delay(100);
                Frame.Navigate(GetType());
                if (Frame.CanGoBack)
                {
                    Frame.BackStack.RemoveAt(0);
                }
                Frame.CacheSize = previousCacheSize;
                await progressbar.HideAsync();
            };
            commandBar.SecondaryCommands.Add(cz);
            commandBar.SecondaryCommands.Add(en);
            BottomAppBar = commandBar;

            try
            {
                var login =
                    (AFForm)
                        AfWindowsPhone.getInstance()
                            .getFormBuilder()
                            .initBuilder(ShowcaseConstants.LOGIN_FORM, "connection.xml",
                                ShowcaseConstants.LOGIN_FORM_CONNECTION_KEY)
                            .setSkin(new LoginFormSkin())
                            .createComponent();
                ContentRoot.Children.Add(login.getView());
            }
            catch (Exception ex)
            {
                ShowcaseUtils.showComponentBuildFailedDialog();
                Debug.WriteLine(ex.StackTrace);
                progressbar.HideAsync();
                return;
            }
            var loginBtn = new Button();
            loginBtn.Content = Localization.translate("btn.login");
            loginBtn.HorizontalAlignment = HorizontalAlignment.Right;
            loginBtn.Click += LoginBtn_Click;
            ContentRoot.Children.Add(loginBtn);

            progressbar.HideAsync();
        }

        /// <summary>
        /// Logs user in.
        /// </summary>
        /// <param name="form">login form</param>
        private void doLogin(AFForm form)
        {
            var usernameField = form.getFieldById("username");
            var passwordField = form.getFieldById("password");
            if (usernameField != null && passwordField != null)
            {
                //save user to shared preferences
                var username = (string)form.getDataFromFieldWithId("username");
                var password = (string)form.getDataFromFieldWithId("password");
                ShowcaseUtils.setUserInPreferences(username, password);

                //change content
                Frame.Navigate(typeof(WelcomePage));
                if (Frame.CanGoBack)
                {
                    Frame.BackStack.RemoveAt(0);
                }
                
            }
            //success
        }

        /// <summary>
        /// Handles login button click. Logs user in.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private async void LoginBtn_Click(object sender, RoutedEventArgs e)
        {
           
            var form = (AFForm)AfWindowsPhone.getInstance().getCreatedComponentByName(ShowcaseConstants.LOGIN_FORM);
            if(form != null) { 
                var progressbar = StatusBar.GetForCurrentView().ProgressIndicator;
                progressbar.Text = Localization.translate("please.wait");
                try
                {
                    await progressbar.ShowAsync();
                    if(await form.sendData())
                    { 
                        doLogin(form);
                    }
                    await progressbar.HideAsync();
                }
                catch (Exception ex)
                {
                    await progressbar.HideAsync();
                    await new MessageDialog(Localization.translate("login.failed")).ShowAsync();
                    Debug.WriteLine(ex.StackTrace);
                }
                
            }
        }


        /// <summary>
        /// Gets the <see cref="NavigationHelper"/> associated with this <see cref="Page"/>.
        /// </summary>
        public NavigationHelper NavigationHelper
        {
            get { return this.navigationHelper; }
        }

        /// <summary>
        /// Gets the view model for this <see cref="Page"/>.
        /// This can be changed to a strongly typed view model.
        /// </summary>
        public ObservableDictionary DefaultViewModel
        {
            get { return this.defaultViewModel; }
        }

        /// <summary>
        /// Populates the page with content passed during navigation.  Any saved state is also
        /// provided when recreating a page from a prior session.
        /// </summary>
        /// <param name="sender">
        /// The source of the event; typically <see cref="NavigationHelper"/>
        /// </param>
        /// <param name="e">Event data that provides both the navigation parameter passed to
        /// <see cref="Frame.Navigate(Type, Object)"/> when this page was initially requested and
        /// a dictionary of state preserved by this page during an earlier
        /// session.  The state will be null the first time a page is visited.</param>
        private void NavigationHelper_LoadState(object sender, LoadStateEventArgs e)
        {
        }

        /// <summary>
        /// Preserves state associated with this page in case the application is suspended or the
        /// page is discarded from the navigation cache.  Values must conform to the serialization
        /// requirements of <see cref="SuspensionManager.SessionState"/>.
        /// </summary>
        /// <param name="sender">The source of the event; typically <see cref="NavigationHelper"/></param>
        /// <param name="e">Event data that provides an empty dictionary to be populated with
        /// serializable state.</param>
        private void NavigationHelper_SaveState(object sender, SaveStateEventArgs e)
        {
        }

        #region NavigationHelper registration

        /// <summary>
        /// The methods provided in this section are simply used to allow
        /// NavigationHelper to respond to the page's navigation methods.
        /// <para>
        /// Page specific logic should be placed in event handlers for the  
        /// <see cref="NavigationHelper.LoadState"/>
        /// and <see cref="NavigationHelper.SaveState"/>.
        /// The navigation parameter is available in the LoadState method 
        /// in addition to page state preserved during an earlier session.
        /// </para>
        /// </summary>
        /// <param name="e">Provides data for navigation methods and event
        /// handlers that cannot cancel the navigation request.</param>
        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
            this.navigationHelper.OnNavigatedTo(e);
        }

        protected override void OnNavigatedFrom(NavigationEventArgs e)
        {
            this.navigationHelper.OnNavigatedFrom(e);
        }

        #endregion
    }
}
