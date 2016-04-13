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
using ShowcaseWP.skins;
using ShowcaseWP.utils;

// The Basic Page item template is documented at http://go.microsoft.com/fwlink/?LinkID=390556

namespace ShowcaseWP.pages
{
    /// <summary>
    /// An empty page that can be used on its own or navigated to within a Frame.
    /// </summary>
    public sealed partial class AbsenceTypesPage : Page
    {
        private NavigationHelper navigationHelper;
        private int selectedCountry = -1;

        public AbsenceTypesPage()
        {
            this.InitializeComponent();

            this.navigationHelper = new NavigationHelper(this);
            this.navigationHelper.LoadState += this.NavigationHelper_LoadState;
            this.navigationHelper.SaveState += this.NavigationHelper_SaveState;

            this.NavigationCacheMode = NavigationCacheMode.Disabled;
        }

        /// <summary>
        /// Gets the <see cref="NavigationHelper"/> associated with this <see cref="Page"/>.
        /// </summary>
        public NavigationHelper NavigationHelper
        {
            get { return this.navigationHelper; }
        }



        private void OnItemClick(object sender, ItemClickEventArgs e)
        {
                AFForm absenceTypeForm =
                    (AFForm)AfWindowsPhone.getInstance().getCreatedComponentByName(ShowcaseConstants.ABSENCE_TYPE_FORM);
                AFList absenceTypesList =
                    (AFList)AfWindowsPhone.getInstance().getCreatedComponentByName(ShowcaseConstants.ABSENCE_TYPE_LIST);
                if(absenceTypeForm != null && absenceTypesList != null) { 
                int position = absenceTypesList.getListView().Items.IndexOf(e.ClickedItem);
                absenceTypeForm.insertData(absenceTypesList.getDataFromItemOnPosition(position));
                absenceTypeForm.hideErrors();
                AbsenceTypePagePivot.SelectedIndex = 1;
            }
        }


        private void Clear_Click(object sender, RoutedEventArgs e)
        {
            AFForm form =
                    (AFForm)AfWindowsPhone.getInstance().getCreatedComponentByName(ShowcaseConstants.ABSENCE_TYPE_FORM);
            if(form != null) { 
                form.clearData();
                form.hideErrors();
            }
        }

        private void Reset_Click(object sender, RoutedEventArgs e)
        {

            AFForm form =
                    (AFForm)AfWindowsPhone.getInstance().getCreatedComponentByName(ShowcaseConstants.ABSENCE_TYPE_FORM);
            if (form != null)
            {
                form.resetData();
                form.hideErrors();
            }
        }

        private async void Perform_Click(object sender, RoutedEventArgs e)
        {

            AFForm form =
               (AFForm)AfWindowsPhone.getInstance().getCreatedComponentByName(ShowcaseConstants.ABSENCE_TYPE_FORM);
            if (form != null)
            {
                try
                {
                    var progressbar = StatusBar.GetForCurrentView().ProgressIndicator;
                    progressbar.Text = Localization.translate("please.wait");
                    await progressbar.ShowAsync();
                    if (await form.sendData())
                    {
                        await progressbar.HideAsync();
                        await new MessageDialog(Localization.translate("addOrUpdate.success")).ShowAsync();
                        AbsenceTypePagePivot.SelectedIndex = 0;
                        //refresh page
                        AbsenceTypesFormPanel.Children.Clear();
                        AbsenceTypesListPanel.Children.Clear();
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
        ///     Populates the page with content passed during navigation.  Any saved state is also
        ///     provided when recreating a page from a prior session.
        /// </summary>
        /// <param name="sender">
        ///     The source of the event; typically <see cref="NavigationHelper" />
        /// </param>
        /// <param name="e">
        ///     Event data that provides both the navigation parameter passed to
        ///     <see cref="Frame.Navigate(Type, Object)" /> when this page was initially requested and
        ///     a dictionary of state preserved by this page during an earlier
        ///     session.  The state will be null the first time a page is visited.
        /// </param>
        private async void NavigationHelper_LoadState(object sender, LoadStateEventArgs e)
        {
            //show loading indicator
            var progressbar = StatusBar.GetForCurrentView().ProgressIndicator;
            progressbar.Text = Localization.translate("please.wait");
            await progressbar.ShowAsync();

            var countryId = e.NavigationParameter as string;

            if (!string.IsNullOrWhiteSpace(countryId))
            {
                try
                {
                    selectedCountry = Convert.ToInt32(countryId);
                    var connectionParameters = ShowcaseUtils.getUserCredentials();
                    connectionParameters.Add(ShowcaseConstants.ID_KEY, selectedCountry.ToString());
                    try
                    {
                        AFList absenceTypesList = (AFList)AfWindowsPhone.getInstance()
                            .getListBuilder()
                            .initBuilder(ShowcaseConstants.ABSENCE_TYPE_LIST, "connection.xml",
                                ShowcaseConstants.ABSENCE_TYPE_LIST_CONNECTION_KEY, connectionParameters)
                            .setSkin(new MyAbsencesSkin())
                            .createComponent();
                        AbsenceTypesListPanel.Children.Add(absenceTypesList.getView());

                        AFForm absenceTypeForm = (AFForm)AfWindowsPhone.getInstance()
                            .getFormBuilder()
                            .initBuilder(ShowcaseConstants.ABSENCE_TYPE_FORM, "connection.xml",
                                ShowcaseConstants.ABSENCE_TYPE_FORM_CONNECTION_KEY, connectionParameters)
                            .createComponent();
                        AbsenceTypesFormPanel.Children.Add(absenceTypeForm.getView());

                        absenceTypesList.getListView().IsItemClickEnabled = true;
                        absenceTypesList.getListView().ItemClick += OnItemClick;
                    }
                    catch (Exception exception)
                    {
                        ShowcaseUtils.showComponentBuildFailedDialog();
                        Debug.WriteLine(exception.StackTrace);
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
                    AbsenceTypesFormPanel.Children.Add(buttons);


                    Debug.WriteLine(selectedCountry);
                }
                catch (Exception ex)
                {
                    Debug.WriteLine("Cannot parse string to integer");
                    Debug.WriteLine(ex.StackTrace);
                }
            }
            else
            {
                Debug.WriteLine("Parameter was not passed succesfully (or is null or empty) ");
            }
            await progressbar.HideAsync();
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
