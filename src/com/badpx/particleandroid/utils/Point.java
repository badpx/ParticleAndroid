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

/**
 * Created with IntelliJ IDEA.
 * User: kanedong
 * Date: 14/11/6
 */
public class Point {
    public float x, y;

    public Point() {
        this(0, 0);
    }

    public Point(Point ohs) {
        x = ohs.x;
        y = ohs.y;
    }

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Point multBy(float s) {
        x *= s;
        y *= s;
        return this;
    }

    public Point addBy(Point ohs) {
        x += ohs.x;
        y += ohs.y;
        return this;
    }

    public Point subBy(Point ohs) {
        x -= ohs.x;
        y -= ohs.y;
        return this;
    }

    public void reset() {
        x = y = 0;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void set(Point pos) {
        x = pos.x;
        y = pos.y;
    }

    public static Point mult(Point p, float s) {
        return new Point(p.x * s, p.y * s);
    }

    public static Point add(Point p1, Point p2) {
        return new Point(p1.x + p2.x, p1.y + p2.y);
    }

    public static Point sub(Point p1, Point p2) {
        return new Point(p1.x - p2.x, p1.y - p2.y);
    }

    public static Point Normalize(Point p) {
        return mult(p, 1.0f / length(p));
    }

    public static float length(Point p) {
        return (float) Math.sqrt(lengthSQ(p));
    }

    private static float lengthSQ(Point p) {
        return dot(p, p);
    }

    public static float dot(Point p1, Point p2)
    {
        return p1.x * p2.x + p1.y * p2.y;
    }
}
