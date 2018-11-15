package id.co.zamrud.emaszamrud.module.liqo;

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
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.zamrud.emaszamrud.R;
import id.co.zamrud.emaszamrud.database.ApiGenerator;
import id.co.zamrud.emaszamrud.database.IDatabaseClient;
import id.co.zamrud.emaszamrud.model.DataLiqo;
import id.co.zamrud.emaszamrud.model.DataPenjualanItemClass;
import id.co.zamrud.emaszamrud.model.PostExecuteDatabase;
import id.co.zamrud.emaszamrud.model.quran.DataSuratClass;
import id.co.zamrud.emaszamrud.model.quran.DataSuratItemClass;
import id.co.zamrud.emaszamrud.util.AlertDialogClass2;
import id.co.zamrud.emaszamrud.util.Config;
import it.starksoftware.ssform.helper.FormBuildHelper;
import it.starksoftware.ssform.interfaces.ButtonCallBack;
import it.starksoftware.ssform.interfaces.SearchableSpinnerCallBack;
import it.starksoftware.ssform.model.FormElement;
import it.starksoftware.ssform.model.FormElementButton;
import it.starksoftware.ssform.model.FormElementMemo;
import it.starksoftware.ssform.model.FormElementSearchableSpinner;
import it.starksoftware.ssform.model.FormObject;
import it.starksoftware.ssform.model.FormSpinnerObject;
import it.starksoftware.ssform.model.Validator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditDataLiqoActivity extends AppCompatActivity implements
        ButtonCallBack,
        SearchableSpinnerCallBack {

    // SPINNER surat
    //diakali pake 3 recycle view soalnya tidak bisa 3 dropdown dalam satu menu
    public FormBuildHelper mFormBuilderSurat;
    @BindView(R.id.rvSpinnerSurat)
    RecyclerView recyclerViewSurat;

    // SPINNER ayat
    public FormBuildHelper mFormBuilderAyat;
    @BindView(R.id.rvSpinnerAyat)
    RecyclerView recyclerViewAyat;

    // SPINNER juz dan all
    public FormBuildHelper mFormBuilderJuz;
    @BindView(R.id.rvSpinnerJuz)
    RecyclerView recyclerViewJuz;

    // SPINNER juz dan all
    public FormBuildHelper mFormBuilderAll;
    @BindView(R.id.rvAddLiqo)
    RecyclerView recyclerViewAll;

    String mTanggal;
    IDatabaseClient mClient;
    ProgressDialog mProgreesDialog;

    DataSuratClass mDataSuratClass;
    DataLiqo mDataLiqo = new DataLiqo();

    public FormElementMemo formElementKeterangan;
    public FormElement formElementSaldoAwal, formElementPemasukan, formElementTema, formElementPJ;
    public FormElementSearchableSpinner formDropDownSurat, formDropDownAyat = new FormElementSearchableSpinner(), formDropDownJuz =  new FormElementSearchableSpinner();
    public FormElementButton formElementButtonUpdate;

    public List<FormObject> formItemsAyat, formItemsJuz;

    public ArrayList<FormSpinnerObject> objSpinnerSurat;
    public ArrayList<FormSpinnerObject> objSpinnerAyat;
    public ArrayList<FormSpinnerObject> objSpinnerJuz;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_liqo);
        mDataLiqo = (DataLiqo) getIntent().getSerializableExtra("pass_data_liqo");


        mProgreesDialog = new ProgressDialog(this);
        mProgreesDialog.setMessage("Authenticating...");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //untuk get tanggal yang kirim dari activity liqo
        mTanggal = getIntent().getStringExtra("tanggal");

        ButterKnife.bind(this);
        mFormBuilderSurat = new FormBuildHelper(this, this, recyclerViewSurat, getSupportFragmentManager());
        mFormBuilderAyat = new FormBuildHelper(this, this, recyclerViewAyat, getSupportFragmentManager());
        mFormBuilderJuz = new FormBuildHelper(this, this, recyclerViewJuz, getSupportFragmentManager());
        mFormBuilderAll = new FormBuildHelper(this, this, recyclerViewAll, getSupportFragmentManager());

        setupFormSurat();
        setupFormAyat(true);
        setupFormJuz(true);
        setupFormAll();
    }

    public void setupFormSurat() {
        final boolean[] status = {false};
        mProgreesDialog.show();
        mClient = ApiGenerator.createServiceQuran(IDatabaseClient.class);
        final Call<DataSuratClass> call = mClient.getDataSurat();
        call.enqueue(new Callback<DataSuratClass>() {
            @Override
            public void onResponse(Call<DataSuratClass> call, Response<DataSuratClass> response) {
                mDataSuratClass = response.body();
                if (mDataSuratClass != null) {
                    if (mDataSuratClass.getStatus().equals("OK")) {
                        objSpinnerSurat = new ArrayList<FormSpinnerObject>();
                        FormSpinnerObject item = new FormSpinnerObject();
                        item.setKey("");
                        item.setValue("");

                        for(DataSuratItemClass data : mDataSuratClass.getData()){
                            item = new FormSpinnerObject();
                            item.setKey(String.valueOf(data.getNumber()));
                            item.setValue(data.getEnglishName());
                            objSpinnerSurat.add(item);
                        }
                        setItemSurat(objSpinnerSurat);
                        mProgreesDialog.dismiss();
                    } else {
                        runAlertDialog3("Tidak ada data !", 1, "refresh");
                        mProgreesDialog.dismiss();
                    }
                } else {
                    runAlertDialog3("Tidak ada data !", 1, "refresh");
                    mProgreesDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<DataSuratClass> call, Throwable t) {
                runAlertDialog3(t.getMessage(), 1, "refresh");
                mProgreesDialog.dismiss();
            }
        });
    }

    public ArrayList<FormSpinnerObject> ayatValues(int jmlAyat) {
        objSpinnerAyat = new ArrayList<FormSpinnerObject>();
        FormSpinnerObject item = new FormSpinnerObject();
        item.setKey("");
        item.setValue("");

        for (int p = 0; p < jmlAyat; p++) {
            item = new FormSpinnerObject();
            item.setKey(String.valueOf(p + 1));
            item.setValue(String.format(Locale.getDefault(), "%d", p + 1));
            objSpinnerAyat.add(item);
        }

        return objSpinnerAyat;
    }

    public ArrayList<FormSpinnerObject> juzValues(int jmlJuz) {
        objSpinnerJuz = new ArrayList<FormSpinnerObject>();
        FormSpinnerObject item = new FormSpinnerObject();
        item.setKey("");
        item.setValue("");

        for (int p = 0; p < jmlJuz; p++) {
            item = new FormSpinnerObject();
            item.setKey(String.valueOf(p + 1));
            item.setValue(String.format(Locale.getDefault(), "%d", p + 1));
            objSpinnerJuz.add(item);
        }

        return objSpinnerJuz;
    }

    public void setItemSurat(ArrayList<FormSpinnerObject> suratValues){
        formDropDownSurat = new FormElementSearchableSpinner();

        formDropDownSurat
                .setCallback(this)
                .setActivity(this)
                .setContext(this)
                .setDialogTitle("Pilih Surat")
                .setValue(mDataLiqo.getNama_surat())
                .setSpinnerObject(suratValues)
                .setTitle("Surat")
                .setTag(1);

        formDropDownAyat = new FormElementSearchableSpinner();

        List<FormObject> formItems = new ArrayList<>();
        formItems.add(formDropDownSurat);

        mFormBuilderSurat.addFormElements(formItems);
        mFormBuilderSurat.refreshView();
    }

    public void setupFormAyat(boolean isAwal){
        formDropDownAyat
                .setCallback(this)
                .setActivity(this)
                .setContext(this)
                .setDialogTitle("Pilih Ayat")
                .setValue(mDataLiqo.getAyat())
                .setTitle("Ayat")
                .setTag(2);

        if(isAwal){
            formDropDownAyat.setSpinnerObject(ayatValues(0));
        }

        formItemsAyat = new ArrayList<>();
        formItemsAyat.add(formDropDownAyat);
        mFormBuilderAyat.addFormElements(formItemsAyat);
        mFormBuilderAyat.refreshView();
    }

    public void setupFormJuz(boolean isAwal){
        formDropDownJuz
                .setCallback(this)
                .setActivity(this)
                .setContext(this)
                .setValue(mDataLiqo.getJuz())
                .setDialogTitle("Pilih Juz")
                .setTitle("Juz")
                .setTag(3);

        if(isAwal){
            formDropDownJuz.setSpinnerObject(juzValues(0));
        }

        formItemsJuz = new ArrayList<>();
        formItemsJuz.add(formDropDownJuz);
        mFormBuilderJuz.addFormElements(formItemsJuz);
        mFormBuilderJuz.refreshView();
    }

    public void setupFormAll(){
        formElementTema = FormElement.createInstance()
                .setTitle("Tema")
                .setRequired(true)
                .setValue(mDataLiqo.getTema())
                .setRequiredResponseMessage("!!! REQUIRED !!!")
                .setTag(4);

        formElementSaldoAwal = FormElement.createInstance()
                .setTitle("Saldo awal (Rp.)")
                .setType(FormElement.TYPE_EDITTEXT_NUMBER_INTEGER)
                .setValue(mDataLiqo.getSaldoAwal())
                .setRequired(true)
                .setRequiredResponseMessage("!!! REQUIRED !!!")
                .setTag(5);

        formElementPemasukan = FormElement.createInstance()
                .setTitle("Pemasukan (Rp.)")
                .setType(FormElement.TYPE_EDITTEXT_NUMBER_INTEGER)
                .setRequired(true)
                .setValue(mDataLiqo.getPemasukan())
                .setRequiredResponseMessage("!!! REQUIRED !!!")
                .setTag(6);

        formElementPJ = FormElement.createInstance()
                .setTitle("PJ")
                .setRequired(true)
                .setValue(mDataLiqo.getPj())
                .setRequiredResponseMessage("!!! REQUIRED !!!")
                .setTag(7);

        formElementKeterangan = FormElementMemo.createInstance()
                .setTitle("Keterangan")
                .setValue(mDataLiqo.getKeterangan())
                .setTag(8);

        formElementButtonUpdate = FormElementButton.createInstance()
                .setTitle("Update")
                .setButtonCallBack(this)
                .setTag(9);

        List<FormObject> formItems = new ArrayList<>();
        formItems.add(formElementTema);
        formItems.add(formElementSaldoAwal);
        formItems.add(formElementPemasukan);
        formItems.add(formElementPJ);
        formItems.add(formElementKeterangan);
        formItems.add(formElementButtonUpdate);

        mFormBuilderAll.addFormElements(formItems);
        mFormBuilderAll.refreshView();
    }

    @Override
    public void callbackButtonReturn(FormElementButton formElementButton, Object o) {
        List<Validator> validatorResult = this.mFormBuilderAll.validateForm();
        if (validatorResult.size() <= 0) {
            mDataLiqo.setTema(formElementTema.getValue());
            mDataLiqo.setKeterangan(formElementKeterangan.getValue());
            mDataLiqo.setSaldoAwal(formElementSaldoAwal.getValue());
            mDataLiqo.setPemasukan(formElementPemasukan.getValue());
            mDataLiqo.setPj(formElementPJ.getValue());
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
            runAlertDialog("Ada data yang kosong", 1);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, LiqoActivity.class);
                intent.putExtra("tanggal", (Serializable) mTanggal);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LiqoActivity.class);
        intent.putExtra("tanggal", (Serializable) mTanggal);
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
        super.onBackPressed();
    }

    private void startUpdate() throws JSONException {
        final String[] message = new String[2];

        ObjectMapper mapper = new ObjectMapper();
        try {
            String ajsonparam = mapper.writeValueAsString(mDataLiqo);
            IDatabaseClient databaseClient = ApiGenerator.createService(IDatabaseClient.class);
            final Call<PostExecuteDatabase> call = databaseClient.updateDataLiqo(ajsonparam);
            call.enqueue(new Callback<PostExecuteDatabase>() {
                @Override
                public void onResponse(Call<PostExecuteDatabase> call, Response<PostExecuteDatabase> response) {
                    PostExecuteDatabase afterSaveClass = response.body();
                    if (afterSaveClass != null) {
                        if (afterSaveClass.getStatus() == 1) {
                            message[0] = afterSaveClass.getMessage();
                            message[1] = afterSaveClass.getStatus().toString();
                        } else {
                            message[0] = afterSaveClass.getMessage();
                            message[1] = afterSaveClass.getStatus().toString();
                        }
                    } else {
                        message[0] = "Tidak mendapatkan data !";
                        message[1] = "0";
                    }
                    if(message[1].equals("1")) {
                        runAlertDialog2(message[0], 2, "update");
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
                        runAlertDialog2(message[0], 2, "update");
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

    @Override
    public void callbackSearchableSpinnerReturn(FormElementSearchableSpinner formElementSearchableSpinner, Object o, FormSpinnerObject formSpinnerObject) {
        if(formElementSearchableSpinner.getTag() == 1) {
            int akey = Integer.valueOf(formSpinnerObject.getKey());
            int jmlAyat = mDataSuratClass.getData().get(akey - 1).getNumberOfAyahs();

            mDataLiqo.setSurat(String.valueOf(akey));
            mDataLiqo.setNama_surat(formSpinnerObject.getValue());
            formItemsAyat.remove(0);
            formDropDownAyat.setSpinnerObject(ayatValues(jmlAyat));
            formDropDownAyat.setValue("");
            setupFormAyat(false);
        } else if(formElementSearchableSpinner.getTag() == 2) {
            mDataLiqo.setAyat(formSpinnerObject.getKey());
            formDropDownJuz.setSpinnerObject(juzValues(30));
            formDropDownAyat.setValue("");
            setupFormJuz(false);
        } else if(formElementSearchableSpinner.getTag() == 3) {
            mDataLiqo.setJuz(formSpinnerObject.getKey());
        }
    }

    //default function
    public void refresh() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
        //Toast.makeText(this, "Anda berhasil logout", Toast.LENGTH_SHORT).show();
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

    public void runAlertDialog2(String imsg, int itipe, String iopsi) { //1 error, 2 sukses, 3 info
        AlertDialog alertDialog = AlertDialogClass2.alertDialog(imsg, itipe, this);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                if(iopsi == "update"){
                    Intent intent = new Intent(EditDataLiqoActivity.this, LiqoActivity.class);
                    intent.putExtra("tanggal", (Serializable) mTanggal);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            }
        });
        alertDialog.show();
    }

    public void runAlertDialog3(String imsg, int itipe, final String iopsi) { //1 error, 2 sukses, 3 info
        AlertDialog alertDialog = AlertDialogClass2.alertDialog(imsg, itipe, this);
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Kembali", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                Intent intent = new Intent(EditDataLiqoActivity.this, LiqoActivity.class);
                intent.putExtra("tanggal", (Serializable) mTanggal);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, iopsi, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                if (iopsi == "refresh") {
                    refresh();
                }
            }
        });

        alertDialog.show();
    }
}
