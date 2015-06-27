package com.badpx.particleandroid;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Base64;
import com.badpx.particleandroid.utils.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by kanedong on 14-12-5.
 */
public class PListParticleSystemHelper {

    public static ParticleSystem create(Resources resources, String assetName) {
        if (null == resources || null == assetName) return null;
        try {
            return create(resources, resources.getAssets().open(assetName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    public static ParticleSystem create(Resources resources, InputStream plistStream) {
        if (null != plistStream) {
            PListParser parser = new PListParser(plistStream);
            if (parser.isValidPList && parser.root instanceof Map) {
                Map kv = (Map)parser.root;
                ParticleSystem particleSystem = null;

                Object v = kv.get("maxParticles");
                if (v instanceof Float) {
                    particleSystem = new ParticleSystem(((Float) v).intValue());
                } else {
                    return null;
                }
                v = kv.get("emitterType");
                if (v instanceof Float) {
                    particleSystem.setEmitterMode((Float) v == 0 ? ParticleSystem.EmitterMode.MODE_GRAVITY : ParticleSystem.EmitterMode.MODE_RADIUS);
                }
                Object x = kv.get("sourcePositionx");
                Object y = kv.get("sourcePositiony");
                if (x instanceof Float && y instanceof Float) {
                    particleSystem.setPosition((Float)x, (Float)y);
                }
                if (particleSystem.getEmitterMode() == ParticleSystem.EmitterMode.MODE_GRAVITY) {
                    v =  kv.get("radialAcceleration");
                    if (v instanceof Float) {
                        particleSystem.setRadialAccel((Float) v);
                    }
                    v = kv.get("tangentialAcceleration");
                    if (v instanceof Float) {
                        particleSystem.setTangentialAccel((Float) v);
                    }
                    x = kv.get("gravityx");
                    y = kv.get("gravityy");
                    if (x instanceof Float && y instanceof Float) {
                        particleSystem.setGravity(new Point((Float)x, (Float)y));
                    }
                    v = kv.get("speedVariance");
                    if (v instanceof Float) {
                        particleSystem.setSpeedVar((Float) v);
                    }
                    v = kv.get("speed");
                    if (v instanceof Float) {
                        particleSystem.setSpeed((Float) v);
                    }
                    v = kv.get("radialAccelVariance");
                    if (v instanceof Float) {
                        particleSystem.setRadialAccelVar((Float) v);
                    }
                    v = kv.get("tangentialAccelVariance");
                    if (v instanceof Float) {
                        particleSystem.setTangentialAccelVar((Float) v);
                    }
                } else {
                    v = kv.get("maxRadius");
                    if (v instanceof Float) {
                        particleSystem.setStartRadius((Float) v);
                    }
                    v = kv.get("minRadius");
                    if (v instanceof Float) {
                        particleSystem.setEndRadius((Float) v);
                    }
                    v = kv.get("maxRadiusVariance");
                    if (v instanceof Float) {
                        particleSystem.setStartRadiusVar((Float) v);
                    }
                    v = kv.get("rotatePerSecond");
                    if (v instanceof Float) {
                        particleSystem.setRotatePerSecond((Float) v);
                    }
                    v = kv.get("rotatePerSecondVariance");
                    if (v instanceof Float) {
                        particleSystem.setRotatePerSecondVar((Float) v);
                    }
                }
                v = kv.get("startParticleSize");
                if (v instanceof Float) {
                    particleSystem.setStartSize((Float) v);
                }
                v = kv.get("finishParticleSize");
                if (v instanceof Float) {
                    particleSystem.setEndSize((Float) v);
                }
                v = kv.get("finishParticleSizeVariance");
                if (v instanceof Float) {
                    particleSystem.setEndSizeVar((Float) v);
                }
                v = kv.get("particleLifespanVariance");
                if (v instanceof Float) {
                    particleSystem.setLifeVar((Float) v);
                }

                int blendFuncSource = -1;
                v = kv.get("blendFuncSource");
                if (v instanceof Integer) {
                    blendFuncSource = (Integer)v;
                }
                int blendFuncDest = -1;
                v = kv.get("blendFuncDestination");
                if (v instanceof Integer) {
                    blendFuncDest = (Integer)v;
                }
                if (blendFuncSource > -1 && blendFuncDest > -1) {
                    PorterDuff.Mode mode =
                            BlendUtils.getPorterDuffModeByBlendFunc(blendFuncSource, blendFuncDest);
                    particleSystem.setBlendMode(mode);
//                    particleSystem.setColorFilterMode(mode);
                }

                v = kv.get("particleLifespan");
                if (v instanceof Float) {
                    particleSystem.setLife((Float) v);
                }
                v = kv.get("angleVariance");
                if (v instanceof Float) {
                    particleSystem.setAngleVar((Float) v);
                }
                x = kv.get("sourcePositionVariancex");
                y = kv.get("sourcePositionVariancey");
                if (x instanceof Float && y instanceof Float) {
                    particleSystem.setPosVar(new Point((Float) x, (Float) y));
                }
                v = kv.get("angle");
                if (v instanceof Float) {
                    particleSystem.setAngle((Float) v);
                }
                v = kv.get("duration");
                if (v instanceof Float) {
                    particleSystem.setDuration((Float) v);
                }
                v = kv.get("startParticleSizeVariance");
                if (v instanceof Float) {
                    particleSystem.setStartSizeVar((Float) v);
                }
                Object a = kv.get("startColorAlpha");
                Object r = kv.get("startColorRed");
                Object g = kv.get("startColorGreen");
                Object b = kv.get("startColorBlue");
                if (a instanceof Float && r instanceof Float &&
                        g instanceof Float && b instanceof Float) {
                    particleSystem.setStartColor(Colour.argb((Float) a, (Float) r, (Float) g, (Float) b));
                }

                a = kv.get("startColorVarianceAlpha");
                r = kv.get("startColorVarianceRed");
                g = kv.get("startColorVarianceGreen");
                b = kv.get("startColorVarianceBlue");
                if (a instanceof Float && r instanceof Float &&
                        g instanceof Float && b instanceof Float) {
                    particleSystem.setStartColorVar(Colour.argb((Float) a, (Float) r, (Float) g, (Float) b));
                }

                a = kv.get("finishColorAlpha");
                r = kv.get("finishColorRed");
                g = kv.get("finishColorGreen");
                b = kv.get("finishColorBlue");
                if (a instanceof Float && r instanceof Float &&
                        g instanceof Float && b instanceof Float) {
                    particleSystem.setEndColor(Colour.argb((Float) a, (Float) r, (Float) g, (Float) b));
                }

                a = kv.get("finishColorVarianceAlpha");
                r = kv.get("finishColorVarianceRed");
                g = kv.get("finishColorVarianceGreen");
                b = kv.get("finishColorVarianceBlue");
                if (a instanceof Float && r instanceof Float &&
                        g instanceof Float && b instanceof Float) {
                    particleSystem.setEndColorVar(Colour.argb((Float)a, (Float)r, (Float)g, (Float)b));
                }

                // emits per frame
                particleSystem.mEmissionRate =
                        particleSystem.mTotalParticles / particleSystem.mLife;

                boolean isTextureSetup = false;
                v = kv.get("textureFileName");
                if (v instanceof String && null != resources) {
                    String fileName = (String)v;
                    try {
                        final Bitmap texture =
                                BitmapFactory.decodeStream(resources.getAssets().open(fileName));
                        if (null != texture) {
                            particleSystem.setParticleFactory(new ParticleSystem.ParticleFactory() {
                                private Drawable mCommonDrawable;
                                @Override
                                public Particle create(ParticleSystem particleSystem) {
                                    if (null == mCommonDrawable) {
                                        mCommonDrawable = new BitmapDrawable(texture);
                                    }
//                                Drawable drawable = mCommonDrawable.getConstantState().newDrawable();
                                    return new DrawableParticle(particleSystem, mCommonDrawable);
                                }
                            });
                            isTextureSetup = true;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (!isTextureSetup) {
                    v = kv.get("textureImageData");
                    if (v instanceof String) {
                        byte[] buffer = Base64.decode((String) v, Base64.DEFAULT);
                        try {
                            byte[] deflated = Misc.unzip(buffer);
                            if (null != deflated) {
                                final Bitmap texture =
                                        BitmapFactory.decodeByteArray(deflated, 0, deflated.length);
                                if (null != texture) {
                                    particleSystem.setParticleFactory(new ParticleSystem.ParticleFactory() {
                                        private Drawable mCommonDrawable;

                                        @Override
                                        public Particle create(ParticleSystem particleSystem) {
                                            if (null == mCommonDrawable) {
                                                mCommonDrawable = new BitmapDrawable(texture);
                                            }
                                            Drawable drawable = mCommonDrawable.getConstantState().newDrawable();
                                            return new DrawableParticle(particleSystem, drawable);
                                        }
                                    });
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                return particleSystem;
            }
        }
        return null;
    }
}
