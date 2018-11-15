package id.co.zamrud.emaszamrud.module.shop;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.CheckBox;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.zamrud.emaszamrud.R;
import id.co.zamrud.emaszamrud.database.ApiGenerator;
import id.co.zamrud.emaszamrud.database.IDatabaseClient;
import id.co.zamrud.emaszamrud.model.DataPenjualanClass;
import id.co.zamrud.emaszamrud.model.DataPenjualanItemClass;
import id.co.zamrud.emaszamrud.model.PostExecuteDatabase;
import id.co.zamrud.emaszamrud.util.AlertDialogClass2;
import id.co.zamrud.emaszamrud.util.Config;
import it.starksoftware.ssform.helper.FormBuildHelper;
import it.starksoftware.ssform.interfaces.ButtonCallBack;
import it.starksoftware.ssform.interfaces.CheckBoxCallBack;
import it.starksoftware.ssform.model.FormElement;
import it.starksoftware.ssform.model.FormElementButton;
import it.starksoftware.ssform.model.FormElementCheckBox;
import it.starksoftware.ssform.model.FormElementMemo;
import it.starksoftware.ssform.model.FormObject;
import it.starksoftware.ssform.model.Validator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditDataPenjualanActivity extends AppCompatActivity implements ButtonCallBack, CheckBoxCallBack {

    // BUILDER
    public FormBuildHelper mFormBuilder;
    @BindView(R.id.rvAddPenjualan)
    RecyclerView recyclerView;
    DataPenjualanItemClass mDataPenjualanItem;

    ProgressDialog mProgreesDialog;

    public FormElementMemo formElementNamaProd, formElementKeterangan;
    public FormElement formElementHarga, formElementJumlah;
    public FormElementButton formElementButtonSave;
    public FormElementCheckBox formElementCheckIsSudahBayar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_penjualan);
        mDataPenjualanItem = (DataPenjualanItemClass) getIntent().getSerializableExtra("pass_penjualan_item");

        mProgreesDialog = new ProgressDialog(this);
        mProgreesDialog.setMessage("Authenticating...");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ButterKnife.bind(this);
        mFormBuilder = new FormBuildHelper(this, this, recyclerView, getSupportFragmentManager());
        setupForm();
    }

    public void setupForm() {
        formElementNamaProd = FormElementMemo.createInstance()
                .setTitle("Nama Produk")
                .setValue(mDataPenjualanItem.getProduk())
                .setRequired(true)
                .setTag(80);

        formElementHarga = FormElement.createInstance()
                .setTitle("Harga (Rp.)")
                .setType(FormElement.TYPE_EDITTEXT_NUMBER_INTEGER)
                .setRequired(true)
                .setValue(mDataPenjualanItem.getHarga())
                .setRequiredResponseMessage("!!! REQUIRED !!!")
                .setTag(70);

        formElementJumlah = FormElement.createInstance()
                .setTitle("Jumlah")
                .setType(FormElement.TYPE_EDITTEXT_NUMBER_INTEGER)
                .setRequired(true)
                .setValue(mDataPenjualanItem.getJumlah())
                .setRequiredResponseMessage("!!! REQUIRED !!!")
                .setTag(10);

        boolean isSudahBayar = mDataPenjualanItem.getIsSudahBayar().equals("1") ? true : false;
        formElementCheckIsSudahBayar = FormElementCheckBox.createInstance()
                .setTitle("Apakah Sudah Bayar ? ")
                .setActivity(this)
                .setContext(this)
                .setCallback(this)
                .setValue(isSudahBayar)
                .setTag(140);

        formElementKeterangan = FormElementMemo.createInstance()
                .setTitle("Keterangan")
                .setValue(mDataPenjualanItem.getKeterangan())
                .setTag(80);

        formElementButtonSave = FormElementButton.createInstance()
                .setTitle("Update")
                .setButtonCallBack(this)
                .setTag(30);

        List<FormObject> formItems = new ArrayList<>();
        formItems.add(formElementNamaProd);
        formItems.add(formElementHarga);
        formItems.add(formElementJumlah);
        formItems.add(formElementCheckIsSudahBayar);
        formItems.add(formElementKeterangan);
        formItems.add(formElementButtonSave);

        mFormBuilder.addFormElements(formItems);
        mFormBuilder.refreshView();
    }

    @Override
    public void callbackButtonReturn(FormElementButton formElementButton, Object o) {
        List<Validator> validatorResult = this.mFormBuilder.validateForm();
        if (validatorResult.size() <= 0) {
            final DataPenjualanItemClass dataPenjualan = new DataPenjualanItemClass();

            dataPenjualan.setId(mDataPenjualanItem.getId());
            dataPenjualan.setProduk(formElementNamaProd.getValue());
            dataPenjualan.setHarga(formElementHarga.getValue());
            dataPenjualan.setJumlah(formElementJumlah.getValue());

            String statusBayar = formElementCheckIsSudahBayar.isChecked() == true ? "1" : "0";
            dataPenjualan.setIsSudahBayar(statusBayar);
            dataPenjualan.setKeterangan(formElementKeterangan.getValue());

            Config.getVibrate(this);
            mProgreesDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        startUpdate(dataPenjualan);
                    } catch (Exception e) {
                        mProgreesDialog.dismiss();
                        e.printStackTrace();
                    }
                }
            }, 2000);
        } else {
            runAlertDialog("Ada data yang kosong", 1, false);
        }
    }

    @Override
    public void callbackCheckBoxReturn(Object o, CheckBox checkBox, boolean b) {
        formElementCheckIsSudahBayar.setValue(b);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, ShopActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                //onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startUpdate(DataPenjualanItemClass dataPenjualan) throws JSONException {
        final String[] message = new String[2];

        ObjectMapper mapper = new ObjectMapper();
        try {
            String ajsonparam = mapper.writeValueAsString(dataPenjualan);
            IDatabaseClient databaseClient = ApiGenerator.createService(IDatabaseClient.class);
            final Call<PostExecuteDatabase> call = databaseClient.updateDataPenjualan(ajsonparam);
            call.enqueue(new Callback<PostExecuteDatabase>() {
                @Override
                public void onResponse(Call<PostExecuteDatabase> call, Response<PostExecuteDatabase> response) {
                    PostExecuteDatabase postExecuteDatabase = response.body();
                    if (postExecuteDatabase != null) {
                        if (postExecuteDatabase.getStatus() == 1) {
                            message[0] = postExecuteDatabase.getMessage();
                            message[1] = postExecuteDatabase.getStatus().toString();
                            clearForm();
                        } else {
                            message[0] = postExecuteDatabase.getMessage();
                            message[1] = postExecuteDatabase.getStatus().toString();
                        }
                    } else {
                        message[0] = "Gagal update data !";
                        message[1] = "0";
                    }
                    if (message[1].equals("1")) {
                        runAlertDialog(message[0], 2, true);
                    } else {
                        runAlertDialog(message[0], 1, false);
                    }
                    mProgreesDialog.dismiss();
                }

                @Override
                public void onFailure(Call<PostExecuteDatabase> call, Throwable t) {
                    message[0] = "Akses web service gagal !";
                    message[1] = "0";
                    if (message[1].equals("1")) {
                        runAlertDialog(message[0], 1, false);
                    } else {
                        runAlertDialog(message[0], 1, false);
                    }
                    mProgreesDialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearForm() {
        formElementNamaProd.setValue("");
        formElementHarga.setValue("");
        formElementJumlah.setValue("");
        mFormBuilder.refreshView();
    }

    //default function
    public void runAlertDialog(String imsg, int itipe, final boolean isUpdate) { //1 error, 2 sukses, 3 info
        AlertDialog alertDialog = AlertDialogClass2.alertDialog(imsg, itipe, this);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                if (isUpdate) {
                    Intent intent = new Intent(EditDataPenjualanActivity.this, ShopActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            }
        });
        alertDialog.show();
    }
}
