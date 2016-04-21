package cz.cvut.fel.matyapav.afandroid.components.parts;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tomscz.afrest.layout.definitions.LayouDefinitions;
import com.tomscz.afrest.layout.definitions.LayoutOrientation;

import cz.cvut.fel.matyapav.afandroid.components.types.AFList;
import cz.cvut.fel.matyapav.afandroid.builders.skins.Skin;
import cz.cvut.fel.matyapav.afandroid.utils.Localization;

/**
 * This class defines custom list adapter, which custom list items.
 *
 * @author Pavel Matyáš (matyapav@fel.cvut.cz)
 *
 * @since 1.0.0.
 */
public class CustomListAdapter extends BaseAdapter {

    private Context context;
    private AFList list;
    private Skin skin;

    public CustomListAdapter(Context context, Skin skin, AFList list) {
        this.context = context;
        this.list = list;
        this.skin = skin;
    }

    public int getCount() {
        return list.getRows().size();
    }

    public Object getItem(int position) {
        return list.getRows().get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = new CustomAdapterView(this.context, list, position, skin);
        return v;
    }


}
