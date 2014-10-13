package com.tomscz.afswinx.unmarshal.factory;

import com.tomscz.afswinx.common.SupportedWidgets;
import com.tomscz.afswinx.rest.dto.AFFieldInfo;
import com.tomscz.afswinx.unmarshal.builders.FieldBuilder;
import com.tomscz.afswinx.unmarshal.builders.InputFieldBuilder;
import com.tomscz.afswinx.unmarshal.builders.LabelFieldBuider;

public class WidgetBuilderFactory {

    private static WidgetBuilderFactory instance;
    
    private WidgetBuilderFactory(){
        
    }
    
    public static synchronized WidgetBuilderFactory getInstance(){
        if(instance == null){
            instance = new WidgetBuilderFactory();
        }
        return instance;
    }
    
    public FieldBuilder createWidgetBuilder(AFFieldInfo fieldInfo){
       return createWidgetBuilder(fieldInfo.getWidgetType());
    }
    
    public FieldBuilder createWidgetBuilder(SupportedWidgets widget){
        if(widget.equals(SupportedWidgets.INPUTFIELD)){
            return new InputFieldBuilder();
        }
        else if(widget.equals(SupportedWidgets.LABEL)){
            return new LabelFieldBuider();
        }
        else{
            return null;
        }    
    }
    
}
