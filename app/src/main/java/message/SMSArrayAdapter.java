package message;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bms.user.bmssmartwatch.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

public class SMSArrayAdapter extends BaseAdapter {

    private String LOG_TAG = "debugdata";

    private Context context;
    private List<String> messages;

    public SMSArrayAdapter(Context context, List<String> lstSMS)
    {
        this.context = context;
        this.messages = lstSMS;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    public void replaceData(List<String> data)
    {
        messages = data;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = view;
        String sms = messages.get(i);

        if (rowView == null) {
            Log.d(LOG_TAG, "SmsAdapter getView : " + sms);
            //LayoutInflater mInflater=(LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            rowView= LayoutInflater.from(context).inflate(R.layout.simple_row, viewGroup, false);

            //rowView = mInflater.inflate(R.layout.simple_row,null);
            TextView label = (TextView) rowView.findViewById(R.id.label);
            label.setText(sms);
        }else{
            TextView label = (TextView) rowView.findViewById(R.id.label);
            label.setText(sms);
        }

       return rowView;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }
}
