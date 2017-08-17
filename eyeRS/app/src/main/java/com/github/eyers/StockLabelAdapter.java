package com.github.eyers;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public final class StockLabelAdapter extends ArrayAdapter<StockLabel> {

    float close = -1;

    public StockLabelAdapter(Context context, ArrayList<StockLabel> stocks) {
        super(context, R.layout.item_stock_label, stocks);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final StockLabel stock = getItem(position);
        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_stock_label, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.lblName);
            viewHolder.symbol = (TextView) convertView.findViewById(R.id.lblSymbol);
            viewHolder.shift = (TextView) convertView.findViewById(R.id.lblShift);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(stock.getName());
        viewHolder.symbol.setText(stock.getSector());
        viewHolder.shift.setText(stock.getSymbol());

//        Thread t = new Thread() {
//
//            @Override
//            public void run() {
//                final StockQuoteRequest request = new StockQuoteRequest();
//                request.setSymbol("JSE:" + stock.getSymbol());
//                request.setStartDate(DateUtils.yearStart());
//                request.setEndDate(DateUtils.yearEnd());
//
//                try {
//                    request.next();
//                    close = request.getClose();
//                } catch (IOException ioe) {
////                        throw new RuntimeException(ioe);
//                }
//
//                ((ListActivity) getContext()).runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        viewHolder.shift.setText(String.format("%.2f", close) + "%");
//                    }
//                });
//            }
//        };
//        t.setPriority(Thread.MIN_PRIORITY);
//        t.start();


        return convertView;
    }

    /**
     * View lookup cache.
     */
    private class ViewHolder {

        TextView name;
        TextView symbol;
        TextView shift;
    }
}
