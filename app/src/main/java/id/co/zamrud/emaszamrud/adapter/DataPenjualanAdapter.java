package id.co.zamrud.emaszamrud.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.zamrud.emaszamrud.MainActivity;
import id.co.zamrud.emaszamrud.R;
import id.co.zamrud.emaszamrud.model.DataPenjualanClass;
import id.co.zamrud.emaszamrud.model.DataPenjualanItemClass;
import id.co.zamrud.emaszamrud.model.MenuClass;
import id.co.zamrud.emaszamrud.module.LoginActivity;
import id.co.zamrud.emaszamrud.module.shop.ShopActivity;

public class DataPenjualanAdapter extends RecyclerView.Adapter<DataPenjualanAdapter.MyViewHolder> {
    private Context mContext;
    public List<DataPenjualanItemClass> dataPenjualanList;
    public List<DataPenjualanItemClass> dataPenjualanList_selected = new ArrayList<>();
    int mTotalAkhir = 0;
    TextView mTxtTotalAkhir;

    SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    SimpleDateFormat mSimpleDateFormat3 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.number)
        TextView number;
        @BindView(R.id.txtNamaProd)
        TextView namaProd;
        @BindView(R.id.txtHarga)
        TextView harga;
        @BindView(R.id.txtJmlProd)
        TextView jmlProd;
        @BindView(R.id.txtTotal)
        TextView total;
        @BindView(R.id.tglInput)
        TextView tglInput;
        @BindView(R.id.keterangan)
        TextView keterangan;
        @BindView(R.id.cvPenjualan)
        CardView cvPenjualan;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public DataPenjualanAdapter(Context mContext,
                                List<DataPenjualanItemClass> dataPenjualanList,
                                List<DataPenjualanItemClass> multiSelectPenjualanList) {
        this.mContext = mContext;
        this.dataPenjualanList = dataPenjualanList;
        this.dataPenjualanList_selected = multiSelectPenjualanList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_report_penjualan, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        DataPenjualanItemClass dataPenjualanItemClass = dataPenjualanList.get(position);

        boolean isSudahBayar = dataPenjualanItemClass.getIsSudahBayar().equals("1") ? true : false;
        String aket = dataPenjualanItemClass.getKeterangan().equals("") ? "-" : dataPenjualanItemClass.getKeterangan();

        holder.number.setText(String.valueOf(position + 1));
        holder.namaProd.setText(dataPenjualanItemClass.getProduk());
        holder.harga.setText(convertStringToCurr(dataPenjualanItemClass.getHarga()));
        holder.jmlProd.setText(dataPenjualanItemClass.getJumlah());
        holder.tglInput.setText(convertDateFormat2(dataPenjualanItemClass.getTglInput()));
        holder.keterangan.setText(aket);

        int total = Integer.parseInt(dataPenjualanItemClass.getHarga()) *
                Integer.parseInt(dataPenjualanItemClass.getJumlah());
        holder.total.setText(convertStringToCurr(String.valueOf(total)));

        if (!isSudahBayar) {
            holder.cvPenjualan.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_belum_bayar));
            holder.number.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle_number_red));
        }

        if (dataPenjualanList_selected.contains(dataPenjualanList.get(position))) {
            if (!isSudahBayar) {
                holder.cvPenjualan.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_selected_state));
                holder.number.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle_number));
            } else {
                holder.cvPenjualan.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_selected_state));
                holder.number.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle_number));
            }
        } else {
            if (!isSudahBayar) {
                holder.cvPenjualan.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_belum_bayar));
                holder.number.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle_number_red));
            } else {
                holder.cvPenjualan.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_normal_state));
                holder.number.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle_number));
            }
        }
        mTotalAkhir += total;
    }

    public String convertDateFormat2(String idate) {
        Date adate = null;
        try {
            adate = mSimpleDateFormat3.parse(idate);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return mSimpleDateFormat.format(adate);
    }

    private String convertStringToCurr(String inumber) {
        String ahasil;

        Double anumber = Double.valueOf(inumber);
        NumberFormat aformat = NumberFormat.getInstance(Locale.GERMANY);
        ahasil = aformat.format(anumber);

        return "Rp. " + ahasil;
    }

    @Override
    public int getItemCount() {
        return dataPenjualanList.size();
    }

    public String getTotalAkhir() {
        return convertStringToCurr(String.valueOf(mTotalAkhir));
    }

    public int getTotalAkhirNotCurr() {
        return mTotalAkhir;
    }
}
