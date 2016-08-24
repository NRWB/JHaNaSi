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
import java.util.logging.Level;
import jhanasi.file.utils.Record;
import jhanasi.file.utils.Search;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Nick
 */
public class SimpleTask {

    private static final Logger logger = LogManager.getLogger(SimpleTask.class);
    
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
            java.util.logging.Logger.getLogger(SimpleTaskWorker.class.getName()).log(Level.SEVERE, null, ex);
            logger.error("SimpleTaskWorker - run() Exception", ex);
        }

        if (files == null || files.isEmpty()) {
            logger.fatal("No files gathered to delegate, terminating app.");
            System.exit(-1);
        }

        // mark start time
        long start = System.currentTimeMillis();

        // determine and run following tasks
        Map<Path, Record> poolA = Collections.synchronizedMap(new HashMap<Path, Record>()); // small files
        Map<Path, Record> poolB = Collections.synchronizedMap(new HashMap<Path, Record>()); // large files

        long startPre = System.currentTimeMillis();
        prework(files, poolA, poolB);
        long endPre = System.currentTimeMillis();
        logger.info("prework time elapsed = " + (endPre - startPre) + " ms");

        long startPoolA = System.currentTimeMillis();
        backgroundWork(poolA, 16);
        long endPoolA = System.currentTimeMillis();
        logger.info("poolA time elapsed = " + (endPoolA - startPoolA) + " ms");
        logger.info("poolA.size(): " + poolA.size());
        long startPoolB = System.currentTimeMillis();
        backgroundWork(poolB, 1); // longer running per file
        long endPoolB = System.currentTimeMillis();
        logger.info("poolB time elapsed = " + (endPoolB - startPoolB) + " ms");
        logger.info("poolB.size(): " + poolB.size());

        // mark end time
        long end = System.currentTimeMillis();
        logger.info("elapsed = " + (end - start) + " ms");
    }

    /**
     * Splits up objects in the master source list into a smaller-files (and
     * likely more amount of smaller files) list and a larger-files (and likely
     * less amount of larger files) list.
     * 
     * @param src The source collection containing target files
     * 
     * @param a The output collection for generally smaller files
     * 
     * @param b The output collection for generally larger files
     */
    private void prework(final List<Record> src, final Map<Path, Record> a, final Map<Path, Record> b) {
        if (src == null || a == null || b == null)
            throw new NullPointerException("null input");
        src.stream().forEach((rp) -> {
            if (rp.getFileSize() < DEFAULT_CUTOFF)
                a.put(rp.getPathName(), rp);
            else
                b.put(rp.getPathName(), rp);
        });
    }

    /**
     * Performs background work on selected files over a given number of threads.
     * 
     * Note 1: Edited in a "ThreadPoolExecutor executor = ..." from the Java
     * core, as the previous line;
     * "ExecutorService executor = Executors.newFixedThreadPool(threadCount);"
     * gave errors during runtime.
     * 
     * Note 2: Stream opted for because of information found here:
     * http://programmers.stackexchange.com/a/297163
     * The previous (valid, yet less legible and potentially less optimize-able)
     * code was:
     * for (Map.Entry<Path, Record> fp : filePaths.entrySet()) {
     *     //Runnable worker = new SimpleTaskWorker(filePaths.get(i).getPathName(), filePaths);
     *     Runnable worker = new SimpleTaskWorker(fp.getKey(), fp.getValue());
     *     executor.execute(worker);
     * }
     * 
     * @param filePaths The collection of files to process with the SimpleTaskWorker.java class
     * 
     * @param threadCount The threads to use for the process work (ideally this is determined when calling this method, and [should] may be very likely related to the number of objects in the @filePaths collection)
     */
    private void backgroundWork(final Map<Path, Record> filePaths, final int threadCount) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(threadCount, threadCount,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>());
        filePaths.entrySet()
            .stream()
                .map((fp) -> new SimpleTaskWorker(fp.getKey(), fp.getValue()))
                .forEach((worker) -> executor.execute(worker));
        executor.shutdown();
        while (!executor.isTerminated()) {
            // wait
        }
    }
}
