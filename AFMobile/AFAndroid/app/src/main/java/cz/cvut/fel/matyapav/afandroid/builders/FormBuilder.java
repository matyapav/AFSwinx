package cz.cvut.fel.matyapav.afandroid.builders;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tomscz.afrest.commons.SupportedComponents;
import com.tomscz.afrest.layout.definitions.LayouDefinitions;
import com.tomscz.afrest.layout.definitions.LayoutOrientation;

import cz.cvut.fel.matyapav.afandroid.AFAndroid;
import cz.cvut.fel.matyapav.afandroid.components.types.AFComponent;
import cz.cvut.fel.matyapav.afandroid.components.parts.AFField;
import cz.cvut.fel.matyapav.afandroid.components.types.AFForm;

/**
 * Builds for from class definition
 * Created by Pavel on 26.12.2015.
 */
public class FormBuilder extends AFComponentBuilder<FormBuilder>{

    @Override
    public AFForm createComponent() throws Exception {
        initializeConnections();
        AFForm form = new AFForm(getActivity(), getConnectionPack(), getSkin());
        form.getModelResponse();
        //create form from response
        buildComponent(form);
        //fill it with data (if there are some)
        String data = form.getDataResponse();
        if(data != null) {
            form.insertData(form.getDataResponse());
        }
        AFAndroid.getInstance().addCreatedComponent(getComponentKeyName(), form);
        return form;
    }

    @Override
    protected View buildComponentView(AFComponent form) {
        LinearLayout formView = new LinearLayout(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(getSkin().getComponentMarginLeft(), getSkin().getComponentMarginTop(),
                getSkin().getComponentMarginRight(), getSkin().getComponentMarginBottom());
        formView.setLayoutParams(params);
        //set form layout orientation
        if(form.getComponentInfo().getLayout().getLayoutOrientation().equals(LayoutOrientation.AXISX)){ //AXIS X
            formView.setOrientation(LinearLayout.VERTICAL);
        }else { //AXIS Y
            formView.setOrientation(LinearLayout.HORIZONTAL);
        }

        //set fields layout orientation
        int setOfFieldsOrientation;
        if(form.getComponentInfo().getLayout().getLayoutOrientation().equals(LayoutOrientation.AXISX)){ //AXIS X
            setOfFieldsOrientation = LinearLayout.HORIZONTAL;
        }else{ //AXIS Y
            setOfFieldsOrientation = LinearLayout.VERTICAL;
        }

        //determine layout
        int numberOfColumns;
        if(form.getComponentInfo().getLayout().getLayoutDefinition().equals(LayouDefinitions.TWOCOLUMNSLAYOUT)){
            numberOfColumns = 2;
        }else {
            numberOfColumns = 1;
        }

        int i = 0;
        LinearLayout setOfFields = null;;
        for (AFField field : form.getFields()) {
            if(!field.getFieldInfo().getVisible()){
                continue;
            }
            if (i % numberOfColumns == 0) {
                if(setOfFields != null) {
                    formView.addView(setOfFields);
                }
                setOfFields = new LinearLayout(getActivity());
                setOfFields.setOrientation(setOfFieldsOrientation);
                setOfFields.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }

            View fieldView = field.getCompleteView();
            fieldView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f / numberOfColumns));
            setOfFields.addView(fieldView);

            if(i == form.getVisibleFieldsCount() - 1){
                formView.addView(setOfFields);
            }

            i++;
        }

        return formView;
    }
}