package com.badpx.particleandroid;

import android.content.res.Resources;
import com.badpx.particleandroid.utils.PListParser;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kanedong on 14-12-5.
 */
public class PListParticleSystemHelper {
    private static final Map<String, Field> FIELD_MAP = new HashMap<String, Field>();
    static {
        Class clazz = ParticleSystem.class;
        try {
            FIELD_MAP.put("radialAcceleration", clazz.getDeclaredField(""));
            FIELD_MAP.put("maxRadius", clazz.getDeclaredField(""));
            FIELD_MAP.put("startParticleSize", clazz.getDeclaredField(""));
            FIELD_MAP.put("startColorVarianceBlue", clazz.getDeclaredField(""));
            FIELD_MAP.put("rotatePerSecondVariance", clazz.getDeclaredField(""));
            FIELD_MAP.put("finishColorVarianceBlue", clazz.getDeclaredField(""));
            FIELD_MAP.put("tangentialAcceleration", clazz.getDeclaredField(""));
            FIELD_MAP.put("startColorRed", clazz.getDeclaredField(""));
            FIELD_MAP.put("rotatePerSecond", clazz.getDeclaredField(""));
            FIELD_MAP.put("finishParticleSize", clazz.getDeclaredField(""));
            FIELD_MAP.put("speedVariance", clazz.getDeclaredField(""));
            FIELD_MAP.put("emitterType", clazz.getDeclaredField(""));
            FIELD_MAP.put("startColorVarianceAlpha", clazz.getDeclaredField(""));
            FIELD_MAP.put("finishParticleSizeVariance", clazz.getDeclaredField(""));
            FIELD_MAP.put("minRadius", clazz.getDeclaredField(""));
            FIELD_MAP.put("particleLifespanVariance", clazz.getDeclaredField(""));
            FIELD_MAP.put("gravityx", clazz.getDeclaredField(""));
            FIELD_MAP.put("gravityy", clazz.getDeclaredField(""));
            FIELD_MAP.put("sourcePositiony", clazz.getDeclaredField(""));
            FIELD_MAP.put("startColorGreen", clazz.getDeclaredField(""));
            FIELD_MAP.put("sourcePositionx", clazz.getDeclaredField(""));
            FIELD_MAP.put("startColorBlue", clazz.getDeclaredField(""));
            FIELD_MAP.put("blendFuncDestination", clazz.getDeclaredField(""));
            FIELD_MAP.put("startColorVarianceRed", clazz.getDeclaredField(""));
            FIELD_MAP.put("speed", clazz.getDeclaredField("mSpeed"));
            FIELD_MAP.put("particleLifespan", clazz.getDeclaredField(""));
            FIELD_MAP.put("angleVariance", clazz.getDeclaredField(""));
            FIELD_MAP.put("sourcePositionVariancex", clazz.getDeclaredField(""));
            FIELD_MAP.put("finishColorBlue", clazz.getDeclaredField(""));
            FIELD_MAP.put("startColorVarianceGreen", clazz.getDeclaredField(""));
            FIELD_MAP.put("sourcePositionVariancey", clazz.getDeclaredField(""));
            FIELD_MAP.put("angle", clazz.getDeclaredField(""));
            FIELD_MAP.put("duration", clazz.getDeclaredField(""));
            FIELD_MAP.put("finishColorAlpha", clazz.getDeclaredField(""));
            FIELD_MAP.put("textureFileName", clazz.getDeclaredField(""));
            FIELD_MAP.put("maxRadiusVariance", clazz.getDeclaredField(""));
            FIELD_MAP.put("maxParticles", clazz.getDeclaredField(""));
            FIELD_MAP.put("finishColorGreen", clazz.getDeclaredField(""));
            FIELD_MAP.put("finishColorVarianceGreen", clazz.getDeclaredField(""));
            FIELD_MAP.put("startColorAlpha", clazz.getDeclaredField(""));
            FIELD_MAP.put("radialAccelVariance", clazz.getDeclaredField(""));
            FIELD_MAP.put("tangentialAccelVariance", clazz.getDeclaredField(""));
            FIELD_MAP.put("blendFuncSource", clazz.getDeclaredField(""));
            FIELD_MAP.put("startParticleSizeVariance", clazz.getDeclaredField(""));
            FIELD_MAP.put("finishColorRed", clazz.getDeclaredField(""));
            FIELD_MAP.put("finishColorVarianceRed", clazz.getDeclaredField(""));
            FIELD_MAP.put("finishColorVarianceAlpha", clazz.getDeclaredField(""));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static ParticleSystem create(Resources resources, String assetName) {
        if (null == resources || null == assetName) return null;
        try {
            return create(resources.getAssets().open(assetName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ParticleSystem create(InputStream plistStream) {
        if (null != plistStream) {
            PListParser parser = new PListParser(plistStream);
            if (parser.isValidPList && parser.root instanceof Map) {
                Map kv = (Map)parser.root;
                ParticleSystem particleSystem = new ParticleSystem();
            }
        }
        return null;
    }
}
