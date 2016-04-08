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
 * Created by Pavel on 25.02.2016.
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
        View v = new CustomAdapterView(this.context, list, position);
        return v;
    }

    class CustomAdapterView extends LinearLayout {

        public CustomAdapterView(Context context, AFList list, int position) {
            super(context);
            setOrientation(LinearLayout.HORIZONTAL);

            //vertical layer for text
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(skin.getListContentWidth(), LayoutParams.WRAP_CONTENT);
            LinearLayout layout = new LinearLayout(context);
            if (list.getComponentInfo().getLayout().getLayoutOrientation().equals(LayoutOrientation.AXISX)) {
                layout.setOrientation(LinearLayout.VERTICAL);
            } else {
                layout.setOrientation(LinearLayout.HORIZONTAL);
            }
            layout.setGravity(Gravity.BOTTOM);
            layout.setBackgroundColor(skin.getListItemBackgroundColor());

            TextView textName = new TextView(context);
            textName.setTextSize(skin.getListItemNameSize());
            textName.setTypeface(skin.getListItemNameFont());
            textName.setTextColor(skin.getListItemNameColor());
            textName.setPadding(skin.getListItemNamePaddingLeft(), skin.getListItemNamePaddingTop(),
                    skin.getListItemNamePaddingRight(), skin.getListItemNamePaddingBottom());

            //if it will be twocolumns layout prepare orientation
            int setOfFieldsOrientation;
            if (list.getComponentInfo().getLayout().getLayoutOrientation().equals(LayoutOrientation.AXISX)) { //AXIS X
                setOfFieldsOrientation = LinearLayout.HORIZONTAL;
            } else { //AXIS Y
                setOfFieldsOrientation = LinearLayout.VERTICAL;
            }

            int i = 0;
            LinearLayout setOfFields = null;
            for (AFField field : list.getFields()) {
                if (!field.getFieldInfo().getVisible()) {
                    continue;
                }
                String label = "";
                if (i == 0) {
                    if(field.getFieldInfo().getLabel() != null) {
                        label = skin.isListItemNameLabelVisible() ? Localization.translate(field.getFieldInfo().getLabel()) + ": " : "";
                    }
                    textName.setText(label + list.getRows().get(position).get(field.getId()));
                    layout.addView(textName);
                } else {
                    if(field.getFieldInfo().getLabel() != null) {
                        label = skin.isListItemTextLabelsVisible() ? Localization.translate(field.getFieldInfo().getLabel()) + ": " : "";
                    }
                    TextView text = new TextView(context);
                    text.setTextSize(skin.getListItemsTextSize());
                    text.setTextColor(skin.getListItemTextColor());
                    text.setTypeface(skin.getListItemTextFont());
                    text.setText(label + list.getRows().get(position).get(field.getId()));
                    text.setPadding(skin.getListItemTextPaddingLeft(), skin.getListItemTextPaddingTop(),
                            skin.getListItemTextPaddingRight(), skin.getListItemTextPaddingBottom());

                    int numberOfColumns;
                    if(list.getComponentInfo().getLayout().getLayoutDefinition().equals(LayouDefinitions.TWOCOLUMNSLAYOUT)){
                        numberOfColumns = 2;
                    }else{
                        numberOfColumns = 1;
                    }

                    if ((i - 1) % numberOfColumns == 0) {
                        if (setOfFields != null) {
                            layout.addView(setOfFields);
                        }
                        setOfFields = new LinearLayout(getContext());
                        setOfFields.setOrientation(setOfFieldsOrientation);
                        setOfFields.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    }

                    if (setOfFieldsOrientation == LinearLayout.HORIZONTAL) {
                        text.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f / numberOfColumns));
                    } else {
                        text.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    }
                    setOfFields.addView(text);

                    if(i == list.getVisibleFieldsCount() - 1){
                        layout.addView(setOfFields);
                    }
                }
                i++;
            }
            addView(layout, params);
        }
    }
}
