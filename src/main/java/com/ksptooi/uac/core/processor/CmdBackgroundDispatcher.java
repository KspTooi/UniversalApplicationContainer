package com.ksptooi.uac.core.processor;

import com.ksptooi.uac.core.entities.CliCommand;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CmdBackgroundDispatcher extends CmdAnnotationDispatcher{

    private final ExecutorService executor = Executors.newCachedThreadPool();


    @Override
    public void publish(CliCommand inVo) {




        super.publish(inVo);

    }
}
