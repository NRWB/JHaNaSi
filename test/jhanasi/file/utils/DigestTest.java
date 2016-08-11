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
public class DigestTest {

    private File f;
    
    public DigestTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        this.f = new File("test.txt");
        try {
            if (!this.f.createNewFile())
                throw new RuntimeException("DigestTest: could not create file");
        } catch (IOException ex) {
            Logger.getLogger(RecordTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Files.write(this.f.toPath(), "hello world".getBytes());
        } catch (IOException ex) {
            Logger.getLogger(RecordTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @After
    public void tearDown() {
        if (!this.f.delete())
            throw new RuntimeException("DigestTest: could not delete file");
    }

    /**
     * Test of getDigestHash method, of class Digest.
     */
    @Test
    public void testGetDigestHash() {
        System.out.println("getDigestHash");

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RecordTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (md == null)
            throw new NullPointerException("null message digest");

        try (InputStream is = Files.newInputStream(this.f.toPath());
                DigestInputStream dis = new DigestInputStream(is, md)) {
            byte[] buffer = new byte[4096];
            while (dis.read(buffer) != -1) {
            }
        } catch (IOException ex) {
            Logger.getLogger(RecordTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        String result = Digest.getDigestHash(md.digest());

        String expResult = "5eb63bbbe01eeed093cb22bb8f5acdc3";

        System.out.println(result + " vs " + expResult);

        assertEquals(expResult, result);
    }
    
}
