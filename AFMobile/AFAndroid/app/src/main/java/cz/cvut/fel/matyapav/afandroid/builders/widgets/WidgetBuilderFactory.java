package cz.cvut.fel.matyapav.afandroid.builders.widgets;

import com.tomscz.afrest.commons.SupportedWidgets;
import com.tomscz.afrest.rest.dto.AFFieldInfo;

import cz.cvut.fel.matyapav.afandroid.builders.skins.Skin;
import cz.cvut.fel.matyapav.afandroid.components.parts.AFField;
import cz.cvut.fel.matyapav.afandroid.utils.Utils;

/**
 * Created by Pavel on 19.02.2016.
 */
public class WidgetBuilderFactory {

    private static WidgetBuilderFactory instance = null;

    public static synchronized WidgetBuilderFactory getInstance() {
        if(instance == null){
            instance = new WidgetBuilderFactory();
        }
        return instance;
    }


    public AbstractWidgetBuilder getWidgetBuilder(AFField field, Skin skin){
        if(Utils.isFieldWritable(field.getFieldInfo().getWidgetType())){
            return new TextWidgetBuilder(skin, field);
        }
        if(field.getFieldInfo().getWidgetType().equals(SupportedWidgets.CALENDAR)) {
            return new DateWidgetBuilder(skin, field);
        }
        if(field.getFieldInfo().getWidgetType().equals(SupportedWidgets.OPTION)){
            return new OptionWidgetBuilder(skin, field);
        }
        if(field.getFieldInfo().getWidgetType().equals(SupportedWidgets.DROPDOWNMENU)){
            return new DropDownWidgetBuilder(skin, field);
        }
        if(field.getFieldInfo().getWidgetType().equals(SupportedWidgets.CHECKBOX)){
            return new CheckboxWidgetBuilder(skin, field);
        }
        System.err.println("BUILDER FOR "+field.getFieldInfo().getWidgetType()+" NOT FOUND");
        return null;
    }
}
