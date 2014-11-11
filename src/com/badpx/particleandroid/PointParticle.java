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

import android.graphics.*;

/**
 * Created with IntelliJ IDEA.
 * User: kanedong
 * Date: 14/11/6
 */
public class PointParticle extends Particle {
    private Paint mPaint = new Paint();

    public PointParticle(ParticleSystem particleSystem) {
        super(particleSystem);
    }

    @Override
    public void draw(Canvas canvas) {
        if (null != parent.mColorFilterMode) {
            mPaint.setColorFilter(
                    new PorterDuffColorFilter(color, parent.mColorFilterMode));
        }
        mPaint.setColor(color);

        if (mPaint.getXfermode() != parent.mXfermode) {
            mPaint.setXfermode(parent.mXfermode);
        }
        canvas.drawCircle(0, 0, size / 2, mPaint);
    }
}
