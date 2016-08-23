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
package jhanasi.task;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import jhanasi.file.utils.Digest;
import jhanasi.file.utils.Record;

/**
 *
 * @author Nick
 */
public class SimpleTaskWorker implements Runnable {

    // Even for smaller files, using 4096 should be OK
    // see: http://tutorials.jenkov.com/java-io/bufferedinputstream.html#setting-buffer-size
    public static final int DEFAULT_BUFFER = 4096; // 4096 8192 16384

    private final Path path;
    private final Record record;

    public SimpleTaskWorker(final Path p, Record r) {
        this.path = p;
        this.record = r;
    }

    @Override
    public void run() {
        try {
            processFile();
        } catch (Exception ex) {
        }
    }

    private void processFile() throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (InputStream is = Files.newInputStream(this.path);
                DigestInputStream dis = new DigestInputStream(is, md)) {
            byte[] buffer = new byte[DEFAULT_BUFFER];
            while (dis.read(buffer) != -1) {
                md.update(buffer);
            }
        }
        this.record.setFileHash(Digest.getDigestHash(md.digest()));
        md.reset();
    }
}
