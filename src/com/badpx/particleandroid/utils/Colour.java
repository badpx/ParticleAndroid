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
package com.badpx.particleandroid.utils;

import android.graphics.Color;

/**
 * Created with IntelliJ IDEA.
 * User: kanedong
 * Date: 14/11/6
 */
public class Colour {
    public int a, r, g, b;

    public Colour(int a, int r, int g, int b) {
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public boolean isEmpty() {
        return 0 == a && 0 == r && 0 == g && 0 == b;
    }

    public void copy(Colour colour) {
        a = colour.a;
        r = colour.r;
        g = colour.g;
        b = colour.b;
    }

    public static int rgb(float r, float g, float b) {
        return Color.rgb(
                (int) (r * 255f), (int) (g * 255f), (int) (b * 255f)
        );
    }

    public static int argb(float a, float r, float g, float b) {
        return Color.argb(
                (int)(a * 255f), (int)(r * 255f), (int)(g * 255f), (int)(b * 255f)
        );
    }
}
