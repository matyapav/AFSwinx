package cz.cvut.fel.matyapav.afandroid.components.types;

import android.app.Activity;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.tomscz.afrest.commons.SupportedComponents;
import com.tomscz.afswinx.rest.connection.AFSwinxConnectionPack;
import com.tomscz.afswinx.rest.rebuild.holder.AFDataHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cz.cvut.fel.matyapav.afandroid.builders.skins.Skin;
import cz.cvut.fel.matyapav.afandroid.utils.Localization;
import cz.cvut.fel.matyapav.afandroid.utils.Utils;

/**
 * Created by Pavel on 20.02.2016.
 */
public class AFTable extends AFComponent {

    private TableLayout contentLayout;
    private TableLayout headerLayout;
    private TableRow headerRow;
    private List<TableRow> rows;
    private int numberOfColumns;
    private ScrollView contentLayoutWrapper;

    public AFTable() {
    }

    public AFTable(Activity activity, AFSwinxConnectionPack connectionPack, Skin skin) {
        super(activity, connectionPack, skin);
    }

    @Override
    public void insertData(String dataResponse, StringBuilder road) {
        //TODO potrebuje mirne predelat obdobne jako u listu
        List<String> longestRowList = new ArrayList<>();
        int longestRowLength = 0;
        try {
            JSONArray jsonArray = new JSONArray(dataResponse);
            for(int i=0; i<jsonArray.length(); i++){
                TableRow row = new TableRow(getActivity());
                row.setClickable(true);
                row.setFocusable(true);

                //list of strings within one row - used to determine longest row
                List<String> rowList = new ArrayList<>();
                int rowLength = 0;

                //iterate over columns
                Iterator<String> recordKeys = ((JSONObject) jsonArray.get(i)).keys();
                while(recordKeys.hasNext()) {
                    String column = recordKeys.next();
                    if(Utils.shouldBeInvisible(column, this)){
                        continue; //do not bother with invisible columns
                    }
                    //build cell
                    TextView cell = new TextView(getActivity());
                    setCellParams(cell, getSkin().getContentGravity(), getSkin().getCellPaddingLeft(),
                            getSkin().getCellPaddingRight(), getSkin().getCellPaddingTop(),
                            getSkin().getCellPaddingBottom(), getSkin().getBorderWidth(), getSkin().getBorderColor());
                    cell.setTextColor(getSkin().getContentTextColor());
                    String value = Localization.translate(((JSONObject) jsonArray.get(i)).
                            get(column).toString());
                    cell.setText(value);     //set text
                    row.addView(cell, new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, getSkin().getContentRowHeight()));

                    //add value in column to row list
                    rowList.add(value);
                    //increase length of row
                    rowLength += value.length();
                }
                getContentLayout().addView(row, new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT)); //add row to table
                addRow(row);
                //set longest row
                if(rowLength > longestRowLength){
                    longestRowList = rowList;
                    longestRowLength = rowLength;
                }
            }

            //add longest row to header as fake row with 0 height to have all columns properly sized
            TableRow fakeHeaderRow = new TableRow(getActivity());
            for (String column: longestRowList) {
                TextView columnView = new TextView(getActivity());
                setCellParams(columnView, getSkin().getHeaderRowGravity(), getSkin().getCellPaddingLeft(),
                        getSkin().getCellPaddingRight(), getSkin().getCellPaddingTop(), getSkin().getCellPaddingBottom(),
                        getSkin().getBorderWidth(), getSkin().getBorderColor());
                columnView.setText(column);
                fakeHeaderRow.addView(columnView, new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 0));
            }
            getHeaderLayout().addView(fakeHeaderRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

        } catch (JSONException e) {
            //TODO better exception handling
            System.err.println("CANNOT PARSE DATA");
            e.printStackTrace();
        }
    }

    @Override
    public SupportedComponents getComponentType() {
        return SupportedComponents.TABLE;
    }

    @Override
    public boolean validateData() {
        throw new UnsupportedOperationException("Table is read only");
    }

    @Override
    public AFDataHolder reserialize() {
        throw new UnsupportedOperationException("Table is read only");
    }

    public TableLayout getContentLayout() {
        return contentLayout;
    }

    public void setContentLayout(TableLayout contentLayout) {
        this.contentLayout = contentLayout;
    }

    public TableLayout getHeaderLayout() {
        return headerLayout;
    }

    public void setHeaderLayout(TableLayout headerLayout) {
        this.headerLayout = headerLayout;
    }

    public List<TableRow> getRows() {
        return rows;
    }

    public void addRow(TableRow row){
        if(rows == null){
            rows = new ArrayList<>();
        }
        rows.add(row);
    }

    public void setHeaderRow(TableRow headerRow) {
        this.headerRow = headerRow;
    }

    public TableRow getHeaderRow() {
        return headerRow;
    }

    public View getCellAt(int row, int col){
        return getRows().get(row).getChildAt(col);
    }

    public void setContentLayoutWrapper(ScrollView contentLayoutWrapper) {
        this.contentLayoutWrapper = contentLayoutWrapper;
    }

    public ScrollView getContentLayoutWrapper() {
        return contentLayoutWrapper;
    }

    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    private void setCellParams(TextView cell, int gravity, int paddingLeft, int paddingRight,
                               int paddingTop, int paddingBottom, int borderWidth, int borderColor){
        //create border
        ShapeDrawable rect = new ShapeDrawable(new RectShape());
        rect.getPaint().setStyle(Paint.Style.STROKE);
        rect.getPaint().setColor(borderColor);
        rect.getPaint().setStrokeWidth(borderWidth);

        cell.setGravity(gravity);
        cell.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        cell.setBackground(rect);
    }
}
