package biz.donvi.taules3;


import biz.donvi.taules3.data.DataManager;
import biz.donvi.taules3.graphing.ClInput;
import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static biz.donvi.taules3.util.Util.getResourceFileAsString;

public class Taules {

    public final JDA                      jda;
    public final DataManager              dataManager = new DataManager(this);
    public final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void main(String[] args) throws LoginException, IOException { new Taules(); }

    private Taules() throws LoginException, IOException {
        GenericListener gl = new GenericListener(this);
        scheduler.scheduleAtFixedRate(() -> {if (!keepRunning()) scheduler.shutdown();}, 1, 1, TimeUnit.MINUTES);
        scheduler.scheduleAtFixedRate(gl::periodicUpdate, 2, 57, TimeUnit.SECONDS);
//
        initDb();
        jda = JDABuilder
            .createDefault(getResourceFileAsString("token"))
            .setEventManager(new AnnotatedEventManager())
            .addEventListeners(gl)
            .build();
        new ClInput(this).inputLoop();
        scheduler.shutdown();
        System.out.println("Taules down.");
    }

    private void initDb() {
        String connDetails = null;
        String[] fillerArray = new String[]{"none", "none", "none"};
        try {
            connDetails = getResourceFileAsString("connectionDetails.txt");
        } catch (IOException e) { e.printStackTrace(); }
        final String[] CONNECTION_DETAILS = connDetails == null ? fillerArray : connDetails.split(" ");
        // datasource
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl(CONNECTION_DETAILS[0]);
        dataSourceConfig.setUsername(CONNECTION_DETAILS[1]);
        dataSourceConfig.setPassword(CONNECTION_DETAILS[2]);

        // configuration ...
        DatabaseConfig config = new DatabaseConfig();
        config.setDataSourceConfig(dataSourceConfig);

        // create database instance
        Database database = DatabaseFactory.create(config);
    }

    public boolean keepRunning(){
        return jda != null && jda.getStatus() != JDA.Status.SHUTDOWN;
    }

}
