using AFWinPhone.components.parts;
using AFWinPhone.builders.skins;
using AFWinPhone.builders.widgets;
using AFWinPhone.enums;
using AFWinPhone.rest.connection;
using AFWinPhone.rest.holder;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Globalization;
using System.Text;
using System.Threading.Tasks;
using Windows.Data.Json;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Media;
using AFWinPhone.utils;

namespace AFWinPhone.components.types
{
    /// <summary>
    /// Represents list component.
    /// </summary>
    public class AFList : AFComponent
    {
        private ListView listView;
        private List<Dictionary<String, String>> rows;

        public AFList()
        {
            rows = new List<Dictionary<String, String>>();
        }

        public AFList(AFSwinxConnectionPack connectionPack, Skin skin) : base(connectionPack, skin)
        {
            rows = new List<Dictionary<String, String>>();
        }


        public override SupportedComponents getComponentType()
        {
            return SupportedComponents.LIST;
        }

        public override object generateSendData()
        {
            throw new NotImplementedException("List is read only");
        }

        public override Task<Boolean> sendData()
        {
            throw new NotImplementedException("List is read only");
        }

        public override void insertData(String dataResponse, StringBuilder road)
        {
            try
            {
                JsonArray jsonArray = JsonArray.Parse(dataResponse);
                for (int i = 0; i < jsonArray.Count; i++)
                {
                    Dictionary<String, String> row = new Dictionary<String, String>();
                    JsonObject jsonObject = (JsonObject) Utils.TryToGetValueFromJson(jsonArray[i]);
                    insertDataObject(jsonObject, road, row);
                    addRow(row);
                    road = new StringBuilder();
                }
                for (int i = 0; i < getRows().Count; i++)
                {
                    getListView().Items.Add(createCustomListItem(i));
                }
                
            }
            catch (Exception e)
            {
                Debug.WriteLine("CANNOT PARSE DATA");
                Debug.WriteLine(e.StackTrace);
            }
        }

        /// <summary>
        /// Inserts data to specific list item.
        /// Exception thrown if there was some error during parsing data object.
        /// </summary>
        /// <param name="jsonObject">JSONObject which contains data for list item</param>
        /// <param name="road">used for inserting data into fields created from inner classes</param>
        /// <param name="row">row which represents data in list item</param>
        private void insertDataObject(JsonObject jsonObject, StringBuilder road, Dictionary<String, String> row)
        {
            foreach(String key in jsonObject.Keys){
                if (jsonObject[key].ValueType == JsonValueType.Object ){
                    String roadBackup = road.ToString();
                    road.Append(key);
                    road.Append(".");
                    insertDataObject((JsonObject) Utils.TryToGetValueFromJson(jsonObject[key]), road, row); //parse class types
                    road = new StringBuilder(roadBackup);
                }else {
                    AFField field = getFieldById(road + key);
                    if (field != null)
                    {
                        Object data = Utils.TryToGetValueFromJson(jsonObject[key]);
                        if (data == null)
                        {
                            data = "";
                        }
                        field.getWidgetBuilder().setData(data);
                        row.Add(road + key, field.getActualData().ToString());
                    }
                }
            }
        }

        public override AFDataHolder reserialize()
        {
            throw new NotImplementedException("List is read only");
        }

        public override bool validateData()
        {
            throw new NotImplementedException("List is read only");
        }

        public void addRow(Dictionary<String, String> values)
        {
            rows.Add(values);
        }

        /// <summary>
        /// Gets data from list item on specified position.
        /// </summary>
        /// <param name="position"> position of list item in list view</param>
        /// <returns>corresponding data</returns>
        public Object getDataFromItemOnPosition(int position)
        {
            AFSwinxConnection sendConnection = getConnectionPack().getSendConnection();
            // Generate send connection based on which will be retrieve data. The send connection is
            // used to generate data in this case it will be generated JSON
            if (sendConnection == null)
            {
                sendConnection = new AFSwinxConnection("", 0, "");
            }

            BaseRestBuilder dataBuilder = RestBuilderFactory.getInstance().getBuilder(sendConnection);
            JsonObject data = (JsonObject) dataBuilder.reselialize(createFormDataFromList(position));
            Debug.WriteLine("DATA " + data.Stringify());
            return data.Stringify();
        }

        /// <summary>
        /// Creates data from list item on given position which can be inserted to form.
        /// </summary>
        /// <param name="position">position of list item in list view</param>
        /// <returns> data of list item prepared to be inserted in form</returns>
        private AFDataHolder createFormDataFromList(int position)
        {
            AFDataHolder dataHolder = new AFDataHolder();
            foreach (AFField field in getFields())
            {
                String propertyName = field.getId();
                Object data = rows[position][propertyName];
                // Based on dot notation determine road. Road is used to add object to its right place
                String[] roadTrace = propertyName.Split(new [] {"."}, StringSplitOptions.None);
                if (roadTrace.Length > 1)
                {
                    AFDataHolder startPoint = dataHolder;
                    for (int i = 0; i < roadTrace.Length; i++)
                    {
                        String roadPoint = roadTrace[i];
                        // If road end then add this property as inner propety
                        if (i + 1 == roadTrace.Length)
                        {
                            startPoint.addPropertyAndValue(roadPoint, (String) data);
                        }
                        else {
                            // Otherwise it will be inner class so add if doesn't exist continue.
                            AFDataHolder roadHolder = startPoint.getInnerClassByKey(roadPoint);
                            if (roadHolder == null)
                            {
                                roadHolder = new AFDataHolder();
                                roadHolder.setClassName(roadPoint);
                                startPoint.addInnerClass(roadHolder);
                            }
                            // Set start point on current possition in tree
                            startPoint = roadHolder;
                        }
                    }
                }
                else {
                    dataHolder.addPropertyAndValue(propertyName, (String) data);
                }
            }
            return dataHolder;
        }

