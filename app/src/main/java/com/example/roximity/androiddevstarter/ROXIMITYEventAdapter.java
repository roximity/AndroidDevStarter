package com.example.roximity.androiddevstarter;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.roximity.sdk.messages.ROXEventInfo;
import com.roximity.sdk.messages.ROXIMITYAction;
import com.roximity.sdk.messages.ROXIMITYSignal;

import java.util.ArrayList;

/**
 * Created by colerichards on 6/9/16.
 */
public class ROXIMITYEventAdapter extends ArrayAdapter<ROXEventInfo> {
    public ROXIMITYEventAdapter(Context context, ArrayList<ROXEventInfo> results) {
        super(context, 0, results);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ROXEventInfo result = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.roximity_event_item, parent, false);
        }

        // Lookup view for data population
        TextView tvEventName = (TextView) convertView.findViewById(R.id.tvEventName);
        TextView tvSignalName = (TextView) convertView.findViewById(R.id.tvSignalName);
        TextView tvTriggerEvent = (TextView) convertView.findViewById(R.id.tvTriggerEvent);
        TextView tvPresentationType = (TextView) convertView.findViewById(R.id.tvPresentationType);
        TextView tvEventMessage = (TextView) convertView.findViewById(R.id.tvMessage);
        TextView tvTimestamp = (TextView) convertView.findViewById(R.id.tvTimestamp);

        TextView tvNameLabel = (TextView) convertView.findViewById(R.id.nameLabel);
//            TextView tvSignalLabel = (TextView) convertView.findViewById(R.id.signalLabel);
//            TextView tvEventTypeLabel = (TextView) convertView.findViewById(R.id.tvEventTypeLabel);
//            TextView tvActionStyleLabel = (TextView) convertView.findViewById(R.id.tvActionStyleLabel);
        TextView tvMessageLabel = (TextView) convertView.findViewById(R.id.tvMessageLabel);


        ROXIMITYAction action = result.getROXIMITYAction();
        ROXIMITYSignal signal = result.getROXIMITYSignal();

        // Populate the data into the template view using the Action and Signal objects
        //SETTING UP THE ACTION NAME
        if (action.getName().length() <= 1) {
            tvNameLabel.setText("");
        } else {
            tvNameLabel.setText("NAME:   ");
        }
        tvEventName.setText(action.getName());

        //SETTING THE SIGNAL NAME
        String space = " - ";
        if (signal.getName().length() <= 1) {
            space = "";
            tvNameLabel.setText("");
        }
        String completeName = signal.getName() + space + signal.getType().toString();
        tvSignalName.setText(completeName);

        //SETTING THE EVENT TYPE (EG ENTRY, EXIT, PROXIMITY, ETC.)
        tvTriggerEvent.setText(action.getTriggerEvent().toString());

        //SETTING THE ACTION PRESENTATION TYPE (EG REQUEST, WEBVIEW, ETC.)
        tvPresentationType.setText(action.getPresentationType().toString());

        //SETTING ACTIONS OR TAGS DEPENDING ON AVAILABILITY
        if (action.getMessage().length() <= 0) {
            tvMessageLabel.setText("TAGS:   ");
            tvEventMessage.setText(signal.getTags().toString());
        } else {
            tvEventMessage.setText(action.getMessage());
            tvMessageLabel.setText("MESSAGE:   ");
        }
        
        //SETTING THE TIMESTAMP OF THE EVENT
        String timestampString = convertDate(result.getTimestamp(), "MM/dd/yy hh:mm:ss");
        tvTimestamp.setText(timestampString);
        // Return the completed view to render on screen
        return convertView;
    }

    private String convertDate(long dateInMilliseconds, String dateFormat) {
        return DateFormat.format(dateFormat, dateInMilliseconds).toString();
    }
}
