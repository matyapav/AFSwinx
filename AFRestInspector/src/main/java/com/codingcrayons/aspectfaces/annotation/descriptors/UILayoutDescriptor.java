package com.codingcrayons.aspectfaces.annotation.descriptors;

import java.util.ArrayList;
import java.util.List;

import com.codingcrayons.aspectfaces.annotation.AnnotationProvider;
import com.codingcrayons.aspectfaces.annotation.registration.AnnotationDescriptor;
import com.codingcrayons.aspectfaces.annotation.registration.pointCut.VariableJoinPoint;
import com.codingcrayons.aspectfaces.annotation.registration.pointCut.properties.Variable;

public class UILayoutDescriptor implements AnnotationDescriptor, VariableJoinPoint {

    @Override
    public List<Variable> getVariables(AnnotationProvider annotationProvider) {
        List<Variable> variables = new ArrayList<Variable>();
        variables.add(new Variable("layout", annotationProvider.getValue("value")));
        return variables;
    }

    @Override
    public String getAnnotationName() {
        return "com.codingcrayons.aspectfaces.annotations.UILayout";
    }

}
