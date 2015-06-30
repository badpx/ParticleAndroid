# ParticleAndroid

---

**A particle system for Android written in Java.**

Demo Video Record:   

![DemoGIF](http://7xjos9.com1.z0.glb.clouddn.com/0630AndroidParticle.gif)

## Introduction
### Goals
We want to provide a lightweight particle system based on JAVA, for fast and easy integration into the Android project.

### Features

* Two emitter modes are supported: Gravity & Radius.
* Create particle system from the **plist** format for **cocos2d**.
* **Embedded** texture resource of the plist file(PNG/JPEG/BMP, but the TIFF format doesn't support yet).
* Blend mode.

## Get started
### Integrate ParticleSystemView
The **ParticleSystemView** is a subclass of androd.view.View, It is responsible for managing the life cycle of particle emitters. It can be created directly or inflate from layout file:

```
    // Create ParticleSystemView by code:
    ParticleSystemView particleSystemView = new ParticleSystemView(context);
    rootLayout.addView(particleSystemView);
```

```
    <!-- Add in a layout xml file -->
    <com.badpx.particleandroid.widget.ParticleSystemView
        android:id="@+id/particle_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
```

### Create Particle Emitter
A particle emitter must be a subclass of **ParticleSystem**, it can create by manual or plist file:

```
// Create particle emitter by manual
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
        mStartSize = 15.0f;
        mStartSizeVar = 10.0f;
        mEndSize = END_SIZE_EQUAL_TO_START_SIZE;

        // emits per second
        mEmissionRate = mTotalParticles / mLife;

        // color of particles
        mStartColor = Colour.argb(1.0f, 0.7f, 0.1f, 0.2f);
        mStartColorVar = Colour.argb(0f, 0.5f, 0.5f, 0.5f);
        mEndColorVar = Colour.argb(0f, 0.5f, 0.5f, 0.5f);
    }
}

// Create particle emitter
ParticleSystem emitter = new ParticleExplosion();
```

```
// Create particle emitter by plist file
ParticleSystem emitter = 
    PListParticleSystemHelper.create(resources, plistPath/* Relative to assets directory */);
```

### Setup emitter to ParticleSystemView

```
// Add one emitter to ParticleSystemView
particleSystemView.addParticleSystem(emitter);
// Start emitting
emitter.startup();
```

### Remove emitter

```
// Shutdown a emitter
emitter.shutdown();
// Remove a emitter from ParticleSystemView
particleSystemView.removeParticleSystem(emitter);
```
