package cz.cvut.fel.matyapav.showcase.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import cz.cvut.fel.matyapav.afandroid.utils.Localization;
import cz.cvut.fel.matyapav.showcase.R;
import cz.cvut.fel.matyapav.showcase.utils.ShowCaseUtils;

/**
 * Welcome screen
 *
 * @author Pavel Matyáš (matyapav@fel.cvut.cz)
 *
 * @since 1.0.0.
 *
 */
public class WelcomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.welcome_fragment_layout, container, false);
        LinearLayout welcomeLayout = (LinearLayout) root.findViewById(R.id.welcomeLayout);
        TextView welcomeUserText = (TextView) welcomeLayout.findViewById(R.id.welcomeUserText);
        welcomeUserText.setText(Localization.translate("welcome.msg")+": " + ShowCaseUtils.getUserCredentials(getActivity()).get("username"));
        return root;
    }
}
