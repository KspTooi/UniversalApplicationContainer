package com.ksptooi.uac.core.cli;

import com.ksptooi.uac.core.annatatiotion.Component;
import com.ksptooi.uac.core.entities.CliCommand;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommandParser{


    public static CliCommand parse(String inCommand) {

        CliCommand pCommand = new CliCommand();

        String commandString = null;

        //预处理
        commandString = inCommand.trim();


        //解析参数
        String[] params = commandString.split(">");

        //无参数
        if(params.length <= 1){
            pCommand.setName(commandString);
            pCommand.setParameter(new ArrayList<>());
            return pCommand;
        }

        List<String> paramList = new ArrayList<>();

        //有参数
        String param = params[1];

        String[] split = param.split(",");

        for(String item:split){

            if(item.trim().equals("")){
                continue;
            }

            paramList.add(item.trim());
        }

        pCommand.setName(params[0]);
        pCommand.setParameter(paramList);
        return pCommand;
    }

}
