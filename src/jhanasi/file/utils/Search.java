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

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * A class to find files on the file system.
 *
 * @author Nick
 */
public class Search {

    private final Path origin;
    private final List<Path> paths;

    public Search(final Path p) {
        this.origin = p;
        this.paths = new ArrayList<Path>();
    }

    private void listFilesRecursively(Path path) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path entry : stream) {
                if (Files.isDirectory(entry))
                    listFilesRecursively(entry);
                this.paths.add(entry);
            }
        }
    }

    public List<Path> getList() throws IOException {
        this.paths.clear();
        listFilesRecursively(this.origin);
        return this.paths;
    }
}
