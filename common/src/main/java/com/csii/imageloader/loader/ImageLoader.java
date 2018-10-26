package com.csii.imageloader.loader;
import android.content.Context;

import com.csii.imageloader.config.ImageConfig;
import com.csii.imageloader.strategy.BaseImageLoaderStrategy;


public final class ImageLoader {

   private static BaseImageLoaderStrategy mStrategy;

    /**
     * 加载图片
     *
     * @param context
     * @param config
     * @param <T>
     */
    public <T extends ImageConfig> void loadImage(Context context, T config) {
        mStrategy.loadImage(context, config);
    }

    /**
     * 停止加载或清理缓存
     *
     * @param context
     * @param config
     * @param <T>
     */
    public <T extends ImageConfig> void clear(Context context, T config) {
        mStrategy.clear(context, config);
    }

    /**
     * 可在运行时随意切换 {@link BaseImageLoaderStrategy};
     *
     * @param strategy
     */
    public static void setLoadImgStrategy(BaseImageLoaderStrategy strategy) {
        mStrategy = strategy;
    }

    public BaseImageLoaderStrategy getLoadImgStrategy() {
        return mStrategy;
    }
}
