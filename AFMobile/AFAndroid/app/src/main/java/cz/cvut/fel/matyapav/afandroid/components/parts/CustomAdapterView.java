package cz.cvut.fel.matyapav.afandroid.components.parts;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tomscz.afrest.layout.definitions.LayouDefinitions;
import com.tomscz.afrest.layout.definitions.LayoutOrientation;

import cz.cvut.fel.matyapav.afandroid.builders.skins.Skin;
import cz.cvut.fel.matyapav.afandroid.components.types.AFList;
import cz.cvut.fel.matyapav.afandroid.utils.Localization;

/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */
/**
 * This class defines look of custom list items.
 *
 * @author Pavel Matyáš (matyapav@fel.cvut.cz)
 *
 * @since 1.0.0.
 */
public class CustomAdapterView extends LinearLayout {

    Skin skin;
    Context context;
    AFList list;

    public CustomAdapterView(Context context, AFList list, int position, Skin skin) {
        super(context);
        this.context = context;
        this.list = list;
        this.skin = skin;
        
        setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(skin.getListContentWidth(), LayoutParams.WRAP_CONTENT);
        LinearLayout layout = prepareLayout();

        //if it will be twocolumns layout prepare orientation
        int setOfFieldsOrientation;
        if (list.getComponentInfo().getLayout().getLayoutOrientation().equals(LayoutOrientation.AXISX)) { //AXIS X
            setOfFieldsOrientation = LinearLayout.HORIZONTAL;
        } else { //AXIS Y
            setOfFieldsOrientation = LinearLayout.VERTICAL;
        }

        createListItem(getContext(), position, layout, setOfFieldsOrientation);

        addView(layout, params);
    }

    /**
     * Creates UI on one list item on specified position.
     *
     * @param context needed to create UI dynamically
     * @param position position of item which UI is being created
     * @param layout layout to which will be UI added
     * @param setOfFieldsOrientation orientation of fields within one list item.
     */
    private void createListItem(Context context, int position, LinearLayout layout, int setOfFieldsOrientation){
        int i = 0;
        LinearLayout setOfFields = null;
        for (AFField field : list.getFields()) {
            //skin not visible fields
            if (!field.getFieldInfo().getVisible()) {
                continue;
            }
            String label = "";
            if (i == 0) {
                if (field.getFieldInfo().getLabel() != null) {
                    label = skin.isListItemNameLabelVisible() ? Localization.translate(field.getFieldInfo().getLabel()) + ": " : "";
                }
                TextView textName = prepateListNameView();
                textName.setText(label + list.getRows().get(position).get(field.getId()));
                layout.addView(textName);
            } else {
                if (field.getFieldInfo().getLabel() != null) {
                    label = skin.isListItemTextLabelsVisible() ? Localization.translate(field.getFieldInfo().getLabel()) + ": " : "";
                }
                TextView text = prepareListTextView();
                text.setText(label + list.getRows().get(position).get(field.getId()));

                int numberOfColumns;
                if (list.getComponentInfo().getLayout().getLayoutDefinition().equals(LayouDefinitions.TWOCOLUMNSLAYOUT)) {
                    numberOfColumns = 2;
                } else {
                    numberOfColumns = 1;
                }

                if ((i - 1) % numberOfColumns == 0) {
                    if (setOfFields != null) {
                        layout.addView(setOfFields);
                    }
                    setOfFields = new LinearLayout(context);
                    setOfFields.setOrientation(setOfFieldsOrientation);
                    setOfFields.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }

                if (setOfFieldsOrientation == LinearLayout.HORIZONTAL) {
                    text.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f / numberOfColumns));
                } else {
                    text.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }
                setOfFields.addView(text);

                if (i == list.getVisibleFieldsCount() - 1) {
                    layout.addView(setOfFields);
                }
            }
            i++;
        }
    }

    /**
     * Prepares list item name, which has different look from other information in list item.
     *
     * @return list item name view
     */
    private TextView prepateListNameView(){
        TextView textName = new TextView(context);
        textName.setTextSize(skin.getListItemNameSize());
        textName.setTypeface(skin.getListItemNameFont());
        textName.setTextColor(skin.getListItemNameColor());
        textName.setPadding(skin.getListItemNamePaddingLeft(), skin.getListItemNamePaddingTop(),
                skin.getListItemNamePaddingRight(), skin.getListItemNamePaddingBottom());
        return textName;
    }

    /**
     * Prepares list item text. With this text view are represented all information except the first one.
     *
     * @return list item text view
     */
    private TextView prepareListTextView(){
        TextView text = new TextView(context);
        text.setTextSize(skin.getListItemsTextSize());
        text.setTextColor(skin.getListItemTextColor());
        text.setTypeface(skin.getListItemTextFont());
        text.setPadding(skin.getListItemTextPaddingLeft(), skin.getListItemTextPaddingTop(),
                skin.getListItemTextPaddingRight(), skin.getListItemTextPaddingBottom());
        return text;
    }

    /**
     * Prepares layout of list item which will be all information add to.
     *
     * @return layout of list item
     */
    private LinearLayout prepareLayout(){
        LinearLayout layout = new LinearLayout(context);
        if (list.getComponentInfo().getLayout().getLayoutOrientation().equals(LayoutOrientation.AXISX)) {
            layout.setOrientation(LinearLayout.VERTICAL);
        } else {
            layout.setOrientation(LinearLayout.HORIZONTAL);
        }
        layout.setGravity(Gravity.BOTTOM);
        layout.setBackgroundColor(skin.getListItemBackgroundColor());
        return layout;
    }

}
