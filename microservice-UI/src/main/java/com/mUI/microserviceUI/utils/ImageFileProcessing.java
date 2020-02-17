package com.mUI.microserviceUI.utils;


import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>Class processes different methods for imageFiles</p>
 */
public class ImageFileProcessing {
    /**
     * <p>Method gets image file from add-form</p>
     * @param file the file from the add-form
     * @return bytes of file
     */
    public static byte[] getImageForEntityAddFromForm(MultipartFile file){
        byte[] bytes = null;
        if(!file.isEmpty()){
            try{
                bytes = file.getBytes();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return bytes;
    }


    /**
     * <p>Method gets image file from form</p>
     * @param file the file from the form
     * @return bytes of file
     */
    public static byte[] getImageForEntityEditFromForm(MultipartFile file){
        byte[] bytes = null;

        try{
            bytes = file.getBytes();
        }catch(IOException e){
            e.printStackTrace();
        }

        return bytes;
    }


    /**
     * <p>Method checks if the file has defined image format (BMP, GIF, JPG and PNG)</p>
     * @param file from form
     * @return false if wrong format, true if valid
     */
    public static boolean checkIfImageIsRightFormat(MultipartFile file){
        boolean format = false;
            try (InputStream input = file.getInputStream()) {
                try {
                    ImageIO.read(input).toString();
                    // It's an image (only BMP, GIF, JPG and PNG are recognized).
                    format = true;
                } catch (Exception e) {
                    format = false;
                }
            } catch (IOException e) {
                format = false;
            }
        return format;
    }

    /**
     * <p>Method checks if the file isn't bigger than 5mb</p>
     * @param file from form
     * @return false if bigger, true if valid
     */
    public static boolean checkIfImageSizeOk(MultipartFile file){
        boolean size = false;
        if(file.getSize() < 5 * 1024 * 1024){
            size = true;
        }
        return size;
    }


}
