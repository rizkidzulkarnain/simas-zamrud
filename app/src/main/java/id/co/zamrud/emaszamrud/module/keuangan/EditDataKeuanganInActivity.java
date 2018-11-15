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
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.zamrud.emaszamrud.R;
import id.co.zamrud.emaszamrud.database.ApiGenerator;
import id.co.zamrud.emaszamrud.database.IDatabaseClient;
import id.co.zamrud.emaszamrud.model.DataLiqo;
import id.co.zamrud.emaszamrud.model.PostExecuteDatabase;
import id.co.zamrud.emaszamrud.model.keuangan.DataKeuanganClass;
import id.co.zamrud.emaszamrud.model.keuangan.DataKeuanganItemClass;
import id.co.zamrud.emaszamrud.module.liqo.EditDataLiqoActivity;
import id.co.zamrud.emaszamrud.module.liqo.LiqoActivity;
import id.co.zamrud.emaszamrud.module.shop.EditDataPenjualanActivity;
import id.co.zamrud.emaszamrud.module.shop.ShopActivity;
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

public class EditDataKeuanganInActivity extends AppCompatActivity implements ButtonCallBack {

    // BUILDER
    public FormBuildHelper mFormBuilder;
    @BindView(R.id.rvAddKeuangan)
    RecyclerView recyclerView;

    ProgressDialog mProgreesDialog;
    DataKeuanganItemClass mDataPemasukanItem = new DataKeuanganItemClass();

    String mTanggal;
    public FormElementMemo formElementJenisPemasukan;
    public FormElement formElementJumlahPemasukan;
    public FormElementButton formElementButtonSave;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_keuangan);
        ButterKnife.bind(this);

        mDataPemasukanItem = (DataKeuanganItemClass) getIntent().getSerializableExtra("pass_data_pemasukan");
        mTanggal = getIntent().getStringExtra("tanggal");

        mProgreesDialog = new ProgressDialog(this);
        mProgreesDialog.setMessage("Authenticating...");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mFormBuilder = new FormBuildHelper(this, this, recyclerView, getSupportFragmentManager());
        setupForm();
    }

    public void setupForm(){
        formElementJenisPemasukan = FormElementMemo.createInstance()
                .setTitle("Jenis Pemasukan")
                .setValue(mDataPemasukanItem.getJenis())
                .setRequired(true)
                .setTag(80);

        formElementJumlahPemasukan = FormElement.createInstance()
                .setTitle("Jumlah Pemasukan (Rp.)")
                .setType(FormElement.TYPE_EDITTEXT_NUMBER_INTEGER)
                .setRequired(true)
                .setValue(mDataPemasukanItem.getJumlah())
                .setRequiredResponseMessage("!!! REQUIRED !!!")
                .setTag(70);

        formElementButtonSave = FormElementButton.createInstance()
                .setTitle("Save")
                .setButtonCallBack(this)
                .setTag(30);

        List<FormObject> formItems = new ArrayList<>();
        formItems.add(formElementJenisPemasukan);
        formItems.add(formElementJumlahPemasukan);
        formItems.add(formElementButtonSave);

        mFormBuilder.addFormElements(formItems);
        mFormBuilder.refreshView();
    }

    @Override
    public void callbackButtonReturn(FormElementButton formElementButton, Object o) {
        List<Validator> validatorResult = this.mFormBuilder.validateForm();
        if (validatorResult.size() <= 0) {
            mDataPemasukanItem.setJenis(formElementJenisPemasukan.getValue());
            mDataPemasukanItem.setJumlah(formElementJumlahPemasukan.getValue());
            mDataPemasukanItem.setTanggal(mTanggal);

            Config.getVibrate(this);
            mProgreesDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        startUpdate();
                    } catch (Exception e) {
                        mProgreesDialog.dismiss();
                        e.printStackTrace();
                    }
                }
            }, 2000);
        }else{
            runAlertDialog("Ada data yang kosong", 1, false);
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

    private void startUpdate() throws JSONException {
        final String[] message = new String[2];

        ObjectMapper mapper = new ObjectMapper();
        try {
            String ajsonparam = mapper.writeValueAsString(mDataPemasukanItem);
            IDatabaseClient databaseClient = ApiGenerator.createService(IDatabaseClient.class);
            final Call<PostExecuteDatabase> call = databaseClient.updateDataPemasukan(ajsonparam);
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
                        runAlertDialog(message[0], 2, true);
                    }else{
                        runAlertDialog(message[0], 1, false);
                    }
                    mProgreesDialog.dismiss();
                }

                @Override
                public void onFailure(Call<PostExecuteDatabase> call, Throwable t) {
                    message[0] = "Akses web service gagal !";
                    message[1] = "0";
                    if(message[1].equals("1")) {
                        runAlertDialog(message[0], 2, false);
                    }else{
                        runAlertDialog(message[0], 1, false);
                    }
                    mProgreesDialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearForm(){
        formElementJenisPemasukan.setValue("");
        formElementJumlahPemasukan.setValue("");
        mFormBuilder.refreshView();
    }

    //default function
    public void runAlertDialog(String imsg, int itipe, final boolean isUpdate) { //1 error, 2 sukses, 3 info
        AlertDialog alertDialog = AlertDialogClass2.alertDialog(imsg, itipe, this);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                if (isUpdate) {
                    Intent intent = new Intent(EditDataKeuanganInActivity.this, KeuanganActivity.class);
                    intent.putExtra("tanggal", (Serializable) mTanggal);
                    intent.putExtra("activity", (Serializable) "edit");
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            }
        });
        alertDialog.show();
    }
}
