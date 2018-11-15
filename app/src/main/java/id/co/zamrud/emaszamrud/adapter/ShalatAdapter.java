package id.co.zamrud.emaszamrud.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import id.co.zamrud.emaszamrud.R;
import id.co.zamrud.emaszamrud.model.shalat.DataShalatItem;

public class ShalatAdapter extends RecyclerView.Adapter<ShalatAdapter.MyViewHolder> {
    private Context mContext;
    private List<DataShalatItem> dataShalatItemList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView waktuShubuh, waktuDhuhur, waktuAshar, waktuMaghrib, waktuIsya;
        public LinearLayout layoutShalat;

        public MyViewHolder(View view) {
            super(view);
            waktuShubuh = (TextView) view.findViewById(R.id.waktuShubuh);
            waktuDhuhur = (TextView) view.findViewById(R.id.waktuDhuhur);
            waktuAshar = (TextView) view.findViewById(R.id.waktuAshar);
            waktuMaghrib = (TextView) view.findViewById(R.id.waktuMaghrib);
            waktuIsya = (TextView) view.findViewById(R.id.waktuIsya);
            layoutShalat = (LinearLayout) view.findViewById(R.id.layoutShalat);
        }
    }

    public ShalatAdapter(Context mContext, List<DataShalatItem> dataShalatItemList) {
        this.mContext = mContext;
        this.dataShalatItemList = dataShalatItemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shalat, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        DataShalatItem dataShalatItem = dataShalatItemList.get(position);

        holder.waktuShubuh.setText(changeFormatTime(dataShalatItem.getFajr()));
        holder.waktuDhuhur.setText(changeFormatTime(dataShalatItem.getDhuhr()));
        holder.waktuAshar.setText(changeFormatTime(dataShalatItem.getAsr()));
        holder.waktuMaghrib.setText(changeFormatTime(dataShalatItem.getMaghrib()));
        holder.waktuIsya.setText(changeFormatTime(dataShalatItem.getIsha()));
    }

    @Override
    public int getItemCount() {
        return dataShalatItemList.size();
    }

    private String changeFormatTime(String itime){
        String time = "";
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("h:mm aa");
            Date myDate = sdf.parse(itime);

            time = new SimpleDateFormat("hh:mm").format(myDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }
}
