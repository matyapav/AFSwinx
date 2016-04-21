package cz.cvut.fel.matyapav.afandroid.builders.widgets;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tomscz.afrest.layout.Layout;
import com.tomscz.afrest.layout.definitions.LabelPosition;
import com.tomscz.afrest.layout.definitions.LayoutOrientation;
import com.tomscz.afrest.rest.dto.AFFieldInfo;

import cz.cvut.fel.matyapav.afandroid.builders.skins.Skin;
import cz.cvut.fel.matyapav.afandroid.utils.Localization;
import cz.cvut.fel.matyapav.afandroid.components.parts.AFField;

/**
 * Builds whole input field. Input field is consisted of label, validation element and widget.
 * This class builds all three elements and put them together according to specified layout settings.
 *
 * @author Pavel Matyáš (matyapav@fel.cvut.cz)
 *
 * @since 1.0.0.
 */
public class FieldBuilder {

    /**
     * Prepares field with all needed information including UI representation.
     *
     * @param properties information about a field, needed for e.g setting id of field
     * @param road if not empty, field belongs to some inner class. This fact must be set into fields id.
     * @param activity is needed to dynamically create parts of field in runtime
     * @param skin defines the look of field
     * @return prepared field with all needed information
     */
    public AFField prepareField(AFFieldInfo properties, StringBuilder road, Activity activity, Skin skin) {

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
        AbstractWidgetBuilder widgetBuilder = WidgetBuilderFactory.getInstance().getWidgetBuilder(field, skin);
        if(widgetBuilder != null && (widget = widgetBuilder.buildFieldView(activity))!= null){
            field.setWidgetBuilder(widgetBuilder);
            field.setFieldView(widget);
        }

        //put it all together
        View completeView = buildCompleteView(field, skin);
        if(!properties.getVisible()){
           completeView.setVisibility(View.GONE);
        }
        field.setCompleteView(completeView);
        return field;
    }

    /**
     * Builds complete view of field. Puts label, active element and validation element togetger in specified order.
     * @param field field of which view is created
     * @param skin in this case defines dimensions of field parts
     * @return complete graphical representation of field
     */
    private View buildCompleteView(AFField field, Skin skin){
        LinearLayout fullLayout = new LinearLayout(field.getFieldView().getContext());
        fullLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        Layout layout = field.getFieldInfo().getLayout();
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
        if (field.getLabel() != null && layout.getLabelPosstion() != null && !layout.getLabelPosstion().equals(LabelPosition.NONE)) {
            if (layout.getLabelPosstion().equals(LabelPosition.BEFORE)) {
                fullLayout.addView(field.getLabel());
            }
        }else if(field.getLabel() != null && layout.getLabelPosstion() == null){
            fullLayout.addView(field.getLabel()); //default is before
        }

        if(field.getFieldView() != null) {
            fullLayout.addView(field.getFieldView());
        }
        //LABEL AFTER
        if(field.getLabel() != null && layout.getLabelPosstion() != null && !layout.getLabelPosstion().equals(LabelPosition.NONE)) {
            if (layout.getLabelPosstion().equals(LabelPosition.AFTER)) {
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

    /**
     * Builds element in which will be displayed validation errors.
     *
     * @param activity is needed to dynamically create parts of field in runtime
     * @param skin defines the look of validations errors
     * @return validation element
     */
    private TextView buildErrorView(Activity activity, Skin skin){
        TextView errorView = new TextView(activity);
        errorView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        errorView.setVisibility(View.GONE);
        errorView.setTextColor(skin.getValidationColor());
        errorView.setTypeface(skin.getValidationFont());
        return errorView;
    }

    /**
     * Builds element for label.
     *
     * @param activity is needed to dynamically create parts of field in runtime
     * @param properties information about label
     * @param skin defines the look of label
     * @return label element
     */
    private TextView buildLabel(Activity activity, AFFieldInfo properties, Skin skin){
        TextView label = new TextView(activity);
        if (properties.getLabel() != null && !properties.getLabel().isEmpty()) {
            String labelText = Localization.translate(properties.getLabel());
            //set label position
            label.setTextColor(skin.getLabelColor());
            label.setTypeface(skin.getLabelFont());
            label.setText(labelText);
            return label;
        }
        return null;
    }

}
