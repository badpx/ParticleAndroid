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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

/**
 * Created with IntelliJ IDEA.
 * User: kanedong
 * Date: 14/11/6
 */
public class Misc {
    public static float clamp(float value, float min_inclusive, float max_inclusive)
    {
        if (min_inclusive > max_inclusive) {
            float t = min_inclusive;
            min_inclusive = max_inclusive;
            max_inclusive = t;
        }
        return value < min_inclusive ? min_inclusive : value < max_inclusive? value : max_inclusive;
    }

    public static int clamp(int value, int min_inclusive, int max_inclusive)
    {
        if (min_inclusive > max_inclusive) {
            int t = min_inclusive;
            min_inclusive = max_inclusive;
            max_inclusive = t;
        }
        return value < min_inclusive ? min_inclusive : value < max_inclusive? value : max_inclusive;
    }

    // Unzip gzip buffer
    public static byte[] unzip(byte[] gzipBuffer) throws IOException {
        if (null != gzipBuffer && gzipBuffer.length > 0) {
            GZIPInputStream gzipInputStream;
            gzipInputStream = new GZIPInputStream(
                    new ByteArrayInputStream(gzipBuffer, 0, gzipBuffer.length));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            while (true) {
                int len = gzipInputStream.read(buffer);
                if (-1 != len) {
                    baos.write(buffer, 0, len);
                } else {
                    break;
                }
            }
            gzipInputStream.close();
            baos.close();

            return baos.toByteArray();
        }
        return null;
    }
}
