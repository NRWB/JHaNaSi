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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import jhanasi.file.utils.Record;
import jhanasi.file.utils.Search;

/**
 *
 * @author Nick
 */
public class SimpleTask {

    private final Path src;

    public static final int DEFAULT_BUFFER = 4096;
    public static final int DEFAULT_CUTOFF = 65536;

    public SimpleTask(final Path start) {
        this.src = start;
    }

    public void delegate() {

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

        // determine and run following tasks
        List<Path> poolA = new ArrayList<Path>(); // small files
        List<Path> poolB = new ArrayList<Path>(); // large files
        prework(files, poolA, poolB);
        List<Record> result = new ArrayList<Record>();
        result.addAll(backgroundWork(poolA, 8));
        result.addAll(backgroundWork(poolA, 1)); // longer running per file

        System.out.println("Processed files: " + result.size());

        // mark end time
        long end = System.currentTimeMillis();
        System.out.println("elapsed = " + (end - start) + " ms");
    }

    private void prework(final List<Path> src, final List<Path> a, final List<Path> b) {
        if (src == null || a == null || b == null)
            throw new NullPointerException("null input");
        src.stream().forEach((p) -> {
            try {
                final long fs = Files.size(p);
                if (fs < DEFAULT_CUTOFF)
                    a.add(p);
                else
                    b.add(p);
            } catch (IOException ex) {
                Logger.getLogger(SimpleTask.class.getName()).log(Level.SEVERE, null, ex);
                // ex.printStackTrace();
            }
        });
    }

    private List<Record> backgroundWork(final List<Path> filePaths, final int threadCount) {
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<Record> result = Collections.synchronizedList(new ArrayList<Record>());
        for (int i = 0; i < filePaths.size(); ++i) {
            Runnable worker = new SimpleTaskWorker(filePaths.get(i), result);
            executor.execute(worker);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
            // wait
        }
        return result;
    }
}
