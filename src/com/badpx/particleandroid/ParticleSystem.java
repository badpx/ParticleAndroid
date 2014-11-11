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
import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import com.badpx.particleandroid.utils.Colour;
import com.badpx.particleandroid.utils.Misc;
import com.badpx.particleandroid.utils.Point;

import java.lang.ref.WeakReference;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: kanedong
 * Date: 14/11/6
 */
public class ParticleSystem implements Runnable {
    public enum PositionType {
        POSITION_FREE,
        POSITION_GROUP,
    }

    public enum EmitterMode {
        MODE_GRAVITY,
        MODE_RADIUS
    }

    public static final float DURATION_INFINITY = -1;
    public static final float END_SIZE_EQUAL_TO_START_SIZE = -1;

    protected Random mRandom;
    protected float mElapsed;
    protected float mLife;
    protected float mLifeVar;
    protected Point mSourcePosition = new Point();
    protected Point mPosVar = new Point();
    protected int mStartColor;
    protected int mEndColor;
    protected int mStartColorVar;
    protected int mEndColorVar;
    protected float mStartSize;
    protected float mStartSizeVar;
    protected float mEndSize = -1f;
    protected float mEndSizeVar;
    protected float mStartSpin;
    protected float mStartSpinVar;
    protected float mEndSpin;
    protected float mEndSpinVar;
    protected Point mPosition;
    protected float mAngle;
    protected float mAngleVar;
    protected float mDuration;
    protected float mEmissionRate;
    protected boolean mIsVisible = true;
    protected long mLastTimestamp;
    protected ModeA modeA;
    protected ModeB modeB;

    //! Array of particles
    protected Particle[] mParticles;
    //! How many particles can be emitted per second
    float mEmitCounter;
    //!  particle idx
    int mParticleIdx;
    protected int mTotalParticles;
    //true if scaled or rotated
    // Number of allocated particles
    int mAllocatedParticles;
    protected int mParticleCount;
    protected PositionType mPositionType;
    protected EmitterMode mEmitterMode;
    protected int mInterval = 20;
    protected Handler mHandler;
    /** Is the emitter active */
    boolean mIsActive = true;
    protected WeakReference<UpdateCallback> mCallbackRef;

    protected ParticleFactory mParticleFactory = new ParticleFactory() {
        @Override
        public Particle create(ParticleSystem particleSystem) {
            return new PointParticle();
        }
    };

    public static interface ParticleFactory {
        Particle create(ParticleSystem particleSystem);
    }

    public static interface UpdateCallback {
        void needInvalidate();
    }

    // Different modes
    //! Mode A:Gravity + Tangential Accel + Radial Accel
    public class ModeA {
        /** Gravity value. Only available in 'Gravity' mode. */
        public Point gravity;
        /** speed of each particle. Only available in 'Gravity' mode.  */
        public float speed;
        /** speed variance of each particle. Only available in 'Gravity' mode. */
        public float speedVar;
        /** tangential acceleration of each particle. Only available in 'Gravity' mode. */
        public float tangentialAccel;
        /** tangential acceleration variance of each particle. Only available in 'Gravity' mode. */
        public float tangentialAccelVar;
        /** radial acceleration of each particle. Only available in 'Gravity' mode. */
        public float radialAccel;
        /** radial acceleration variance of each particle. Only available in 'Gravity' mode. */
        public float radialAccelVar;
    }

    //! Mode B: circular movement (gravity, radial accel and tangential accel don't are not used in this mode)
    class ModeB {
        /** The starting radius of the particles. Only available in 'Radius' mode. */
        public float startRadius;
        /** The starting radius variance of the particles. Only available in 'Radius' mode. */
        public float startRadiusVar;
        /** The ending radius of the particles. Only available in 'Radius' mode. */
        public float endRadius;
        /** The ending radius variance of the particles. Only available in 'Radius' mode. */
        public float endRadiusVar;
        /** Number of degrees to rotate a particle around the source pos per second. Only available in 'Radius' mode. */
        public float rotatePerSecond;
        /** Variance in degrees for rotatePerSecond. Only available in 'Radius' mode. */
        public float rotatePerSecondVar;
    }

