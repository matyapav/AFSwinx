package cz.cvut.fel.matyapav.showcase.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.InputStream;
import java.util.HashMap;

import cz.cvut.fel.matyapav.afandroid.AFAndroid;
import cz.cvut.fel.matyapav.afandroid.components.types.AFForm;
import cz.cvut.fel.matyapav.afandroid.utils.Localization;
import cz.cvut.fel.matyapav.showcase.R;
import cz.cvut.fel.matyapav.showcase.utils.ShowCaseUtils;
import cz.cvut.fel.matyapav.showcase.utils.ShowcaseConstants;

/**
 * Profile screen
 *
 * @author Pavel Matyáš (matyapav@fel.cvut.cz)
 *
 * @since 1.0.0.
 */
public class ProfileFragment extends Fragment {

    /**
     * Handles update button click. Updates person.
     */
    private View.OnClickListener onPersonUpdateBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AFForm form = (AFForm) AFAndroid.getInstance().getCreateComponentByName(ShowcaseConstants.PROFILE_FORM);
            if (form != null) {
                try {
                    if(form.sendData()) {
                        ShowCaseUtils.refreshCurrentFragment(getActivity());
                        Toast.makeText(getActivity(), Localization.translate("person.updateSuccess"),
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    //update failed
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    alertDialog.setTitle(Localization.translate("person.updateFailed"));
                    alertDialog.setMessage(Localization.translate("error.reason") + e.getMessage());
                    alertDialog.show();
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * Handles reset button click. Resets profile form.
     */
    private View.OnClickListener onResetBtnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            AFForm form = (AFForm) AFAndroid.getInstance().getCreateComponentByName(ShowcaseConstants.PROFILE_FORM);
            if(form != null){
                form.resetData();
                form.hideErrors();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        InputStream connectionResource = getResources().openRawResource(R.raw.connection);

        View root = inflater.inflate(R.layout.profile_fragment_layout, container, false);
        LinearLayout layout = (LinearLayout) root.findViewById(R.id.profileLayout);
        HashMap<String, String> securityConstrains = ShowCaseUtils.getUserCredentials(getActivity());

        try {
            AFForm form = AFAndroid.getInstance().getFormBuilder().initBuilder(getActivity(),
                    ShowcaseConstants.PROFILE_FORM, getResources().openRawResource(
                            R.raw.connection),
                    ShowcaseConstants.PROFILE_FORM_CONNECTION_KEY, securityConstrains).createComponent();
            layout.addView(form.getView());
        } catch (Exception e) {
            ShowCaseUtils.showBuildingFailedDialog(getActivity(), e);
            System.err.println("BUILDING FAILED");
            e.printStackTrace();
        }

        Button btn = new Button(getActivity());
        btn.setText(Localization.translate("button.update"));
        btn.setOnClickListener(onPersonUpdateBtnClick);

        Button reset = new Button(getActivity());
        reset.setText(Localization.translate("button.reset"));
        reset.setOnClickListener(onResetBtnClick);

        LinearLayout btns = new LinearLayout(getActivity());
        btns.setOrientation(LinearLayout.HORIZONTAL);
        btns.setGravity(Gravity.CENTER);
        btns.addView(btn);
        btns.addView(reset);
        layout.addView(btns);

        return root;
    }

}
