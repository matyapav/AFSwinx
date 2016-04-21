package cz.cvut.fel.matyapav.afandroid.builders.widgets;

import com.tomscz.afrest.commons.SupportedWidgets;
import com.tomscz.afrest.rest.dto.AFFieldInfo;

import cz.cvut.fel.matyapav.afandroid.builders.skins.Skin;
import cz.cvut.fel.matyapav.afandroid.components.parts.AFField;
import cz.cvut.fel.matyapav.afandroid.utils.Utils;

/**
 * Factory class which is used for picking corresponding widget builder based on widget type.
 *
 * @author Pavel Matyáš (matyapav@fel.cvut.cz)
 *
 * @since 1.0.0.
 */
public class WidgetBuilderFactory {

    private static WidgetBuilderFactory instance = null;

    /**
     * Gets the instance of WidgetBuilderFactory
     *
     * @return instance of factory
     */
    public static synchronized WidgetBuilderFactory getInstance() {
        if(instance == null){
            instance = new WidgetBuilderFactory();
        }
        return instance;
    }

    /**
     * Gets corresponding widget builder based on input type
     *
     * @param field holds information about input type
     * @param skin passed to builders to define look of widgets
     * @return corresponding widget builder
     */
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
