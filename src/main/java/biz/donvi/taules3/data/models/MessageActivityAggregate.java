package biz.donvi.taules3.data.models;

import io.ebean.RawSql;
import io.ebean.RawSqlBuilder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter @Setter
public class MessageActivityAggregate {
     static final String rawSqlForThis =
        "SELECT Msg_Count_Info.Day_Of_Week    AS N_Day_Of_Week,\n" +
        "       Msg_Count_Info.Minute_Of_Day  AS N_Minute_Of_Day,\n" +
        "       Msg_Count_Info.Message_Count  AS N_Message_Count,\n" +
//        "       Msg_Count_Info.Day_Sent       AS N_Day_Sent,\n" +
        "       Dis_Days.Distinct_Appearences AS N_Days_Sampled\n" +
        "FROM (SELECT DATE_FORMAT(time_sent, '%w')                                     AS Day_Of_Week,\n" +
        "             DATE_FORMAT(time_sent, '%H') * 60 + DATE_FORMAT(time_sent, '%i') AS Minute_Of_Day,\n" +
        "             COUNT(*)                                                         AS Message_Count,\n" +
        "             DATE_FORMAT(time_sent, '%Y-%m-%d')                               AS Day_Sent\n" +
        "      FROM message_log\n" +
        "           LEFT JOIN guild_user gu ON gu.id = message_log.guild_user\n" +
        "           LEFT JOIN guild g ON gu.guild = g.guild_id\n" +
        "      WHERE g.name = '{{FILLER}}'\n" +
        "      GROUP BY Day_Of_Week, Minute_Of_Day) AS Msg_Count_Info\n" +
        "   NATURAL JOIN\n" +
        "     (SELECT DATE_FORMAT(time_sent, '%w')                       AS Day_Of_Week,\n" +
        "             COUNT(DISTINCT DATE_FORMAT(time_sent, '%Y-%m-%d')) AS Distinct_Appearences\n" +
        "      FROM message_log\n" +
        "           LEFT JOIN guild_user gu ON gu.id = message_log.guild_user\n" +
        "           LEFT JOIN guild g ON gu.guild = g.guild_id\n" +
        "      WHERE g.name = '{{FILLER}}'\n" +
        "      GROUP BY Day_Of_Week) AS Dis_Days";

    public static RawSql getRawSQL(String guildName){
        return RawSqlBuilder
            .parse(rawSqlForThis.replaceAll("\\{\\{FILLER}}", guildName))
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