    @Override
    public void run() {
        float delta = 0f;
        if (0 == mLastTimestamp) {
            mLastTimestamp = SystemClock.uptimeMillis();
        } else {
            long timestamp = SystemClock.uptimeMillis();
            long diff = timestamp - mLastTimestamp;
            mLastTimestamp = timestamp;
            delta = (float)diff / 1000f;  // Time delta in second
        }

        update(delta);
        mHandler.postDelayed(this, mInterval);
    }

    public ParticleSystem() {
        setup(150);
    }

    public ParticleSystem(int numberOfParticles) {
        setup(numberOfParticles);
    }

    protected void setup(int numberOfParticles) {
        if (numberOfParticles < 0) {
            throw new IllegalArgumentException("Number of particle can't be 0!");
        }

        mParticles = new Particle[numberOfParticles];
        mPosition = new Point();
        mRandom = new Random(SystemClock.uptimeMillis());
        modeA = new ModeA();
        modeB = new ModeB();
        mTotalParticles = numberOfParticles;
        mAllocatedParticles = numberOfParticles;
        mPositionType = PositionType.POSITION_FREE;
        mEmitterMode = EmitterMode.MODE_GRAVITY;

        mHandler = new Handler();
        mHandler.post(this);
    }

    public void tearDown() {
        stopSystem();
        mHandler.removeCallbacks(this);
    }

    public void setParticleFactory(ParticleFactory factory) {
        if (null != factory) {
            mParticleFactory = factory;
        }
    }

    public void setUpdateCallback(UpdateCallback callback) {
        mCallbackRef = new WeakReference<UpdateCallback>(callback);
    }

    public void setInterval(int interval) {
        mInterval = interval;
    }

    public void setPosition(float x, float y) {
        mPosition.set(x, y);
    }

    // ParticleSystem - Properties of Gravity Mode
    public void setTangentialAccel(float t) {
        MyAssert(mEmitterMode == EmitterMode.MODE_GRAVITY, "Particle Mode should be Gravity");
        modeA.tangentialAccel = t;
    }

    public float getTangentialAccel() {
        MyAssert(mEmitterMode == EmitterMode.MODE_GRAVITY, "Particle Mode should be Gravity");
        return modeA.tangentialAccel;
    }

    public void setTangentialAccelVar(float t) {
        MyAssert(mEmitterMode == EmitterMode.MODE_GRAVITY, "Particle Mode should be Gravity");
        modeA.tangentialAccelVar = t;
    }

    public float getTangentialAccelVar() {
        MyAssert(mEmitterMode == EmitterMode.MODE_GRAVITY, "Particle Mode should be Gravity");
        return modeA.tangentialAccelVar;
    }

    public void setRadialAccel(float t) {
        MyAssert(mEmitterMode == EmitterMode.MODE_GRAVITY, "Particle Mode should be Gravity");
        modeA.radialAccel = t;
    }

    public float getRadialAccel() {
        MyAssert(mEmitterMode == EmitterMode.MODE_GRAVITY, "Particle Mode should be Gravity");
        return modeA.radialAccel;
    }

    public void setRadialAccelVar(float t) {
        MyAssert(mEmitterMode == EmitterMode.MODE_GRAVITY, "Particle Mode should be Gravity");
        modeA.radialAccelVar = t;
    }

    public float getRadialAccelVar() {
        MyAssert(mEmitterMode == EmitterMode.MODE_GRAVITY, "Particle Mode should be Gravity");
        return modeA.radialAccelVar;
    }

    public void setGravity(Point g) {
        MyAssert(mEmitterMode == EmitterMode.MODE_GRAVITY, "Particle Mode should be Gravity");
        modeA.gravity = g;
    }

    public Point getGravity() {
        MyAssert(mEmitterMode == EmitterMode.MODE_GRAVITY, "Particle Mode should be Gravity");
        return modeA.gravity;
    }

    public void setSpeed(float speed)
    {
        MyAssert(mEmitterMode == EmitterMode.MODE_GRAVITY, "Particle Mode should be Gravity");
        modeA.speed = speed;
    }

    public float getSpeed() {
        MyAssert(mEmitterMode == EmitterMode.MODE_GRAVITY, "Particle Mode should be Gravity");
        return modeA.speed;
    }

