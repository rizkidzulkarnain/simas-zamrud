package id.co.zamrud.emaszamrud.module.kajian;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.appus.splash.Splash;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.zamrud.emaszamrud.R;
import id.co.zamrud.emaszamrud.adapter.KajianAdapter;
import id.co.zamrud.emaszamrud.adapter.MenuAdapter;
import id.co.zamrud.emaszamrud.adapter.MenuSliderAdapter;
import id.co.zamrud.emaszamrud.database.MenuSliderLoadingService;
import id.co.zamrud.emaszamrud.model.DataKajian;
import id.co.zamrud.emaszamrud.model.MenuClass;
import id.co.zamrud.emaszamrud.module.MenuActivity;
import id.co.zamrud.emaszamrud.module.liqo.LiqoActivity;
import id.co.zamrud.emaszamrud.util.AlertDialogClass2;
import id.co.zamrud.emaszamrud.util.Config;
import ss.com.bannerslider.Slider;

public class KajianActivity extends AppCompatActivity {
    @BindView(R.id.rv_kajian)
    RecyclerView mRvKajian;

    private KajianAdapter adapter;
    private List<DataKajian> dataKajianList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kajian);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        dataKajianList = new ArrayList<>();
        adapter = new KajianAdapter(this, dataKajianList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(KajianActivity.this);
        mRvKajian.setLayoutManager(mLayoutManager);
        mRvKajian.setAdapter(adapter);

        prepareKajians();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(KajianActivity.this, MenuActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void prepareKajians() {
        int[] fotoPenceramahs = new int[]{
                R.drawable.ustadz,
                R.drawable.ustadz,
                R.drawable.ustadz,
                R.drawable.ustadz
        };

        int[] colorBGs = new int[]{
                R.color.colorKajian1,
                R.color.colorKajian2,
                R.color.colorKajian3,
                R.color.colorKajian4
        };

        DataKajian d = new DataKajian(
                fotoPenceramahs[3],
                "KH. Asep Dadang Ponpes Darussalam",
                "Nashoihul Ibad",
                "Tasawuf",
                "Rutin setiap hari selasa malam rabu",
                "di Pondok Pesantren Darussalam",
                colorBGs[3]
        );
        dataKajianList.add(d);

        DataKajian b = new DataKajian(
                fotoPenceramahs[1],
                "Ustadz Hidayatullah",
                "Al-quran",
                "Tafsir Al-quran",
                "Rutin setiap hari rabu malam kamis",
                "di Masjid Zamrud Komp. Permata Cimahi",
                colorBGs[1]
        );
        dataKajianList.add(b);

        DataKajian c = new DataKajian(
                fotoPenceramahs[2],
                "Anggota Liqo",
                "Al-quran",
                "Tadarrus Bersama",
                "Rutin setiap hari kamis malam jumat",
                "di Masjid Zamrud Komp. Permata Cimahi",
                colorBGs[2]
        );
        dataKajianList.add(c);



        DataKajian a = new DataKajian(
                fotoPenceramahs[0],
                "KH. Asep Dadang Ponpes Darussalam",
                "Tanwirul Qulub",
                "Akhlak",
                "Rutin setiap hari sabtu malam minggu",
                "di Masjid Zamrud Komp. Permata Cimahi",
                colorBGs[0]
        );
        dataKajianList.add(a);

        adapter.notifyDataSetChanged();
    }
}
