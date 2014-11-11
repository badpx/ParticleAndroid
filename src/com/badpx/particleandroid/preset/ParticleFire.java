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
 * Date: 14/11/6
 */
public class ParticleFire extends ParticleSystem {

    public ParticleFire() {
        super(77);
    }

    public ParticleFire(int num) {
        super(num);
    }

    @Override
    protected void setup(int numberOfParticles) {
        super.setup(numberOfParticles);

        // duration
        mDuration = -1;

        // Gravity Mode
        this.mEmitterMode = EmitterMode.MODE_GRAVITY;
        this.mPositionType = PositionType.POSITION_FREE;

        // Gravity Mode: gravity
        this.modeA.gravity = new Point();

        // Gravity Mode: radial acceleration
        this.modeA.radialAccel = 0;
        this.modeA.radialAccelVar = 0;

        // Gravity Mode: speed of particles
        this.modeA.speed = 225;
        this.modeA.speedVar = 30;

        // starting angle
        mAngle = 90;
        mAngleVar = 10;

        // emitter position variance
        this.mPosVar = new Point(7, 7);
//        this.mSourcePosition = new Point(7, 7);

        // life of particles
        mLife = 1;
        mLifeVar = 0;

        // size, in pixels
        mStartSize = 64.0f;
        mStartSizeVar = 5.0f;
        mEndSize = 0;

        // emits per frame
        mEmissionRate = mTotalParticles / mLife;

        // color of particles
        mStartColor = Colour.argb(1.0f, 0.76f, 0.25f, 0.12f);
    }
}
