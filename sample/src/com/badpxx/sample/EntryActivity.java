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
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import com.badpx.particleandroid.DrawableParticle;
import com.badpx.particleandroid.Particle;
import com.badpx.particleandroid.ParticleSystem;
import com.badpx.particleandroid.preset.ParticleFire;
import com.badpx.particleandroid.widget.ParticleSystemView;


public class EntryActivity extends Activity implements View.OnTouchListener {

    private ParticleSystem mParticleSystem;
    private ParticleSystemView mParticleView;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mParticleView = (ParticleSystemView) findViewById(R.id.ps);

        int posX = getResources().getDisplayMetrics().widthPixels / 2;
        int posY = getResources().getDisplayMetrics().heightPixels / 2;

        final Drawable drawable = getResources().getDrawable(R.drawable.halo);
        mParticleSystem = new ParticleFire();
        mParticleSystem.setPosition(posX, posY);
        mParticleSystem.setInterval(1000 / 60);
        mParticleSystem.setParticleFactory(new ParticleSystem.ParticleFactory() {
            @Override
            public Particle create(ParticleSystem particleSystem) {
                Drawable particleBase = drawable.getConstantState().newDrawable();
                particleBase.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                return new DrawableParticle(particleBase);
            }
        });
        mParticleView.addParticleSystem(mParticleSystem);
        mParticleView.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mParticleSystem.setPosition(event.getX(), event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mParticleSystem.startSystem();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mParticleSystem.stopSystem();
                break;
        }
        return true;
    }
}
