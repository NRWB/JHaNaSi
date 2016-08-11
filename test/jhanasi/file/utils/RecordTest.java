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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Nick
 */
public class RecordTest {

    private File f1, f2;
    private Record r1, r2;

    public RecordTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        this.f1 = new File("test1.txt");
        this.f2 = new File("test2.txt");

        try {
            if (!this.f1.createNewFile())
                throw new RuntimeException("SimpleTaskTest: could not create file 1");
        } catch (IOException ex) {
            Logger.getLogger(RecordTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            if (!this.f2.createNewFile())
                throw new RuntimeException("SimpleTaskTest: could not create file 2");
        } catch (IOException ex) {
            Logger.getLogger(RecordTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            Files.write(this.f1.toPath(), "hello world".getBytes());
        } catch (IOException ex) {
            Logger.getLogger(RecordTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Files.write(this.f2.toPath(), "hello world".getBytes());
        } catch (IOException ex) {
            Logger.getLogger(RecordTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            this.r1 = new Record(f1.toPath(), Files.size(f1.toPath()), null);
        } catch (IOException ex) {
            Logger.getLogger(RecordTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            this.r2 = new Record(f2.toPath(), Files.size(f2.toPath()), "");
        } catch (IOException ex) {
            Logger.getLogger(RecordTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @After
    public void tearDown() {
        if (!this.f1.delete())
            throw new RuntimeException("SimpleTaskTest: could not delete file 1");
        if (!this.f2.delete())
            throw new RuntimeException("SimpleTaskTest: could not delete file 2");
    }

    /**
     * Test of getPathName method, of class Record.
     */
    @Test
    public void testGetPathName() {
        System.out.println("getPathName");
        Record instance = this.r1;
        Path expResult = this.f1.toPath();
        Path result = instance.getPathName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getFileSize method, of class Record.
     */
    @Test
    public void testGetFileSize() {
        System.out.println("getFileSize");
        Record instance = this.r1;
        long expResult = this.f1.length();
        long result = instance.getFileSize();
        assertEquals(expResult, result);
    }

    /**
     * Test of getFileHash method, of class Record.
     */
    @Test
    public void testGetFileHash() {
        System.out.println("getFileHash");
        Record instance = this.r1;

        // expected ( actual hash should = 5eb63bbbe01eeed093cb22bb8f5acdc3 )
        MessageDigest msgDigest = null;
        try {
            msgDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RecordTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (msgDigest == null)
            throw new NullPointerException("null message digest (1)");
        final String hashStr = Digest.getDigestHash(msgDigest.digest("hello world".getBytes()));
        System.out.println(hashStr);

        // tested
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RecordTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (md == null)
            throw new NullPointerException("null message digest (2)");

        try (InputStream is = Files.newInputStream(this.r1.getPathName());
                DigestInputStream dis = new DigestInputStream(is, md)) {
            byte[] buffer = new byte[4096];
            while (dis.read(buffer) != -1) {
            }
        } catch (IOException ex) {
            Logger.getLogger(RecordTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.r1.setFileHash(Digest.getDigestHash(md.digest()));

        String result = instance.getFileHash();
        assertEquals(hashStr, result);
    }
    
}