    public void setSpeedVar(float speedVar) {
        MyAssert(mEmitterMode == EmitterMode.MODE_GRAVITY, "Particle Mode should be Gravity");
        modeA.speedVar = speedVar;
    }

    public float getSpeedVar() {
        MyAssert(mEmitterMode == EmitterMode.MODE_GRAVITY, "Particle Mode should be Gravity");
        return modeA.speedVar;
    }

    // ParticleSystem - Properties of Radius Mode
    public void setStartRadius(float startRadius) {
        MyAssert(mEmitterMode == EmitterMode.MODE_RADIUS, "Particle Mode should be Radius");
        modeB.startRadius = startRadius;
    }

    public float getStartRadius() {
        MyAssert(mEmitterMode == EmitterMode.MODE_RADIUS, "Particle Mode should be Radius");
        return modeB.startRadius;
    }

    public void setStartRadiusVar(float startRadiusVar) {
        MyAssert(mEmitterMode == EmitterMode.MODE_RADIUS, "Particle Mode should be Radius");
        modeB.startRadiusVar = startRadiusVar;
    }

    public float getStartRadiusVar() {
        MyAssert(mEmitterMode == EmitterMode.MODE_RADIUS, "Particle Mode should be Radius");
        return modeB.startRadiusVar;
    }

    public void setEndRadius(float endRadius) {
        MyAssert(mEmitterMode == EmitterMode.MODE_RADIUS, "Particle Mode should be Radius");
        modeB.endRadius = endRadius;
    }

    public float getEndRadius() {
        MyAssert(mEmitterMode == EmitterMode.MODE_RADIUS, "Particle Mode should be Radius");
        return modeB.endRadius;
    }

    public void setEndRadiusVar(float endRadiusVar) {
        MyAssert(mEmitterMode == EmitterMode.MODE_RADIUS, "Particle Mode should be Radius");
        modeB.endRadiusVar = endRadiusVar;
    }

    public float getEndRadiusVar() {
        MyAssert(mEmitterMode == EmitterMode.MODE_RADIUS, "Particle Mode should be Radius");
        return modeB.endRadiusVar;
    }

    public void setRotatePerSecond(float degrees) {
        MyAssert(mEmitterMode == EmitterMode.MODE_RADIUS, "Particle Mode should be Radius");
        modeB.rotatePerSecond = degrees;
    }

    public float getRotatePerSecond() {
        MyAssert(mEmitterMode == EmitterMode.MODE_RADIUS, "Particle Mode should be Radius");
        return modeB.rotatePerSecond;
    }

    public void setRotatePerSecondVar(float degrees) {
        MyAssert(mEmitterMode == EmitterMode.MODE_RADIUS, "Particle Mode should be Radius");
        modeB.rotatePerSecondVar = degrees;
    }

    public float getRotatePerSecondVar() {
        MyAssert(mEmitterMode == EmitterMode.MODE_RADIUS, "Particle Mode should be Radius");
        return modeB.rotatePerSecondVar;
    }

    public boolean isActive()
    {
        return mIsActive;
    }

    public int getParticleCount() {
        return mParticleCount;
    }

    public float getDuration() {
        return mDuration;
    }

    public void setDuration(float var) {
        mDuration = var;
    }

    public Point getSourcePosition() {
        return mSourcePosition;
    }

    public void setSourcePosition(Point var) {
        mSourcePosition.set(var.x, var.y);
    }

    public Point getPosVar() {
        return mPosVar;
    }

    public void setPosVar(Point var)
    {
        mPosVar.set(var.x, var.y);
    }

    public float getLife() {
        return mLife;
    }

    public void setLife(float var) {
        mLife = var;
    }

    public float getLifeVar() {
        return mLifeVar;
    }

    public void setLifeVar(float var) {
        mLifeVar = var;
    }

    public float getAngle() {
        return mAngle;
    }

    public void setAngle(float var) {
        mAngle = var;
    }

    public float getAngleVar() {
        return mAngleVar;
    }

    public void setAngleVar(float var) {
        mAngleVar = var;
    }

    public float getStartSize() {
        return mStartSize;
    }

