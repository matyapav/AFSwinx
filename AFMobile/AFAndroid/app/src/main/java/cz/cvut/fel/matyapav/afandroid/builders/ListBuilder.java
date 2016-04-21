package cz.cvut.fel.matyapav.afandroid.builders;

import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.tomscz.afrest.commons.SupportedComponents;

import cz.cvut.fel.matyapav.afandroid.AFAndroid;
import cz.cvut.fel.matyapav.afandroid.components.types.AFComponent;
import cz.cvut.fel.matyapav.afandroid.components.types.AFList;

/**
 * This class is responsible for creating a list component, which presents bigger amount of data to user.
 *
 * @author Pavel Matyáš (matyapav@fel.cvut.cz)
 *
 * @since 1.0.0.
 */
public class ListBuilder extends AFComponentBuilder<ListBuilder> {

    @Override
    public AFList createComponent() throws Exception {
        //long start = System.currentTimeMillis();
        initializeConnections();
        AFList list = new AFList(getActivity(), getConnectionPack(), getSkin());

        //create form from response
        buildComponent(list);
        //long mezicas = (System.currentTimeMillis() - start);
        // System.err.println("Mezicas buildeni" + mezicas);
        //fill it with data (if there are some)zz
        String data = list.getDataResponse();
        if(data != null) {
            list.insertData(data);
        }
        AFAndroid.getInstance().addCreatedComponent(getComponentKeyName(), list);
        //System.err.println("Celkovy cas buildeni " + (System.currentTimeMillis() - start));
        return list;
    }

    @Override
    protected View buildComponentView(AFComponent component) {
        ListView listView = new ListView(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getSkin().getListWidth(), getSkin().getListHeight());
        params.setMargins(getSkin().getComponentMarginLeft(), getSkin().getComponentMarginTop(),
                getSkin().getComponentMarginRight(), getSkin().getComponentMarginBottom());
        listView.setLayoutParams(params);
        listView.setAdapter(null);
        //create border
        if(getSkin().getListBorderWidth() > 0) {
            RectShape rect = new RectShape();
            ShapeDrawable rectShapeDrawable = new ShapeDrawable(rect);
            Paint paint = rectShapeDrawable.getPaint();
            paint.setColor(getSkin().getListBorderColor());
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(getSkin().getListBorderWidth());
            listView.setBackground(rectShapeDrawable);
        }

        listView.setScrollbarFadingEnabled(!getSkin().isListScrollBarAlwaysVisible());
        ((AFList)component).setListView(listView);
        return listView;
    }




}
