package com.imme.immeclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.journeyapps.barcodescanner.ViewfinderView;

/**
 * Created by sysadm@ilccourse.com on 2/5/2016.
 */
public class MainMenuAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private View MainMenuView;

    public MainMenuAdapter(Context c) {
        mContext = c;
        MainMenuView = new View(mContext);
    }

    public int getCount() {
        return MenuImage.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            MainMenuView = inflater.inflate(R.layout.grid_main_menu, null);
            MainMenuView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
            ImageView MainMenuImage = (ImageView) MainMenuView.findViewById(R.id.MainMenuImage);
            TextView MainMenuText = (TextView) MainMenuView.findViewById(R.id.MainMenuText);

            MainMenuImage.setImageResource(MenuImage[position]);
            MainMenuText.setText(MenuText[position]);
        } else {
            MainMenuView = (View) convertView;
        }
        return MainMenuView;
    }

    // references to our images
    private Integer[] MenuImage = {
            R.mipmap.main_button_send_icon,
            R.mipmap.main_button_receive_icon,
            R.mipmap.main_button_topup_icon,
            R.mipmap.main_button_near_me_icon,
            //R.mipmap.icon_transjakarta,
            //R.mipmap.icon_sevel
    };

    private String[] MenuText = {
            "Transfer",
            "Receive",
            "Deposit",
            "Near Me",
            //"Transjakarta",
            //"Seven Eleven"
    };
}