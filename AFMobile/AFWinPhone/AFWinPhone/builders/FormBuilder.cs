using System;
using System.Text;
using System.Threading.Tasks;
using AFWinPhone.components.types;
using Windows.UI.Xaml;
using AFWinPhone.enums;
using Windows.UI.Xaml.Controls;
using AFWinPhone.components.parts;

namespace AFWinPhone.builders
{
    /// <summary>
    /// This class if responsible for building form component, which should mainly hadle user input.
    /// </summary>
    public class FormBuilder : AFComponentBuilder<FormBuilder>
    {
        public override AFComponent createComponent()
        {
            this.initializeConnections();
            AFForm form = new AFForm(getConnectionPack(), getSkin());

            buildComponent(form);

            var dataTask = Task.Run(form.getDataResponse);
            dataTask.Wait();
            String data = dataTask.Result;
            if (data != null)
            {
                form.insertData(data);
            }

            AfWindowsPhone.getInstance().addCreatedComponent(this.getComponentKeyName(), form);
            return form;
        }

        protected override FrameworkElement buildComponentView(AFComponent form)
        {
            StackPanel formView = new StackPanel();
            formView.Margin = new Thickness(getSkin().getComponentMarginLeft(), getSkin().getComponentMarginTop(),
                getSkin().getComponentMarginRight(), getSkin().getComponentMarginBottom());
            //set form layout orientation
            if (form.getComponentInfo().getLayout().getLayoutOrientation().Equals(LayoutOrientation.AXISX))
            { //AXIS X
                formView.Orientation = Orientation.Vertical;
            }
            else { //AXIS Y
                formView.Orientation = Orientation.Horizontal;
            }

            //set fields layout orientation
            Orientation setOfFieldsOrientation;
            if (form.getComponentInfo().getLayout().getLayoutOrientation().Equals(LayoutOrientation.AXISX))
            { //AXIS X
                setOfFieldsOrientation = Orientation.Horizontal;
            }
            else { //AXIS Y
                setOfFieldsOrientation = Orientation.Vertical;
            }

            //determine layout
            int numberOfColumns = form.getComponentInfo().getLayout().getLayoutDefinition().getNumberOfColumns();

            int i = 0;
            StackPanel setOfFields = null; ;
            foreach (AFField field in form.getFields())
            {
                if (!field.getFieldInfo().isVisible())
                {
                    continue;
                }
                if (i % numberOfColumns == 0)
                {
                    if (setOfFields != null)
                    {
                        formView.Children.Add(setOfFields);
                    }
                    setOfFields = new StackPanel();
                    setOfFields.Orientation = setOfFieldsOrientation;
                }

                FrameworkElement fieldView = field.getCompleteView();
                setOfFields.Children.Add(fieldView);

                if (i == form.getVisibleFieldsCount() - 1)
                {
                    formView.Children.Add(setOfFields);
                }

                i++;
            }

            return formView;
        }
    }
}
