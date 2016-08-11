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
package jhanasi;

import java.nio.file.Path;
import java.nio.file.Paths;
import jhanasi.task.SimpleTask;

/**
 *
 * @author Nick
 */
public class JHaNaSi {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Path p = Paths.get("C:\\Users\\Nick\\Desktop\\web1\\");
        SimpleTask task = new SimpleTask(p);
        task.delegate();
    }
    
}
