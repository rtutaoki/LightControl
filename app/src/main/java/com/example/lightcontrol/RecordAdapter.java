package com.example.lightcontrol;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder>{
    private List<Record> mRwcordList;
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView recordName;
        TextView recordTime;
        TextView recordAverageLight;
        public ViewHolder(View view) {
            super(view);
            recordName = (TextView) view.findViewById(R.id.record_name);
            recordTime = (TextView) view.findViewById(R.id.record_time);
            recordAverageLight = (TextView) view.findViewById(R.id.record_aveagelight);
        }
    }
    public RecordAdapter(List<Record> recordList) {
        mRwcordList = recordList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Record record = mRwcordList.get(position);
        holder.recordName.setText(record.getName());
        holder.recordTime.setText(record.getTime());
        holder.recordAverageLight.setText(record.getAveragelight()+" Lux");
    }
    @Override
    public int getItemCount() {
        return mRwcordList.size();
    }

}
