/*
  You may freely copy, distribute, modify and use this class as long
  as the original author attribution remains intact.  See message
  below.

  Copyright (C) 2003 Christian Pesch. All Rights Reserved.
*/

package slash.metamusic.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Load a library for the class path. This avoids setting
 * the <const>java.library.path</const> to the directory
 * with the native libraries and thus simplifies deployment
 * and testing.
 *
 * @author Christian Pesch
 * @version $Id: LibraryLoader.java 433 2004-10-27 18:20:11Z cpesch $
 */

public class LibraryLoader {
  public static int getBits() {
    return Integer.parseInt(System.getProperty("sun.arch.data.model"));
  }

  public static String loadLibrary(ClassLoader classLoader, String libname) throws IOException {
    String platformLibName = System.mapLibraryName(libname);
    InputStream in = classLoader.getResourceAsStream(platformLibName);
    if (in == null) {
      throw new FileNotFoundException("Native library " + platformLibName + " not in class path");
    }

    String prefix = Files.removeExtension(platformLibName);
    String suffix = Files.getExtension(platformLibName);

    File f = File.createTempFile(prefix, "." + suffix);
    f.deleteOnExit();

    FileOutputStream out = new FileOutputStream(f);
    InputOutput inout = new InputOutput(in, out);
    inout.start();
    inout.close();

    System.load(f.getAbsolutePath());
    return f.getAbsolutePath();
  }
}
