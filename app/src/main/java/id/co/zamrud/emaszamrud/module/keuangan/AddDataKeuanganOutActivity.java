package id.co.zamrud.emaszamrud.module.keuangan;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.zamrud.emaszamrud.R;
import id.co.zamrud.emaszamrud.database.ApiGenerator;
import id.co.zamrud.emaszamrud.database.IDatabaseClient;
import id.co.zamrud.emaszamrud.model.DataPenjualanItemClass;
import id.co.zamrud.emaszamrud.model.PostExecuteDatabase;
import id.co.zamrud.emaszamrud.model.keuangan.DataKeuanganItemClass;
import id.co.zamrud.emaszamrud.util.AlertDialogClass2;
import id.co.zamrud.emaszamrud.util.Config;
import it.starksoftware.ssform.helper.FormBuildHelper;
import it.starksoftware.ssform.interfaces.ButtonCallBack;
import it.starksoftware.ssform.model.FormElement;
import it.starksoftware.ssform.model.FormElementButton;
import it.starksoftware.ssform.model.FormElementMemo;
import it.starksoftware.ssform.model.FormObject;
import it.starksoftware.ssform.model.Validator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDataKeuanganOutActivity extends AppCompatActivity implements ButtonCallBack {

    // BUILDER
    public FormBuildHelper mFormBuilder;
    @BindView(R.id.rvAddKeuangan)
    RecyclerView recyclerView;

    ProgressDialog mProgreesDialog;

    String mTanggal;
    public FormElementMemo formElementJenisPengeluaran;
    public FormElement formElementJumlahPengeluaran;
    public FormElementButton formElementButtonSave;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_keuangan);
        ButterKnife.bind(this);

        mTanggal = getIntent().getStringExtra("tanggal");

        mProgreesDialog = new ProgressDialog(this);
        mProgreesDialog.setMessage("Authenticating...");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mFormBuilder = new FormBuildHelper(this, this, recyclerView, getSupportFragmentManager());
        setupForm();
    }

    public void setupForm(){
        formElementJenisPengeluaran = FormElementMemo.createInstance()
                .setTitle("Jenis Pengeluaran")
                .setRequired(true)
                .setTag(80);

        formElementJumlahPengeluaran = FormElement.createInstance()
                .setTitle("Jumlah Pengeluaran (Rp.)")
                .setType(FormElement.TYPE_EDITTEXT_NUMBER_INTEGER)
                .setRequired(true)
                .setRequiredResponseMessage("!!! REQUIRED !!!")
                .setTag(70);

        formElementButtonSave = FormElementButton.createInstance()
                .setTitle("Save")
                .setButtonCallBack(this)
                .setTag(30);

        List<FormObject> formItems = new ArrayList<>();
        formItems.add(formElementJenisPengeluaran);
        formItems.add(formElementJumlahPengeluaran);
        formItems.add(formElementButtonSave);

        mFormBuilder.addFormElements(formItems);
        mFormBuilder.refreshView();
    }

    @Override
    public void callbackButtonReturn(FormElementButton formElementButton, Object o) {
        List<Validator> validatorResult = this.mFormBuilder.validateForm();
        if (validatorResult.size() <= 0) {
            final DataKeuanganItemClass dataPengeluaran = new DataKeuanganItemClass();
            dataPengeluaran.setJenis(formElementJenisPengeluaran.getValue());
            dataPengeluaran.setJumlah(formElementJumlahPengeluaran.getValue());
            dataPengeluaran.setTanggal(mTanggal);

            Config.getVibrate(this);
            mProgreesDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        startSave(dataPengeluaran);
                    } catch (Exception e) {
                        mProgreesDialog.dismiss();
                        e.printStackTrace();
                    }
                }
            }, 2000);
        }else{
            runAlertDialog("Ada data yang kosong", 1);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, KeuanganActivity.class);
                intent.putExtra("activity", "add");
                intent.putExtra("tanggal", mTanggal);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, KeuanganActivity.class);
        intent.putExtra("activity", "add");
        intent.putExtra("tanggal", mTanggal);
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
        super.onBackPressed();
    }

    private void startSave(DataKeuanganItemClass dataPengeluaran) throws JSONException {
        final String[] message = new String[2];

        ObjectMapper mapper = new ObjectMapper();
        try {
            String ajsonparam = mapper.writeValueAsString(dataPengeluaran);
            IDatabaseClient databaseClient = ApiGenerator.createService(IDatabaseClient.class);
            final Call<PostExecuteDatabase> call = databaseClient.insertDataPengeluaran(ajsonparam);
            call.enqueue(new Callback<PostExecuteDatabase>() {
                @Override
                public void onResponse(Call<PostExecuteDatabase> call, Response<PostExecuteDatabase> response) {
                    PostExecuteDatabase afterSaveClass = response.body();
                    if (afterSaveClass != null) {
                        if (afterSaveClass.getStatus() == 1) {
                            message[0] = afterSaveClass.getMessage();
                            message[1] = afterSaveClass.getStatus().toString();
                            clearForm();
                        } else {
                            message[0] = afterSaveClass.getMessage();
                            message[1] = afterSaveClass.getStatus().toString();
                        }
                    } else {
                        message[0] = "Tidak mendapatkan data !";
                        message[1] = "0";
                    }
                    if(message[1].equals("1")) {
                        runAlertDialog(message[0], 2);
                    }else{
                        runAlertDialog(message[0], 1);
                    }
                    mProgreesDialog.dismiss();
                }

                @Override
                public void onFailure(Call<PostExecuteDatabase> call, Throwable t) {
                    message[0] = "Akses web service gagal !";
                    message[1] = "0";
                    if(message[1].equals("1")) {
                        runAlertDialog(message[0], 2);
                    }else{
                        runAlertDialog(message[0], 1);
                    }
                    mProgreesDialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearForm(){
        formElementJenisPengeluaran.setValue("");
        formElementJumlahPengeluaran.setValue("");
        mFormBuilder.refreshView();
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
}