    public void setStartSize(float var) {
        mStartSize = var;
    }

    public float getStartSizeVar() {
        return mStartSizeVar;
    }

    public void setStartSizeVar(float var) {
        mStartSizeVar = var;
    }

    public float getEndSize() {
        return mEndSize;
    }

    public void setEndSize(float var) {
        mEndSize = var;
    }

    public float getEndSizeVar() {
        return mEndSizeVar;
    }

    public void setEndSizeVar(float var) {
        mEndSizeVar = var;
    }

    public int getStartColor() {
        return mStartColor;
    }

    public void setStartColor(int var) {
        mStartColor = var;
    }

    public int getStartColorVar() {
        return mStartColorVar;
    }

    public void setStartColorVar(int var) {
        mStartColorVar = var;
    }

    public int getEndColor() {
        return mEndColor;
    }

    public void setEndColor(int var) {
        mEndColor = var;
    }

    public int getEndColorVar() {
        return mEndColorVar;
    }

    public void setEndColorVar(int var) {
        mEndColorVar = var;
    }

    public float getStartSpin() {
        return mStartSpin;
    }

    public void setStartSpin(float var) {
        mStartSpin = var;
    }

    public float getStartSpinVar() {
        return mStartSpinVar;
    }

    public void setStartSpinVar(float var) {
        mStartSpinVar = var;
    }

    public float getEndSpin() {
        return mEndSpin;
    }

    public void setEndSpin(float var) {
        mEndSpin = var;
    }
    public float getEndSpinVar() {
        return mEndSpinVar;
    }

    public void setEndSpinVar(float var) {
        mEndSpinVar = var;
    }

    public float getEmissionRate() {
        return mEmissionRate;
    }

    public void setEmissionRate(float var) {
        mEmissionRate = var;
    }

    public int getTotalParticles() {
        return mTotalParticles;
    }

    public void setTotalParticles( int var) {
        MyAssert(var <= mAllocatedParticles, "Particle: resizing particle array only supported for quads");
        mTotalParticles = var;
    }

    public PositionType getPositionType() {
        return mPositionType;
    }

    public void setPositionType(PositionType var) {
        mPositionType = var;
    }

    public EmitterMode getEmitterMode() {
        return mEmitterMode;
    }

    public void setEmitterMode(EmitterMode var) {
        mEmitterMode = var;
    }

    //! Add a particle to the emitter
    protected boolean addParticle() {
        if (isFull()) {
            return false;
        }

        Particle particle = mParticles[mParticleCount];
        if (null == particle) {
            particle = mParticles[mParticleCount] = mParticleFactory.create(this);
        }
        initParticle(particle);
        ++mParticleCount;

        return true;
    }

