package com.example.administrator.essim.utils_re;

import android.view.View;

import com.ToxicBakery.viewpager.transforms.ABaseTransformer;

public class ZoomTrans extends ABaseTransformer{


        @Override
        protected void onTransform(View view, float position) {
            final float scale = 0.5f + Math.abs(position);
            view.setScaleX(scale);
            view.setScaleY(scale);
            view.setPivotX(view.getWidth() * 0.25f);
            view.setPivotY(view.getHeight() * 0.25f);
            view.setAlpha(position < -1f || position > 1f ? 0f : 1f - (scale - 1f));
            if(position == -1){
                view.setTranslationX(view.getWidth() * -1);
            }

    }
}
