package biz.donvi.taules3;


import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;

import javax.security.auth.login.LoginException;
import java.io.IOException;

import static biz.donvi.taules3.Util.getResourceFileAsString;

public class Taules {

    public final JDA jda;
//    public final ScheduledExecutorService scheduler   = Executors.newScheduledThreadPool(1);

    public static void main(String[] args) throws LoginException, IOException { new Taules(); }

    private Taules() throws LoginException, IOException {
//        scheduler.scheduleAtFixedRate(this::maybeDisableScheduler, 1, 1, TimeUnit.MINUTES);
//        scheduler.scheduleAtFixedRate(dataManager::update, 5, 15, TimeUnit.SECONDS);
//
        initDb();
        jda = JDABuilder
            .createDefault(getResourceFileAsString("token"))
            .setEventManager(new AnnotatedEventManager())
            .addEventListeners(new GenericListener(this))
            .build();
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

    private void maybeDisableScheduler() {
//        if (jda == null || jda.getStatus() == JDA.Status.SHUTDOWN) scheduler.shutdown();
    }
}
