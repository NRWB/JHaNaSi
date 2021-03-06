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

import java.nio.file.Path;

/**
 * Record class holds basic information.
 * 
 * The following information includes:
 * - File hash
 * - File name
 * - File size
 *
 * @author Nick
 */

public class Record {

    private final Path pathName;
    private final long fileSize;
    private String fileHash;

    public Record(final Path name, final long fs, final String hash) {
        this.pathName = name;
        this.fileSize = fs;
        this.fileHash = hash;
    }

    public Path getPathName() {
        return this.pathName;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public String getFileHash() {
        return this.fileHash;
    }

    public void setFileHash(final String hash) {
        this.fileHash = hash;
    }
    
    /**
     * 
     * Create a string representation of this object in the format of:
     * [
     * {path:\path\to\file.txt},
     * {size:1048}
     * {hash: ... hash here ... }
     * ]
     * 
     * *note: the above 5 line example is condensed to one line
     * 
     * @return A string representation of this objects private data fields.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[{path:");
        sb.append(this.pathName.toString());
        sb.append("},{size:");
        sb.append(this.fileSize);
        sb.append("},{hash:");
        sb.append(this.fileHash);
        sb.append("}]");
        return sb.toString();
    }

}
