package id.co.zamrud.emaszamrud.module.shop;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shagi.materialdatepicker.date.DatePickerFragmentDialog;
import com.shagi.materialdatepicker.date.DatePickerFragmentDialog.OnDateSetListener;

import org.json.JSONException;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.zamrud.emaszamrud.R;
import id.co.zamrud.emaszamrud.adapter.DataPenjualanAdapter;
import id.co.zamrud.emaszamrud.database.ApiGenerator;
import id.co.zamrud.emaszamrud.database.IDatabaseClient;
import id.co.zamrud.emaszamrud.model.DataPenjualanClass;
import id.co.zamrud.emaszamrud.model.DataPenjualanItemClass;
import id.co.zamrud.emaszamrud.model.PostExecuteDatabase;
import id.co.zamrud.emaszamrud.module.MenuActivity;
import id.co.zamrud.emaszamrud.util.AlertDialogClass2;
import id.co.zamrud.emaszamrud.util.Config;
import id.co.zamrud.emaszamrud.util.RecyclerItemClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopActivity extends AppCompatActivity {

    @BindView(R.id.tglAwal)
    TextView mTglAwal;
    @BindView(R.id.tglAkhir)
    TextView mTglAkhir;
    @BindView(R.id.txtError)
    TextView mTxtError;
    @BindView(R.id.total_akhir)
    TextView mTotalAkhir;
    @BindView(R.id.rvDataPenjualan)
    RecyclerView mRvDataPenjualan;
    @BindView(R.id.layout_rv)
    LinearLayout mLayoutRv;
    @BindView(R.id.footer)
    LinearLayout mFooterLayout;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    ActionMode mActionMode;
    Menu context_menu;
    boolean isRefresh = false;

    DataPenjualanAdapter mAdapter;
    ProgressDialog mProgreesDialog;
    IDatabaseClient mClient;
    DataPenjualanClass mDataPenjualan;
    DataPenjualanClass mMultiSelectDataPenjualan;
    boolean isMultiSelect;


    SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    SimpleDateFormat mSimpleDateFormat2 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    SimpleDateFormat mSimpleDateFormat3 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        ButterKnife.bind(this);

        mProgreesDialog = new ProgressDialog(this);
        mProgreesDialog.setMessage("Authenticating...");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        onSetDefaultDate();
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                getData();
            }
        });

        swipeContainer.setColorSchemeResources(
                android.R.color.holo_green_light,
                android.R.color.holo_green_dark
        );

        //to show recyclerview
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ShopActivity.this);
        mRvDataPenjualan.setLayoutManager(mLayoutManager);

        //get footer height
        mFooterLayout.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int height = mFooterLayout.getMeasuredHeight();

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)mLayoutRv.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, height);  // left, top, right, bottom
        mLayoutRv.setLayoutParams(layoutParams);
        /*ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) mRvDataPenjualan.getLayoutParams();
        marginLayoutParams.setMargins(0, 0, 0, height);*/
        //mRvDataPenjualan.setLayoutParams(marginLayoutParams);
        mRvDataPenjualan.addOnItemTouchListener(new RecyclerItemClickListener(ShopActivity.this, mRvDataPenjualan, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect) {
                    multi_select(position);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!isMultiSelect) {
                    mMultiSelectDataPenjualan.setDataPenjualan(new ArrayList<DataPenjualanItemClass>());
                    isMultiSelect = true;

                    if (mActionMode == null) {
                        mActionMode = startActionMode(mActionModeCallback);
                    }
                }
                multi_select(position);
            }
        }));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        try {
            mProgreesDialog.show();
            CallPenjualanAPI();
        } catch (JSONException e) {
            mProgreesDialog.dismiss();
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_data_penjualan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                intent = new Intent(ShopActivity.this, MenuActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                //runAlertDialog3("Apakah anda yakin ingin logout ?", 3, "logout");
                return true;
            case R.id.add_data:
                intent = new Intent(this, AddDataPenjualanActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.tglAwal)
    public void showTglAwal(View view) {
        Config.getVibrate(this);
        onDateClick(mTglAwal.getText().toString(), 1);
    }

    @OnClick(R.id.tglAkhir)
    public void showTglAkhir(View view) {
        Config.getVibrate(this);
        onDateClick(mTglAkhir.getText().toString(), 2);
    }

    @OnClick(R.id.btGetData)
    public void getData(View view) {
        getData();
    }

    public void getData(){
        Config.getVibrate(this);
        if(!isRefresh) {
            mProgreesDialog.show();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    CallPenjualanAPI();
                } catch (JSONException e) {
                    mProgreesDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, 2000);
    }

    public void onDateClick(String idate, final int itipe) {
        String tanggal = convertDateFormat(idate);
        final OnDateSetListener listener = new OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerFragmentDialog view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                switch (itipe) {
                    case 1:
                        mTglAwal.setText(mSimpleDateFormat.format(calendar.getTime()));
                        break;
                    case 2:
                        mTglAkhir.setText(mSimpleDateFormat.format(calendar.getTime()));
                        break;
                }
            }
        };


        DatePickerFragmentDialog dialog = DatePickerFragmentDialog.newInstance(
                listener,
                Integer.parseInt(tanggal.split("/")[2]),
                Integer.parseInt(tanggal.split("/")[1]) - 1,
                Integer.parseInt(tanggal.split("/")[0])
        );
        dialog.show(getSupportFragmentManager(), "tag");
    }

    public void CallPenjualanAPI() throws JSONException {
        mDataPenjualan = new DataPenjualanClass();
        mMultiSelectDataPenjualan = new DataPenjualanClass();
        mMultiSelectDataPenjualan.setDataPenjualan(new ArrayList<DataPenjualanItemClass>());
        isMultiSelect = false;

        mClient = ApiGenerator.createService(IDatabaseClient.class);
        String tgl_awal = convertDateFormat2(mTglAwal.getText().toString());
        String tgl_akhir = convertDateFormat2(mTglAkhir.getText().toString());
        final Call<DataPenjualanClass> call = mClient.getDataPenjualan(tgl_awal, tgl_akhir);
        call.enqueue(new Callback<DataPenjualanClass>() {
            @Override
            public void onResponse(Call<DataPenjualanClass> call, Response<DataPenjualanClass> response) {
                mDataPenjualan = response.body();
                if (mDataPenjualan != null) {
                    if (mDataPenjualan.getStatus() == 1) {
                        mAdapter = new DataPenjualanAdapter(
                                ShopActivity.this,
                                mDataPenjualan.getDataPenjualan(),
                                mMultiSelectDataPenjualan.getDataPenjualan()
                        );
                        mRvDataPenjualan.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mTotalAkhir.setText(mAdapter.getTotalAkhir());
                                mProgreesDialog.dismiss();
                            }
                        }, 2000);
                        showAnyData();
                    } else {
                        runAlertDialog(mDataPenjualan.getMessage(), 1);
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
            public void onFailure(Call<DataPenjualanClass> call, Throwable t) {
                showEmptyData();
                runAlertDialog(t.getMessage(), 1);
                mProgreesDialog.dismiss();
            }
        });
    }

    private void callEditPenjualan(){
        if(mMultiSelectDataPenjualan.getDataPenjualan().size() == 1) {
            Intent intent = new Intent(ShopActivity.this, EditDataPenjualanActivity.class);
            intent.putExtra("pass_penjualan_item", (Serializable) mMultiSelectDataPenjualan.getDataPenjualan().get(0));
            startActivity(intent);
        }else{
            runAlertDialog("Pilih satu data untuk di edit", 1);
        }
    }

    private void callDeletePenjualanAPI() throws JSONException {
        final String[] message = new String[2];

        ObjectMapper mapper = new ObjectMapper();
        try {
            String ajsonparam = mapper.writeValueAsString(mMultiSelectDataPenjualan);
            IDatabaseClient databaseClient = ApiGenerator.createService(IDatabaseClient.class);
            final Call<PostExecuteDatabase> call = databaseClient.deleteDataPenjualan(ajsonparam);
            call.enqueue(new Callback<PostExecuteDatabase>() {
                @Override
                public void onResponse(Call<PostExecuteDatabase> call, Response<PostExecuteDatabase> response) {
                    PostExecuteDatabase postExecuteDatabase = response.body();
                    if (postExecuteDatabase != null) {
                        if (postExecuteDatabase.getStatus() == 1) {
                            for (int i = 0; i < mMultiSelectDataPenjualan.getDataPenjualan().size(); i++)
                                mDataPenjualan.getDataPenjualan().remove(mMultiSelectDataPenjualan.getDataPenjualan().get(i));
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
    }

    public void multi_select(int position) {
        if (mActionMode != null) {
            if (mMultiSelectDataPenjualan.getDataPenjualan().contains(mDataPenjualan.getDataPenjualan().get(position)))
                mMultiSelectDataPenjualan.getDataPenjualan().remove(mDataPenjualan.getDataPenjualan().get(position));
            else
                mMultiSelectDataPenjualan.getDataPenjualan().add(mDataPenjualan.getDataPenjualan().get(position));
            if (mMultiSelectDataPenjualan.getDataPenjualan().size() > 0) {
                mActionMode.setTitle("" + mMultiSelectDataPenjualan.getDataPenjualan().size());
                if (mMultiSelectDataPenjualan.getDataPenjualan().size() == 1) {
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
        mAdapter.dataPenjualanList_selected = mMultiSelectDataPenjualan.getDataPenjualan();
        mAdapter.dataPenjualanList = mDataPenjualan.getDataPenjualan();
        mAdapter.notifyDataSetChanged();
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        private int statusBarColor;

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
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
                    callEditPenjualan();
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
            mMultiSelectDataPenjualan.setDataPenjualan(new ArrayList<DataPenjualanItemClass>());
            refreshAdapter();
        }
    };

    public void onSetDefaultDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        mTglAwal.setText(dateFormat.format(new Date()));
        mTglAkhir.setText(dateFormat.format(new Date()));
    }

    public String convertDateFormat(String idate) {
        Date adate = null;
        try {
            adate = mSimpleDateFormat.parse(idate);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return mSimpleDateFormat2.format(adate);
    }

    public String convertDateFormat2(String idate) {
        Date adate = null;
        try {
            adate = mSimpleDateFormat.parse(idate);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return mSimpleDateFormat3.format(adate);
    }

    public void showEmptyData() {
        if(isRefresh){
            swipeContainer.setRefreshing(false);
            isRefresh = false;
        }
        mTxtError.setVisibility(View.VISIBLE);
        mRvDataPenjualan.setVisibility(View.GONE);
    }

    public void showAnyData() {
        if(isRefresh){
            swipeContainer.setRefreshing(false);
            isRefresh = false;
        }
        mTxtError.setVisibility(View.GONE);
        mRvDataPenjualan.setVisibility(View.VISIBLE);
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
                Config.getVibrate(ShopActivity.this);
                mProgreesDialog.show();
                try {
                    callDeletePenjualanAPI();
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
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "LOGOUT", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                if(iopsi == "logout"){
                    Intent intent = new Intent(ShopActivity.this, MenuActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            }
        });

        alertDialog.show();
    }
}
