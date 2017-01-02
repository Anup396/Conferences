package com.project.anupamchugh.conferences.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.project.anupamchugh.conferences.R;
import com.project.anupamchugh.conferences.models.Users;

import java.util.List;

/**
 * Created by anupamchugh on 31/12/16.
 */

public class InviteListAdapter extends BaseAdapter {
    private List<Users> mData;
    private boolean fromAdmin;

    public InviteListAdapter(List<Users> doctorList, boolean fromAdminEnd) {
        mData=doctorList;
        fromAdmin=fromAdminEnd;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Users getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO implement you own logic with ID
        return mData.get(position).user_id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.invite_list_row, parent, false);
        } else {
            result = convertView;
        }


        Users item = getItem(position);
        ((CheckBox) result.findViewById(R.id.checkBox)).setClickable(false);

        // TODO replace findViewById by ViewHolder
        ((TextView) result.findViewById(R.id.doctorName)).setText(item.username);
        if(fromAdmin)
        ((CheckBox) result.findViewById(R.id.checkBox)).setChecked(item.selected);
        else
            ((CheckBox) result.findViewById(R.id.checkBox)).setChecked(!item.selected);





        return result;
    }
}

