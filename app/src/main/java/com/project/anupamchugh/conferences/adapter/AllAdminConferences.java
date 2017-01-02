package com.project.anupamchugh.conferences.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.project.anupamchugh.conferences.R;
import com.project.anupamchugh.conferences.models.AllConferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by anupamchugh on 09/02/16.
 */
public class AllAdminConferences extends ArrayAdapter<AllConferences> {

    private List<AllConferences> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtDate;
        TextView txtDescription;
        TextView txtCreatedBy;
        TextView txtCreatedAt;
        TextView txtModifiedAt;
    }



    public AllAdminConferences(List<AllConferences> data, Context context) {
        super(context, R.layout.conference_admin_items, data);
        this.dataSet = data;
        this.mContext=context;


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        AllConferences conferences = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.conference_admin_items, parent, false);
            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.date);
            viewHolder.txtDescription = (TextView) convertView.findViewById(R.id.description);
            viewHolder.txtCreatedBy = (TextView) convertView.findViewById(R.id.createdBy);
            viewHolder.txtCreatedAt = (TextView) convertView.findViewById(R.id.createdAt);
            viewHolder.txtModifiedAt = (TextView) convertView.findViewById(R.id.modifiedAt);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.txtDate.setText("Scheduled on : " +conferences.date);


        viewHolder.txtDescription.setText("Description : "+ conferences.description);
        viewHolder.txtCreatedBy.setText("Created By : "+conferences.createdBy);
        viewHolder.txtCreatedAt.setText("Created At: "+ convertStringtoDate(conferences.createdAt));
        viewHolder.txtModifiedAt.setText("Modified At: "+convertStringtoDate(conferences.modifiedAt));
        // Return the completed view to render on screen
        return convertView;
    }

    public String convertStringtoDate(String dateString)
    {

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d = dateFormat.parse(dateString);


            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy - hh:mm:ss a", Locale.US);

            String string = simpleDateFormat.format(d);

            return string;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }


}
