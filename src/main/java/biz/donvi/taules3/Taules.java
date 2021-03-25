package biz.donvi.taules3;


import biz.donvi.taules3.data.DataManager;
import biz.donvi.taules3.data.models.BotLifeModel;
import biz.donvi.taules3.util.Util;
import io.ebean.DB;
import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.Scanner;
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
        scheduler.scheduleAtFixedRate(this::maybeShutDown,     5,  15, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(gl::periodicUpdate,      2,  7,  TimeUnit.MINUTES);
        scheduler.scheduleAtFixedRate(Taules::addLivingRecord, 9,  59, TimeUnit.MINUTES);
        initDb();
        jda = JDABuilder
            .createDefault(getResourceFileAsString("token"))
            .setEventManager(new AnnotatedEventManager())
            .addEventListeners(gl)
            .build();

//        new ClInput(this).inputLoop();
        inputLoop();
        System.out.println("The bot is down, shutting down the scheduler.");
        scheduler.shutdown();
        System.out.println("Taules down.");
    }

    private static void addLivingRecord() {DB.save(new BotLifeModel(Util.timeStampNow()));}

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

    private void maybeShutDown() {if (!keepRunning()) scheduler.shutdown();}

    public void inputLoop() {
        Scanner in = new Scanner(System.in);
        while (keepRunning()) try {
            String input = in.nextLine();
            if (input.equalsIgnoreCase("q")) {
                System.out.println("Received shutdown request.");
                jda.shutdown();
            } else System.out.println(input); // DEBUG LINE
        } catch (Throwable t) {
            t.printStackTrace();
        }
        in.close();
    }
}
