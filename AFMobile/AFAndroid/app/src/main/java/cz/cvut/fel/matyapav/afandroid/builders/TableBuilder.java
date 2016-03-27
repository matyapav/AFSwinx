package cz.cvut.fel.matyapav.afandroid.builders;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.tomscz.afrest.commons.SupportedComponents;

import cz.cvut.fel.matyapav.afandroid.AFAndroid;
import cz.cvut.fel.matyapav.afandroid.components.types.AFComponent;
import cz.cvut.fel.matyapav.afandroid.components.types.AFTable;
import cz.cvut.fel.matyapav.afandroid.components.parts.AFField;
import cz.cvut.fel.matyapav.afandroid.utils.Localization;
import cz.cvut.fel.matyapav.afandroid.utils.Utils;


/**
 * Created by Pavel on 20.02.2016.
 */
public class TableBuilder extends AFComponentBuilder<TableBuilder> {


    @Override
    public AFTable createComponent() throws Exception {
        initializeConnections();
        String modelResponse = getModelResponse();
        //create form from response
        AFTable table = (AFTable) buildComponent(modelResponse, SupportedComponents.TABLE);
        //fill it with data (if there are some)
        String data = getDataResponse();
        if(data != null) {
            table.insertData(data);
        }
        AFAndroid.getInstance().addCreatedComponent(getComponentKeyName(), table);
        return table;
    }


    @Override
    protected View buildComponentView(AFComponent component) {
        //setup wrapper for table
        LinearLayout tableWrapper = new LinearLayout(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getSkin().getTableHeight());
        params.setMargins(getSkin().getComponentMarginLeft(), getSkin().getComponentMarginTop(),
                getSkin().getComponentMarginRight(), getSkin().getComponentMarginBottom());
        tableWrapper.setLayoutParams(params);
        tableWrapper.setOrientation(LinearLayout.VERTICAL);
        //setup table layout for header row
        TableLayout headerTableLayout = new TableLayout(getActivity());
        headerTableLayout.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, getSkin().getHeaderRowHeight()));
        headerTableLayout.setStretchAllColumns(true);
        headerTableLayout.setShrinkAllColumns(true);

        //setup vertical scroll view for table content
        ScrollView scrollView = new ScrollView(getActivity());
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getSkin().getTableHeight()-getSkin().getHeaderRowHeight()));
        scrollView.setScrollbarFadingEnabled(false);

        //setup table layout for table content
        TableLayout contentTableLayout = new TableLayout(getActivity());
        contentTableLayout.setBackgroundColor(getSkin().getContentBackgroundColor());
        contentTableLayout.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
        contentTableLayout.setStretchAllColumns(true);
        contentTableLayout.setShrinkAllColumns(true);

        TableRow headerRow = new TableRow(getActivity());
        headerRow.setBackgroundColor(getSkin().getHeaderRowBackgroundColor());

        TableRow fakeContentRow = new TableRow(getActivity());

        //insert to header row
        int numberOfColumns = 0;
        for (AFField field : component.getFields()) {
            if (!field.getFieldInfo().getVisible()) {
                continue;
            }
            numberOfColumns++;

            TextView columnHeaderText = new TextView(getActivity());
            Utils.setCellParams(columnHeaderText, getSkin().getHeaderRowGravity(), getSkin().getCellPaddingLeft(),
                    getSkin().getCellPaddingRight(), getSkin().getCellPaddingTop(), getSkin().getCellPaddingBottom(),
                    getSkin().getBorderWidth(), getSkin().getBorderColor());
            columnHeaderText.setTextColor(getSkin().getHeaderRowTextColor());
            if(field.getFieldInfo().getLabel() != null) {
                columnHeaderText.setText(Localization.translate(field.getFieldInfo().getLabel()));
            }
            headerRow.addView(columnHeaderText, new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, getSkin().getHeaderRowHeight()));
            ((AFTable) component).setHeaderRow(headerRow);

            //create fake header row which will be added to content table to align items according to header row
            TextView fakeColumnHeaderText = new TextView(getActivity());
            Utils.setCellParams(fakeColumnHeaderText, getSkin().getContentGravity(), getSkin().getCellPaddingLeft(),
                    getSkin().getCellPaddingRight(), getSkin().getCellPaddingTop(), getSkin().getCellPaddingBottom(),
                    getSkin().getBorderWidth(), getSkin().getBorderColor());
            if(field.getFieldInfo().getLabel() != null) {
                fakeColumnHeaderText.setText(Localization.translate(field.getFieldInfo().getLabel()));
            }
            fakeContentRow.addView(fakeColumnHeaderText, new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 0));
        }
        //set numberOfColumns
        ((AFTable) component).setNumberOfColumns(numberOfColumns);

        //add header row to header table and assign header layout to table (to be able to get it later)
        headerTableLayout.addView(headerRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT));
        ((AFTable) component).setHeaderLayout(headerTableLayout);


        //add fake header row to content table and assign content layout to table (to be able to get it later)
        contentTableLayout.addView(fakeContentRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
        ((AFTable) component).setContentLayout(contentTableLayout);

        scrollView.addView(contentTableLayout);
        ((AFTable) component).setContentLayoutWrapper(scrollView);
        tableWrapper.addView(headerTableLayout);
        tableWrapper.addView(scrollView);

        return tableWrapper;
    }




}
