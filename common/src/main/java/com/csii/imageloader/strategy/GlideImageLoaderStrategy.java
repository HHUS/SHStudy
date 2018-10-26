package com.csii.imageloader.strategy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.zybank.common.imageloader.config.ImageConfigImpl;
import com.zybank.common.imageloader.loader.BlurTransformation;
import com.zybank.common.imageloader.loader.GlideAppliesOptions;

public class GlideImageLoaderStrategy implements BaseImageLoaderStrategy<ImageConfigImpl>, GlideAppliesOptions {
    @Override
    public void loadImage(Context ctx, ImageConfigImpl config) {

        RequestManager requestManager = Glide.with(ctx);

        RequestBuilder<Drawable> requestBuilder = requestManager.load(config.getUrl());

        RequestOptions options = new RequestOptions();


        //缓存策略
        switch (config.getCacheStrategy()) {
            case 0:
                options.diskCacheStrategy(DiskCacheStrategy.ALL);
                break;
            case 1:
                options.diskCacheStrategy(DiskCacheStrategy.NONE);
                break;
            case 2:
                options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                break;
            case 3:
                options.diskCacheStrategy(DiskCacheStrategy.DATA);
                break;
            case 4:
                options.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                break;
            default:
                options.diskCacheStrategy(DiskCacheStrategy.ALL);
                break;
        }

        if (config.isCrossFade()) {
            requestBuilder.transition(DrawableTransitionOptions.withCrossFade());
        }

        if (config.isImageRadius()) {
            options.transform(new RoundedCorners(config.getImageRadius()));
        }

        if (config.isBlurImage()) {
            options.transform(new BlurTransformation(config.getBlurValue()));
        }

        //glide用它来改变图形的形状
        if (config.getTransformation() != null) {
            options.transform(config.getTransformation());
        }

        if (config.getPlaceHolderDrawble() != null) {
            options.placeholder(config.getPlaceHolderDrawble());
        }
        //设置占位符
        if (config.getPlaceholder() != 0) {
            options.placeholder(config.getPlaceholder());
        }

        //设置错误的图片
        if (config.getErrorPic() != 0) {
            options.error(config.getErrorPic());
        }

        //设置请求 url 为空图片
        if (config.getFallback() != 0) {
            options.fallback(config.getFallback());
        }

        if (config.getResizeX() != 0 && config.getResizeY() != 0) {
            options.override(config.getResizeX(), config.getResizeY());
        }

        if (config.isCropCenter()) {
            options.centerCrop();
        }

        if (config.isCropCircle()) {
            options.circleCrop();
        }

        if (config.decodeFormate() != null) {
            options.format(config.decodeFormate());
        }

        if (config.isFitCenter()) {
            options.fitCenter();
        }

        requestBuilder.apply(options)
                .into(config.getImageView());

    }

    @Override
    public void clear(final Context ctx, ImageConfigImpl config) {

        //取消在执行的任务并且释放资源
        if (config.getImageViews() != null && config.getImageViews().length > 0) {
            for (ImageView imageView : config.getImageViews()) {
                Glide.get(ctx).getRequestManagerRetriever().get(ctx).clear(imageView);
            }
        }

        //清除本地缓存
        if (config.isClearDiskCache()) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Glide.get(ctx).clearDiskCache();

                }
            }).start();

        }

        //清除内存缓存
        if (config.isClearMemory()) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Glide.get(ctx).clearMemory();

                }
            }).start();
        }
    }

    @Override
    public void applyGlideOptions(Context context, GlideBuilder builder) {

    }
}
