package com.badpx.particleandroid.utils;

import android.graphics.PorterDuff;
import android.util.SparseArray;

/**
 * Created by badpx on 15-6-27.
 */
public class BlendUtils {
    /**
     * CONSTANT                 RGB-factor                  Alpha-factor
     * GL_ZERO                  (0, 0, 0)                   0
     * GL_ONE                   (1, 1, 1)                   1
     * GL_SRC_COLOR             (Rs, Gs, Bs)                As
     * GL_ONE_MINUS_SRC_COLOR   (1, 1, 1) - (Rs, Gs, Bs)    1 - As
     * GL_DST_COLOR             (Rd, Gd, Bd)                Ad
     * GL_ONE_MINUS_DST_COLOR   (1, 1, 1) - (Rd, Gd, Bd)    1 - Ad
     * GL_SRC_ALPHA             (As, As, As)                As
     * GL_ONE_MINUS_SRC_ALPHA   (1, 1, 1) - (As, As, As)    1 - As
     * GL_DST_ALPHA             (Ad, Ad, Ad)                Ad
     * GL_ONE_MINUS_DST_ALPHA   (1, 1, 1) - (Ad, Ad, Ad)    1 - Ad
     * GL_CONSTANT_ALPHA        (Ac, Ac, Ac)                Ac
     * GL_SRC_ALPHA_SATURATE    (f, f, f);f=min(As, 1 - Ad) 1
     * */
    public static final int GL_ZERO                = 0;
    public static final int GL_ONE                 = 1;
    public static final int GL_SRC_COLOR           = 0x0300;
    public static final int GL_ONE_MINUS_SRC_COLOR = 0x0301;
    public static final int GL_SRC_ALPHA           = 0x0302;
    public static final int GL_ONE_MINUS_SRC_ALPHA = 0x0303;
    public static final int GL_DST_ALPHA           = 0x0304;
    public static final int GL_ONE_MINUS_DST_ALPHA = 0x0305;
    public static final int GL_DST_COLOR           = 0x0306;
    public static final int GL_ONE_MINUS_DST_COLOR = 0x0307;
    public static final int GL_SRC_ALPHA_SATURATE  = 0x0308;

    public static final SparseArray<PorterDuff.Mode> BLEND_MODE_TO_PORTER_DUFF =
            new SparseArray<PorterDuff.Mode>();
    static {
        // Reference source: aosp/frameworks/base/libs/hwui/OpenGLRenderer.cpp

        BLEND_MODE_TO_PORTER_DUFF.put(combineKey(GL_ZERO, GL_ONE_MINUS_SRC_ALPHA), PorterDuff.Mode.CLEAR);
        BLEND_MODE_TO_PORTER_DUFF.put(combineKey(GL_ONE, GL_ZERO), PorterDuff.Mode.SRC);
        BLEND_MODE_TO_PORTER_DUFF.put(combineKey(GL_ZERO, GL_ONE), PorterDuff.Mode.DST);
        BLEND_MODE_TO_PORTER_DUFF.put(combineKey(GL_ONE, GL_ONE_MINUS_SRC_ALPHA), PorterDuff.Mode.SRC_OVER);
        BLEND_MODE_TO_PORTER_DUFF.put(combineKey(GL_ONE_MINUS_DST_ALPHA, GL_ONE), PorterDuff.Mode.DST_OVER);
        BLEND_MODE_TO_PORTER_DUFF.put(combineKey(GL_DST_ALPHA, GL_ZERO), PorterDuff.Mode.SRC_IN);
        BLEND_MODE_TO_PORTER_DUFF.put(combineKey(GL_ZERO, GL_SRC_ALPHA), PorterDuff.Mode.DST_IN);
        BLEND_MODE_TO_PORTER_DUFF.put(combineKey(GL_ONE_MINUS_DST_ALPHA, GL_ZERO), PorterDuff.Mode.SRC_OUT);
        BLEND_MODE_TO_PORTER_DUFF.put(combineKey(GL_ZERO, GL_ONE_MINUS_SRC_ALPHA), PorterDuff.Mode.DST_OUT);
        BLEND_MODE_TO_PORTER_DUFF.put(combineKey(GL_DST_ALPHA, GL_ONE_MINUS_SRC_ALPHA), PorterDuff.Mode.SRC_ATOP);
        BLEND_MODE_TO_PORTER_DUFF.put(combineKey(GL_ONE_MINUS_DST_ALPHA, GL_SRC_ALPHA), PorterDuff.Mode.DST_ATOP);
        BLEND_MODE_TO_PORTER_DUFF.put(combineKey(GL_ONE_MINUS_DST_ALPHA, GL_ONE_MINUS_SRC_ALPHA), PorterDuff.Mode.XOR);
        BLEND_MODE_TO_PORTER_DUFF.put(combineKey(GL_ONE, GL_ONE), PorterDuff.Mode.ADD);
        BLEND_MODE_TO_PORTER_DUFF.put(combineKey(GL_DST_COLOR, GL_SRC_COLOR), PorterDuff.Mode.MULTIPLY);
        BLEND_MODE_TO_PORTER_DUFF.put(combineKey(GL_ONE, GL_ONE_MINUS_SRC_COLOR), PorterDuff.Mode.SCREEN);
        // Fake Additive mode:
        BLEND_MODE_TO_PORTER_DUFF.put(combineKey(GL_SRC_ALPHA, GL_ONE), PorterDuff.Mode.ADD);
    }

    private static int combineKey(int sfactor, int dfactor) {
        return (sfactor << 16) | (dfactor & 0xFFFF);
    }

    public static PorterDuff.Mode getPorterDuffModeByBlendFunc(int sfactor, int dfactor) {
        int key = combineKey(sfactor, dfactor);
        return BLEND_MODE_TO_PORTER_DUFF.get(key);
    }
}
