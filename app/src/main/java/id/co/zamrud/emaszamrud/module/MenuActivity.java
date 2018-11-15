package id.co.zamrud.emaszamrud.module;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.appus.splash.Splash;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.zamrud.emaszamrud.R;
import id.co.zamrud.emaszamrud.adapter.MenuAdapter;
import id.co.zamrud.emaszamrud.adapter.MenuSliderAdapter;
import id.co.zamrud.emaszamrud.database.MenuSliderLoadingService;
import id.co.zamrud.emaszamrud.model.MenuClass;
import id.co.zamrud.emaszamrud.module.liqo.AddDataLiqoActivity;
import id.co.zamrud.emaszamrud.module.liqo.LiqoActivity;
import id.co.zamrud.emaszamrud.module.shop.ShopActivity;
import id.co.zamrud.emaszamrud.util.AlertDialogClass2;
import id.co.zamrud.emaszamrud.util.Config;
import ss.com.bannerslider.ImageLoadingService;
import ss.com.bannerslider.Slider;

public class MenuActivity extends AppCompatActivity {

    @BindView(R.id.rv_menu)
    RecyclerView mRvMenu;

    @BindView(R.id.banner_slider1)
    Slider mBannerSlider;

    private MenuAdapter adapter;
    private List<MenuClass> menuList;
    MenuSliderLoadingService mImageLoadingService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);

        Splash.Builder splash = new Splash.Builder(this, getSupportActionBar());
        splash.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        splash.setSplashImage(getResources().getDrawable(R.drawable.medium_logo));
        splash.perform();

        menuList = new ArrayList<>();
        adapter = new MenuAdapter(this, menuList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        mRvMenu.setLayoutManager(mLayoutManager);
        mRvMenu.setAdapter(adapter);

        prepareMenus();
        prepareSlider();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_data_menu, menu);
        if (Config.IS_LOGIN) {
            menu.findItem(R.id.action_login).setVisible(false);
            menu.findItem(R.id.action_logout).setVisible(true);
        } else {
            menu.findItem(R.id.action_login).setVisible(true);
            menu.findItem(R.id.action_logout).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_login:
                Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_logout:
                runAlertDialog3("Apakah anda yakin ingin logout ?", 3, "logout");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void prepareMenus() {
        int[] covers = new int[]{
                R.drawable.ngaji,
                R.drawable.shop_icon,
                R.drawable.shalat,
                R.drawable.ustadz,
                R.drawable.keuangan
        };

        MenuClass a = new MenuClass(1, getResources().getString(R.string.menu1), covers[0]);
        menuList.add(a);

        a = new MenuClass(2, getResources().getString(R.string.menu2), covers[1]);
        menuList.add(a);

        a = new MenuClass(3, getResources().getString(R.string.menu3), covers[2]);
        menuList.add(a);

        a = new MenuClass(4, getResources().getString(R.string.menu4), covers[3]);
        menuList.add(a);

        a = new MenuClass(5, getResources().getString(R.string.menu5), covers[4]);
        menuList.add(a);

        adapter.notifyDataSetChanged();
    }

    private void prepareSlider() {
        mImageLoadingService = new MenuSliderLoadingService(this);
        Slider.init(mImageLoadingService);
        mBannerSlider.setAdapter(new MenuSliderAdapter());
    }

    public void refresh() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
        Toast.makeText(this, "Anda berhasil logout", Toast.LENGTH_SHORT).show();
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
                    Config.IS_LOGIN = false;
                    refresh();
                }
            }
        });

        alertDialog.show();
    }
}
