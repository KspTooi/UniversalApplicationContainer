package com.ksptooi.uac.extendsbuildin.service;

import com.alibaba.fastjson.JSON;
import com.google.inject.Inject;
import com.ksptooi.uac.commons.CommandLineTable;
import com.ksptooi.uac.commons.FileUtils;
import com.ksptooi.uac.core.entities.Command;
import com.ksptooi.uac.core.entities.Document;
import com.ksptooi.uac.core.service.CommandService;
import com.ksptooi.uac.core.service.DocumentService;
import com.ksptooi.uac.extendsbuildin.entities.ApplicationData;
import com.ksptooi.uac.extendsbuildin.enums.BuildIn;
import com.ksptooi.uac.extendsbuildin.enums.DocumentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppLibraryService {

    private final Logger logger = LoggerFactory.getLogger(AppLibraryService.class);

    @Inject
    private DocumentService documentService;

    @Inject
    private CommandService commandService;

    @Inject
    private ApplicationService applicationService;

    public void showAppLibraryList(){

        List<Document> documentByType = documentService.getDocumentByType(DocumentType.APP_LIB.getName());

        CommandLineTable cliTable = new CommandLineTable();
        cliTable.setShowVerticalLines(true);

        cliTable.setHeaders("Name","Metadata");

        for(Document item : documentByType){
            cliTable.addRow(item.getName(),item.getMetadata());
        }

        cliTable.print();
    }

    public void getMissingApp(){

        //获取lib
        List<Document> libs = documentService.getDocumentByType(DocumentType.APP_LIB.getName());

        List<Command> apps = commandService.getCommandByProcessorName(BuildIn.APP_RUNNER.getProcessorName());

        Map<String,Command> missingApps = new HashMap<>();

        //获取不存在的app
        for(Command item:apps){

            ApplicationData data = JSON.parseObject(item.getMetadata(), ApplicationData.class);
            boolean exists = Files.exists(Paths.get(data.getPath()));

            if(!exists){
                missingApps.put(data.getFileName(),item);
            }
        }

        logger.info("当前已安装应用:{} 缺失的应用:{}",apps.size() - missingApps.size(),missingApps.size());

        for(Document lib:libs){

            String path = JSON.parseObject(lib.getMetadata()).getString("path");

            boolean exists = Files.exists(Paths.get(path));

            if(!exists){
                logger.info("应用库:{}:{}不存在!",lib.getName(),path);
                continue;
            }

            //在AppLib中查找缺失的app
            for(Map.Entry<String,Command> item: missingApps.entrySet()){

                Command app = item.getValue();
                ApplicationData appData = JSON.parseObject(app.getMetadata(), ApplicationData.class);

                if(appData.isDirectory()){
                    continue;
                }

                List<File> files = FileUtils.searchFileInDir(path, appData.getFileName());

                if(files.size() > 0){
                    logger.info("已查询到应用:{}->{}", app.getName(),files.get(0).getPath());
                    applicationService.appRemove(app.getName());
                    applicationService.appInstall(app.getName(),files.get(0).getPath());
                    missingApps.remove(item.getKey());
                }

            }

        }

        //打印查找失败的应用
        CommandLineTable cliTable = new CommandLineTable();
        cliTable.setShowVerticalLines(true);
        cliTable.setHeaders("name","path","status");


        for(Map.Entry<String,Command> item: missingApps.entrySet()){
            Command app = item.getValue();
            ApplicationData appData = JSON.parseObject(app.getMetadata(), ApplicationData.class);
            cliTable.addRow(item.getKey(),appData.getPath(),"missing");
        }

        if(missingApps.size()>0){
            logger.info("以下应用查找失败!");
            cliTable.print();
        }


    }


}
