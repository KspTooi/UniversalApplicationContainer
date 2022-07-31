package com.ksptooi.wphub.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.ksptooi.wphub.cli.CommandLine;
import com.ksptooi.wphub.cli.OperateCli;
import com.ksptooi.wphub.command.CommandParser;
import com.ksptooi.wphub.command.InnerCommandParser;
import com.ksptooi.wphub.executor.dispatch.CommandDispatcher;
import com.ksptooi.wphub.executor.dispatch.CommandScheduler;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.XMLMyBatisModule;
import org.mybatis.guice.datasource.druid.DruidDataSourceProvider;

public class ApplicationModule extends AbstractModule {


    @Override
    protected void configure() {



        //install(new ComponentScanModule("com.ksptooi", Comp.class));

        //命令解析器
        bind(CommandParser.class).to(InnerCommandParser.class).in(Scopes.SINGLETON);

        //命令调度器
        bind(CommandScheduler.class).to(CommandDispatcher.class).in(Scopes.SINGLETON);

        //Cli
        bind(CommandLine.class).to(OperateCli.class).in(Scopes.SINGLETON);


        XMLMyBatisModule myBatisModule = new XMLMyBatisModule() {

            @Override
            protected void initialize() {
                setEnvironmentId("prod");
                setClassPathResource("mybatis-config.xml");
            }
        };

        install(myBatisModule);


    }


}