    //! Initializes a particle
    protected void initParticle(Particle particle) {
        // timeToLive
        // no negative life. prevent division by 0
        particle.timeToLive = mLife + mLifeVar * randomMinus1To1();
        particle.timeToLive = Math.max(0, particle.timeToLive);

        // position
        particle.pos.x = mSourcePosition.x + mPosVar.x * randomMinus1To1();

        particle.pos.y = mSourcePosition.y + mPosVar.y * randomMinus1To1();


        // Color
        int start;
        int r = Misc.clamp(Color.red(mStartColor) + (int)(Color.red(mStartColorVar) * randomMinus1To1()), 0, 255);
        int g = Misc.clamp(Color.green(mStartColor) + (int) (Color.green(mStartColorVar) * randomMinus1To1()), 0, 255);
        int b = Misc.clamp(Color.blue(mStartColor) + (int) (Color.blue(mStartColorVar) * randomMinus1To1()), 0, 255);
        int a = Misc.clamp(Color.alpha(mStartColor) + (int) (Color.alpha(mStartColorVar) * randomMinus1To1()), 0, 255);
        start = Color.argb(a, r, g, b);

        int end;
        r = Misc.clamp(Color.red(mEndColor) + (int)(Color.red(mEndColorVar) * randomMinus1To1()), 0, 255);
        g = Misc.clamp(Color.green(mEndColor) + (int)(Color.green(mEndColorVar) * randomMinus1To1()), 0, 255);
        b = Misc.clamp(Color.blue(mEndColor) + (int)(Color.blue(mEndColorVar) * randomMinus1To1()), 0, 255);
        a = Misc.clamp(Color.alpha(mEndColor) + (int)(Color.alpha(mEndColorVar) * randomMinus1To1()), 0, 255);
        end = Color.argb(a, r, g, b);

        particle.color = start;
        r = (int)((Color.red(end) - Color.red(start)) / particle.timeToLive);
        g = (int)((Color.green(end) - Color.green(start)) / particle.timeToLive);
        b = (int)((Color.blue(end) - Color.blue(start)) / particle.timeToLive);
        a = (int)((Color.alpha(end) - Color.alpha(start)) / particle.timeToLive);
        particle.deltaColour = new Colour(a, r, g, b);

        // size
        float startS = mStartSize + mStartSizeVar * randomMinus1To1();
        startS = Math.max(0, startS); // No negative value

        particle.size = startS;

        if( mEndSize < 0 ) {
            particle.deltaSize = 0;
        } else {
            float endS = mEndSize + mEndSizeVar * randomMinus1To1();
            endS = Math.max(0, endS); // No negative values
            particle.deltaSize = (endS - startS) / particle.timeToLive;
        }

        // rotation
        float startA = mStartSpin + mStartSpinVar * randomMinus1To1();
        float endA = mEndSpin + mEndSpinVar * randomMinus1To1();
        particle.rotation = startA;
        particle.deltaRotation = (endA - startA) / particle.timeToLive;

        if( mPositionType == PositionType.POSITION_GROUP) {
            particle.startPos = new Point();
        } else {
            particle.startPos = new Point(mPosition);
        }

        // direction
        double radians = Math.toRadians(mAngle + mAngleVar * randomMinus1To1());

        // Mode Gravity: A
        if (mEmitterMode == EmitterMode.MODE_GRAVITY) {
            Point v = new Point((float)Math.cos(radians), (float)Math.sin( radians ));
            float s = modeA.speed + modeA.speedVar * randomMinus1To1();

            // direction
            particle.modeA.dir = Point.mult( v, s );

            // radial accel
            particle.modeA.radialAccel = modeA.radialAccel + modeA.radialAccelVar * randomMinus1To1();

            // tangential accel
            particle.modeA.tangentialAccel = modeA.tangentialAccel + modeA.tangentialAccelVar * randomMinus1To1();

        } else { // Mode Radius: B
            // Set the default diameter of the particle from the source position
            float startRadius = modeB.startRadius + modeB.startRadiusVar * randomMinus1To1();
            float endRadius = modeB.endRadius + modeB.endRadiusVar * randomMinus1To1();

            particle.modeB.radius = startRadius;

            if(modeB.endRadius < 0) {
                particle.modeB.deltaRadius = 0;
            } else {
                particle.modeB.deltaRadius = (endRadius - startRadius) / particle.timeToLive;
            }

            particle.modeB.angle = a;
            particle.modeB.degreesPerSecond =
                    (float) Math.toRadians(modeB.rotatePerSecond + modeB.rotatePerSecondVar * randomMinus1To1());
        }

    }

    public void startSystem() {
        mIsActive = true;
        mElapsed = 0;
    }

    //! stop emitting particles. Running particles will continue to run until they die
    public void stopSystem() {
        mIsActive = false;
        mElapsed = mDuration;
        mEmitCounter = 0;
    }

    //! Kill all living particles.
    public void resetSystem() {
        mIsActive = true;
        mElapsed = 0;
        for (mParticleIdx = 0; mParticleIdx < mParticleCount; ++mParticleIdx)
        {
            Particle p = mParticles[mParticleIdx];
            p.timeToLive = 0;
        }
    }

    //! whether or not the system is full
    protected boolean isFull() {
        return (mParticleCount == mTotalParticles);
    }


    //! should be overridden by subclasses
    protected void postStep() {
    }

    public void updateWithNoTime(Canvas canvas) {
        update(0f);
    }

