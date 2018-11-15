package it.starksoftware.ssform.interfaces;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.List;

import it.starksoftware.ssform.activities.ShadowActivity;
import it.starksoftware.ssform.features.ImagePicker;
import it.starksoftware.ssform.features.ImagePickerActivity;
import it.starksoftware.ssform.model.Image;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;


public class ImagePickerObservable implements Observable.OnSubscribe<List<Image>> {

    private Context context;
    private ImagePicker imagePicker;

    public ImagePickerObservable(Context context, ImagePicker builder) {
        this.imagePicker = builder;
        this.context = context.getApplicationContext();
    }

    @Override
    public void call(Subscriber<? super List<Image>> subscriber) {
        startImagePicker();

        subscriber.add(Subscriptions.create(new Action0() {
            @Override
            public void call() {
                finishImagePicker();
            }
        }));
        subscriber.onCompleted();
    }

    private void finishImagePicker() {
        Intent intent = new Intent(context, ImagePickerActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        context.startActivity(intent);
    }

    private void startImagePicker() {
        Bundle bundle = imagePicker.getIntent(context).getExtras();
        Intent intent = ShadowActivity.getStartIntent(context, bundle)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }
}
