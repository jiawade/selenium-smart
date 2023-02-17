package io.smart.utils;


import io.smart.exception.FileNotFoundException;
import io.smart.exception.NotDirectoryException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.filefilter.IOFileFilter;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
public class FileUtils {

    private FileUtils() {

    }

    public static List<File> listFiles(String filePath) {
        try {
            return Files.walk(Paths.get(filePath))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error(e.toString(), e);
        }
        return new ArrayList<>();
    }


    public static List<File> listFiles(String directoryPath, String[] extensions, boolean recursive) {
        Collection<File> files = org.apache.commons.io.FileUtils.listFiles(new File(directoryPath), extensions, recursive);
        return new ArrayList<>(files);
    }

    public static List<File> listFiles(String directoryPath, IOFileFilter fileFilter, IOFileFilter dirFilter) {
        Collection<File> files = org.apache.commons.io.FileUtils.listFiles(new File(directoryPath), fileFilter, dirFilter);
        return new ArrayList<>(files);
    }

    public static Stream<File> listFilesToStream(String directoryPath, boolean recursive, String... extensions) {
        List<String> list = Arrays.stream(extensions).filter(i -> !i.isEmpty()).collect(Collectors.toList());
        if (list.isEmpty()) {
            throw new IllegalArgumentException("extensions must be not empty");
        }
        try {
            return org.apache.commons.io.FileUtils.streamFiles(new File(directoryPath), recursive, extensions);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Stream.empty();
    }


    public static String readFileToString(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("file not found: " + file);
        }
        String string = null;
        try {
            string = org.apache.commons.io.FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error(e.toString(), e);
        }
        return string;
    }


    public static String readFileToString(String filePath, Charset charsets) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("file not found: " + file);
        }
        String string = null;
        try {
            string = org.apache.commons.io.FileUtils.readFileToString(file, charsets);
        } catch (IOException e) {
            log.error(e.toString(), e);
        }
        return string;
    }


    public static byte[] readFileToByteArray(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("file not found: " + file);
        }
        byte[] string = null;
        try {
            string = org.apache.commons.io.FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            log.error(e.toString(), e);
        }
        return string;
    }


    public static void writeStringToFile(String filePath, String string) {
        File file = new File(filePath);
        try {
            org.apache.commons.io.FileUtils.write(file, string, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error(e.toString(), e);
        }
    }

    public static void writeStringToFile(String filePath, String string, boolean append) {
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            org.apache.commons.io.FileUtils.write(file, string, StandardCharsets.UTF_8, append);
        } catch (IOException e) {
            log.error(e.toString(), e);
        }
    }


    public static void deleteDirectory(String directoryPath) {
        File file = new File(directoryPath);
        if (!file.isDirectory()) {
            throw new NotDirectoryException("not directory: " + directoryPath);
        }
        try {
            org.apache.commons.io.FileUtils.deleteDirectory(file);
        } catch (IOException e) {
            log.error(e.toString(), e);
        }
    }

    public static void cleanDirectory(String directoryPath) {
        File file = new File(directoryPath);
        if (!file.isDirectory()) {
            throw new NotDirectoryException("not directory: " + directoryPath);
        }
        try {
            org.apache.commons.io.FileUtils.cleanDirectory(file);
        } catch (IOException e) {
            log.error(e.toString(), e);
        }
    }

    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("file not found: " + filePath);
        }
        try {
            org.apache.commons.io.FileUtils.forceDelete(file);
        } catch (IOException e) {
            log.error(e.toString(), e);
        }
    }

    public static void renameFile(String fileDir, String oldName, String newName) {
        File file = new File(fileDir + "/" + oldName);
        if (!file.exists()) {
            throw new FileNotFoundException("file not found: " + oldName);
        }
        try {
            org.apache.commons.io.FileUtils.moveFile(file, new File(fileDir + "/" + newName));
        } catch (IOException e) {
            log.error(e.toString(), e);
        }
    }

    public static void contentEquals(String fileOne, String fileTwo) {
        File file1 = new File(fileOne);
        File file2 = new File(fileTwo);
        if (!file1.exists() || !file2.exists()) {
            throw new FileNotFoundException("file: " + fileOne + " or file: " + fileTwo + " not found");
        }
        try {
            org.apache.commons.io.FileUtils.contentEquals(file1, file2);
        } catch (IOException e) {
            log.error(e.toString(), e);
        }
    }

    public static void contentEquals(String fileOne, String fileTwo, String charsetName) {
        File file1 = new File(fileOne);
        File file2 = new File(fileTwo);
        if (!file1.exists() || !file2.exists()) {
            throw new FileNotFoundException("file: " + fileOne + " or file: " + fileTwo + " not found");
        }
        try {
            org.apache.commons.io.FileUtils.contentEqualsIgnoreEOL(file1, file2, charsetName);
        } catch (IOException e) {
            log.error(e.toString(), e);
        }
    }

    public static void moveFileToDirectory(String filePath, String newDir) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("file not found: " + filePath);
        }
        try {
            org.apache.commons.io.FileUtils.moveFileToDirectory(file, new File(newDir), true);
        } catch (IOException e) {
            log.error(e.toString(), e);
        }
    }

    public static List<String> readLines(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("file not found: " + file);
        }
        try {
            return org.apache.commons.io.FileUtils.readLines(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public static List<String> readLines(String filePath, Charset charset) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("file not found: " + file);
        }
        try {
            return org.apache.commons.io.FileUtils.readLines(file, charset);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public static void createFile(String filePath) {
        File file = new File(filePath);
        try {
            org.apache.commons.io.FileUtils.touch(file);
        } catch (IOException e) {
            log.error(e.toString(), e);
        }
    }

    public static File urlToFile(URL url) {
        return org.apache.commons.io.FileUtils.toFile(url);
    }

    public static File getTempDirectory() {
        return org.apache.commons.io.FileUtils.getTempDirectory();
    }

    public static File getUserDirectory() {
        return org.apache.commons.io.FileUtils.getUserDirectory();
    }


    public static boolean waitFor(String file, int seconds) {
        return org.apache.commons.io.FileUtils.waitFor(new File(file), seconds);
    }

    public static BigInteger sizeOf(String file) {
        return org.apache.commons.io.FileUtils.sizeOfAsBigInteger(new File(file));
    }

    public static BigInteger sizeOfDirectory(String file) {
        return org.apache.commons.io.FileUtils.sizeOfDirectoryAsBigInteger(new File(file));
    }
}
