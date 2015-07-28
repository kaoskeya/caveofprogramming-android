package com.lostinkaos.android.customlists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by keya on 28/7/15.
 */
public class MessageAdapter extends BaseAdapter implements ListAdapter {

    private List<Message> messages;
    private Context context;

    public MessageAdapter(Context context, List<Message> messages) {
        this.messages = messages;
        this.context = context;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return messages.get(i).getId();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.list_message_item, null);

        Message message = messages.get(i);

        String title = message.getTitle();
        String sender = message.getSender();

        TextView titleView = (TextView) view.findViewById(R.id.list_message_title);
        TextView senderView = (TextView) view.findViewById(R.id.list_message_sender);

        titleView.setText(title);
        senderView.setText(sender);

        return view;
    }
}
