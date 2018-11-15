package id.co.zamrud.emaszamrud.database;


import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import ss.com.bannerslider.ImageLoadingService;

public class MenuSliderLoadingService implements ImageLoadingService {
    public Context context;

    public MenuSliderLoadingService(Context context) {
        this.context = context;
    }

    @Override
    public void loadImage(String url, ImageView imageView) {
        Glide.with(context).load(url).into(imageView);
    }

    @Override
    public void loadImage(int resource, ImageView imageView) {
        Glide.with(context).load(resource).into(imageView);
    }

    @Override
    public void loadImage(String url, int placeHolder, int errorDrawable, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
                                .placeholder(placeHolder)
                                .error(errorDrawable)
                )
                .into(imageView);
    }
}
