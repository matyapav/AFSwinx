﻿using ShowcaseWP.Common;
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
    public sealed partial class CreateAbsencePage : Page
    {
        private NavigationHelper navigationHelper;
        private ObservableDictionary defaultViewModel = new ObservableDictionary();

        public CreateAbsencePage()
        {
            this.InitializeComponent();

            this.navigationHelper = new NavigationHelper(this);
            this.navigationHelper.LoadState += this.NavigationHelper_LoadState;
            this.navigationHelper.SaveState += this.NavigationHelper_SaveState;

            var progressbar = StatusBar.GetForCurrentView().ProgressIndicator;
            progressbar.Text = Localization.translate("please.wait");
            progressbar.ShowAsync();

            var userCredentials = ShowcaseUtils.getUserCredentials();
            userCredentials.Add("user", userCredentials["username"]);
            try
            {
                AFForm ćreateAbsenceForm =
                    (AFForm)
                        AfWindowsPhone.getInstance()
                            .getFormBuilder()
                            .initBuilder(ShowcaseConstants.ABSENCE_ADD_FORM, "connection.xml",
                                ShowcaseConstants.ABSENCE_ADD_FORM_CONNECTION_KEY, userCredentials)
                            .createComponent();
                ContentRoot.Children.Add(ćreateAbsenceForm.getView());
            }
            catch (Exception ex)
            {
                ShowcaseUtils.showComponentBuildFailedDialog();
                Debug.WriteLine(ex.StackTrace);
                progressbar.HideAsync();
                return;
            }
            var addButton = new Button();
            addButton.Content = Localization.translate("button.add");
            addButton.HorizontalAlignment = HorizontalAlignment.Center;
            addButton.Click += AddButtonOnClick;

            ContentRoot.Children.Add(addButton);

            progressbar.HideAsync();
        }

        /// <summary>
        /// Handles add absence button click. Adds new absence.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="routedEventArgs"></param>
        private async void AddButtonOnClick(object sender, RoutedEventArgs routedEventArgs)
        {
            AFForm form =
                (AFForm)AfWindowsPhone.getInstance().getCreatedComponentByName(ShowcaseConstants.ABSENCE_ADD_FORM);
            if(form != null) { 
            try
            {
                var progressbar = StatusBar.GetForCurrentView().ProgressIndicator;
                progressbar.Text = Localization.translate("please.wait");
                await progressbar.ShowAsync();
                if (await form.sendData())
                {
                    await new MessageDialog(Localization.translate("add.success")).ShowAsync();
                    await progressbar.HideAsync();
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
                await new MessageDialog(Localization.translate("add.failed")).ShowAsync();
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
