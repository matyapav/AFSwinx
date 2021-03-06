﻿using AFWinPhone.components.parts.validators;
using AFWinPhone.components.types;
using System;
using System.Diagnostics;
using System.Text;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using AFWinPhone.builders.widgets;

namespace AFWinPhone.components.parts
{
    /// <summary>
    /// This class defines GUI of the field + operations with it like validation
    /// </summary>
    public class AFField
    {
        private AFFieldInfo fieldInfo;
        private String id;
        private TextBlock label; 

        private FrameworkElement fieldView;
        private TextBlock errorView;
        private FrameworkElement completeView;
        private Object actualData;
        private AbstractWidgetBuilder widgetBuilder;

        private AFComponent parent;

        public AFField(AFFieldInfo fieldInfo)
        {
            this.fieldInfo = fieldInfo;
        }

        /// <summary>
        /// Validates all rules connected with field. If any validation failed, validaiton element with erros messages is shown to user.
        /// </summary>
        /// <returns>true if all validations passed, false otherwise</returns>
        public bool validate()
        {
            bool allValidationsFine = true;
            StringBuilder errorMsgs = new StringBuilder();
            errorView.Visibility = (Visibility.Collapsed);
            if (fieldInfo.getRules() != null)
            {
                //add number validation here because it is not among rules in definition
                foreach (AFValidationRule rule in fieldInfo.getRules())
                {
                    AFValidator validator = ValidatorFactory.getInstance().getValidator(rule);
                    Debug.WriteLine("VALIDATION RULE " + rule);
                    Debug.WriteLine("VALIDATOR " + validator);
                    bool validationResult = validator.validate(this, errorMsgs, rule);
                    if (allValidationsFine)
                    { //if once false stays false
                        allValidationsFine = validationResult;
                    }
                    Debug.WriteLine("RESULT " + allValidationsFine);
                    if(rule.getValue() != null)
                    {
                        Debug.WriteLine("RULE VALUE " + rule.getValue());
                    }
                }
            }
            if (!allValidationsFine)
            {
                errorView.Text = errorMsgs.ToString();
                errorView.Visibility = Visibility.Visible;
            }
            return allValidationsFine;
        }

        public TextBlock getLabel()
        {
            return this.label;
        }

        public void setLabel(TextBlock label)
        {
            this.label = label;
        }

        public FrameworkElement getFieldView()
        {
            return this.fieldView;
        }

        public void setFieldView(FrameworkElement fieldView)
        {
            this.fieldView = fieldView;
        }

        public String getId()
        {
            return this.id;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public TextBlock getErrorView()
        {
            return this.errorView;
        }

        public void setErrorView(TextBlock errorView)
        {
            this.errorView = errorView;
        }

        public void setCompleteView(FrameworkElement view)
        {
            this.completeView = view;
        }

        public FrameworkElement getCompleteView()
        {
            return this.completeView;
        }

        public AFFieldInfo getFieldInfo()
        {
            return this.fieldInfo;
        }

        public void setActualData(Object actualData)
        {
            this.actualData = actualData;
        }

        public Object getActualData()
        {
            return this.actualData;
        }

        public void setParent(AFComponent parent)
        {
            this.parent = parent;
        }

        public AFComponent getParent()
        {
            return this.parent;
        }

        public AbstractWidgetBuilder getWidgetBuilder()
        {
            return this.widgetBuilder;
        }

        public void setWidgetBuilder(AbstractWidgetBuilder widgetBuilder)
        {
            this.widgetBuilder = widgetBuilder;
        }


    }
}
