package com.example.tsmc_prize_simulation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class PrizeItemAdapter extends ArrayAdapter<HistoryQueryResponseResult> {

    private Context mContext;
    String tag = getClass().toString();
    static class ViewHolder {
        TextView mIndex;
        TextView mInvoiceDate;
        TextView mInvoiceNo;
        TextView mStatus;
        TextView mIsPrint;
    }

    public PrizeItemAdapter(Context ctx) {
        super(ctx, R.layout.prize_list_item);

        mContext = ctx;
    }
    static int _position;
    ViewGroup _parent;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        _position = position;
        _parent = parent;
        if (null == convertView) {

            LayoutInflater inflater =  (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.prize_list_item, null);

            holder = new ViewHolder();
            holder.mIndex = convertView.findViewById(R.id.txt_index_prize);
            holder.mInvoiceNo = convertView.findViewById(R.id.txt_inv_no_prize);
            holder.mInvoiceDate = convertView.findViewById(R.id.txt_term_prize);
            holder.mStatus = convertView.findViewById(R.id.txt_class_prize);
            holder.mIsPrint = convertView.findViewById(R.id.txt_is_print_prize);
            convertView.setTag(holder);
        } else {
            holder = (PrizeItemAdapter.ViewHolder) convertView.getTag();
        }

        HistoryQueryResponseResult entry = getItem(position);
        holder.mIndex.setText(String.valueOf(position + 1));

        if (null != entry) {
//            convertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.d(tag,"convertView position="+_position);
//                    Toast.makeText(ctx_main, "onItemClick!", Toast.LENGTH_SHORT).show();
//
//                    ctx_main.enablePrizeOKButton(true);
//                    ctx_main.mSelectedPrizeEntry = (HistoryQueryResponseResult) ((ListView)_parent).getItemAtPosition(_position);
//
//                    Global.selected_jsonobj = (JSONObject) Global.json_array_4card.get(_position);
//                }
//            });
            holder.mInvoiceNo.setText(entry.number);
            holder.mStatus.setText(entry.winningStatus);

            //display date format
            Date displayDate = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            dateFormat.setTimeZone(TimeZone.getTimeZone("TAIWAN"));
//            try {
//                displayDate = dateFormat.parse(entry.date);
//            } catch (ParseException | java.text.ParseException e) {
//                e.printStackTrace();
//            }
//            if (null != displayDate) {
//                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss", Locale.TAIWAN);
//                holder.mInvoiceDate.setText(df.format(displayDate));
//            }
            holder.mInvoiceDate.setText(entry.date);
            //display IsPrint
            String tmp = entry.isPrinted;
            if (entry.isPrinted.equals("0")) {
                holder.mIsPrint.setText("未列印");
            } else if (entry.isPrinted.equals("1")) {
                holder.mIsPrint.setText("已列印");
            }
        } else {
            holder.mInvoiceNo.setText("");
            holder.mInvoiceDate.setText("");
            holder.mStatus.setText("");
            holder.mIsPrint.setText("");
        }
        return convertView;
    }

}
