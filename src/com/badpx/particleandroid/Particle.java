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

import android.graphics.Canvas;
import com.badpx.particleandroid.utils.Colour;
import com.badpx.particleandroid.utils.Point;

/**
 * Created with IntelliJ IDEA.
 * User: kanedong
 * Date: 14/11/6
 */
public abstract class Particle {

    protected Point     pos;
    protected Point     startPos;

    protected int    color;
    protected Colour deltaColour;

    protected float        size;
    protected float        deltaSize;

    protected float        rotation;
    protected float        deltaRotation;

    protected float        timeToLive;

    ModeA modeA;
    ModeB modeB;

    //! Mode A: gravity, direction, radial accel, tangential accel
    class ModeA {
        Point dir = new Point();
        float        radialAccel;
        float        tangentialAccel;

        void copy(ModeA ohs) {
            dir.set(ohs.dir);
            radialAccel = ohs.radialAccel;
            tangentialAccel = ohs.tangentialAccel;
        }
    }

    //! Mode B: radius mode
    class ModeB {
        float        angle;
        float        degreesPerSecond;
        float        radius;
        float        deltaRadius;

        void copy(ModeB ohs) {
            angle = ohs.angle;
            degreesPerSecond = ohs.degreesPerSecond;
            radius = ohs.radius;
            deltaRadius = ohs.deltaRadius;
        }
    }

    public Particle() {
        pos = new Point();
        modeA = new ModeA();
        modeB = new ModeB();
    }

    void copy(Particle ohs) {
        pos.set(ohs.pos);
        startPos.set(ohs.startPos);

        color = ohs.color;
        deltaColour.copy(ohs.deltaColour);

        size = ohs.size;
        deltaSize = ohs.deltaSize;

        rotation = ohs.rotation;
        deltaRotation = ohs.deltaRotation;

        timeToLive = ohs.timeToLive;

        modeA.copy(ohs.modeA);
        modeB.copy(ohs.modeB);
    }

    public abstract void draw(Canvas canvas);
}
