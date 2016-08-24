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
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.logging.Level;
import jhanasi.file.utils.Digest;
import jhanasi.file.utils.Record;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Nick
 */
public class SimpleTaskWorker implements Runnable {
    
    private static final Logger logger = LogManager.getLogger(SimpleTaskWorker.class);

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
            java.util.logging.Logger.getLogger(SimpleTaskWorker.class.getName()).log(Level.SEVERE, null, ex);
            logger.error("SimpleTaskWorker - run() Exception", ex);
        }
    }

    private void processFile() throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        
        final long s = System.currentTimeMillis();
        
        try (InputStream is = Files.newInputStream(this.path);
                DigestInputStream dis = new DigestInputStream(is, md)) {
            byte[] buffer = new byte[DEFAULT_BUFFER];
            while (dis.read(buffer) != -1) {
                md.update(buffer);
            }
        }
        
        final long e = System.currentTimeMillis();
        
        this.record.setFileHash(Digest.getDigestHash(md.digest()));
        
        StringBuilder sb = new StringBuilder();
        sb.append("elapsed = ");
        sb.append((e - s));
        sb.append(", ");
        sb.append("results = ");
        sb.append(this.record.toString());
        logger.trace(sb.toString());
        
        md.reset();
    }
}
