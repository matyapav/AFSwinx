package cz.cvut.fel.matyapav.afandroid.builders.widgets;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import cz.cvut.fel.matyapav.afandroid.builders.widgets.WidgetBuilderFactory;
import cz.cvut.fel.matyapav.afandroid.builders.widgets.AbstractWidgetBuilder;
import cz.cvut.fel.matyapav.afandroid.components.parts.LayoutProperties;
import cz.cvut.fel.matyapav.afandroid.builders.skins.Skin;
import cz.cvut.fel.matyapav.afandroid.enums.LayoutOrientation;
import cz.cvut.fel.matyapav.afandroid.utils.Localization;
import cz.cvut.fel.matyapav.afandroid.components.parts.AFField;
import cz.cvut.fel.matyapav.afandroid.components.parts.FieldInfo;
import cz.cvut.fel.matyapav.afandroid.enums.LabelPosition;

/**
 * Builds input field
 * Created by Pavel on 25.12.2015.
 */
public class FieldBuilder {

    public AFField prepareField(FieldInfo properties, StringBuilder road, Activity activity, Skin skin) {

        AFField field = new AFField(properties);
        field.setId(road.toString()+properties.getId());

        //LABEL
        TextView label = buildLabel(activity, properties, skin);
        field.setLabel(label);

        //ERROR TEXT
        TextView errorView = buildErrorView(activity, skin);
        field.setErrorView(errorView);

        //Input view
        View widget = null;
        AbstractWidgetBuilder widgetBuilder = WidgetBuilderFactory.getInstance().getFieldBuilder(properties, skin);
        if(widgetBuilder != null && (widget = widgetBuilder.buildFieldView(activity))!= null){
            field.setFieldView(widget);
        }

        //put it all together
        //when field is not visible don't even add it to form;
        View completeView = buildCompleteView(field, skin);
        if(!properties.isVisible()){
           completeView.setVisibility(View.GONE);
        }
        field.setCompleteView(completeView);
        return field;
    }

    private View buildCompleteView(AFField field, Skin skin){
        LinearLayout fullLayout = new LinearLayout(field.getFieldView().getContext());
        fullLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        LayoutProperties layout = field.getFieldInfo().getLayout();
        //set orientation of label and field itself
        if(layout.getLayoutOrientation() != null){
            if(layout.getLayoutOrientation().equals(LayoutOrientation.AXISY)){
                fullLayout.setOrientation(LinearLayout.HORIZONTAL);
            }else if(layout.getLayoutOrientation().equals(LayoutOrientation.AXISX)){
                fullLayout.setOrientation(LinearLayout.VERTICAL);
            }
        }else{
            fullLayout.setOrientation(LinearLayout.VERTICAL); //default
        }

        //set label and field view layout params
        if(field.getLabel() != null) {
            field.getLabel().setLayoutParams(new LinearLayout.LayoutParams(skin.getLabelWidth(), skin.getLabelHeight()));
        }
        field.getFieldView().setLayoutParams(new LinearLayout.LayoutParams(skin.getInputWidth(), ViewGroup.LayoutParams.WRAP_CONTENT));

        //LABEL BEFORE
        if (field.getLabel() != null && layout.getLabelPosition() != null && !layout.getLabelPosition().equals(LabelPosition.NONE)) {
            if (layout.getLabelPosition().equals(LabelPosition.BEFORE)) {
                fullLayout.addView(field.getLabel());
            }
        }else if(field.getLabel() != null && layout.getLabelPosition() == null){
            fullLayout.addView(field.getLabel()); //default is before
        }

        if(field.getFieldView() != null) {
            fullLayout.addView(field.getFieldView());
        }
        //LABEL AFTER
        if(field.getLabel() != null && layout.getLabelPosition() != null && !layout.getLabelPosition().equals(LabelPosition.NONE)) {
            if (layout.getLabelPosition().equals(LabelPosition.AFTER)) {
                fullLayout.addView(field.getLabel());
            }
        }

        //add errorview on the top of field
        LinearLayout fullLayoutWithErrors = new LinearLayout(field.getFieldView().getContext());
        fullLayoutWithErrors.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        fullLayoutWithErrors.setPadding(0, 0, 10, 10);
        fullLayoutWithErrors.setOrientation(LinearLayout.VERTICAL);
        fullLayoutWithErrors.addView(field.getErrorView());
        fullLayoutWithErrors.addView(fullLayout);
        return fullLayoutWithErrors;
    }

    private TextView buildErrorView(Activity activity, Skin skin){
        TextView errorView = new TextView(activity);
        errorView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        errorView.setVisibility(View.GONE);
        errorView.setTextColor(skin.getValidationColor());
        errorView.setTypeface(skin.getValidationFont());
        return errorView;
    }

    private TextView buildLabel(Activity activity, FieldInfo properties, Skin skin){
        TextView label = new TextView(activity);
        if (properties.getLabelText() != null && !properties.getLabelText().isEmpty()) {
            String labelText = Localization.translate(properties.getLabelText());
            //set label position
            LabelPosition pos = properties.getLayout().getLabelPosition();
            label.setTextColor(skin.getLabelColor());
            label.setTypeface(skin.getLabelFont());
            label.setText(labelText);
            return label;
        }
        return null;
    }

}
