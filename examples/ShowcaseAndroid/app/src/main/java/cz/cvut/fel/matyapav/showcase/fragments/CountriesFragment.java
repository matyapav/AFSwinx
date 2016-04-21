package cz.cvut.fel.matyapav.showcase.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.HashMap;

import cz.cvut.fel.matyapav.afandroid.AFAndroid;
import cz.cvut.fel.matyapav.afandroid.builders.FormBuilder;
import cz.cvut.fel.matyapav.afandroid.builders.ListBuilder;
import cz.cvut.fel.matyapav.afandroid.components.types.AFForm;
import cz.cvut.fel.matyapav.afandroid.components.types.AFList;
import cz.cvut.fel.matyapav.showcase.R;

import cz.cvut.fel.matyapav.showcase.skins.CountryFormSkin;
import cz.cvut.fel.matyapav.showcase.skins.ListSkin;
import cz.cvut.fel.matyapav.showcase.utils.ShowCaseUtils;
import cz.cvut.fel.matyapav.showcase.utils.ShowcaseConstants;


/**
 * Supported countries screen
 *
 * @author Pavel Matyáš (matyapav@fel.cvut.cz)
 *
 * @since 1.0.0.
 */
public class CountriesFragment extends Fragment {

    /**
     * On perform button click handler. Adds or updates country
     */
    private View.OnClickListener onCountryPerformListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AFForm form = (AFForm) AFAndroid.getInstance().getCreateComponentByName(ShowcaseConstants.COUNTRY_FORM);
            if (form != null) {
                try {
                    if(form.sendData()){
                        Toast.makeText(getActivity(), "Add or update complete", Toast.LENGTH_SHORT).show();
                        ShowCaseUtils.refreshCurrentFragment(getActivity());
                    }
                } catch (Exception e) {
                    //error while sending
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * On reset button click handler. Resets country form.
     */
    private View.OnClickListener onCountryResetListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AFForm form = (AFForm) AFAndroid.getInstance().getCreateComponentByName(ShowcaseConstants.COUNTRY_FORM);
            if(form != null) {
                form.resetData();
                form.hideErrors();
            }
        }
    };

    /**
     * On clear button click handler. Clears country form.
     */
    private View.OnClickListener onCountryClearListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AFForm form = (AFForm) AFAndroid.getInstance().getCreateComponentByName(ShowcaseConstants.COUNTRY_FORM);
            if(form != null) {
                form.clearData();
                form.hideErrors();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.countries_fragment_layout, container, false);
        //get layouts where we want to put components
        LinearLayout countriesTableLayout = (LinearLayout) root.findViewById(R.id.countriesTable);
        LinearLayout countriesFormLayout = (LinearLayout) root.findViewById(R.id.countriesForm);

        //initialize builders
        HashMap<String, String> securityConstrains = ShowCaseUtils.getUserCredentials(getActivity());

        ListBuilder listBuilder = AFAndroid.getInstance().getListBuilder().initBuilder(getActivity(),
                ShowcaseConstants.COUNTRY_LIST, getResources().openRawResource(R.raw.connection),
                ShowcaseConstants.COUNTRY_LIST_CONNECTION_KEY, securityConstrains).setSkin(new ListSkin(getContext()));

        FormBuilder formBuilder = AFAndroid.getInstance().getFormBuilder().initBuilder(getActivity(),
                ShowcaseConstants.COUNTRY_FORM, getResources().openRawResource(R.raw.connection),
                ShowcaseConstants.COUNTRY_FORM_CONNECTION_KEY, securityConstrains).setSkin(new CountryFormSkin(getContext()));


        //create and insert form
        try {
            final AFList list = listBuilder.createComponent();
            countriesTableLayout.addView(list.getView());

            AFForm form = formBuilder.createComponent();

            Button perform =(Button) root.findViewById(R.id.countriesBtnAdd);
            perform.setOnClickListener(onCountryPerformListener);
            Button reset = (Button) root.findViewById(R.id.countriesBtnReset);
            reset.setOnClickListener(onCountryResetListener);
            countriesFormLayout.addView(form.getView());
            Button clear = (Button) root.findViewById(R.id.countriesBtnClear);
            clear.setOnClickListener(onCountryClearListener);
        } catch (Exception e) {
            ShowCaseUtils.showBuildingFailedDialog(getActivity(), e);
            e.printStackTrace();
        }

        //connect list and form
        final AFList countryList = (AFList) AFAndroid.getInstance().getCreateComponentByName(ShowcaseConstants.COUNTRY_LIST);
        final AFForm countryForm = (AFForm) AFAndroid.getInstance().getCreateComponentByName(ShowcaseConstants.COUNTRY_FORM);

        if(countryList != null && countryForm != null) {
            countryList.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    countryForm.insertData(countryList.getDataFromItemOnPosition(position));
                    countryForm.hideErrors();
                }
            });
        }

        return root;
    }

}
