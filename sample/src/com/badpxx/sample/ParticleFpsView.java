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

package com.badpxx.sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import com.badpx.particleandroid.ParticleSystem;
import com.badpx.particleandroid.widget.ParticleSystemView;

/**
 * Created with IntelliJ IDEA.
 * User: kanedong
 * Date: 14/11/11
 */
public final class ParticleFpsView extends ParticleSystemView {
    private Paint mPaint = new Paint();
    private long mTimer;
    private int mFrameCount;
    private int mFps;
    private int mPosX, mPoxY;

    public ParticleFpsView(Context context) {
        super(context);
    }

    public ParticleFpsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mPaint.setColor(Color.GRAY);
        mPaint.setTextSize(32);
        mPosX = 50;
        mPoxY = 50;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        long time = SystemClock.uptimeMillis();
        if (time - mTimer >= 1000) {
            mTimer = time;
            mFps = mFrameCount;
            mFrameCount = 0;
        }
        ++mFrameCount;
        canvas.drawText(String.format("FPS: %d", mFps), mPosX, mPoxY, mPaint);

        int particleTotal = 0;
        for (ParticleSystem particleSystem : mParticleSystems) {
            particleTotal += particleSystem.getParticleCount();
        }
        canvas.drawText(String.format("Particle Count: %d", particleTotal), mPosX, mPoxY + 50, mPaint);
    }
}
