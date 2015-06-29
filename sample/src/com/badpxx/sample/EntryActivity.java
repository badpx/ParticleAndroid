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
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.badpx.particleandroid.DrawableParticle;
import com.badpx.particleandroid.PListParticleSystemHelper;
import com.badpx.particleandroid.Particle;
import com.badpx.particleandroid.ParticleSystem;
import com.badpxx.sample.preset.ParticleExplosion;
import com.badpxx.sample.preset.ParticleFire;
import com.badpxx.sample.preset.ParticleSnow;
import com.badpxx.sample.preset.ParticleSpin;
import com.badpx.particleandroid.widget.ParticleSystemView;


public class EntryActivity extends Activity implements View.OnTouchListener,
        View.OnClickListener {

    interface ParticleSystemCreator {
        public ParticleSystem create(Resources resources);
    }

    static class PresetCreator implements ParticleSystemCreator {
        Class clazz;
        int particleRes;
        ParticleSystem particleSystem;
        PorterDuff.Mode blendMode;

        public PresetCreator(Class sys, int res, PorterDuff.Mode mode) {
            clazz = sys;
            particleRes = res;
            blendMode = mode;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName();
        }

        public ParticleSystem create(Resources resources) {
            if (null == particleSystem) {
                try {
                    particleSystem =
                            (ParticleSystem) clazz.newInstance();
                    final Drawable drawableCommon = resources.getDrawable(particleRes);
                    particleSystem.setBlendMode(blendMode);
                    particleSystem.setParticleFactory(new ParticleSystem.ParticleFactory() {
                        @Override
                        public Particle create(ParticleSystem particleSystem) {
                            return new DrawableParticle(particleSystem, drawableCommon);
                        }
                    });
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return particleSystem;
        }
    }

    static class PListCreator implements ParticleSystemCreator {
        final String plistPath;
        ParticleSystem particleSystem;

        public PListCreator(String plistPath) {
            this.plistPath = plistPath;
        }

        @Override
        public String toString() {
            return plistPath;
        }

        @Override
        public ParticleSystem create(Resources resources) {
            if (null == particleSystem) {
                particleSystem = PListParticleSystemHelper.create(resources, plistPath);
            }
            return particleSystem;
        }
    }

    private static final ParticleSystemCreator[] PARTICLE_INFOS = {
            new PresetCreator(ParticleFire.class, R.drawable.fire, PorterDuff.Mode.ADD),
            new PresetCreator(ParticleExplosion.class, R.drawable.fire, null),
            new PresetCreator(ParticleSnow.class, R.drawable.snow, null),
            new PresetCreator(ParticleSpin.class, R.drawable.stars, null),
            new PListCreator("BurstPipe.plist"),
            new PListCreator("Spiral.plist"),
            new PListCreator("Phoenix.plist"),
            new PListCreator("lava_flow.plist"),
            new PListCreator("Galaxy.plist"),
            new PListCreator("debian.plist"),
            new PListCreator("Flower.plist"),
            new PListCreator("BoilingFoam.plist"),
            new PListCreator("Sun.plist"),
            new PListCreator("Upsidedown.plist"),
            new PListCreator("volcano.plist"),
//            new PListCreator("Comet.plist"),
//            new PListCreator("ExplodingRing.plist"),
//            new PListCreator("lines.plist"),
//            new PListCreator("SmallSun.plist"),
//            new PListCreator("SpinningPeas.plist"),
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
        btn = (Button) findViewById(R.id.reset);
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
            mParticleSystem.stopEmitting();
        }

        mParticleSystem = PARTICLE_INFOS[mIndex].create(getResources());
        if (null != mParticleSystem) {
            mParticleSystem.reset();
            mTitle.setText(PARTICLE_INFOS[mIndex].toString());

            mParticleSystem.setPosition(posX, posY);
            mParticleSystem.setInterval(1000 / 60);
            particleView.addParticleSystem(mParticleSystem);
            particleView.setOnTouchListener(this);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mParticleSystem.setPosition(event.getX(), event.getY());
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset:
                mParticleSystem.reset();
                break;
            case R.id.free_mode:
                mParticleSystem.setPositionType(ParticleSystem.PositionType.POSITION_FREE);
                break;

            case R.id.group_mode:
                mParticleSystem.setPositionType(ParticleSystem.PositionType.POSITION_GROUP);
                break;

            case R.id.prev:
                mParticleSystem.stopEmitting();
                mIndex = (0 == mIndex) ? PARTICLE_INFOS.length - 1 : mIndex - 1;
                setupParticleSystem();
                break;

            case R.id.next:
                mParticleSystem.stopEmitting();
                mIndex = (PARTICLE_INFOS.length - 1 == mIndex) ? 0 : mIndex + 1;
                setupParticleSystem();
                break;
        }
    }
}
