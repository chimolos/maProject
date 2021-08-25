package com.second.maproject;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class FileUploadHelper {

    public static boolean deleteFile(String dir) {
        boolean f = false;
        try {
            File file = new File(dir);
            f = file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

//    public boolean uploadFile(MultipartFile file) {
//        boolean fa = false;
//        try {
////            InputStream is = file.getInputStream();
////            byte data[] = new byte[is.available()];
////            is.read(data);
////
////            FileOutputStream fos = new FileOutputStream(UPLOAD_DIR+File.separator+file.getOriginalFilename());
////            fos.write(data);
////
////            fos.flush();
////            fos.close();
//            Files.copy(file.getInputStream(), Paths.get(UPLOAD_DIR+File.separator+file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
//            fa = true;
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return fa;
//    }
}
