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

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import jhanasi.file.utils.Search;

/**
 *
 * @author Nick
 */
public class SimpleTask {

    private final Path src;

    public SimpleTask(final Path start) {
        this.src = start;
    }

    public void calc(final boolean simple) {

        // Gather input files
        Search fileFinder = new Search(this.src);
        List<Path> files = null;
        try {
            files = fileFinder.getList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (files == null || files.isEmpty())
            throw new RuntimeException("no files gathered");
        // System.out.println("Gathering complete");

        // mark start time
        long start = System.currentTimeMillis();

        // determine and run following task
        // add file profiling before this?
        // -- e.g. calculate # of files
        //         && the file sizes?
        // -- or, run all files on advanced that are under x-amount kb
        //        && remaining files on the simple one?
        try {
            if (simple)
                simple(files);
            else
                advanced(files);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // mark end time
        long end = System.currentTimeMillis();
        System.out.println("elapsed = " + (end - start) + " ms");
    }

    private void simple(final List<Path> filePaths) {
    }

    private void advanced(final List<Path> filePaths) {
    }
}
