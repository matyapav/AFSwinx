﻿using AFWindowsPhone.Common;
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
using AFWindowsPhone.builders.components.types;
using AFWindowsPhone.utils;

// The Basic Page item template is documented at http://go.microsoft.com/fwlink/?LinkID=390556

namespace AFWindowsPhone.showcase.pages
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

            AFList countryList =
                (AFList)
                    AFWindowsPhone.getInstance()
                        .getListBuilder()
                        .initBuilder(ShowcaseConstants.COUNTRY_LIST, "connection.xml",
                            ShowcaseConstants.COUNTRY_LIST_CONNECTION_KEY, ShowcaseUtils.getUserCredentials())
                        .createComponent();
            CountryListPanel.Children.Add(countryList.getView());
 
            AFForm countryForm = (AFForm) AFWindowsPhone.getInstance()
                .getFormBuilder()
                .initBuilder(ShowcaseConstants.COUNTRY_FORM, "connection.xml",
                    ShowcaseConstants.COUNTRY_FORM_CONNECTION_KEY, ShowcaseUtils.getUserCredentials()).createComponent();
            CountryFormPanel.Children.Add(countryForm.getView());

            Button perform = new Button();
            perform.Content = "Perform";
            perform.Click += Perform_Click;

            Button reset = new Button();
            reset.Content = "Reset";
            reset.Click += Reset_Click;

            Button clear = new Button();
            clear.Content = "Clear";
            clear.Click += Clear_Click;

            StackPanel buttons = new StackPanel();
            buttons.HorizontalAlignment = HorizontalAlignment.Center;
            buttons.Orientation = Orientation.Horizontal;
            buttons.Children.Add(perform);
            buttons.Children.Add(reset);
            buttons.Children.Add(clear);
            CountryFormPanel.Children.Add(buttons);

            countryList.getListView().IsItemClickEnabled = true;
            countryList.getListView().ItemClick += OnItemClick;
        }

        private void OnItemClick(object sender, ItemClickEventArgs itemClickEventArgs)
        {
            if (AFWindowsPhone.getInstance().getCreatedComponents().ContainsKey(ShowcaseConstants.COUNTRY_LIST) &&
                AFWindowsPhone.getInstance().getCreatedComponents().ContainsKey(ShowcaseConstants.COUNTRY_FORM))
            {
                AFForm countryForm = (AFForm) AFWindowsPhone.getInstance().getCreatedComponents()[ShowcaseConstants.COUNTRY_FORM];
                AFList countryList = (AFList) AFWindowsPhone.getInstance().getCreatedComponents()[ShowcaseConstants.COUNTRY_LIST];
                int position = countryList.getListView().Items.IndexOf(itemClickEventArgs.ClickedItem);
                countryForm.insertData(countryList.getDataFromItemOnPosition(position));
            }
          
            
        }

        private void Clear_Click(object sender, RoutedEventArgs e)
        {
            if (AFWindowsPhone.getInstance().getCreatedComponents().ContainsKey(ShowcaseConstants.COUNTRY_FORM))
            {
                AFForm form = (AFForm) AFWindowsPhone.getInstance().getCreatedComponents()[ShowcaseConstants.COUNTRY_FORM];
                form.clearData();
            };
        }

        private void Reset_Click(object sender, RoutedEventArgs e)
        {
            if (AFWindowsPhone.getInstance().getCreatedComponents().ContainsKey(ShowcaseConstants.COUNTRY_FORM))
            {
                AFForm form = (AFForm)AFWindowsPhone.getInstance().getCreatedComponents()[ShowcaseConstants.COUNTRY_FORM];
                form.resetData();
            };
        }

        private async void Perform_Click(object sender, RoutedEventArgs e)
        {
            if (AFWindowsPhone.getInstance().getCreatedComponents().ContainsKey(ShowcaseConstants.COUNTRY_FORM))
            {
                AFForm form = (AFForm)AFWindowsPhone.getInstance().getCreatedComponents()[ShowcaseConstants.COUNTRY_FORM];
                if (form.validateData()) { 
                try
                {
                    StatusBarProgressIndicator progressbar = StatusBar.GetForCurrentView().ProgressIndicator;
                    progressbar.Text = Localization.translate("please.wait");
                    await progressbar.ShowAsync();
                    await form.sendData();
                    await progressbar.HideAsync();
                    await new MessageDialog("Add or update was successfull").ShowAsync();
                    //refresh page
                    Frame.GoBack();
                    Frame.GoForward();
                } 
                catch (Exception ex)
                {
                    await new MessageDialog("Add or update failed").ShowAsync();
                    Debug.WriteLine(ex.StackTrace);
                }
                }
            };
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
