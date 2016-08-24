/*
 * Copyright (C) 2016 Nick
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package jhanasi.file.utils;

/**
 * Digest interface to call for hash.
 *
 * @author Nick
 */
public interface Digest {

    // global immutable char[] that holds base 16 characters
    public static final char[] BASE16 = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * My created version of obtaining a String represented hash from an array
     * of bytes that is reliable and somewhat fast.
     * 
     * @param arr The input byte[] to use
     * 
     * @return The String representation of the input bytes (e.g. a digest hash)
     * 
     */
    public static String getDigestHash(final byte[] arr) {
        final int len = arr.length;
        StringBuilder result = new StringBuilder(len * 2);
        for (int i = 0; i < len; ++i) {
            final byte b = arr[i];
            result.append(BASE16[(b >> 4) & 0xF]);
            result.append(BASE16[(b & 0xF)]);
        }
        return result.toString();
    }
}
