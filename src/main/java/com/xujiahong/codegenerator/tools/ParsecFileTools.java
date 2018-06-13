package com.xujiahong.codegenerator.tools;

import java.io.*;

/**
 * Created by xujiahong on 2016/11/4.
 * ======================功能列表======================
 */
public class ParsecFileTools {

    /**
     * 将 StringBuffer 写入文件
     * @param path
     * @param content
     * @throws IOException
     */
    public static void writeFile(String path, StringBuffer content){
        File f = new File(path);
        if (f.getParentFile() != null && !f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        OutputStream opt = null;
        try {
            opt = new FileOutputStream(f);
            String contentStr = content.toString();
            if (contentStr != null) {
                opt.write(contentStr.getBytes());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                opt.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
