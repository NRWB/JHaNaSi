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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Nick
 */
public class SimpleTaskTest {

    private File folder;
    private File file;

    public SimpleTaskTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        this.folder = new File("testFolder");
        if (!this.folder.mkdir())
            throw new RuntimeException("SimpleTaskTest: could not create folder");
        this.file = new File(this.folder, "test.txt");
        try {
            if (!this.file.createNewFile())
                throw new RuntimeException("SimpleTaskTest: could not create file");
        } catch (IOException ex) {
            Logger.getLogger(SimpleTaskTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Files.write(this.file.toPath(), "hello world".getBytes());
        } catch (IOException ex) {
            Logger.getLogger(SimpleTaskTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @After
    public void tearDown() {
        if (!this.file.delete())
            throw new RuntimeException("SimpleTaskTest: could not delete file");
        if (!this.folder.delete())
            throw new RuntimeException("SimpleTaskTest: could not delete folder");
    }

    /**
     * Test of delegate method, of class SimpleTask.
     * 
     * @ToDo move some code to setup?
     */
    @Test
    public void testDelegate() {
        System.out.println("delegate");
        SimpleTask instance = new SimpleTask(this.folder.toPath());
        instance.delegate();
    }
    
}
