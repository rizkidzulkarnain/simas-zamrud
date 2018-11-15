package id.co.zamrud.emaszamrud.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.List;
import id.co.zamrud.emaszamrud.R;
import id.co.zamrud.emaszamrud.model.DataKajian;

public class KajianAdapter extends RecyclerView.Adapter<KajianAdapter.MyViewHolder> {
    private Context mContext;
    private List<DataKajian> dataKajianList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView fotoPenceramah;
        public TextView penceramah;
        public TextView kitabTema;
        public TextView waktu;
        public TextView tempat;

        public CardView card_viewkajian;

        public MyViewHolder(View view) {
            super(view);
            fotoPenceramah = (ImageView) view.findViewById(R.id.fotoPenceramah);
            penceramah = (TextView) view.findViewById(R.id.penceramah);
            kitabTema = (TextView) view.findViewById(R.id.kitabTema);
            waktu = (TextView) view.findViewById(R.id.waktu);
            tempat = (TextView) view.findViewById(R.id.tempat);
            card_viewkajian = (CardView) view.findViewById(R.id.card_viewkajian);
        }
    }

    public KajianAdapter(Context mContext, List<DataKajian> dataKajianList) {
        this.mContext = mContext;
        this.dataKajianList = dataKajianList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kajian, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        DataKajian dataKajian = dataKajianList.get(position);


        Glide.with(mContext).load(dataKajian.getFotoPenceramah()).into(holder.fotoPenceramah);
        holder.penceramah.setText(dataKajian.getPenceramah());
        holder.kitabTema.setText(dataKajian.getKitab() + " (" + dataKajian.getTema() +")");
        holder.waktu.setText(dataKajian.getWaktu());
        holder.tempat.setText(dataKajian.getTempat());
        holder.card_viewkajian.setBackgroundColor(ContextCompat.getColor(mContext, dataKajian.getColorBG()));
    }

    @Override
    public int getItemCount() {
        return dataKajianList.size();
    }
}
