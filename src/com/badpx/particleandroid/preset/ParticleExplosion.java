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

package com.badpx.particleandroid.preset;

import com.badpx.particleandroid.ParticleSystem;
import com.badpx.particleandroid.utils.Colour;
import com.badpx.particleandroid.utils.Point;

/**
 * Created with IntelliJ IDEA.
 * User: kanedong
 * Date: 14/11/10
 */
public class ParticleExplosion extends ParticleSystem {
    public ParticleExplosion() {
        super();
    }

    public ParticleExplosion(int numOfParticles) {
        super(numOfParticles);
    }

    @Override
    protected void setup(int numberOfParticles) {
        super.setup(numberOfParticles);

        // duration
        mDuration = -1f;

        mEmitterMode = EmitterMode.MODE_GRAVITY;

        // Gravity Mode: gravity
        modeA.gravity = new Point(0, 0);

        // Gravity Mode: speed of particles
        modeA.speed = 70;
        modeA.speedVar = 40;

        // Gravity Mode: radial
        modeA.radialAccel = 0;
        modeA.radialAccelVar = 0;

        // Gravity Mode: tangential
        modeA.tangentialAccel = 0;
        modeA.tangentialAccelVar = 0;

        // angle
        mAngle = 90;
        mAngleVar = 360;

        mPosVar = new Point();

        // life of particles
        mLife = 2.0f;
        mLifeVar = 1;

        // size, in pixels
        mStartSize = 1.0f;
        mStartSizeVar = 0.25f;
        mEndSize = END_SIZE_EQUAL_TO_START_SIZE;

        // emits per second
        mEmissionRate = mTotalParticles / mLife;

        // color of particles
        mStartColor = Colour.argb(1.0f, 0.7f, 0.1f, 0.2f);
        mStartColorVar = Colour.argb(0f, 0.5f, 0.5f, 0.5f);
        mEndColorVar = Colour.argb(0f, 0.5f, 0.5f, 0.5f);
    }
}
