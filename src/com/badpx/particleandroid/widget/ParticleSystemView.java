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
package com.badpx.particleandroid.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import com.badpx.particleandroid.ParticleSystem;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: kanedong
 * Date: 14/11/6
 */
public class ParticleSystemView extends View {
    protected Set<ParticleSystem> mParticleSystems;
    private ParticleSystem.UpdateCallback mUpdateCallback =
            new ParticleSystem.UpdateCallback() {
                    @Override
                    public void needInvalidate() {
                        ParticleSystemView.this.invalidate();
                    }
                };

    public ParticleSystemView(Context context) {
        super(context);
        init();
    }

    public ParticleSystemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ParticleSystemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mParticleSystems = new HashSet<ParticleSystem>();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        restartParticleSystems();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        shutdownParticleSystems();
    }

    public void addParticleSystem(ParticleSystem particleSystem) {
        if (null != particleSystem) {
            if (mParticleSystems.add(particleSystem)) {
                particleSystem.setUpdateCallback(mUpdateCallback);
            }
        }
    }

    public void removeParticleSystem(ParticleSystem particleSystem) {
        if (null != particleSystem) {
            if (mParticleSystems.remove(particleSystem)) {
                particleSystem.setUpdateCallback(null);
            }
        }
    }

    public void clearParticleSystems() {
        for (ParticleSystem particleSystem : mParticleSystems) {
            particleSystem.setUpdateCallback(null);
        }
        mParticleSystems.clear();
    }

    void shutdownParticleSystems() {
        for (ParticleSystem particleSystem : mParticleSystems) {
            particleSystem.shutdown();
        }
    }

    void restartParticleSystems() {
        for (ParticleSystem particleSystem : mParticleSystems) {
            particleSystem.start();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        for (ParticleSystem particleSystem : mParticleSystems) {
            particleSystem.draw(canvas);
        }
    }
}
