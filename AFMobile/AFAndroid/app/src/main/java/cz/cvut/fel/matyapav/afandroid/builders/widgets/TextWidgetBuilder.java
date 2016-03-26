package cz.cvut.fel.matyapav.afandroid.builders.widgets;

import android.app.Activity;
import android.graphics.Color;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cz.cvut.fel.matyapav.afandroid.components.parts.AFField;
import cz.cvut.fel.matyapav.afandroid.components.parts.FieldInfo;
import cz.cvut.fel.matyapav.afandroid.builders.skins.Skin;
import cz.cvut.fel.matyapav.afandroid.enums.SupportedWidgets;

/**
 * Created by Pavel on 14.02.2016.
 */
public class TextWidgetBuilder extends BasicWidgetBuilder {


    public TextWidgetBuilder(Skin skin, FieldInfo properties) {
        super(skin, properties);
    }

    @Override
    public View buildFieldView(Activity activity) {
        EditText text = new EditText(activity);
        text.setTextColor(getSkin().getFieldColor());
        text.setTypeface(getSkin().getFieldFont());
        addInputType(text, getProperties().getWidgetType());
        if(getProperties().isReadOnly()){
            text.setInputType(InputType.TYPE_NULL);
            text.setTextColor(Color.LTGRAY);
        }
        return text;
    }

    @Override
    public void setData(final AFField field, final Object value) {
        if(value != null) {
            ((EditText) field.getFieldView()).setText(value.toString());
            field.setActualData(value);

            //TODO zvazit tuhle feature .. non editable texty nejdou posunout v pripade, ze je user nevidi cele.
            if(field.getFieldInfo().isReadOnly()) {
                field.getFieldView().setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Toast.makeText(field.getFieldView().getContext(), value.toString(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
            }
        }else{
            ((EditText) field.getFieldView()).setText("");
            field.setActualData("");
        }

    }

    @Override
    public Object getData(AFField field) {
        return ((EditText)field.getFieldView()).getText().toString();
    }

    private void addInputType(EditText field, SupportedWidgets widgetType){
        //textfield or password
        if (widgetType.equals(SupportedWidgets.TEXTFIELD)) {
            field.setInputType(InputType.TYPE_CLASS_TEXT);
        } else if (widgetType.equals(SupportedWidgets.PASSWORD)) {
            field.setTransformationMethod(PasswordTransformationMethod.getInstance());
            field.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else if (widgetType.equals(SupportedWidgets.NUMBERFIELD)) {
            field.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        } else if (widgetType.equals(SupportedWidgets.NUMBERDOUBLEFIELD)){
            field.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
    }
}
