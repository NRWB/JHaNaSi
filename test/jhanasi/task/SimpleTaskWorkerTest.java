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
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import jhanasi.file.utils.Record;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Nick
 */
public class SimpleTaskWorkerTest {

    private File folder;
    private File file;

    public SimpleTaskWorkerTest() {
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
            throw new RuntimeException("SimpleTaskWorkerTest: could not create folder");
        this.file = new File(this.folder, "test.txt");
        try {
            if (!this.file.createNewFile())
                throw new RuntimeException("SimpleTaskWorkerTest: could not create file");
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
            throw new RuntimeException("SimpleTaskWorkerTest: could not delete file");
        if (!this.folder.delete())
            throw new RuntimeException("SimpleTaskWorkerTest: could not delete folder");
    }

    /**
     * Test of run method, of class SimpleTaskWorker.
     */
    @Test
    public void testRun() {
        System.out.println("run");
        Path p = this.file.toPath();
        Record r = null;
        try {
            r = new Record(p, Files.size(p), null);
        } catch (IOException ex) {
            Logger.getLogger(SimpleTaskWorkerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (r == null)
            throw new NullPointerException("SimpleTaskWorkerTest: Record not created.");
        SimpleTaskWorker instance = new SimpleTaskWorker(p, r);
        ExecutorService executor = Executors.newFixedThreadPool(1); // this test only needs 1 thread...
        executor.execute(instance);
        executor.shutdown();
        while (!executor.isTerminated()) {
            // wait
        }
        System.out.println("SimpleTaskWorkerTest: result record = " + r);
    }
}
