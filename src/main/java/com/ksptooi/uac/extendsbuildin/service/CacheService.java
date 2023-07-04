package com.ksptooi.uac.extendsbuildin.service;


import com.google.gson.Gson;
import com.ksptooi.uac.commons.CliProgressBar;
import com.ksptooi.uac.commons.ZipCompress;
import com.ksptooi.uac.core.entities.Document;
import com.ksptooi.uac.extendsbuildin.entities.cache.CacheMetadata;
import com.ksptooi.uac.extendsbuildin.processor.CacheProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.zip.ZipOutputStream;

public class CacheService {

    private final Logger logger = LoggerFactory.getLogger(CacheService.class);


    /**
     * 将文件夹压缩
     */
    public ByteArrayOutputStream compressDirectory(Path path,Document document){


        return null;
    }



    /**
     * 将文件读入Document
     * @param path 文件路径
     * @param document doc
     * @return 成功返回true 失败返回false
     */
    public boolean readToDocument(Path path, Document document){

        if(Files.isDirectory(path)){

            ZipCompress compress = new ZipCompress(path);

            logger.info("将文件夹转换为二进制数据.");

            try{
                document.setBinaryData(compress.compress());
            }catch (Exception e){
                return false;
            }

            //创建metadata
            CacheMetadata metadata = new CacheMetadata();
            metadata.setFileName(path.getFileName().toString());
            metadata.setPath(path.toString());
            metadata.setLength((long) document.getBinaryData().length);
            metadata.setDirectory(true);
            document.setMetadata(new Gson().toJson(metadata));

            return true;
        }

        try {

            InputStream is = Files.newInputStream(path);

            byte[] read = new byte[1024*512];

            long count = 0L;

            long size = Files.size(path);

            while (true){

                int len = is.read(read);

                if(len < 1){
                    break;
                }
                count = count + len;

                CliProgressBar.updateProgressBar("处理中",count/1024/1024,size/1024/1024);
                document.appendBinaryData(read,len);
            }

            System.out.print("\r\n");

            //创建metadata
            CacheMetadata metadata = new CacheMetadata();
            metadata.setFileName(path.getFileName().toString());
            metadata.setPath(path.toString());
            metadata.setLength(size);
            metadata.setDirectory(false);
            document.setMetadata(new Gson().toJson(metadata));

            is.close();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }



    public void cache(String path){



    }

}