package id.co.zamrud.emaszamrud.module;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.zamrud.emaszamrud.MainActivity;
import id.co.zamrud.emaszamrud.R;
import id.co.zamrud.emaszamrud.database.ApiGenerator;
import id.co.zamrud.emaszamrud.database.IDatabaseClient;
import id.co.zamrud.emaszamrud.model.LoginClass;
import id.co.zamrud.emaszamrud.module.shop.ShopActivity;
import id.co.zamrud.emaszamrud.util.AlertDialogClass2;
import id.co.zamrud.emaszamrud.util.Config;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.txt_username)
    TextView mTxtUsername;
    @BindView(R.id.txt_password)
    TextView mTxtPassword;
    @BindView(R.id.txtVersi)
    TextView mTxtVersi;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    ProgressDialog mProgreesDialog;
    IDatabaseClient mClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        try {
            mTxtVersi.setText("Versi : " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        mProgreesDialog = new ProgressDialog(this);
        mProgreesDialog.setMessage("Authenticating...");
    }

    @OnClick(R.id.btn_login)
    public void submit(View view) {
        Config.getVibrate(LoginActivity.this);
        mProgreesDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    CallLoginAPI();
                } catch (JSONException e) {
                    mProgreesDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, 2000);
    }

    public void CallLoginAPI() throws JSONException {
        if (!validate()) {
            onLoginFailed();
            mProgreesDialog.dismiss();
            return;
        }

        JSONObject ajsonobj = new JSONObject();
        ajsonobj.put("username", mTxtUsername.getText().toString());
        ajsonobj.put("password", mTxtPassword.getText().toString());

        mClient = ApiGenerator.createService(IDatabaseClient.class);
        final Call<LoginClass> call = mClient.LoginUser(ajsonobj.toString());
        call.enqueue(new Callback<LoginClass>() {
            @Override
            public void onResponse(Call<LoginClass> call, Response<LoginClass> response) {
                Config.LOGIN_CLASS = response.body();
                if (Config.LOGIN_CLASS != null) {
                    Config.LOGIN_CLASS.setUsername(mTxtUsername.getText().toString());
                    Config.LOGIN_CLASS.setPassword(mTxtPassword.getText().toString());
                    if (Config.LOGIN_CLASS.getStatus() == 1) {
                        Config.IS_LOGIN = true;
                        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                        startActivity(intent);
                    } else {
                        runAlertDialog(Config.LOGIN_CLASS.getMessage(), 1);
                    }
                } else {
                    runAlertDialog("No Internet Connection", 1);
                }
                mProgreesDialog.dismiss();
            }

            @Override
            public void onFailure(Call<LoginClass> call, Throwable t) {
                mProgreesDialog.dismiss();
            }
        });
    }

    public void onLoginFailed() {
        runAlertDialog("Login Failed !", 1);
        mBtnLogin.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        if (mTxtUsername.getText().toString().isEmpty()) {
            mTxtUsername.setError("enter a valid username");
            valid = false;
        } else {
            mTxtUsername.setError(null);
        }

        if (mTxtPassword.getText().toString().isEmpty()) {
            mTxtPassword.setError("enter a valid password");
            valid = false;
        } else {
            mTxtPassword.setError(null);
        }
        return valid;
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
