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

/**
 * Created with IntelliJ IDEA.
 * User: kanedong
 * Date: 14/11/10
 */
public class ParticleSpin extends ParticleSystem {
    public ParticleSpin(int numOfParticles) {
        super(numOfParticles);
    }

    @Override
    protected void setup(int numberOfParticles) {
        super.setup(numberOfParticles);
        // duration
        setDuration(DURATION_INFINITY);

        // radius mode
        setEmitterMode(EmitMode.MODE_RADIUS);

        // radius mode: start and end radius in pixels
        setStartRadius(0);
        setStartRadiusVar(0);
        setEndRadius(160);
        setEndRadiusVar(0);

        // radius mode: degrees per second
        setRotatePerSecond(180);
        setRotatePerSecondVar(0);

        // angle
        setAngle(90);
        setAngleVar(0);

        // life of particles
        setLife(5);
        setLifeVar(0);

        // spin of particles
        setStartSpin(0);
        setStartSpinVar(0);
        setEndSpin(0);
        setEndSpinVar(0);

        // color of particles
        setStartColor(Colour.argb(1.0f, 0.5f, 0.5f, 0.5f));
        setStartColorVar(Colour.argb(1.0f, 0.5f, 0.5f, 0.5f));
        setEndColor(Colour.argb(0.2f, 0.1f, 0.1f, 0.1f));
        setEndColorVar(Colour.argb(0.2f, 0.1f, 0.1f, 0.1f));

        // size factor
        setStartSize(1.5f);
        setStartSizeVar(0.5f);
        setEndSize(END_SIZE_EQUAL_TO_START_SIZE);

        // emits per second
        setEmissionRate(getTotalParticles() / getLife());
    }
}
