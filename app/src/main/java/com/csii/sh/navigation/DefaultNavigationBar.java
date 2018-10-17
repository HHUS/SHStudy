package com.csii.sh.navigation;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.csii.sh.R;

/**
 * autor : sunhao
 * time  : 2018/01/18  22:35
 * desc  :
 */

public class DefaultNavigationBar extends AbsNavigation<DefaultNavigationBar.Builder.DefaultNavigationParams> {

    public DefaultNavigationBar(Builder.DefaultNavigationParams params) {
        super(params);
    }

    @Override
    public int bindLayoutId() {
        return R.layout.navigation_default;
    }

    @Override
    public void applyView() {
        // 给我们的导航条绑定资源
        setImageResource(R.id.img_left_Id, getParams().leftIconRes);
        setImageResource(R.id.img_right_Id, getParams().rightIconRes);
        setText(R.id.tv_title_id, getParams().title);
        setBackgroundColor(R.id.tv_title_id, getParams().bgColor);
        setOnClickListener(R.id.left_ll, getParams().leftOnClickListener);
        setOnClickListener(R.id.right_ll, getParams().rightOnClickListener);

        if (getParams().rightIconShow) {
            findViewById(R.id.right_ll).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.right_ll).setVisibility(View.GONE);
        }

        if (getParams().leftIconShow) {
            findViewById(R.id.left_ll).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.left_ll).setVisibility(View.GONE);
        }

    }

    // 构建导航条类
    public static class Builder extends AbsNavigation.Builder {

        private DefaultNavigationParams params;

        public Builder(Context context, ViewGroup parent) {
            params = new DefaultNavigationParams(context, parent);
        }

        public Builder setTitle(String title) {
            params.title = title;
            return this;
        }

        public Builder setLeftIcon(int iconRes) {
            params.leftIconRes = iconRes;
            return this;
        }

        public Builder setRightIcon(int iconRes) {
            params.rightIconRes = iconRes;
            return this;
        }

        public Builder setTitleBackgroundColor(int bgColor) {
            params.bgColor = bgColor;
            return this;
        }

        public Builder setLeftOnClickListener(View.OnClickListener onClickListener) {
            params.leftOnClickListener = onClickListener;
            return this;
        }

        public Builder setRightIconShow(boolean isShow) {
            params.rightIconShow = isShow;
            return this;
        }

        public Builder setLeftIconShow(boolean isShow) {
            params.leftIconShow = isShow;
            return this;
        }

        public Builder setRightOnClickListener(View.OnClickListener onClickListener) {
            params.rightOnClickListener = onClickListener;
            return this;
        }

        @Override
        public DefaultNavigationBar create() {
            DefaultNavigationBar navigation = new DefaultNavigationBar(params);
            return navigation;
        }

        // 默认的配置参数
        public static class DefaultNavigationParams extends NavigationParams {
            //标题
            public String title;
            //左边图片资源
            public int leftIconRes;
            //右边图片资源
            public int rightIconRes;
            //左边的点击事件
            public View.OnClickListener leftOnClickListener;
            //右边的点击事件
            public View.OnClickListener rightOnClickListener;

            public int bgColor = R.color.colorAccent
                    ;

            public boolean rightIconShow = true;

            public boolean leftIconShow = true;

            public DefaultNavigationParams(Context context, ViewGroup parent) {
                super(context, parent);
            }
        }
    }

}
