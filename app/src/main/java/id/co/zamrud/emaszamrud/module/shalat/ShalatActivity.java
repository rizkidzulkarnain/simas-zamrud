package id.co.zamrud.emaszamrud.module.shalat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.zamrud.emaszamrud.R;
import id.co.zamrud.emaszamrud.adapter.ShalatAdapter;
import id.co.zamrud.emaszamrud.adapter.ShalatOtherServerAdapter;
import id.co.zamrud.emaszamrud.database.ApiGenerator;
import id.co.zamrud.emaszamrud.database.IDatabaseClient;
import id.co.zamrud.emaszamrud.model.kota.DataKotaItemClass;
import id.co.zamrud.emaszamrud.model.kota.DataKotaParentClass;
import id.co.zamrud.emaszamrud.model.kota.DataKotaStatusClass;
import id.co.zamrud.emaszamrud.model.shalat.DataShalatItem;
import id.co.zamrud.emaszamrud.model.shalat.DataShalatParent;
import id.co.zamrud.emaszamrud.model.shalat.otherServer.Data;
import id.co.zamrud.emaszamrud.model.shalat.otherServer.ShalatOtherServer;
import id.co.zamrud.emaszamrud.module.MenuActivity;
import id.co.zamrud.emaszamrud.util.AlertDialogClass2;
import id.co.zamrud.emaszamrud.util.Config;
import it.starksoftware.ssform.helper.FormBuildHelper;
import it.starksoftware.ssform.interfaces.ButtonCallBack;
import it.starksoftware.ssform.interfaces.SearchableSpinnerCallBack;
import it.starksoftware.ssform.model.FormElementButton;
import it.starksoftware.ssform.model.FormElementSearchableSpinner;
import it.starksoftware.ssform.model.FormObject;
import it.starksoftware.ssform.model.FormSpinnerObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShalatActivity extends AppCompatActivity implements ButtonCallBack, SearchableSpinnerCallBack {

    @BindView(R.id.bannerShalat)
    ImageView bannerShalat;

    @BindView(R.id.rv_form_shalat)
    RecyclerView rvFormShalat;

    @BindView(R.id.rv_jadwal_shalat)
    RecyclerView rvJadwalShalat;

    @BindView(R.id.kota)
    TextView txtKota;

    @BindView(R.id.hari)
    TextView txtHari;

    @BindView(R.id.tanggal)
    TextView txtTanggal;

    IDatabaseClient mClient;
    ProgressDialog mProgreesDialog;

    public FormBuildHelper mFormBuilder;
    public FormElementSearchableSpinner mKotaSpinner;
    public FormElementButton mBtSearch;

    ArrayList<FormSpinnerObject> objSpinnerKota;

    //karena muslimsalat.com sering down jadi di command
    //DataShalatParent mDataShalatParent;
    //private List<DataShalatItem> dataShalatList;
    //private ShalatAdapter adapter;

    ShalatOtherServer mShalatOtherServer;
    private List<Data> dataShalatList;
    private ShalatOtherServerAdapter adapter;
    String mKota = "";
    boolean isAwal = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shalat);
        ButterKnife.bind(this);

        mProgreesDialog = new ProgressDialog(this);
        mProgreesDialog.setMessage("Authenticating...");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Glide.with(this).load("http://zamrud.primafora.com/assets/img/Android/menu_banner/banner3.jpg").into(bannerShalat);

        dataShalatList = new ArrayList<>();

        //karena sering down yang url muslimshalat.com
        //adapter = new ShalatAdapter(this, dataShalatList);

        adapter = new ShalatOtherServerAdapter(this, dataShalatList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvJadwalShalat.setLayoutManager(mLayoutManager);
        rvJadwalShalat.setAdapter(adapter);

        mFormBuilder = new FormBuildHelper(this, this, rvFormShalat, getSupportFragmentManager());
        setupForm();
    }

    public void setupForm() {
        mProgreesDialog.show();
        mClient = ApiGenerator.createServiceKota(IDatabaseClient.class);
        final Call<DataKotaParentClass> call = mClient.getKota();
        call.enqueue(new Callback<DataKotaParentClass>() {
            @Override
            public void onResponse(Call<DataKotaParentClass> call, Response<DataKotaParentClass> response) {
                DataKotaParentClass dataKotaParentClass = response.body();
                DataKotaStatusClass data = dataKotaParentClass.getRajaongkir();

                if (dataKotaParentClass != null) {
                    if (data.getStatus().getCode().equals(200)) {
                        objSpinnerKota = new ArrayList<FormSpinnerObject>();
                        FormSpinnerObject item = new FormSpinnerObject();
                        item.setKey("");
                        item.setValue("");

                        for (DataKotaItemClass itemKota : data.getResults()) {
                            item = new FormSpinnerObject();
                            item.setKey(itemKota.getCityName());
                            if (itemKota.getType().toLowerCase().equals("kota")) {
                                item.setValue(itemKota.getCityName());
                            } else {
                                item.setValue("Kab. " + itemKota.getCityName());
                            }
                            objSpinnerKota.add(item);
                        }
                        setItemKota(objSpinnerKota);
                        mKota = "Bandung Barat";
                        searchJadwalShalatOtherServer();
                        //mProgreesDialog.dismiss();
                    } else {
                        runAlertDialog("Tidak ada data !", 1);
                        mProgreesDialog.dismiss();
                    }
                } else {
                    runAlertDialog("Tidak ada data !", 1);
                    mProgreesDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<DataKotaParentClass> call, Throwable t) {
                runAlertDialog(t.getMessage(), 1);
                mProgreesDialog.dismiss();
            }
        });
    }

    /*public void searchJadwalShalat() {
        mProgreesDialog.show();
        mClient = ApiGenerator.createServiceShalat(IDatabaseClient.class);
        final Call<DataShalatParent> call = mClient.getJadwalShalat(mKota, Config.API_KEY_SHALAT);
        call.enqueue(new Callback<DataShalatParent>() {
            @Override
            public void onResponse(Call<DataShalatParent> call, Response<DataShalatParent> response) {
                mDataShalatParent = response.body();

                if (mDataShalatParent != null) {
                    if (mDataShalatParent.getStatusCode().equals(1)) {
                        setupJadwalShalat();
                        mProgreesDialog.dismiss();
                    } else {
                        runAlertDialog("Tidak ada data !", 1);
                        mProgreesDialog.dismiss();
                    }
                } else {
                    runAlertDialog("Tidak ada data !", 1);
                    mProgreesDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<DataShalatParent> call, Throwable t) {
                runAlertDialog(t.getMessage() == null ? String.valueOf(t.getCause()) : t.getMessage(), 1);
                mProgreesDialog.dismiss();
            }
        });
    }*/

    public void searchJadwalShalatOtherServer() {
        mProgreesDialog.show();
        mClient = ApiGenerator.createServiceShalatOtherServer(IDatabaseClient.class);
        final Call<ShalatOtherServer> call = mClient.getJadwalShalatOtherServer(mKota);
        call.enqueue(new Callback<ShalatOtherServer>() {
            @Override
            public void onResponse(Call<ShalatOtherServer> call, Response<ShalatOtherServer> response) {
                mShalatOtherServer = response.body();

                if (mShalatOtherServer != null) {
                    if (mShalatOtherServer.getStatus().equals("OK")) {
                        setupJadwalShalatOtherServer();
                        mProgreesDialog.dismiss();
                    } else {
                        runAlertDialog("Tidak ada data !", 1);
                        mProgreesDialog.dismiss();
                    }
                } else {
                    runAlertDialog("Tidak ada data !", 1);
                    mProgreesDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ShalatOtherServer> call, Throwable t) {
                runAlertDialog(t.getMessage() == null ? String.valueOf(t.getCause()) : t.getMessage(), 1);
                mProgreesDialog.dismiss();
            }
        });
    }

    public void setItemKota(ArrayList<FormSpinnerObject> kotaValues) {
        mKotaSpinner = new FormElementSearchableSpinner();

        mKotaSpinner
                .setCallback(this)
                .setActivity(this)
                .setContext(this)
                .setDialogTitle("Pilih Kota")
                .setSpinnerObject(kotaValues)
                .setTitle("Kota")
                .setValue("Kab. Bandung Barat")
                .setTag(1);


        mBtSearch = FormElementButton.createInstance()
                .setTitle("Search")
                .setButtonCallBack(this)
                .setTag(2);

        List<FormObject> formItems = new ArrayList<>();

        formItems.add(mKotaSpinner);
        formItems.add(mBtSearch);

        mFormBuilder.addFormElements(formItems);
        mFormBuilder.refreshView();
    }

    //dicommand karena sering error server
    /*public void setupJadwalShalat() {
        String tanggal = mDataShalatParent.getItems().get(0).getDateFor();

        Locale id = new Locale("in", "ID");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date = null;
        try {
            date = sdf.parse(tanggal);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE,dd MMMM yyyy", id);
        String tanggal2 = sdf2.format(date);

        String[] split = tanggal2.split(",");

        txtKota.setText(mKota);
        txtHari.setText(split[0]);
        txtTanggal.setText(split[1]);

        dataShalatList.clear();
        dataShalatList.addAll(mDataShalatParent.getItems());
        adapter.notifyDataSetChanged();
    }*/

    public void setupJadwalShalatOtherServer() {
        String tanggal = mShalatOtherServer.getTime().getDate();

        Locale id = new Locale("in", "ID");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date = null;
        try {
            date = sdf.parse(tanggal);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE,dd MMMM yyyy", id);
        String tanggal2 = sdf2.format(date);

        String[] split = tanggal2.split(",");

        txtKota.setText(mKota);
        txtHari.setText(split[0]);
        txtTanggal.setText(split[1]);

        dataShalatList.clear();

        List<Data> dataList = new ArrayList<>();
        dataList.add(mShalatOtherServer.getData());

        dataShalatList.addAll(dataList);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void callbackButtonReturn(FormElementButton object, Object tag) {
        if (mKota != "") {
            searchJadwalShalatOtherServer();
        } else {
            runAlertDialog("Belum memilih kota !", 1);
        }
    }

    public void runAlertDialog(String imsg, int itipe) { //1 error, 2 sukses, 3 info
        AlertDialog alertDialog = AlertDialogClass2.alertDialog(imsg, itipe, this);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    public void callbackSearchableSpinnerReturn(FormElementSearchableSpinner object, Object tag, FormSpinnerObject spinnerObject) {
        mKota = spinnerObject.getKey();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
