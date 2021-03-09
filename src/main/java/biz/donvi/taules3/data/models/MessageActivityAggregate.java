package biz.donvi.taules3.data.models;

import io.ebean.RawSql;
import io.ebean.RawSqlBuilder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter @Setter
public class MessageActivityAggregate {
     static final String[] rawSqlForThis = {
        "SELECT Day_Of_Week     AS N_Day_Of_Week,\n" +
        "       Minute_Of_Day   AS N_Minute_Of_Day,\n" +
        "       Message_Count   AS N_Message_Count,\n" +
        "       COUNT(Day_Sent) AS N_Days_Sampled\n" +
        "FROM (SELECT DATE_FORMAT(time_sent, '%w')                                     AS Day_Of_Week,\n" +
        "             DATE_FORMAT(time_sent, '%H') * 60 + DATE_FORMAT(time_sent, '%i') AS Minute_Of_Day,\n" +
        "             COUNT(*)                                                         AS Message_Count,\n" +
        "             DATE_FORMAT(time_sent, '%Y-%m-%d')                               AS Day_Sent\n" +
        "      FROM message_log\n" +
        "           LEFT JOIN guild_user gu ON gu.id = message_log.guild_user\n" +
        "           LEFT JOIN guild g ON gu.guild = g.guild_id\n",
//        "      WHERE g.name = 'Pineapple Land'\n" +
        "      GROUP BY Day_Of_Week, Minute_Of_Day) AS innerData\n" +
        "GROUP BY innerData.Day_Of_Week, innerData.Minute_Of_Day, innerData.Message_Count;"};

    public static RawSql getRawSQL(String guildName){
        return RawSqlBuilder
            .parse(rawSqlForThis[0] + "WHERE g.name = '" + guildName + "'\n" + rawSqlForThis[1])
            .columnMapping("N_Day_Of_Week","dayOfWeek")
            .columnMapping("N_Minute_Of_Day","timeOfDay")
            .columnMapping("N_Message_Count","messageCount")
            .columnMapping("N_Days_Sampled","daysSampled")
            .create();
    }

    int    dayOfWeek;
    int    timeOfDay;
    int    messageCount;
    int    daysSampled;

}
