package com.docvin.alet.client.utils;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.util.List;

public class CopyUtils {

    /**
     * Returns string of the file's path.
     *
     * @param clipboard Toolkit.getDefaultToolkit().getSystemClipboard()
     * @return
     */
    public static String getCopiedFilePath(Clipboard clipboard) {
        String path = readAsFileList(clipboard);
        return path == null ? readAsString(clipboard) : path;
    }

    private static String readAsFileList(Clipboard clipboard) {
        try {
            Object clip = clipboard.getData(DataFlavor.javaFileListFlavor);
            if (clip instanceof List) {
                List<?> list = (List<?>) clip;
                for (Object item : list)
                    if (item instanceof File)
                        return ((File) item).getAbsolutePath();
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
