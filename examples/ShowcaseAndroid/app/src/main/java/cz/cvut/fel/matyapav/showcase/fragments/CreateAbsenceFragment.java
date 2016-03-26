package cz.cvut.fel.matyapav.showcase.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.HashMap;

import cz.cvut.fel.matyapav.afandroid.AFAndroid;
import cz.cvut.fel.matyapav.afandroid.components.types.AFForm;
import cz.cvut.fel.matyapav.afandroid.utils.Localization;
import cz.cvut.fel.matyapav.showcase.R;
import cz.cvut.fel.matyapav.showcase.skins.CreateAbsenceFormSkin;
import cz.cvut.fel.matyapav.showcase.utils.ShowCaseUtils;
import cz.cvut.fel.matyapav.showcase.utils.ShowcaseConstants;


/**
 * Created by Pavel on 28.02.2016.
 */
public class CreateAbsenceFragment extends Fragment {

    private View.OnClickListener onCreateAbsenceClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AFForm createAbsenceForm = (AFForm) AFAndroid.getInstance().getCreatedComponents()
                    .get(ShowcaseConstants.ABSENCE_ADD_FORM);
            if(createAbsenceForm != null){
                try {
                    if(createAbsenceForm.sendData()) {
                        Toast.makeText(getActivity(), Localization.translate("absence.create"),
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    alertDialog.setTitle(Localization.translate("absence.createFailed"));
                    alertDialog.setMessage(Localization.translate("error.reason") + e.getMessage());
                    alertDialog.show();
                    e.printStackTrace();
                }

            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.create_absence_fragment_layout, container, false);
        LinearLayout createAbsenceLayout = (LinearLayout) root.findViewById(R.id.createAbsenceLayout);
        HashMap<String, String> securityConstrains = ShowCaseUtils.getUserCredentials(getActivity());
        securityConstrains.put("user", ShowCaseUtils.getUserLogin(getActivity()));
        try {
            AFForm createAbsenceForm = AFAndroid.getInstance().getFormBuilder().initBuilder(getActivity(),
                    ShowcaseConstants.ABSENCE_ADD_FORM, getResources().openRawResource(R.raw.connection),
                    ShowcaseConstants.ABSENCE_ADD_FORM_CONNECTION_KEY,
                    securityConstrains).setSkin(new CreateAbsenceFormSkin(getContext())).createComponent();
            createAbsenceLayout.addView(createAbsenceForm.getView());
        } catch (Exception e) {
            ShowCaseUtils.showBuildingFailedDialog(getActivity(), e);
            e.printStackTrace();
        }

        Button button = new Button(getActivity());
        button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        button.setText(Localization.translate("button.create"));
        button.setOnClickListener(onCreateAbsenceClick);
        createAbsenceLayout.addView(button);
        return root;
    }

}