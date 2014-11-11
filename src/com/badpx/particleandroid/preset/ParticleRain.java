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
public class ParticleRain extends ParticleSystem {
    private int mHorizontal;

    public ParticleRain(int horizontalRang) {
        this(horizontalRang, 150);
    }

    public ParticleRain(int horizontalRang, int numOfParticles) {
        mHorizontal = horizontalRang;
        setup(numOfParticles);
    }

    @Override
    protected void setup(int numberOfParticles) {
        super.setup(numberOfParticles);
        // duration
        mDuration = DURATION_INFINITY;

        setEmitterMode(EmitMode.MODE_GRAVITY);

        // Gravity Mode: gravity
        setGravity(new Point(10,-10));

        // Gravity Mode: radial
        setRadialAccel(0);
        setRadialAccelVar(1);

        // Gravity Mode: tangential
        setTangentialAccel(0);
        setTangentialAccelVar(1);

        // Gravity Mode: speed of particles
        setSpeed(130);
        setSpeedVar(30);

        // angle
        mAngle = -90;
        mAngleVar = 5;

        setPosVar(new Point(mHorizontal, 0));

        // life of particles
        mLife = 4.5f;
        mLifeVar = 0;

        // size, in pixels
        mStartSize = 1.0f;
        mStartSizeVar = 0.5f;
        mEndSize = END_SIZE_EQUAL_TO_START_SIZE;

        // emits per second
//        mEmissionRate = 20;
        mEmissionRate = mTotalParticles / mLife;

        // color of particles
        mStartColor = Colour.argb(1.0f, 0.7f, 0.8f, 1.0f);
        mStartColorVar = 0;
        mEndColor = Colour.argb(0.5f, 0.7f, 0.8f, 1.0f);
        mEndColor = 0;

    }
}
