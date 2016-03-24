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
using Windows.UI.ViewManagement;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Data;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Navigation;
using AFWinPhone;
using AFWinPhone.components;
using AFWinPhone.components.types;
using AFWinPhone.enums;
using AFWinPhone.utils;
using ShowcaseWP.utils;

// The Basic Page item template is documented at http://go.microsoft.com/fwlink/?LinkID=390556

namespace ShowcaseWP.pages
{
    /// <summary>
    /// An empty page that can be used on its own or navigated to within a Frame.
    /// </summary>
    public sealed partial class WelcomePage : Page
    {
        private NavigationHelper navigationHelper;
        private ObservableDictionary defaultViewModel = new ObservableDictionary();

        public WelcomePage()
        {
            this.InitializeComponent();

            this.navigationHelper = new NavigationHelper(this);
            this.navigationHelper.LoadState += this.NavigationHelper_LoadState;
            this.navigationHelper.SaveState += this.NavigationHelper_SaveState;


            //language changer - start
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
                Localization.changeLanguage(SupportedLanguages.CZ);
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
                Localization.changeLanguage(SupportedLanguages.EN);
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
            //language changer end

            var welcomeUser = new TextBlock();
            welcomeUser.Text = Localization.translate("welcome.msg") + ":" + ShowcaseUtils.getUserLogin();
            welcomeUser.FontSize = 22;

            var profile = new Button();
            profile.Content = Localization.translate("menu.profile");
            profile.HorizontalAlignment = HorizontalAlignment.Stretch;
            profile.Click += (sender, args) => { Frame.Navigate(typeof(ProfilePage)); };

            var logout = new Button();
            logout.Content = Localization.translate("btn.logout");
            logout.HorizontalAlignment = HorizontalAlignment.Stretch;
            logout.Click += (sender, args) =>
            {
                ShowcaseUtils.clearUserInPreferences();
                Frame.Navigate(typeof(LoginPage));
                Frame.BackStack.Clear();
            };

            var absenceManagement = new Button();
            absenceManagement.Content = Localization.translate("menu.absenceManagement");
            absenceManagement.HorizontalAlignment = HorizontalAlignment.Stretch;
            absenceManagement.Click += (sender, args) => { Frame.Navigate(typeof(AbsenceManagementPage)); };

            var supportedCountries = new Button();
            supportedCountries.Content = Localization.translate("menu.countries");
            supportedCountries.HorizontalAlignment = HorizontalAlignment.Stretch;
            supportedCountries.Click += (sender, args) => { Frame.Navigate(typeof(SupportedCountriesPage)); };

            var myAbsences = new Button();
            myAbsences.Content = Localization.translate("menu.myAbsences");
            myAbsences.HorizontalAlignment = HorizontalAlignment.Stretch;
            myAbsences.Click += (sender, args) => { Frame.Navigate(typeof(MyAbsencesPage)); };

            var createAbsence = new Button();
            createAbsence.Content = Localization.translate("menu.createAbsence");
            createAbsence.HorizontalAlignment = HorizontalAlignment.Stretch;
            createAbsence.Click += (sender, args) => { Frame.Navigate(typeof(CreateAbsencePage)); };

            var absenceTypes = new Button();
            absenceTypes.Content = Localization.translate("menu.absenceTypes");
            absenceTypes.HorizontalAlignment = HorizontalAlignment.Stretch;
            absenceTypes.Click += (sender, args) => { showChooseCountryDialogAndNavigate(); };

            ContentRoot.Children.Add(welcomeUser);
            ContentRoot.Children.Add(supportedCountries);
            ContentRoot.Children.Add(profile);
            ContentRoot.Children.Add(myAbsences);
            ContentRoot.Children.Add(absenceManagement);
            ContentRoot.Children.Add(createAbsence);
            ContentRoot.Children.Add(absenceTypes);
            ContentRoot.Children.Add(logout);

            progressbar.HideAsync();
        }

        private async void showChooseCountryDialogAndNavigate()
        {
            var progressbar = StatusBar.GetForCurrentView().ProgressIndicator;
            progressbar.Text = Localization.translate("please.wait");
            await progressbar.ShowAsync();
            var dialog = new ContentDialog
            {
                Title = Localization.translate("choose.country"),
                RequestedTheme = ElementTheme.Dark,
                MaxWidth = ActualWidth // Required for Mobile!
            };

            // Setup Content
            var panel = new StackPanel();
            panel.Orientation = Orientation.Horizontal;
            AFForm chooseCountryForm = null;
            try
            {
                chooseCountryForm =
                    (AFForm)
                        AfWindowsPhone.getInstance()
                            .getFormBuilder()
                            .initBuilder(ShowcaseConstants.CHOOSE_COUNTRY_FORM, "connection.xml",
                                ShowcaseConstants.CHOOSE_COUNTRY_FORM_CONNECTION_KEY, ShowcaseUtils.getUserCredentials())
                            .createComponent();
                panel.Children.Add(chooseCountryForm.getView());
            }
            catch (Exception ex)
            {
                ShowcaseUtils.showComponentBuildFailedDialog();
                Debug.WriteLine(ex.StackTrace);
                await progressbar.HideAsync();
                return;
            }

            dialog.Content = panel;

            // Add Buttons
            dialog.PrimaryButtonText = "OK";
            dialog.IsPrimaryButtonEnabled = true;
            dialog.PrimaryButtonClick += delegate
            {
                if (chooseCountryForm.validateData())
                {
                    try
                    {
                        var country =
                            chooseCountryForm.reserialize()
                                .getPropertiesAndValues()[
                                    ShowcaseConstants.COUNTRY_KEY];
                        dialog.Closing +=
                            delegate (ContentDialog sender, ContentDialogClosingEventArgs args) { args.Cancel = false; };
                        Frame.Navigate(typeof(AbsenceTypesPage), country);
                    }
                    catch (Exception e)
                    {
                        Debug.WriteLine(e.StackTrace);
                    }
                }
                else
                {
                    dialog.Closing +=
                        delegate (ContentDialog sender, ContentDialogClosingEventArgs args) { args.Cancel = true; };
                }
            };

            dialog.SecondaryButtonText = "Cancel";
            dialog.SecondaryButtonClick +=
                delegate
                {
                    dialog.Closing +=
                        delegate (ContentDialog sender, ContentDialogClosingEventArgs args) { args.Cancel = false; };
                };

            // Show Dialog
            await progressbar.HideAsync();
            await dialog.ShowAsync();
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
