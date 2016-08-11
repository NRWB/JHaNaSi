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

import java.nio.file.Path;
import java.util.List;
import jhanasi.file.utils.Record;

/**
 *
 * @author Nick
 */
public class SimpleTaskWorker implements Runnable {

    private final Path path;
    private final List<Record> records;

    public SimpleTaskWorker(final Path p, final List<Record> r) {
        this.path = p;
        this.records = r;
    }

    @Override
    public void run() {
        final String name = this.path.toFile().getName();
    }
}