    public void update(float dt) {
        if (mIsActive && mEmissionRate > 0) {
            float rate = 1.0f / mEmissionRate;
            //issue #1201, prevent bursts of particles, due to too high emitCounter
            if (mParticleCount < mTotalParticles)
            {
                mEmitCounter += dt;
            }

            while (mParticleCount < mTotalParticles && mEmitCounter > rate)
            {
                this.addParticle();
                mEmitCounter -= rate;
            }

            mElapsed += dt;
            if (mDuration != -1 && mDuration < mElapsed)
            {
                this.stopSystem();
            }
        }

        mParticleIdx = 0;

        if (mIsVisible) {
            boolean needInvalidate = mParticleIdx < mParticleCount;
            while (mParticleIdx < mParticleCount) {
                Particle p = mParticles[mParticleIdx];

                // life
                p.timeToLive -= dt;

                if (p.timeToLive > 0)
                {
                    // Mode A: gravity, direction, tangential accel & radial accel
                    if (mEmitterMode == EmitterMode.MODE_GRAVITY)
                    {
                        Point tmp, radial, tangential;

                        // radial acceleration
                        if (p.pos.x != 0 || p.pos.y != 0) {
                            radial = Point.Normalize(p.pos);
                        } else {
                            radial = new Point();
                        }
                        tangential = radial;
                        radial = Point.mult(radial, p.modeA.radialAccel);

                        // tangential acceleration
                        float newY = tangential.x;
                        tangential.x = -tangential.y;
                        tangential.y = newY;
                        tangential.multBy(p.modeA.tangentialAccel);

                        // (gravity + radial + tangential) * dt
//                        tmp = Point.add(Point.add(radial, tangential), modeA.gravity);
                        tmp = tangential;
                        tmp.addBy(radial);
                        tmp.addBy(modeA.gravity);
                        tmp.multBy(dt);
                        p.modeA.dir.addBy(tmp);
                        p.pos.addBy(Point.mult(p.modeA.dir, dt));
                    } else { // Mode B: radius movement
                        // Update the angle and radius of the particle.
                        p.modeB.angle += p.modeB.degreesPerSecond * dt;
                        p.modeB.radius += p.modeB.deltaRadius * dt;

                        p.pos.x = (float) (- Math.cos(p.modeB.angle) * p.modeB.radius);
                        p.pos.y = (float) (- Math.sin(p.modeB.angle) * p.modeB.radius);
                    }

                    // color
                    int r = Color.red(p.color) + (int) (p.deltaColour.r * dt);
                    int g = Color.green(p.color) + (int) (p.deltaColour.g * dt);
                    int b = Color.blue(p.color) + (int) (p.deltaColour.b * dt);
                    int a = Color.alpha(p.color) + (int) (p.deltaColour.a * dt);
                    p.color = Color.argb(a, r, g, b);

                    // size
                    p.size += (p.deltaSize * dt);
                    p.size = Math.max( 0, p.size );

                    // angle
                    p.rotation += (p.deltaRotation * dt);

                    // update particle counter
                    ++mParticleIdx;
                } else { // life < 0
                    if( mParticleIdx != mParticleCount - 1 )
                    {
                        mParticles[mParticleIdx].copy(mParticles[mParticleCount - 1]);
                    }

                    --mParticleCount;
                }
            } //while

            if (needInvalidate && null != mCallbackRef) {
                UpdateCallback callback = mCallbackRef.get();
                if (null != callback) {
                    callback.needInvalidate();
                }
            }
        }
        postStep();
    }

    public void draw(Canvas canvas) {
        for (int i = 0; i < mParticleCount; ++i) {
            Particle p = mParticles[i];
            if (null != p) {
                Point newPos;
                if (mPositionType == PositionType.POSITION_GROUP) {
                    newPos = Point.sub(mPosition, p.pos);
                } else {
                    newPos = Point.sub(p.startPos, p.pos);
                }

                canvas.save();
                canvas.translate(newPos.x, newPos.y);
                p.draw(canvas);
                canvas.restore();
            }
        }
    }

    void MyAssert(boolean condition, String assertFailedDesc) {
        if (!condition) {
            throw new RuntimeException(assertFailedDesc);
        }
    }

    float randomMinus1To1() {
        return (2.0f * mRandom.nextFloat()) - 1.0f;
    }
}
