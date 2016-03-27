using AFWinPhone.components.parts.validators;
using AFWinPhone.components.types;
using System;
using System.Diagnostics;
using System.Text;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;

namespace AFWinPhone.components.parts
{
    public class AFField
    {
        private AFFieldInfo fieldInfo;
        private String id;
        private TextBlock label; 

        private FrameworkElement fieldView;
        private TextBlock errorView;
        private FrameworkElement completeView;
        private Object actualData;

        private AFComponent parent;

        public AFField(AFFieldInfo fieldInfo)
        {
            this.fieldInfo = fieldInfo;
        }

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


    }
}
