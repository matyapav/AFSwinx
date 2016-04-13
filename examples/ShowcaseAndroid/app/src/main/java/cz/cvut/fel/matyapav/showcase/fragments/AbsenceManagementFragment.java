package cz.cvut.fel.matyapav.showcase.fragments;

import android.app.AlertDialog;
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
import java.io.InputStream;
import java.util.HashMap;

import cz.cvut.fel.matyapav.afandroid.AFAndroid;
import cz.cvut.fel.matyapav.afandroid.builders.widgets.WidgetBuilderFactory;
import cz.cvut.fel.matyapav.afandroid.components.types.AFForm;
import cz.cvut.fel.matyapav.afandroid.components.types.AFList;
import cz.cvut.fel.matyapav.afandroid.utils.Localization;
import cz.cvut.fel.matyapav.showcase.R;
import cz.cvut.fel.matyapav.showcase.skins.AbsenceManagementFormSkin;
import cz.cvut.fel.matyapav.showcase.skins.AbsenceManagementListSkin;
import cz.cvut.fel.matyapav.showcase.utils.ShowCaseUtils;
import cz.cvut.fel.matyapav.showcase.utils.ShowcaseConstants;


/**
 * Created by Pavel on 16.02.2016.
 */
public class AbsenceManagementFragment extends Fragment {

    private View.OnClickListener onPerformButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AFForm form = (AFForm) AFAndroid.getInstance().getCreateComponentByName(ShowcaseConstants.ABSENCE_INSTANCE_EDIT_FORM);
            if(form != null){
                try {
                    if(form.sendData()) {
                        ShowCaseUtils.refreshCurrentFragment(getActivity());
                        Toast.makeText(getActivity(), Localization.translate("success.addOrUpdate"),
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    alertDialog.setTitle(Localization.translate("error.addOrUpdate"));
                    alertDialog.setMessage(Localization.translate("error.reason") + e.getMessage());
                    alertDialog.show();
                    e.printStackTrace();
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.absence_management_fragment, container, false);
        LinearLayout layout = (LinearLayout) root.findViewById(R.id.absenceManagementLayout);

        //get connection.xml as stream
        InputStream connectionResource = getResources().openRawResource(R.raw.connection);
        //build security constraints
        HashMap<String, String> securityConstrains = ShowCaseUtils.getUserCredentials(getActivity());

        try {
            AFList list = AFAndroid.getInstance().getListBuilder().initBuilder(getActivity(),
                    ShowcaseConstants.ABSENCE_INSTANCE_EDIT_LIST, connectionResource,
                    ShowcaseConstants.ABSENCE_INSTANCE_EDIT_LIST_CONNECTION_KEY,
                    securityConstrains).setSkin(new AbsenceManagementListSkin(getContext())).createComponent();
            layout.addView(list.getView());
            connectionResource = getResources().openRawResource(R.raw.connection); //must be called again
            AFForm form = AFAndroid.getInstance().getFormBuilder().initBuilder(getActivity(),
                    ShowcaseConstants.ABSENCE_INSTANCE_EDIT_FORM, connectionResource,
                    ShowcaseConstants.ABSENCE_INSTANCE_EDIT_FORM_CONNECTION_KEY,
                    securityConstrains).setSkin(new AbsenceManagementFormSkin(getContext())).createComponent();

            Button button = new Button(getActivity());
            button.setText(Localization.translate("button.perform"));
            button.setOnClickListener(onPerformButtonClick);

            layout.addView(form.getView());
            layout.addView(button);
        } catch (Exception e) {
            ShowCaseUtils.showBuildingFailedDialog(getActivity(), e);
            e.printStackTrace();
        }

        //connect list and form
        final AFList absenceList = (AFList) AFAndroid.getInstance().getCreateComponentByName(ShowcaseConstants.ABSENCE_INSTANCE_EDIT_LIST);
        final AFForm absenceForm = (AFForm) AFAndroid.getInstance().getCreateComponentByName(ShowcaseConstants.ABSENCE_INSTANCE_EDIT_FORM);

        if(absenceList != null) {
            absenceList.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    absenceForm.insertData(absenceList.getDataFromItemOnPosition(position));
                    absenceForm.hideErrors();
                }
            });
        }


        return root;
    }

}


