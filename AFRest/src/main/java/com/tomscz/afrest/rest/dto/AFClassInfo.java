package com.tomscz.afrest.rest.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.tomscz.afrest.layout.TopLevelLayout;

public class AFClassInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private TopLevelLayout layout;
    //TODO remove it, because it cant handle order
    private List<AFFieldInfo> fieldInfo;
    private List<AFClassInfo> innerClasses;

    private int order;
    
    public void addFieldInfo(AFFieldInfo fieldInfoToAdd) {
        if (this.fieldInfo == null) {
            this.fieldInfo = new ArrayList<AFFieldInfo>();
        }
        this.fieldInfo.add(fieldInfoToAdd);

    }
    
    public void addInnerClass(AFClassInfo classInfoToAdd){
        if(innerClasses == null){
            innerClasses = new ArrayList<AFClassInfo>();
        }
        innerClasses.add(classInfoToAdd);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AFFieldInfo> getFieldInfo() {
        return fieldInfo;
    }

    public void setFieldInfo(List<AFFieldInfo> fieldInfo) {
        this.fieldInfo = fieldInfo;
    }

    public TopLevelLayout getLayout() {
        return layout;
    }

    public void setLayout(TopLevelLayout layout) {
        this.layout = layout;
    }

    public void setOptionsToField(HashMap<String, String> options, String fieldId) {
       ArrayList<AFOptions> afOptions = new ArrayList<AFOptions>();
        for(String key:options.keySet()){
           afOptions.add(new AFOptions(key, options.get(key)));
       }
        setOptionsToField(afOptions, fieldId);
    }
    
    private void setOptionsToField(List<AFOptions> options, String fieldId){
        for(AFFieldInfo fiedlInfo:fieldInfo){
            if(fiedlInfo != null && fiedlInfo.getId().equals(fieldId)){
                fiedlInfo.addOption(options);
            }
        }

    }

    public void setOptionsToFields(List<String> options, String fieldId) {
        ArrayList<AFOptions> afOtions = new ArrayList<AFOptions>();
        for (String option : options) {
           afOtions.add(new AFOptions(option, option));
        }
        setOptionsToField(afOtions, fieldId);
    }

    public List<AFClassInfo> getInnerClasses() {
        return innerClasses;
    }

    public void setInnerClasses(List<AFClassInfo> innerClasses) {
        this.innerClasses = innerClasses;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}
