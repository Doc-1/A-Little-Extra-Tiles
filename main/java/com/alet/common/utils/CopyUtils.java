package com.alet.common.utils;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.util.List;

public class CopyUtils {
    
    /** Returns string of the file's path.
     * 
     * @param clipboard
     *            Toolkit.getDefaultToolkit().getSystemClipboard()
     * @return */
    public static String getCopiedFilePath(Clipboard clipboard) {
        String path = readAsFileList(clipboard);
        return path == null ? readAsString(clipboard) : path;
    }
    
    private static String readAsFileList(Clipboard clipboard) {
        try {
            List<File> paths = (List<File>) clipboard.getData(DataFlavor.javaFileListFlavor);
            for (File path : paths) {
                if (path != null) {
                    return path.getAbsolutePath();
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    private static String readAsString(Clipboard clipboard) {
        try {
            return (String) clipboard.getData(DataFlavor.stringFlavor);
        } catch (Exception e) {
            return null;
        }
    }
}