        /// <summary>
        /// Creates custom list item.
        /// </summary>
        /// <param name="position">position of item in list view</param>
        /// <returns>graphical representation of custom list item</returns>
        private StackPanel createCustomListItem(int position)
        {         
            StackPanel panel = new StackPanel();
            panel.Orientation = Orientation.Horizontal;
            if (getSkin().getListContentWidth() < 0)
            {
                panel.HorizontalAlignment = getSkin().getListContentHorizontalAlignment();
            }
            else
            { 
                panel.Width = getSkin().getListContentWidth();
            }
            panel.Background = new SolidColorBrush(getSkin().getListItemBackgroundColor());
            
            Grid layout = new Grid();
            for (int j = 0; j < getComponentInfo().getLayout().getLayoutDefinition().getNumberOfColumns(); j++)
            {
                if (getComponentInfo().getLayout().getLayoutOrientation().Equals(LayoutOrientation.AXISX))
                {
                    layout.ColumnDefinitions.Add(new ColumnDefinition());
                }else if (getComponentInfo().getLayout().getLayoutOrientation().Equals(LayoutOrientation.AXISY))
                {
                    layout.RowDefinitions.Add(new RowDefinition());                    
                }
            }

            TextBlock textName = new TextBlock();
            textName.FontSize = getSkin().getListItemNameSize();
            textName.FontFamily = getSkin().getListItemNameFont();
            textName.Foreground = new SolidColorBrush(getSkin().getListItemNameColor());
            textName.Margin = new Thickness(getSkin().getListItemNamePaddingLeft(), getSkin().getListItemNamePaddingTop(),
                    getSkin().getListItemNamePaddingRight(), getSkin().getListItemNamePaddingBottom());

            int i = 0;
            int row = 0;
            int column = 0;
            foreach (AFField field in getFields())
            {
                if (!field.getFieldInfo().isVisible())
                {
                    continue;
                }
                String label = "";
                if (i == 0)
                {
                    if (field.getFieldInfo().getLabelText() != null)
                    {
                        label = getSkin().isListItemNameLabelVisible()
                            ? Localization.translate(field.getFieldInfo().getLabelText()) + ": "
                            : "";
                    }
                   
                    textName.Text = label + getRows()[position][field.getId()];
                   
                    layout.RowDefinitions.Add(new RowDefinition());
                    Grid.SetRow(textName, row);
                    Grid.SetColumnSpan(textName, getComponentInfo().getLayout().getLayoutDefinition().getNumberOfColumns());
                    layout.Children.Add(textName);
                }
                else
                {
                    if (field.getFieldInfo().getLabelText() != null)
                    {
                        label = getSkin().isListItemTextLabelsVisible()
                            ? Localization.translate(field.getFieldInfo().getLabelText()) + ": "
                            : "";
                    }
                    TextBlock text = new TextBlock();
                    text.FontSize = getSkin().getListItemsTextSize();
                    text.Foreground = new SolidColorBrush(getSkin().getListItemTextColor());
                    text.FontFamily = getSkin().getListItemTextFont();
                  
                    text.Text = label + getRows()[position][field.getId()];
                    
                    text.Margin = new Thickness(getSkin().getListItemTextPaddingLeft(),
                        getSkin().getListItemTextPaddingTop(),
                        getSkin().getListItemTextPaddingRight(), getSkin().getListItemTextPaddingBottom());

                    int numberOfColumns = getComponentInfo().getLayout().getLayoutDefinition().getNumberOfColumns();

                    if ((i - 1)%numberOfColumns == 0)
                    {
                        if (getComponentInfo().getLayout().getLayoutOrientation().Equals(LayoutOrientation.AXISX))
                        {
                            layout.RowDefinitions.Add(new RowDefinition());
                            row++;
                            column = 0;
                        }
                        else if (getComponentInfo().getLayout().getLayoutOrientation().Equals(LayoutOrientation.AXISY))
                        {
                            layout.ColumnDefinitions.Add(new ColumnDefinition());
                            column++;
                            row = 1;
                        }
                    }
                    Grid.SetRow(text, row);
                    Grid.SetColumn(text, column);
                    layout.Children.Add(text);

                    if (getComponentInfo().getLayout().getLayoutOrientation().Equals(LayoutOrientation.AXISX))
                    {
                        column++;
                    }
                    else if (getComponentInfo().getLayout().getLayoutOrientation().Equals(LayoutOrientation.AXISY))
                    {
                        row++;
                    }
                }
                i++;
            }
            panel.Children.Add(layout);
            return panel;
        }

        //GETTERS AND SETTERS

        public List<Dictionary<String, String>> getRows()
        {
            return this.rows;
        }

        /// <summary>
        /// Gets list view. Use this if there is need to catch some events happened in list view like tapping on list item.
        /// </summary>
        /// <returns>list view</returns>
        public ListView getListView()
        {
            return listView;
        }

        public void setListView(ListView listView)
        {
            this.listView = listView;
        }
    }
}
