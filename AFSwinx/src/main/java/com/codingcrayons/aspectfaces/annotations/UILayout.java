package com.codingcrayons.aspectfaces.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.tomscz.afswinx.layout.definitions.LabelPosition;
import com.tomscz.afswinx.layout.definitions.LayouDefinitions;
import com.tomscz.afswinx.layout.definitions.LayoutOrientation;

/**
 * This annotation provide information about {@link LayouDefinitions}, {@link LabelPosition} and {@link LayoutOrientation}
 * @author Martin Tomasek (martin@toms-cz.com)
 *
 * @since 1.0.0.
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface UILayout {
    public LayouDefinitions layout() default LayouDefinitions.ONECOLUMNLAYOUT; 
    public LabelPosition labelPossition() default LabelPosition.LEFT;
    public LayoutOrientation layoutOrientation() default LayoutOrientation.AXISY;
}
