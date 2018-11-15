package id.co.zamrud.emaszamrud.module.keuangan;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
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
import id.co.zamrud.emaszamrud.module.liqo.AddDataLiqoActivity;
import id.co.zamrud.emaszamrud.module.liqo.EditDataLiqoActivity;
import id.co.zamrud.emaszamrud.util.AlertDialogClass2;
import id.co.zamrud.emaszamrud.util.Config;
import id.co.zamrud.emaszamrud.util.ConvertDateFormat;
import id.co.zamrud.emaszamrud.util.RecyclerItemClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KeuanganActivity extends AppCompatActivity {
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
    ProgressDialog mProgreesDialog;

    DataKeuanganAdapter mAdapterPemasukan, mAdapterPengeluaran;
    DataKeuanganClass mDataPemasukan, mMultiSelectDataPemasukan;
    DataKeuanganClass mDataPengeluaran, mMultiSelectDataPengeluaran;

    int mTotalPemasukan = 0, mTotalPengeluaran = 0;
    int mSession = 0; // 0 --> pemasukan dan 1 --> pengeluaran

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keuangan);
        ButterKnife.bind(this);

        mProgreesDialog = new ProgressDialog(this);
        mProgreesDialog.setMessage("Loading...");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setLayoutCalendar();

        RecyclerView.LayoutManager mLayoutManagerPemasukan = new LinearLayoutManager(KeuanganActivity.this);
        mRvDataPemasukan.setLayoutManager(mLayoutManagerPemasukan);

        RecyclerView.LayoutManager mLayoutManagerPengeluaran = new LinearLayoutManager(KeuanganActivity.this);
        mRvDataPengeluaran.setLayoutManager(mLayoutManagerPengeluaran);

        mRvDataPemasukan.addOnItemTouchListener(new RecyclerItemClickListener(KeuanganActivity.this, mRvDataPemasukan, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                /*if (isMultiSelect) {
                    multi_select_pemasukan(position);
                }*/
            }

            @Override
            public void onItemLongClick(View view, int position) {
                mSession = 0;
                if (!isMultiSelect) {
                    mMultiSelectDataPemasukan.setDataKeuangan(new ArrayList<DataKeuanganItemClass>());
                    isMultiSelect = true;

                    if (mActionMode == null) {
                        mActionMode = startActionMode(mActionModeCallback);
                    }
                }
                multi_select_pemasukan(position);
            }
        }));

        mRvDataPengeluaran.addOnItemTouchListener(new RecyclerItemClickListener(KeuanganActivity.this, mRvDataPengeluaran, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                /*if (isMultiSelect) {
                    multi_select_pengeluaran(position);
                }*/
            }

            @Override
            public void onItemLongClick(View view, int position) {
                mSession = 1;
                if (!isMultiSelect) {
                    mMultiSelectDataPengeluaran.setDataKeuangan(new ArrayList<DataKeuanganItemClass>());
                    isMultiSelect = true;

                    if (mActionMode == null) {
                        mActionMode = startActionMode(mActionModeCallback);
                    }
                }
                multi_select_pengeluaran(position);
            }
        }));


        //setelah sesalai back
        String activity = getIntent().getStringExtra("activity") == null ? "" : getIntent().getStringExtra("activity");
        if (activity.equals("add") || activity.equals("edit")) {
            String tanggal2 = getIntent().getStringExtra("tanggal");
            if (!tanggal2.equals("")) {
                Config.getVibrate(this);
                mProgreesDialog.show();
                CallGetDataPemasukan(tanggal2);
            } else {
                runAlertDialog("Pilih tanggal dulu", 1);
            }
        }
    }

    @OnClick(R.id.btGetData)
    public void getData(View view) {
        String tanggal2 = getTanggal();
        if (!tanggal2.equals("")) {
            Config.getVibrate(this);
            mProgreesDialog.show();
            CallGetDataPemasukan(tanggal2);
        } else {
            runAlertDialog("Pilih tanggal dulu", 1);
        }
    }

    @OnClick(R.id.addPemasukan)
    public void addPemasukan(View view) {
        String tanggal2 = getTanggal();
        if (!tanggal2.equals("")) {
            Config.getVibrate(this);
            mProgreesDialog.show();

            Intent intent = new Intent(KeuanganActivity.this, AddDataKeuanganInActivity.class);
            intent.putExtra("tanggal", tanggal2);
            startActivity(intent);
        } else {
            runAlertDialog("Pilih tanggal dulu", 1);
        }
    }

    @OnClick(R.id.addPengeluaran)
    public void addPengeluaran(View view) {
        String tanggal2 = getTanggal();
        if (!tanggal2.equals("")) {
            Config.getVibrate(this);
            mProgreesDialog.show();

            Intent intent = new Intent(KeuanganActivity.this, AddDataKeuanganOutActivity.class);
            intent.putExtra("tanggal", tanggal2);
            startActivity(intent);
        } else {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                intent = new Intent(KeuanganActivity.this, MenuActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void CallGetDataPemasukan(String itgl) {
        mDataPemasukan = new DataKeuanganClass();
        mMultiSelectDataPemasukan = new DataKeuanganClass();
        mMultiSelectDataPemasukan.setDataKeuangan(new ArrayList<>());
        isMultiSelect = false;

        mClient = ApiGenerator.createService(IDatabaseClient.class);
        final Call<DataKeuanganClass> call = mClient.getDataPemasukan(itgl);
        call.enqueue(new Callback<DataKeuanganClass>() {
            @Override
            public void onResponse(Call<DataKeuanganClass> call, Response<DataKeuanganClass> response) {
                mDataPemasukan = response.body();
                if (mDataPemasukan != null) {
                    if (mDataPemasukan.getStatus() == 1) {
                        showAnyDataPemasukan();
                        mAdapterPemasukan = new DataKeuanganAdapter(
                                KeuanganActivity.this,
                                mDataPemasukan.getDataKeuangan(),
                                mMultiSelectDataPemasukan.getDataKeuangan()
                        );
                        mRvDataPemasukan.setAdapter(mAdapterPemasukan);
                        mAdapterPemasukan.notifyDataSetChanged();
                        mAdapterPemasukan.setOnBottomReachedListener(new OnBottomReachedListener() {
                            @Override
                            public void onBottomReached(int position) {
                                mTotalPemasukan = mAdapterPemasukan.getTotalAkhir();
                                mTxtTotalPemasukan.setText(mAdapterPemasukan.convertStringToCurr(String.valueOf(mTotalPemasukan)));
                                CallGetDataPengeluaran(itgl);
                            }
                        });
                    } else {
                        runAlertDialog(mDataPemasukan.getMessage(), 1);
                        showEmptyDataPemasukan();
                        CallGetDataPengeluaran(itgl);
                    }
                } else {
                    runAlertDialog("Gagal Mendapatkan Data", 1);
                    showEmptyDataPemasukan();
                    CallGetDataPengeluaran(itgl);
                }
            }

            @Override
            public void onFailure(Call<DataKeuanganClass> call, Throwable t) {
                showEmptyDataPemasukan();
                runAlertDialog(t.getMessage(), 1);
                CallGetDataPengeluaran(itgl);
            }
        });
    }

    public void CallGetDataPengeluaran(String itgl) {
        mDataPengeluaran = new DataKeuanganClass();
        mMultiSelectDataPengeluaran = new DataKeuanganClass();
        mMultiSelectDataPengeluaran.setDataKeuangan(new ArrayList<DataKeuanganItemClass>());
        isMultiSelect = false;

        mClient = ApiGenerator.createService(IDatabaseClient.class);
        final Call<DataKeuanganClass> call = mClient.getDataPengeluaran(itgl);
        call.enqueue(new Callback<DataKeuanganClass>() {
            @Override
            public void onResponse(Call<DataKeuanganClass> call, Response<DataKeuanganClass> response) {
                mDataPengeluaran = response.body();
                if (mDataPengeluaran != null) {
                    if (mDataPengeluaran.getStatus() == 1) {
                        showAnyDataPengeluaran();
                        mAdapterPengeluaran = new DataKeuanganAdapter(
                                KeuanganActivity.this,
                                mDataPengeluaran.getDataKeuangan(),
                                mMultiSelectDataPengeluaran.getDataKeuangan()
                        );
                        mRvDataPengeluaran.setAdapter(mAdapterPengeluaran);
                        mAdapterPengeluaran.notifyDataSetChanged();
                        mAdapterPengeluaran.setOnBottomReachedListener(new OnBottomReachedListener() {
                            @Override
                            public void onBottomReached(int position) {
                                mTotalPengeluaran = mAdapterPengeluaran.getTotalAkhir();
                                mTxtTotalPengeluaran.setText(mAdapterPengeluaran.convertStringToCurr(String.valueOf(mTotalPengeluaran)));
                                mTxtTotalAkhir.setText(mAdapterPengeluaran.convertStringToCurr(String.valueOf(mTotalPemasukan - mTotalPengeluaran)));
                                mProgreesDialog.dismiss();
                            }
                        });
                    } else {
                        runAlertDialog(mDataPengeluaran.getMessage(), 1);
                        showEmptyDataPengeluaran();
                        mProgreesDialog.dismiss();
                    }
                } else {
                    runAlertDialog("Gagal Mendapatkan Data", 1);
                    showEmptyDataPengeluaran();
                    mProgreesDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<DataKeuanganClass> call, Throwable t) {
                showEmptyDataPengeluaran();
                runAlertDialog(t.getMessage(), 1);
                mProgreesDialog.dismiss();
            }
        });
    }

    public void multi_select_pemasukan(int position) {
        if (mActionMode != null) {
            if (mMultiSelectDataPemasukan.getDataKeuangan().contains(mDataPemasukan.getDataKeuangan().get(position)))
                mMultiSelectDataPemasukan.getDataKeuangan().remove(mDataPemasukan.getDataKeuangan().get(position));
            else
                mMultiSelectDataPemasukan.getDataKeuangan().add(mDataPemasukan.getDataKeuangan().get(position));
            if (mMultiSelectDataPemasukan.getDataKeuangan().size() > 0) {
                mActionMode.setTitle("" + mMultiSelectDataPemasukan.getDataKeuangan().size());
                if (mMultiSelectDataPemasukan.getDataKeuangan().size() == 1) {
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
    }

    public void multi_select_pengeluaran(int position) {
        if (mActionMode != null) {
            if (mMultiSelectDataPengeluaran.getDataKeuangan().contains(mDataPengeluaran.getDataKeuangan().get(position)))
                mMultiSelectDataPengeluaran.getDataKeuangan().remove(mDataPengeluaran.getDataKeuangan().get(position));
            else
                mMultiSelectDataPengeluaran.getDataKeuangan().add(mDataPengeluaran.getDataKeuangan().get(position));
            if (mMultiSelectDataPengeluaran.getDataKeuangan().size() > 0) {
                mActionMode.setTitle("" + mMultiSelectDataPengeluaran.getDataKeuangan().size());
                if (mMultiSelectDataPengeluaran.getDataKeuangan().size() == 1) {
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
    }

    public void refreshAdapter() {
        if (mSession == 0) {
            mAdapterPemasukan.dataKeuanganList_selected = mMultiSelectDataPemasukan.getDataKeuangan();
            mAdapterPemasukan.dataKeuanganList = mDataPemasukan.getDataKeuangan();
            mAdapterPemasukan.setTotalAkhir(0);
            mAdapterPemasukan.notifyDataSetChanged();
        }else{
            mAdapterPengeluaran.dataKeuanganList_selected = mMultiSelectDataPengeluaran.getDataKeuangan();
            mAdapterPengeluaran.dataKeuanganList = mDataPengeluaran.getDataKeuangan();
            mAdapterPengeluaran.setTotalAkhir(0);
            mAdapterPengeluaran.notifyDataSetChanged();
        }
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        private int statusBarColor;

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.keuangan_multi_select, menu);
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
                    String tanggal2 = getTanggal();
                    if (!tanggal2.equals("")) {
                        Config.getVibrate(KeuanganActivity.this);
                        mProgreesDialog.show();
                        if(mSession == 0) {
                            callEditDataPemasukan(tanggal2);
                        }else{
                            callEditDataPengeluaran(tanggal2);
                        }
                    } else {
                        runAlertDialog("Pilih tanggal dulu", 1);
                    }
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
            if(mSession == 0) {
                mMultiSelectDataPemasukan.setDataKeuangan(new ArrayList<DataKeuanganItemClass>());
            }else{
                mMultiSelectDataPengeluaran.setDataKeuangan(new ArrayList<DataKeuanganItemClass>());
            }
            refreshAdapter();
        }
    };

    private void callEditDataPemasukan(String tanggal) {
        if (mMultiSelectDataPemasukan.getDataKeuangan().size() == 1) {
            Intent intent = new Intent(KeuanganActivity.this, EditDataKeuanganInActivity.class);
            intent.putExtra("pass_data_pemasukan", (Serializable) mMultiSelectDataPemasukan.getDataKeuangan().get(0));
            intent.putExtra("tanggal", (Serializable) tanggal);
            startActivity(intent);
        } else {
            runAlertDialog("Pilih satu data untuk di edit", 1);
        }
    }

    private void callEditDataPengeluaran(String tanggal) {
        if (mMultiSelectDataPengeluaran.getDataKeuangan().size() == 1) {
            Intent intent = new Intent(KeuanganActivity.this, EditDataKeuanganOutActivity.class);
            intent.putExtra("pass_data_pengeluaran", (Serializable) mMultiSelectDataPengeluaran.getDataKeuangan().get(0));
            intent.putExtra("tanggal", (Serializable) tanggal);
            startActivity(intent);
        } else {
            runAlertDialog("Pilih satu data untuk di edit", 1);
        }
    }

    private void callDeleteDataPemasukan() throws JSONException {
        final String[] message = new String[2];

        try {
            String id_pemasukan = mMultiSelectDataPemasukan.getDataKeuangan().get(0).getId();
            IDatabaseClient databaseClient = ApiGenerator.createService(IDatabaseClient.class);
            final Call<PostExecuteDatabase> call = databaseClient.deleteDataPemasukan(id_pemasukan);
            call.enqueue(new Callback<PostExecuteDatabase>() {
                @Override
                public void onResponse(Call<PostExecuteDatabase> call, Response<PostExecuteDatabase> response) {
                    PostExecuteDatabase postExecuteDatabase = response.body();
                    if (postExecuteDatabase != null) {
                        if (postExecuteDatabase.getStatus() == 1) {
                            for (int i = 0; i < mMultiSelectDataPemasukan.getDataKeuangan().size(); i++)
                                mDataPemasukan.getDataKeuangan().remove(mMultiSelectDataPemasukan.getDataKeuangan().get(i));
                            mAdapterPemasukan.notifyDataSetChanged();
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
    }

    private void callDeleteDataPengeluaran() throws JSONException {
        final String[] message = new String[2];
        try {
            String id_pengeluaran = mMultiSelectDataPengeluaran.getDataKeuangan().get(0).getId();
            IDatabaseClient databaseClient = ApiGenerator.createService(IDatabaseClient.class);
            final Call<PostExecuteDatabase> call = databaseClient.deleteDataPengeluaran(id_pengeluaran);
            call.enqueue(new Callback<PostExecuteDatabase>() {
                @Override
                public void onResponse(Call<PostExecuteDatabase> call, Response<PostExecuteDatabase> response) {
                    PostExecuteDatabase postExecuteDatabase = response.body();
                    if (postExecuteDatabase != null) {
                        if (postExecuteDatabase.getStatus() == 1) {
                            for (int i = 0; i < mMultiSelectDataPengeluaran.getDataKeuangan().size(); i++)
                                mDataPengeluaran.getDataKeuangan().remove(mMultiSelectDataPengeluaran.getDataKeuangan().get(i));
                            mAdapterPengeluaran.notifyDataSetChanged();
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
    }

    private void setLayoutCalendar() {
        mCalendarView.setSelectionType(SelectionType.SINGLE);
        mCalendarView.setCalendarOrientation(OrientationHelper.HORIZONTAL);
        mCalendarView.setWeekendDays(new HashSet() {{
            add(Calendar.FRIDAY);
        }});
        mCalendarView.setCurrentDayTextColor(getResources().getColor(R.color.orange_800));
        mCalendarView.update();
    }

    public void showEmptyDataPemasukan() {
        mTxtErrorPemasukan.setVisibility(View.VISIBLE);
        mRvDataPemasukan.setVisibility(View.GONE);
        mTxtTotalPemasukan.setText("0");
    }

    public void showAnyDataPemasukan() {
        mTxtErrorPemasukan.setVisibility(View.GONE);
        mRvDataPemasukan.setVisibility(View.VISIBLE);
    }

    public void showEmptyDataPengeluaran() {
        mTxtErrorPengeluaran.setVisibility(View.VISIBLE);
        mRvDataPengeluaran.setVisibility(View.GONE);
        mTxtTotalPengeluaran.setText("0");
        mTxtTotalAkhir.setText("0");
    }

    public void showAnyDataPengeluaran() {
        mTxtErrorPengeluaran.setVisibility(View.GONE);
        mRvDataPengeluaran.setVisibility(View.VISIBLE);
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

    public void runAlertDialog2(String imsg, int itipe) { //1 error, 2 sukses, 3 info
        AlertDialog alertDialog = AlertDialogClass2.alertDialog(imsg, itipe, this);
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "DELETE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Config.getVibrate(KeuanganActivity.this);
                mProgreesDialog.show();
                try {
                    if (mSession == 0) {
                        callDeleteDataPemasukan();
                    }else{
                        callDeleteDataPengeluaran();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

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
                    Intent intent = new Intent(KeuanganActivity.this, MenuActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                } else if (iopsi == "LOGIN") {
                    Intent intent = new Intent(KeuanganActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        alertDialog.show();
    }
}
