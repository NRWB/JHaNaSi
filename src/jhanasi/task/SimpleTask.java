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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import jhanasi.file.utils.Record;
import jhanasi.file.utils.Search;

/**
 *
 * @author Nick
 */
public class SimpleTask {

    private final Path src;
    public static final int DEFAULT_CUTOFF = 65536;

    public SimpleTask(final Path start) {
        this.src = start;
    }

    public void delegate() {

        // Gather input files
        Search fileFinder = new Search(this.src);
        List<Record> files = null;
        try {
            files = fileFinder.getList();
        } catch (IOException ex) {
        }

        if (files == null || files.isEmpty())
            throw new RuntimeException("no files gathered");

        // mark start time
        long start = System.currentTimeMillis();

        // determine and run following tasks
        Map<Path, Record> poolA = Collections.synchronizedMap(new HashMap<Path, Record>()); // small files
        Map<Path, Record> poolB = Collections.synchronizedMap(new HashMap<Path, Record>()); // large files

        long startPre = System.currentTimeMillis();
        prework(files, poolA, poolB);
        long endPre = System.currentTimeMillis();
        System.out.println("prework time elapsed = " + (endPre - startPre) + " ms");

        long startPoolA = System.currentTimeMillis();
        backgroundWork(poolA, 16);
        long endPoolA = System.currentTimeMillis();
        System.out.println("poolA time elapsed = " + (endPoolA - startPoolA) + " ms");
        System.out.println("poolA.size(): " + poolA.size());
        long startPoolB = System.currentTimeMillis();
        backgroundWork(poolB, 1); // longer running per file
        long endPoolB = System.currentTimeMillis();
        System.out.println("poolB time elapsed = " + (endPoolB - startPoolB) + " ms");
        System.out.println("poolB.size(): " + poolB.size());

        // mark end time
        long end = System.currentTimeMillis();
        System.out.println("elapsed = " + (end - start) + " ms");
    }

    private void prework(final List<Record> src, final Map<Path, Record> a, final Map<Path, Record> b) {
        if (src == null || a == null || b == null)
            throw new NullPointerException("null input");
        src.stream().forEach((rp) -> {
            if (rp.getFileSize() < DEFAULT_CUTOFF)
                //a.add(rp);
                a.put(rp.getPathName(), rp);
            else
                //b.add(rp);
                b.put(rp.getPathName(), rp);
        });
    }

    private void backgroundWork(final Map<Path, Record> filePaths, final int threadCount) {
        //ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(threadCount, threadCount,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>());
        for (Map.Entry<Path, Record> fp : filePaths.entrySet()) {
            //Runnable worker = new SimpleTaskWorker(filePaths.get(i).getPathName(), filePaths);
            Runnable worker = new SimpleTaskWorker(fp.getKey(), fp.getValue());
            executor.execute(worker);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
            // wait
        }
    }
}
