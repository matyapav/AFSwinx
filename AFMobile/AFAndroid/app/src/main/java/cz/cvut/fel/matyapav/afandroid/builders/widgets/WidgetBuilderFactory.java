package cz.cvut.fel.matyapav.afandroid.builders.widgets;

import com.tomscz.afrest.commons.SupportedWidgets;
import com.tomscz.afrest.rest.dto.AFFieldInfo;

import cz.cvut.fel.matyapav.afandroid.builders.skins.Skin;
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


    public AbstractWidgetBuilder getFieldBuilder(AFFieldInfo properties, Skin skin){
        if(Utils.isFieldWritable(properties.getWidgetType())){
            return new TextWidgetBuilder(skin, properties);
        }
        if(properties.getWidgetType().equals(SupportedWidgets.CALENDAR)) {
            return new DateWidgetBuilder(skin, properties);
        }
        if(properties.getWidgetType().equals(SupportedWidgets.OPTION)){
            return new OptionWidgetBuilder(skin, properties);
        }
        if(properties.getWidgetType().equals(SupportedWidgets.DROPDOWNMENU)){
            return new DropDownWidgetBuilder(skin, properties);
        }
        if(properties.getWidgetType().equals(SupportedWidgets.CHECKBOX)){
            return new CheckboxWidgetBuilder(skin, properties);
        }
        System.err.println("BUILDER FOR "+properties.getWidgetType()+" NOT FOUND");
        return null;
    }
}
