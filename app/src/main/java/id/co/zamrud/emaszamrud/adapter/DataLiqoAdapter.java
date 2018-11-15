package id.co.zamrud.emaszamrud.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.zamrud.emaszamrud.R;
import id.co.zamrud.emaszamrud.model.DataLiqo;

public class DataLiqoAdapter extends RecyclerView.Adapter<DataLiqoAdapter.MyViewHolder> {
    private Context mContext;
    public List<DataLiqo> dataLiqoList;
    public List<DataLiqo> dataLiqoList_selected;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtAyatLiqo)
        TextView ayatLiqo;
        @BindView(R.id.txtTemaLiqo)
        TextView temaLiqo;
        @BindView(R.id.txtKeterangan)
        TextView keterangan;
        @BindView(R.id.txtPemasukan)
        TextView pemasukan;
        @BindView(R.id.txtSaldoAwal)
        TextView saldoAwal;
        @BindView(R.id.txtSaldoAkhir)
        TextView saldoAkhir;
        @BindView(R.id.txtTgl)
        TextView tglLiqo;
        @BindView(R.id.txtPJ)
        TextView pj;
        @BindView(R.id.cvDataLiqo)
        CardView cvDataLiqo;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public DataLiqoAdapter(Context mContext, List<DataLiqo> dataLiqo, List<DataLiqo> dataLiqoSelect) {
        this.mContext = mContext;
        this.dataLiqoList = dataLiqo;
        this.dataLiqoList_selected = dataLiqoSelect;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data_liqo, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        DataLiqo dataLiqo = dataLiqoList.get(position);

        String aket = dataLiqo.getKeterangan().equals("") ? "-" : dataLiqo.getKeterangan();

        holder.ayatLiqo.setText("Q.S. " + dataLiqo.getNama_surat() + "(" + dataLiqo.getSurat() + ") Ayat " + dataLiqo.getAyat() + " Juz " + dataLiqo.getJuz());
        holder.temaLiqo.setText(dataLiqo.getTema());
        holder.keterangan.setText(aket);
        holder.saldoAwal.setText(convertStringToCurr(dataLiqo.getSaldoAwal()));
        holder.pemasukan.setText(convertStringToCurr(dataLiqo.getPemasukan()));

        int saldoAkhir = Integer.valueOf(dataLiqo.getSaldoAwal()) + Integer.valueOf(dataLiqo.getPemasukan());
        holder.saldoAkhir.setText(convertStringToCurr(String.valueOf(saldoAkhir)));
        holder.tglLiqo.setText(dataLiqo.getTanggal());
        holder.pj.setText(dataLiqo.getPj());

        if (dataLiqoList_selected.contains(dataLiqoList.get(position))) {
            holder.cvDataLiqo.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_selected_state));
        } else {
            holder.cvDataLiqo.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_normal_state));
        }
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
        return dataLiqoList.size();
    }
}
