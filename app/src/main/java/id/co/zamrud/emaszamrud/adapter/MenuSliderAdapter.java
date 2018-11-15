package id.co.zamrud.emaszamrud.adapter;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class MenuSliderAdapter extends SliderAdapter {
    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder viewHolder) {
        switch (position) {
            case 0:
                viewHolder.bindImageSlide("http://zamrud.primafora.com/assets/img/Android/menu_banner/banner1.jpg");
                break;
            case 1:
                viewHolder.bindImageSlide("http://zamrud.primafora.com/assets/img/Android/menu_banner/banner2.jpg");
                break;
            case 2:
                viewHolder.bindImageSlide("http://zamrud.primafora.com/assets/img/Android/menu_banner/banner3.jpg");
                break;
        }
    }
}
