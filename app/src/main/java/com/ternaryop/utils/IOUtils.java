package com.ternaryop.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IOUtils {
    private static final int BUFFER_SIZE = 100 * 1024;

    public static void copy(InputStream is, OutputStream os) throws IOException {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            byte[] buff = new byte[BUFFER_SIZE];
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(os);
            int bytesRead = 0;
            while (bytesRead != -1) {
                bytesRead = bis.read(buff); //-1, 0, or more
                if (bytesRead > 0) {
                    bos.write(buff, 0, bytesRead);
                }
            }
            bos.flush();
        }
        finally {
            closeSilently(bis);
            closeSilently(bos);
        }
    }

    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        copy(new FileInputStream(sourceFile), new FileOutputStream(targetFile));
    }

    public static void closeSilently(InputStream is) {
        try { if (is != null) is.close(); } catch (IOException ignored) {}
    }

    public static void closeSilently(OutputStream os) {
        try { if (os != null) os.close(); } catch (IOException ignored) {}
    }

    public static void moveFile(File sourceFile, File targetFile) throws IOException {
        if (!sourceFile.exists()) {
            throw new FileNotFoundException("Source '" + sourceFile + "' does not exist");
        }

        if (sourceFile.isDirectory()) {
            throw new IOException("Source '" + sourceFile + "' is a directory");
        }

        if (targetFile.exists()) {
            throw new IOException("Destination '" + targetFile + "' already exists");
        }

        if (targetFile.isDirectory()) {
            throw new IOException("Destination '" + targetFile + "' is a directory");
        }
        if (!sourceFile.renameTo(targetFile)) {
            copyFile(sourceFile, targetFile);
            if (!sourceFile.delete()) {
                targetFile.delete();
                throw new IOException("Failed to delete original file '" + sourceFile + "' after copy to '" + targetFile + "'");
            }
        }
    }

    public static String generateUniqueFileName(String path) {
        File file = new File(path);
        File parentFile = file.getParentFile();

        String nameWithExt = file.getName();
        Pattern patternCount = Pattern.compile("(.*) \\((\\d+)\\)");
        
        while (file.exists()) {
            String name;
            String ext;
            int extPos = nameWithExt.lastIndexOf('.');
            if (extPos < 0) {
                name = nameWithExt;
                ext = "";
            } else {
                name = nameWithExt.substring(0, extPos);
                // contains dot
                ext = nameWithExt.substring(extPos); 
            }
            Matcher matcherCount = patternCount.matcher(name);
            int count = 1;
            if (matcherCount.matches()) {
                name = matcherCount.group(1);
                count = Integer.parseInt(matcherCount.group(2)) + 1;
            }
            nameWithExt = name + " (" + count + ")" + ext;
            file = new File(parentFile, nameWithExt);
        }
        return file.getAbsolutePath();
    }
}
