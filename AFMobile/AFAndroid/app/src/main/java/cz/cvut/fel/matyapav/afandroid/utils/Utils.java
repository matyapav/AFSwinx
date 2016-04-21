package cz.cvut.fel.matyapav.afandroid.utils;

import android.annotation.TargetApi;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.widget.TextView;

import com.tomscz.afrest.commons.SupportedWidgets;
import com.tomscz.afswinx.rest.connection.AFSwinxConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.cvut.fel.matyapav.afandroid.components.types.AFComponent;
import cz.cvut.fel.matyapav.afandroid.components.parts.AFField;

/**
 * Util methods used within the application.
 *
 * @author Pavel Matyáš (matyapav@fel.cvut.cz)
 *
 * @since 1.0.0.
 */
public class Utils {

    /**
     * Converts input stream to string.
     *
     * @param inputStream input stream to convert
     * @return converted string
     * @throws IOException thew if input stream cannot be read.
     */
    public static String convertInputStreamToString(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    /**
     * Determines if field is writable based on widget type, that means user can write into it.
     *
     * @param widgetType widget type of field
     * @return true if the field is writable, false otherwise
     */
    public static boolean isFieldWritable(SupportedWidgets widgetType){
        return widgetType.equals(SupportedWidgets.TEXTFIELD) || widgetType.equals(SupportedWidgets.NUMBERFIELD)
                || widgetType.equals(SupportedWidgets.NUMBERDOUBLEFIELD) || widgetType.equals(SupportedWidgets.PASSWORD);
    }

    /**
     * Determine if field is number field, that means user can only write numbers in it.
     *
     * @param field field to check
     * @return true if field is number field, false otherwise
     */
    public static boolean isFieldNumberField(AFField field){
        return field.getFieldInfo().getWidgetType().equals(SupportedWidgets.NUMBERFIELD)
                || field.getFieldInfo().getWidgetType().equals(SupportedWidgets.NUMBERDOUBLEFIELD);
    }

    /**
     * Gets connection end point URL made from parts saved in given connection.
     *
     * @param connection holds all parts of end point
     * @return string representing connection end point
     */
    public static String getConnectionEndPoint(AFSwinxConnection connection){
        StringBuilder endPointBuilder = new StringBuilder();
        if(connection.getProtocol() != null && !connection.getProtocol().isEmpty()){
            endPointBuilder.append(connection.getProtocol());
            endPointBuilder.append("://");
        }
        if(connection.getAddress() != null && !connection.getAddress().isEmpty()){
            endPointBuilder.append(connection.getAddress());
        }
        if(connection.getPort() != 0){
            endPointBuilder.append(":");
            endPointBuilder.append(connection.getPort());
        }
        if(connection.getParameters() != null && !connection.getParameters().isEmpty()){
            endPointBuilder.append(connection.getParameters());
        }
        return endPointBuilder.toString();
    }

    /**
     * Determines if column in list should be invisible or not.
     *
     * @param column specified column to check
     * @param component component to which should be column added
     * @return true, if should be invisible, false otherwise
     */
    public static boolean shouldBeInvisible(String column, AFComponent component) {
        for(AFField field: component.getFields()){
            if(field.getId().equals(column)){
                return !field.getFieldInfo().getVisible();
            }
        }
        return true;
    }

    /**
     * Sets parameters to cell in table component.
     *
     * @param cell correcponting cell
     * @param gravity gravity of cell to be set
     * @param paddingLeft left padding of cell in pixels
     * @param paddingRight right padding of cell in pixels
     * @param paddingTop top padding of cell in pixels
     * @param paddingBottom bottom padding of cell in pixels
     * @param borderWidth width of cell border in pixels
     * @param borderColor color of cell border
     */
    @TargetApi(16)
    public static void setCellParams(TextView cell, int gravity, int paddingLeft, int paddingRight,
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

    /**
     * Parses string into date using ISO format or dd.MM.yyyy format.
     *
     * @param date string representation of date
     * @return parsed date if it was in one of formats, null if date is not in specified formats.
     */
    public static Date parseDate(String date){
    String[] formats = {"yyyy-MM-dd'T'HH:mm:ss.SSSZ", "dd.MM.yyyy"};
        if(date != null){
            for (String format: formats) {
                SimpleDateFormat formatter = new SimpleDateFormat(format);
                try{
                    return formatter.parse(date);
                } catch (ParseException e) {
                    System.err.println("Cannot parse date "+date+" using format "+ format);
                }
            }
        }
        return null;
    }


}
