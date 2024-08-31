package com.electronic.store.ElectronicStore_webapp.services.impl;

import com.electronic.store.ElectronicStore_webapp.exceptions.BadApiRequestException;
import com.electronic.store.ElectronicStore_webapp.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private Logger logger= LoggerFactory.getLogger(FileServiceImpl.class);
    @Override
    public String uploadFile(MultipartFile multipartFile, String path) throws IOException {

        String originalFileName = multipartFile.getOriginalFilename();
        logger.info("Original File name: {}", originalFileName);

        String fileName = UUID.randomUUID().toString();
        logger.info("file name without extension: {}", fileName);

        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileNameWithExtension = fileName + extension;
        logger.info("file name with extension: {}", fileNameWithExtension);

        String fullPathFileName = path +fileNameWithExtension;

        if(extension.equalsIgnoreCase(".png") ||
                extension.equalsIgnoreCase(".jpg")
                || extension.equalsIgnoreCase(".jpeg")){

            //save your file
            File folder = new File(path);
            if(!folder.exists()){
                //create folder
                folder.mkdirs();
            }

            //upload on the path
            Files.copy(multipartFile.getInputStream(), Paths.get(fullPathFileName));

        }else {
            throw new BadApiRequestException("File with this extension: "+ extension + " not allowed");
        }

        return fileNameWithExtension;
    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
        String fullPath = path + name;
        InputStream inputStream = new FileInputStream(fullPath);

        return inputStream;
    }
}
