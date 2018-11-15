package id.co.zamrud.emaszamrud.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import id.co.zamrud.emaszamrud.MainActivity;
import id.co.zamrud.emaszamrud.R;
import id.co.zamrud.emaszamrud.model.MenuClass;
import id.co.zamrud.emaszamrud.module.LoginActivity;
import id.co.zamrud.emaszamrud.module.MenuActivity;
import id.co.zamrud.emaszamrud.module.kajian.KajianActivity;
import id.co.zamrud.emaszamrud.module.keuangan.KeuanganActivity;
import id.co.zamrud.emaszamrud.module.keuangan2.Keuangan2Activity;
import id.co.zamrud.emaszamrud.module.liqo.LiqoActivity;
import id.co.zamrud.emaszamrud.module.shalat.ShalatActivity;
import id.co.zamrud.emaszamrud.module.shop.ShopActivity;
import id.co.zamrud.emaszamrud.util.AlertDialogClass2;
import id.co.zamrud.emaszamrud.util.Config;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {
    private Context mContext;
    private List<MenuClass> menuList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView aTitle;
        public ImageView aIconMenu;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            aTitle = (TextView) view.findViewById(R.id.titleMenu);
            aIconMenu = (ImageView) view.findViewById(R.id.iconMenu);
            cardView = (CardView) view.findViewById(R.id.card_viewmenu);
        }
    }

    public MenuAdapter(Context mContext, List<MenuClass> menuList) {
        this.mContext = mContext;
        this.menuList = menuList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        MenuClass menuClass = menuList.get(position);
        holder.aTitle.setText(menuClass.getTitle());
        Glide.with(mContext).load(menuClass.getIconMenu()).into(holder.aIconMenu);

        final int id = menuClass.getId();

        holder.aIconMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Class classActivity = getMenuActivity(id);
                if (classActivity == null) {
                    runAlertDialog("Silahkan login terlebih dahulu !", 3);
                }else {
                    Intent aintent = new Intent(v.getContext(), classActivity);
                    ((Activity) v.getContext()).startActivityForResult(aintent, 1);
                }
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Class classActivity = getMenuActivity(id);
                if (classActivity == null) {
                    runAlertDialog("Silahkan login terlebih dahulu !", 3);
                }else {
                    Intent aintent = new Intent(v.getContext(), classActivity);
                    ((Activity) v.getContext()).startActivityForResult(aintent, 1);
                }
            }
        });
    }

    private Class getMenuActivity(int id) {
        Class aclass = null;
        switch (id) {
            case 1:
                aclass = LiqoActivity.class;
                break;
            case 2:
                if (Config.IS_LOGIN) {aclass = ShopActivity.class;}
                break;
            case 3:
                aclass = ShalatActivity.class;
                break;
            case 4:
                aclass = KajianActivity.class;
                break;
            case 5:
                if (Config.IS_LOGIN) {aclass = KeuanganActivity.class;}
                break;
        }
        return aclass;
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    //default function
    public void runAlertDialog(String imsg, int itipe) { //1 error, 2 sukses, 3 info
        AlertDialog alertDialog = AlertDialogClass2.alertDialog(imsg, itipe, mContext);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }
}
