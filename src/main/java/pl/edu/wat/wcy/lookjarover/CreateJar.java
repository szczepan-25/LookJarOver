package pl.edu.wat.wcy.lookjarover;

import java.io.*;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

class CreateJar {

    CreateJar() {
    }


    void createJar(File source, JarOutputStream target, File tempDir) {

        BufferedInputStream in = null;

        try {
            if (!source.getAbsolutePath().equals(tempDir.getAbsolutePath())) {
                String name = source.getAbsolutePath().substring(tempDir.getAbsolutePath().length() + 1);
                if (source.isDirectory()) {
                    if (!name.isEmpty()) {
                        name = name.replace("\\", "/");
                        if (!name.endsWith("/")) {
                            name += "/";
                        }

                        JarEntry entry = new JarEntry(name);
                        target.putNextEntry(entry);
                        target.closeEntry();
                    }
                    File[] sourceFiles = source.listFiles();
                    if (sourceFiles != null) {
                        for (File nestedFile : sourceFiles) {
                            createJar(nestedFile, target, tempDir);
                        }
                    }
                } else {
                    if (!source.getAbsolutePath().endsWith(".MF")) {
                        name = name.replace("\\", "/");
                        JarEntry entry = new JarEntry(name);
                        target.putNextEntry(entry);
                        in = new BufferedInputStream(new FileInputStream(source));

                        byte[] buffer = new byte[1024];
                        int count;
                        while (!((count = in.read(buffer)) == -1)) {
                            target.write(buffer, 0, count);
                        }
                        target.closeEntry();
                    }
                }
            } else {
                File[] files = source.listFiles();

                if (files != null) {
                    for (File f : files) {
                        createJar(f, target, tempDir);
                    }
                }
            }

        } catch (IOException e) {
            LookJarOver.errorMessage(e);
//            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    LookJarOver.errorMessage(e);
//                    e.printStackTrace();
                }
            }
        }
    }
}
