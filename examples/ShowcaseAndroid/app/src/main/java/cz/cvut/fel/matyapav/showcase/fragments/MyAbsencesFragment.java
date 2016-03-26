package cz.cvut.fel.matyapav.showcase.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.HashMap;

import cz.cvut.fel.matyapav.afandroid.AFAndroid;
import cz.cvut.fel.matyapav.afandroid.components.types.AFList;
import cz.cvut.fel.matyapav.showcase.R;
import cz.cvut.fel.matyapav.showcase.skins.MyAbsencesListSkin;
import cz.cvut.fel.matyapav.showcase.utils.ShowCaseUtils;
import cz.cvut.fel.matyapav.showcase.utils.ShowcaseConstants;

/**
 * Created by Pavel on 26.02.2016.
 */
public class MyAbsencesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.my_absences_fragment_layout, container, false);
        LinearLayout myAbsencesLayout = (LinearLayout) root.findViewById(R.id.myAbsencesLayout);

        //initialize builders
        HashMap<String, String> securityConstrains = ShowCaseUtils.getUserCredentials(getActivity());

        try {
            AFList list = AFAndroid.getInstance().getListBuilder().initBuilder(getActivity(),
                    ShowcaseConstants.MY_ABSENCES_LIST , getResources().openRawResource(R.raw.connection),
                    ShowcaseConstants.MY_ABSENCES_CONNECTION_KEY, securityConstrains)
                    .setSkin(new MyAbsencesListSkin(getContext())).createComponent();
            myAbsencesLayout.addView(list.getView());
        } catch (Exception e) {
            ShowCaseUtils.showBuildingFailedDialog(getActivity(), e);
            e.printStackTrace();
        }
        return root;
    }
}
