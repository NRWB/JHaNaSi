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
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jhanasi.task.SimpleTaskTest;
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
public class SearchTest {

    private File folder;
    private File subFolder;
    private File file;

    public SearchTest() {
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
        this.subFolder = new File(this.folder, "testSubFolder");
        if (!this.folder.mkdir())
            throw new RuntimeException("SimpleTaskTest: could not create folder");
        if (!this.subFolder.mkdir())
            throw new RuntimeException("SimpleTaskTest: could not create sub folder");
        this.file = new File(this.subFolder, "test.txt");
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
        if (!this.subFolder.delete())
            throw new RuntimeException("SimpleTaskTest: could not delete sub folder");
        if (!this.folder.delete())
            throw new RuntimeException("SimpleTaskTest: could not delete folder");
    }

    /**
     * Test of getList method, of class Search.
     */
    @Test
    public void testGetList() throws Exception {
        System.out.println("getList");
        Search instance = new Search(this.folder.toPath());
        final int expResult = 1;
        List<Record> result = instance.getList();
        // result.forEach((k) -> System.out.println(k.getPathName()));
        assertEquals(expResult, result.size());
    }
}
