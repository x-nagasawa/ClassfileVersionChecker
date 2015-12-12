package com.github.x_nagasawa.cvc;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class ClassfileVersionChecker {
    public static void main(String[] args) {
        try {
            int limit = Integer.parseInt(args[0]);   // 上限バージョン
            File target = new File(args[1]);     // 対象
            sweep(target, limit);
        } catch (Exception e) {
            System.out.println("Usage: java -jar ClassfileVersionChecker.jar (under limit) (target file or path)");
            System.out.println(" The argument (under limt) is under limit version of classfile your acceptable like as follow");
            for (int i=0; i < jdks.length; i++) {
                System.out.println("  " + (i+45) + " " + jdks[i]);
            }
            System.out.println(" The argument (target file or path) is *.jar or *.class or directory");
            System.out.println();
            System.out.println("Ex1) Check a jar file contains too new classfile");
            System.out.println(" > ClassfileVersionChecker.jar 48 ClassfileVersionChecker.jar");
            System.out.println(" ---- Output sample ----");
            System.out.println(" ClassfileVersionChecker.jar");
            System.out.println(" com/github/x_nagasawa/cvc/ClassfileVersionChecker.class is compiled in J2SE 5.0");
            System.out.println(" ----------------------");
            System.out.println(" It shows class files which are compiled greater than JDK1.4 (48) in ClassfileVersionChecker.jar");
            System.out.println();
            System.out.println("Ex2) Check jar files in a directory contain too new classfile");
            System.out.println(" > ClassfileVersionChecker.jar 51 ./libs");
            System.out.println(" ---- Output sample ----");
            System.out.println(" ./libs/slf4j-api-1.7.13.jar");
            System.out.println(" ./libs/jetty-http-9.3.6.v20151106.jar");
            System.out.println(" org/eclipse/jetty/http/BadMessageException.class is compiled in J2SE 8.0");
            System.out.println(" org/eclipse/jetty/http/DateGenerator$1.class is compiled in J2SE 8.0");
            System.out.println(" org/eclipse/jetty/http/DateGenerator.class is compiled in J2SE 8.0");
            System.out.println(" org/eclipse/jetty/http/DateParser$1.class is compiled in J2SE 8.0");
            System.out.println(" org/eclipse/jetty/http/DateParser.class is compiled in J2SE 8.0");
            System.out.println(" (snip)");
            System.out.println(" ----------------------");
            System.out.println(" It shows class files which are compiled greater than J2SE7 (51) in ./libs");
            System.out.println();

            e.printStackTrace();
        }
    }

    public static void sweep(File target, int limit) {
        if (target.isFile()) {
            if (target.getName().endsWith(".jar")) {
                try {
                    sweepJar(target, limit);
                } catch (IOException e) {
                    System.out.println(target.getPath() + " is " + e.getMessage());
                }
            } else if (target.getName().endsWith(".class")) {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(target);
                    int ver = getClassfileVersion(fis);
                    if (limit < ver) {
                        System.out.println(target.getPath() + " is compiled in " + getJDKVer(ver));
                    }
                } catch (IOException e) {
                    System.out.println(target.getPath() + " is " + e.getMessage());
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            System.out.println("Failed to close file " + target.getName());
                        }
                    }
                }
            }
        } else if (target.isDirectory()) {
            for (File f : target.listFiles()) {
                sweep(f, limit);
            }
        }
    }

    static String[] jdks = new String[]{
            "JDK 1.1",  // 45 0x2D
            "JDK 1.2",  // 46 0x2E
            "JDK 1.3",  // 47 0x2F
            "JDK 1.4",  // 48 0x30
            "J2SE 5.0", // 49 0x31
            "J2SE 6.0", // 50 0x32
            "J2SE 7.0", // 51 0x33
            "J2SE 8.0"};    // 52 0x34
    private static String getJDKVer(int ver) {
        if (ver < 45 || 52 < ver)
            return "Unknown(raw verion is "+ver+")";
        return jdks[ver-45];
    }

    private static int getClassfileVersion(InputStream is) throws IOException {
        byte[] header = new byte[8];
        if (is.read(header) != header.length) {
            throw new IOException("Stream too short");
        }
        byte[] signiture = new byte[]{(byte) 0xca, (byte) 0xfe, (byte) 0xba, (byte) 0xbe};
        for (int i=0; i < signiture.length; i++) {
            if (header[i] != signiture[i])
                throw new IOException("Invalid classfile");
        }
        int fileversion = 
                (((int) header[6] << 8) & 0x0000ff00) | (((int) header[7]) & 0x000000ff);
        return fileversion;
    }

    public static void sweepJar(File target, int limit) throws IOException {
        System.out.println(target);
        JarInputStream jis = null;
        try {
            jis = new JarInputStream(new BufferedInputStream(new FileInputStream(target)));
            while (true) {
                final JarEntry entry = jis.getNextJarEntry();
                if (entry == null)
                    break;
                if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                    try {
                        int ver = getClassfileVersion(jis);
                        if (limit < ver) {
                            System.out.println(entry.getName() + " is compiled in " + getJDKVer(ver));
                        }
                    } catch (IOException e) {
                        System.out.println(target.getPath() + " is " + e.getMessage());
                    }
                }
            }
        } finally {
            if (jis != null) {
                try {
                    jis.close();
                } catch (IOException e) {
                    System.out.println("Failed to close file " + target.getName());
                }
            }
        }
    }
}
