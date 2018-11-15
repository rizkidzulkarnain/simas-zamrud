package id.co.zamrud.emaszamrud.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.zamrud.emaszamrud.R;
import id.co.zamrud.emaszamrud.model.DataLiqo;
import id.co.zamrud.emaszamrud.model.keuangan.DataKeuanganItemClass;
import id.co.zamrud.emaszamrud.module.keuangan.OnBottomReachedListener;

public class DataKeuanganAdapter extends RecyclerView.Adapter<DataKeuanganAdapter.MyViewHolder> {
    int mTotalAkhir = 0;
    private Context mContext;
    OnBottomReachedListener onBottomReachedListener;
    public List<DataKeuanganItemClass> dataKeuanganList;
    public List<DataKeuanganItemClass> dataKeuanganList_selected;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.jenis)
        TextView jenis;
        @BindView(R.id.jumlah)
        TextView jumlah;
        @BindView(R.id.tanggal)
        TextView tanggal;
        @BindView(R.id.layoutKeuangan)
        LinearLayout layoutKeuangan;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public DataKeuanganAdapter(Context mContext, List<DataKeuanganItemClass> dataKeuangan, List<DataKeuanganItemClass> dataKeuanganSelect) {
        this.mContext = mContext;
        this.dataKeuanganList = dataKeuangan;
        this.dataKeuanganList_selected= dataKeuanganSelect;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_keuangan, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        DataKeuanganItemClass dataKeuanganItem = dataKeuanganList.get(position);

        holder.jenis.setText(dataKeuanganItem.getJenis());
        holder.jumlah.setText(convertStringToCurr(dataKeuanganItem.getJumlah()));
        holder.tanggal.setText(dataKeuanganItem.getTanggal());
        mTotalAkhir += Integer.parseInt(dataKeuanganItem.getJumlah());

        if (position == dataKeuanganList.size() - 1){
            onBottomReachedListener.onBottomReached(position);
        }

        if (dataKeuanganList_selected.contains(dataKeuanganList.get(position))) {
            holder.layoutKeuangan.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_selected_state));
        } else {
            holder.layoutKeuangan.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_normal_state));
        }
    }

    public String convertStringToCurr(String inumber) {
        String ahasil;

        Double anumber = Double.valueOf(inumber);
        NumberFormat aformat = NumberFormat.getInstance(Locale.GERMANY);
        ahasil = aformat.format(anumber);

        return "Rp. " + ahasil;
    }

    public int getTotalAkhir() {
        return mTotalAkhir;
    }

    public void setTotalAkhir(int total) {
        mTotalAkhir = total;
    }

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener){
        this.onBottomReachedListener = onBottomReachedListener;
    }

    @Override
    public int getItemCount() {
        return dataKeuanganList.size();
    }
}
