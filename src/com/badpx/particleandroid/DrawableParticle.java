/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.badpx.particleandroid;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: kanedong
 * Date: 14/11/10
 */
public class DrawableParticle extends Particle {
    protected Drawable mDrawable;
    protected Paint mPaint;

    public DrawableParticle(ParticleSystem particleSystem, Drawable drawable) {
        super(particleSystem);
        mDrawable = drawable;
        if (null != mDrawable) {
            Class clazz = mDrawable.getClass();
            try {
                Method m = clazz.getMethod("getPaint");
                mPaint = (Paint) m.invoke(mDrawable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        Drawable drawable = mDrawable;
        if (null != drawable) {
            if (null != parent.mColorFilterMode) {
                if (!deltaColor.isEmpty()) {
                    drawable.setColorFilter(color, parent.mColorFilterMode);
                }
            } else {
                drawable.clearColorFilter();
            }
            drawable.setAlpha(Color.alpha(color));

            Paint paint = mPaint;
            if (null != paint && paint.getXfermode() != parent.mXfermode) {
                paint.setXfermode(parent.mXfermode);
            }

            int s = (int)size;
            drawable.setBounds(0, 0, s, s);
            float pivot = (s >> 1);

            canvas.save();

            canvas.translate(-pivot, -pivot);
            if (0 != rotation) {
                canvas.rotate(rotation, pivot, pivot);
            }
            drawable.draw(canvas);

            canvas.restore();
        }
    }
}
