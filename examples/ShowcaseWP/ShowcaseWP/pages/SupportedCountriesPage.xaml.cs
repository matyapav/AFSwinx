using ShowcaseWP.Common;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
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
using AFWinPhone.components;
using AFWinPhone.components.types;
using AFWinPhone.utils;
using ShowcaseWP.utils;

// The Basic Page item template is documented at http://go.microsoft.com/fwlink/?LinkID=390556

namespace ShowcaseWP.pages
{
    /// <summary>
    /// An empty page that can be used on its own or navigated to within a Frame.
    /// </summary>
    public sealed partial class SupportedCountriesPage : Page
    {
        private NavigationHelper navigationHelper;
        private ObservableDictionary defaultViewModel = new ObservableDictionary();

        public SupportedCountriesPage()
        {
            this.InitializeComponent();

            this.navigationHelper = new NavigationHelper(this);
            this.navigationHelper.LoadState += this.NavigationHelper_LoadState;
            this.navigationHelper.SaveState += this.NavigationHelper_SaveState;


            //show loading indicator
            var progressbar = StatusBar.GetForCurrentView().ProgressIndicator;
            progressbar.Text = Localization.translate("please.wait");
            progressbar.ShowAsync();

            try
            {
                var countryList =
                    (AFList)
                        AfWindowsPhone.getInstance()
                            .getListBuilder()
                            .initBuilder(ShowcaseConstants.COUNTRY_LIST, "connection.xml",
                                ShowcaseConstants.COUNTRY_LIST_CONNECTION_KEY, ShowcaseUtils.getUserCredentials())
                            .createComponent();
                CountryListPanel.Children.Add(countryList.getView());


                var countryForm = (AFForm)AfWindowsPhone.getInstance()
                    .getFormBuilder()
                    .initBuilder(ShowcaseConstants.COUNTRY_FORM, "connection.xml",
                        ShowcaseConstants.COUNTRY_FORM_CONNECTION_KEY, ShowcaseUtils.getUserCredentials())
                    .createComponent();
                CountryFormPanel.Children.Add(countryForm.getView());

                countryList.getListView().IsItemClickEnabled = true;
                countryList.getListView().ItemClick += OnItemClick;
            }
            catch (Exception ex)
            {
                ShowcaseUtils.showComponentBuildFailedDialog();
                Debug.WriteLine(ex.StackTrace);
                progressbar.HideAsync();
                return;
            }
            var perform = new Button();
            perform.Content = Localization.translate("btn.perform");
            perform.Click += Perform_Click;

            var reset = new Button();
            reset.Content = Localization.translate("btn.reset");
            reset.Click += Reset_Click;

            var clear = new Button();
            clear.Content = Localization.translate("btn.clear");
            clear.Click += Clear_Click;

            var buttons = new StackPanel();
            buttons.HorizontalAlignment = HorizontalAlignment.Center;
            buttons.Orientation = Orientation.Horizontal;
            buttons.Children.Add(perform);
            buttons.Children.Add(reset);
            buttons.Children.Add(clear);
            CountryFormPanel.Children.Add(buttons);

            progressbar.HideAsync();
        }

        private void OnItemClick(object sender, ItemClickEventArgs itemClickEventArgs)
        {
            if (AfWindowsPhone.getInstance().getCreatedComponents().ContainsKey(ShowcaseConstants.COUNTRY_LIST) &&
                AfWindowsPhone.getInstance().getCreatedComponents().ContainsKey(ShowcaseConstants.COUNTRY_FORM))
            {
                var countryForm =
                    (AFForm)AfWindowsPhone.getInstance().getCreatedComponents()[ShowcaseConstants.COUNTRY_FORM];
                var countryList =
                    (AFList)AfWindowsPhone.getInstance().getCreatedComponents()[ShowcaseConstants.COUNTRY_LIST];
                var position = countryList.getListView().Items.IndexOf(itemClickEventArgs.ClickedItem);
                countryForm.insertData(countryList.getDataFromItemOnPosition(position));
                countryForm.hideErrors();
            }
        }

        private void Clear_Click(object sender, RoutedEventArgs e)
        {
            if (AfWindowsPhone.getInstance().getCreatedComponents().ContainsKey(ShowcaseConstants.COUNTRY_FORM))
            {
                var form = (AFForm)AfWindowsPhone.getInstance().getCreatedComponents()[ShowcaseConstants.COUNTRY_FORM];
                form.clearData();
                form.hideErrors();
            }
        }

        private void Reset_Click(object sender, RoutedEventArgs e)
        {
            if (AfWindowsPhone.getInstance().getCreatedComponents().ContainsKey(ShowcaseConstants.COUNTRY_FORM))
            {
                var form = (AFForm)AfWindowsPhone.getInstance().getCreatedComponents()[ShowcaseConstants.COUNTRY_FORM];
                form.resetData();
                form.hideErrors();
            }
        }

        private async void Perform_Click(object sender, RoutedEventArgs e)
        {
            if (AfWindowsPhone.getInstance().getCreatedComponents().ContainsKey(ShowcaseConstants.COUNTRY_FORM))
            {
                var form = (AFForm)AfWindowsPhone.getInstance().getCreatedComponents()[ShowcaseConstants.COUNTRY_FORM];
                    try
                    {
                        var progressbar = StatusBar.GetForCurrentView().ProgressIndicator;
                        progressbar.Text = Localization.translate("please.wait");
                        await progressbar.ShowAsync();
                        if (await form.sendData())
                        {
                            await progressbar.HideAsync();
                            await new MessageDialog(Localization.translate("addOrUpdate.success")).ShowAsync();
                            //refresh page
                            Frame.GoBack();
                            Frame.GoForward();
                        }
                        else
                        {
                            await progressbar.HideAsync();
                        }
                    }
                    catch (Exception ex)
                    {
                        await new MessageDialog(Localization.translate("addOrUpdate.failed")).ShowAsync();
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
