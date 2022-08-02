package com.ksptooi.asf.extendsbuildin.processor;

import com.google.gson.Gson;
import com.ksptooi.asf.core.annatatiotion.Processor;
import com.ksptooi.asf.core.entities.Command;
import com.ksptooi.asf.core.entities.CliCommand;
import com.ksptooi.asf.core.processor.AbstractProcessor;
import com.ksptooi.asf.extendsbuildin.entities.SoftwarePack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Processor
public class PackRunnerProcessor extends AbstractProcessor {

    private final Logger logger = LoggerFactory.getLogger(PackRunnerProcessor.class);

    @Override
    public void onCommand(CliCommand preparedCommand, Command command) {

        logger.info("运行软件包:" + preparedCommand);

        SoftwarePack softwarePack = new Gson().fromJson(command.getMetadata(), SoftwarePack.class);

        try {

            Runtime.getRuntime().exec("explorer " + softwarePack.getPath());

        } catch (IOException e) {
            e.printStackTrace();
            logger.info("运行软件包失败:" + preparedCommand.getName());
        }

    }

}
