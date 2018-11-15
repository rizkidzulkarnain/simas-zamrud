package id.co.zamrud.emaszamrud.module.keuangan2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.applikeysolutions.cosmocalendar.model.Day;
import com.applikeysolutions.cosmocalendar.utils.SelectionType;
import com.applikeysolutions.cosmocalendar.view.CalendarView;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.zamrud.emaszamrud.R;
import id.co.zamrud.emaszamrud.adapter.DataKeuanganAdapter;
import id.co.zamrud.emaszamrud.adapter.DataLiqoAdapter;
import id.co.zamrud.emaszamrud.database.ApiGenerator;
import id.co.zamrud.emaszamrud.database.IDatabaseClient;
import id.co.zamrud.emaszamrud.model.DataLiqo;
import id.co.zamrud.emaszamrud.model.DataLiqoClass;
import id.co.zamrud.emaszamrud.model.PostExecuteDatabase;
import id.co.zamrud.emaszamrud.model.keuangan.DataKeuanganClass;
import id.co.zamrud.emaszamrud.model.keuangan.DataKeuanganItemClass;
import id.co.zamrud.emaszamrud.module.LoginActivity;
import id.co.zamrud.emaszamrud.module.MenuActivity;
import id.co.zamrud.emaszamrud.module.keuangan.OnBottomReachedListener;
import id.co.zamrud.emaszamrud.module.liqo.AddDataLiqoActivity;
import id.co.zamrud.emaszamrud.module.liqo.EditDataLiqoActivity;
import id.co.zamrud.emaszamrud.util.AlertDialogClass2;
import id.co.zamrud.emaszamrud.util.Config;
import id.co.zamrud.emaszamrud.util.ConvertDateFormat;
import id.co.zamrud.emaszamrud.util.RecyclerItemClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Keuangan2Activity extends AppCompatActivity {
    @BindView(R.id.calendar_view_keuangan)
    CalendarView mCalendarView;
    @BindView(R.id.btGetData)
    Button mBtnGetData;
    @BindView(R.id.rvDataPemasukan)
    RecyclerView mRvDataPemasukan;
    @BindView(R.id.rvDataPengeluaran)
    RecyclerView mRvDataPengeluaran;
    @BindView(R.id.txtErrorPemasukan)
    TextView mTxtErrorPemasukan;
    @BindView(R.id.txtErrorPengeluaran)
    TextView mTxtErrorPengeluaran;
    @BindView(R.id.txtTotalPemasukan)
    TextView mTxtTotalPemasukan;
    @BindView(R.id.txtTotalPengeluaran)
    TextView mTxtTotalPengeluaran;
    @BindView(R.id.txtTotalAkhir)
    TextView mTxtTotalAkhir;


    Menu context_menu;
    boolean isMultiSelect;
    ActionMode mActionMode;
    IDatabaseClient mClient;
    DataKeuanganAdapter mAdapter;
    ProgressDialog mProgreesDialog;
    DataKeuanganClass mDataKeuangan;
    DataKeuanganClass mMultiDataKeuangan;

    int mTotalPemasukan = 0, mTotalPengeluaran = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keuangan);
        ButterKnife.bind(this);

        mProgreesDialog = new ProgressDialog(this);
        mProgreesDialog.setMessage("Authenticating...");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setLayoutCalendar();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Keuangan2Activity.this);
        mRvDataPemasukan.setLayoutManager(mLayoutManager);
    }

    @OnClick(R.id.btGetData)
    public void getData(View view) {
        String tanggal2 = getTanggal();
        if (!tanggal2.equals("")) {
            Config.getVibrate(this);
            mProgreesDialog.show();
            CallGetDataKeuangan(tanggal2);
        }else{
            runAlertDialog("Pilih tanggal dulu", 1);
        }
    }

    public String getTanggal() {
        String tanggal2 = "";
        if (mCalendarView.getSelectedDays().size() > 0) {
            Day day = mCalendarView.getSelectedDays().get(0);
            String date = day.toString().substring(day.toString().indexOf("{") + 1, day.toString().indexOf("}"));
            String[] split = date.split(" ");
            String tanggal = split[2] + " " + split[1] + " " + split[split.length - 1];
            tanggal2 = ConvertDateFormat.Convert(tanggal);
        }
        return tanggal2;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_data_liqo, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                intent = new Intent(Keuangan2Activity.this, MenuActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                //runAlertDialog3("Apakah anda yakin ingin logout ?", 3, "logout");
                return true;
            case R.id.add_data:
                if (Config.IS_LOGIN) {
                    /*String tanggal2 = getTanggal();
                    if (!tanggal2.equals("")) {
                        Config.getVibrate(this);

                        Date adate = null;
                        try {
                            adate = new SimpleDateFormat("yyyy-MM-dd").parse(tanggal2);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(adate);
                        boolean isKamis = cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY;

                        if(isKamis) {
                            mProgreesDialog.show();
                            CallGetAnyDataLiqoAPI(tanggal2);
                        }else{
                            runAlertDialog("Hanya bisa pilih hari kamis (warna hijau) !", 1);
                        }
                    }else{
                        runAlertDialog("Pilih tanggal dulu", 1);
                    }*/
                } else {
                    runAlertDialog3("Silahkan login terlebih dahulu !", 3, "LOGIN");
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void CallGetDataKeuangan(String itgl) {
        mDataKeuangan = new DataKeuanganClass();
        mMultiDataKeuangan = new DataKeuanganClass();
        mMultiDataKeuangan.setDataKeuangan(new ArrayList<DataKeuanganItemClass>());
        isMultiSelect = false;

        mClient = ApiGenerator.createService(IDatabaseClient.class);
        final Call<DataKeuanganClass> call = mClient.getDataPemasukan(itgl);
        call.enqueue(new Callback<DataKeuanganClass>() {
            @Override
            public void onResponse(Call<DataKeuanganClass> call, Response<DataKeuanganClass> response) {
                mDataKeuangan = response.body();
                if (mDataKeuangan != null) {
                    if (mDataKeuangan.getStatus() == 1) {
                        mAdapter = new DataKeuanganAdapter(
                                Keuangan2Activity.this,
                                mDataKeuangan.getDataKeuangan(),
                                mMultiDataKeuangan.getDataKeuangan()
                        );
                        mRvDataPemasukan.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        mAdapter.setOnBottomReachedListener(new OnBottomReachedListener() {
                            @Override
                            public void onBottomReached(int position) {
                                mTotalPemasukan = mAdapter.getTotalAkhir();
                                mTxtTotalPemasukan.setText(mAdapter.convertStringToCurr(String.valueOf(mTotalPemasukan)));
                                /*CallGetDataPengeluaran(itgl);
                                showAnyDataPemasukan();*/
                                mProgreesDialog.dismiss();
                                showAnyData();
                            }
                        });
                    } else {
                        runAlertDialog(mDataKeuangan.getMessage(), 1);
                        showEmptyData();
                        mProgreesDialog.dismiss();
                    }
                } else {
                    runAlertDialog("Gagal Mendapatkan Data", 1);
                    showEmptyData();
                    mProgreesDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<DataKeuanganClass> call, Throwable t) {
                showEmptyData();
                runAlertDialog(t.getMessage(), 1);
                mProgreesDialog.dismiss();
            }
        });
    }

    /*public void CallGetAnyDataLiqoAPI(String itgl) {
        mClient = ApiGenerator.createService(IDatabaseClient.class);
        final Call<DataLiqoClass> call = mClient.getDataLiqo(itgl);
        call.enqueue(new Callback<DataLiqoClass>() {
            @Override
            public void onResponse(Call<DataLiqoClass> call, Response<DataLiqoClass> response) {
                mDataLiqo = response.body();
                if (mDataLiqo != null) {
                    if (mDataLiqo.getStatus() == 1) {
                        runAlertDialog("Sudah ada data pada tgl tersebut !", 3);
                        mProgreesDialog.dismiss();
                    } else {
                        Intent intent = new Intent(Keuangan2Activity.this, AddDataLiqoActivity.class);
                        intent.putExtra("tanggal", itgl);
                        startActivity(intent);
                        mProgreesDialog.dismiss();
                    }
                } else {
                    runAlertDialog("Error saat cek ketersediaan tanggal", 1);
                    mProgreesDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<DataLiqoClass> call, Throwable t) {
                runAlertDialog(t.getMessage(), 1);
                mProgreesDialog.dismiss();
            }
        });
    }*/

    /*public void multi_select(int position) {
        if (mActionMode != null) {
            if (mMultiSelectDataLiqo.getDataLiqo().contains(mDataLiqo.getDataLiqo().get(position)))
                mMultiSelectDataLiqo.getDataLiqo().remove(mDataLiqo.getDataLiqo().get(position));
            else
                mMultiSelectDataLiqo.getDataLiqo().add(mDataLiqo.getDataLiqo().get(position));
            if (mMultiSelectDataLiqo.getDataLiqo().size() > 0) {
                mActionMode.setTitle("" + mMultiSelectDataLiqo.getDataLiqo().size());
                if (mMultiSelectDataLiqo.getDataLiqo().size() == 1) {
                    mActionMode.getMenu().findItem(R.id.action_edit).setVisible(true);
                    mActionMode.getMenu().findItem(R.id.action_delete).setVisible(true);
                } else {
                    mActionMode.getMenu().findItem(R.id.action_delete).setVisible(true);
                    mActionMode.getMenu().findItem(R.id.action_edit).setVisible(false);
                }
            } else {
                mActionMode.setTitle("0");
                mActionMode.getMenu().findItem(R.id.action_edit).setVisible(false);
                mActionMode.getMenu().findItem(R.id.action_delete).setVisible(false);
            }
            refreshAdapter();
        }
    }*/

    /*public void refreshAdapter() {
        mAdapter.dataLiqoList_selected = mMultiSelectDataLiqo.getDataLiqo();
        mAdapter.dataLiqoList = mDataLiqo.getDataLiqo();
        mAdapter.notifyDataSetChanged();
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        private int statusBarColor;

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.penjualan_multi_select, menu);
            context_menu = menu;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                statusBarColor = getWindow().getStatusBarColor();
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            }
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    runAlertDialog2("Apakah yakin ingin menghapus ?", 3);
                    return true;
                case R.id.action_edit:
                    callEditDataLiqo();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(statusBarColor);
            }
            mActionMode = null;
            isMultiSelect = false;
            mMultiSelectDataLiqo.setDataLiqo(new ArrayList<DataLiqo>());
            refreshAdapter();
        }
    };

    private void callEditDataLiqo() {
        if (mMultiSelectDataLiqo.getDataLiqo().size() == 1) {
            Intent intent = new Intent(Keuangan2Activity.this, EditDataLiqoActivity.class);
            intent.putExtra("pass_data_liqo", (Serializable) mMultiSelectDataLiqo.getDataLiqo().get(0));
            startActivity(intent);
        } else {
            runAlertDialog("Pilih satu data untuk di edit", 1);
        }
    }*/

    /*private void callDeleteDataLiqo() throws JSONException {
        final String[] message = new String[2];

        ObjectMapper mapper = new ObjectMapper();
        try {
            String ajsonparam = mapper.writeValueAsString(mMultiSelectDataLiqo);
            IDatabaseClient databaseClient = ApiGenerator.createService(IDatabaseClient.class);
            final Call<PostExecuteDatabase> call = databaseClient.deleteDataLiqo(ajsonparam);
            call.enqueue(new Callback<PostExecuteDatabase>() {
                @Override
                public void onResponse(Call<PostExecuteDatabase> call, Response<PostExecuteDatabase> response) {
                    PostExecuteDatabase postExecuteDatabase = response.body();
                    if (postExecuteDatabase != null) {
                        if (postExecuteDatabase.getStatus() == 1) {
                            for (int i = 0; i < mMultiSelectDataLiqo.getDataLiqo().size(); i++)
                                mDataLiqo.getDataLiqo().remove(mMultiSelectDataLiqo.getDataLiqo().get(i));
                            mAdapter.notifyDataSetChanged();
                            if (mActionMode != null) {
                                mActionMode.finish();
                            }
                            message[0] = postExecuteDatabase.getMessage();
                            message[1] = postExecuteDatabase.getStatus().toString();
                        } else {
                            message[0] = postExecuteDatabase.getMessage();
                            message[1] = postExecuteDatabase.getStatus().toString();
                        }
                    } else {
                        message[0] = "Tidak mendapatkan data !";
                        message[1] = "0";
                    }
                    if (message[1].equals("1")) {
                        runAlertDialog(message[0], 2);
                    } else {
                        runAlertDialog(message[0], 1);
                    }
                    mProgreesDialog.dismiss();
                }

                @Override
                public void onFailure(Call<PostExecuteDatabase> call, Throwable t) {
                    message[0] = t.getMessage();
                    message[1] = "0";
                    if (message[1].equals("1")) {
                        runAlertDialog(message[0], 2);
                    } else {
                        runAlertDialog(message[0], 1);
                    }
                    mProgreesDialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private void setLayoutCalendar() {
        mCalendarView.setSelectionType(SelectionType.SINGLE);
        mCalendarView.setCalendarOrientation(OrientationHelper.HORIZONTAL);
        mCalendarView.setWeekendDays(new HashSet() {{
            add(Calendar.FRIDAY);
        }});
        mCalendarView.setCurrentDayTextColor(getResources().getColor(R.color.orange_800));
        mCalendarView.update();
    }

    public void showEmptyData() {
        mTxtErrorPemasukan.setVisibility(View.VISIBLE);
        mRvDataPemasukan.setVisibility(View.INVISIBLE);
    }

    public void showAnyData() {
        mTxtErrorPemasukan.setVisibility(View.INVISIBLE);
        mRvDataPemasukan.setVisibility(View.VISIBLE);
    }

    //default function
    public void runAlertDialog(String imsg, int itipe) { //1 error, 2 sukses, 3 info
        AlertDialog alertDialog = AlertDialogClass2.alertDialog(imsg, itipe, this);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    /*public void runAlertDialog2(String imsg, int itipe) { //1 error, 2 sukses, 3 info
        AlertDialog alertDialog = AlertDialogClass2.alertDialog(imsg, itipe, this);
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "DELETE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Config.getVibrate(Keuangan2Activity.this);
                mProgreesDialog.show();
                try {
                    callDeleteDataLiqo();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }
*/
    public void runAlertDialog3(String imsg, int itipe, final String iopsi) { //1 error, 2 sukses, 3 info
        AlertDialog alertDialog = AlertDialogClass2.alertDialog(imsg, itipe, this);
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, iopsi, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                if (iopsi == "LOGOUT") {
                    Intent intent = new Intent(Keuangan2Activity.this, MenuActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                } else if (iopsi == "LOGIN") {
                    Intent intent = new Intent(Keuangan2Activity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        alertDialog.show();
    }
}
