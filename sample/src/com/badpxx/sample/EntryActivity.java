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

import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.badpx.particleandroid.DrawableParticle;
import com.badpx.particleandroid.Particle;
import com.badpx.particleandroid.ParticleSystem;
import com.badpx.particleandroid.preset.ParticleExplosion;
import com.badpx.particleandroid.preset.ParticleFire;
import com.badpx.particleandroid.preset.ParticleRain;
import com.badpx.particleandroid.preset.ParticleSpin;
import com.badpx.particleandroid.widget.ParticleSystemView;


public class EntryActivity extends Activity implements View.OnTouchListener,
        View.OnClickListener {

    static class ParticleInfo {
        public Class clazz;
        public int particleRes;
        public PorterDuff.Mode colorFilter;
        public ParticleSystem particleSystem;

        public ParticleInfo(Class sys, int res, PorterDuff.Mode mode) {
            clazz = sys;
            particleRes = res;
            colorFilter = mode;
        }
    }

    private static final ParticleInfo[] PARTICLE_INFOS = {
            new ParticleInfo(ParticleFire.class, R.drawable.halo, null),
            new ParticleInfo(ParticleExplosion.class, R.drawable.fire, PorterDuff.Mode.SRC_IN),
            new ParticleInfo(ParticleRain.class, 0, null),
            new ParticleInfo(ParticleSpin.class, R.drawable.stars, PorterDuff.Mode.SRC_IN),
    };


    private ParticleSystem mParticleSystem;
    private int mIndex;
    private TextView mTitle;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mTitle = (TextView) findViewById(R.id.name);
        Button btn = (Button) findViewById(R.id.free_mode);
        btn.setOnClickListener(this);
        btn = (Button) findViewById(R.id.group_mode);
        btn.setOnClickListener(this);
        btn = (Button) findViewById(R.id.prev);
        btn.setOnClickListener(this);
        btn = (Button) findViewById(R.id.next);
        btn.setOnClickListener(this);

        setupParticleSystem();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ParticleSystemView particleView = (ParticleSystemView) findViewById(R.id.ps);
        particleView.clearParticleSystems();
    }

    private void setupParticleSystem() {
        ParticleSystemView particleView = (ParticleSystemView) findViewById(R.id.ps);

        int posX = getResources().getDisplayMetrics().widthPixels / 2;
        int posY = getResources().getDisplayMetrics().heightPixels / 2;

        if (null != mParticleSystem) {
            mParticleSystem.stopSystem();
        }

        mParticleSystem = PARTICLE_INFOS[mIndex].particleSystem;
        if (null == mParticleSystem) {
            try {
                mParticleSystem = PARTICLE_INFOS[mIndex].particleSystem =
                        (ParticleSystem)PARTICLE_INFOS[mIndex].clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                return ;
            }
        } else {
            mParticleSystem.resetSystem();
        }
        mTitle.setText(mParticleSystem.getClass().getSimpleName());

        mParticleSystem.setPosition(posX, posY);
        mParticleSystem.setInterval(1000 / 60);
        int resId = PARTICLE_INFOS[mIndex].particleRes;
        if (0 != resId) {
            final Drawable drawableCommon =
                    getResources().getDrawable(PARTICLE_INFOS[mIndex].particleRes);
            mParticleSystem.setColorFilterMode(PARTICLE_INFOS[mIndex].colorFilter);
            mParticleSystem.setBlendMode(PorterDuff.Mode.ADD);
            mParticleSystem.setParticleFactory(new ParticleSystem.ParticleFactory() {
                @Override
                public Particle create(ParticleSystem particleSystem) {
                    Drawable drawable = drawableCommon.getConstantState().newDrawable();
                    DrawableParticle particle = new DrawableParticle(particleSystem, drawable);
                    return particle;
                }
            });
        }
        particleView.addParticleSystem(mParticleSystem);
        particleView.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mParticleSystem.setPosition(event.getX(), event.getY());
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.free_mode:
                mParticleSystem.setPositionType(ParticleSystem.PositionType.POSITION_FREE);
                break;

            case R.id.group_mode:
                mParticleSystem.setPositionType(ParticleSystem.PositionType.POSITION_GROUP);
                break;

            case R.id.prev:
                mParticleSystem.stopSystem();
                mIndex = (0 == mIndex) ? PARTICLE_INFOS.length - 1 : mIndex - 1;
                setupParticleSystem();
                break;

            case R.id.next:
                mParticleSystem.stopSystem();
                mIndex = (PARTICLE_INFOS.length - 1 == mIndex) ? 0 : mIndex + 1;
                setupParticleSystem();
                break;
        }
    }
}
