package cc.stbl.token.innerdisc.restapi.admin.centerManager;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 文件处理类
 * @author caojinbo
 */
public class FileUtil {
    /**
     * 上传文件
     * @param file
     * @param filePath
     * @param fileName
     * @throws Exception
     */
    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath+fileName);
        out.write(file);
        out.flush();
        out.close();
    }

    /**
     * 根据文件路径，删除文件
     * @param fullpath 文件的完整路径
     * @return 操作结果
     */
    public static String deleteFileByPath(String fullpath) {
        File dFile = new File(fullpath);
        String msg = "";
        if(dFile.exists()) {
            boolean dflag = dFile.delete();
            if (dflag) {
                msg = "已经成功删除文件，文件完整路径是：" + fullpath;
            } else {
                msg = "删除文件失败，文件路径是：" + fullpath;
            }
        } else {
            msg = "删除文件失败，文件路径：" + fullpath +",对应的文件不存在";
        }
        return msg;
    }
    public static void main(String[] args) {
        System.out.println(FileUtil.deleteFileByPath("/mnt/public/20181003/6e3f97c0e7f740448eb6d10d99bbdfa6/5827b5e5629a4acaacff88ac971d6605.jpg") );
        String aaa = "xxxx/ssss/";
        System.out.println(aaa.substring(0, aaa.length()-1));


    }
}
