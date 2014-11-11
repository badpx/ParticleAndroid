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
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;

/**
 * Created with IntelliJ IDEA.
 * User: kanedong
 * Date: 14/11/10
 */
public class DrawableParticle extends Particle {
    protected Drawable mDrawable;

    public DrawableParticle(Drawable drawable) {
        mDrawable = drawable;
    }

    @Override
    public void draw(Canvas canvas) {
        if (null != mDrawable) {
            if (mDrawable instanceof ColorDrawable &&
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                ((ColorDrawable)mDrawable).setColor(color);
            }
            mDrawable.setAlpha(Color.alpha(color));

            int pivotX = (mDrawable.getBounds().width() >> 1);
            int pivotY = (mDrawable.getBounds().height() >> 1);

            canvas.save();

            canvas.translate(-pivotX, -pivotY);
            canvas.scale(size, size, pivotX, pivotY);
            canvas.rotate(rotation, pivotX, pivotY);
            mDrawable.draw(canvas);

            canvas.restore();
        }
    }
}
