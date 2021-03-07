package biz.donvi.taules3.data.models;

import io.ebean.RawSql;
import io.ebean.RawSqlBuilder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter @Setter
public class MessageAggregate {
    public static final String rawSqlForThis =
        "SELECT Day_Of_Week     AS N_Day_Of_Week,\n" +
        "       Hour_Of_Day     AS N_Hour_Of_Day,\n" +
        "       Message_Count   AS N_Message_Count,\n" +
        "       COUNT(Day_Sent) AS N_Days_Sampled\n" +
        "FROM (SELECT DATE_FORMAT(time_sent, '%Y-%m-%d') AS Day_Sent,\n" +
        "             DATE_FORMAT(time_sent, '%w')       AS Day_Of_Week,\n" +
        "             DATE_FORMAT(time_sent, '%H')       AS Hour_Of_Day,\n" +
        "             COUNT(*)                           AS Message_Count\n" +
        "      FROM message_log\n" +
        "           LEFT JOIN guild_user gu ON gu.id = message_log.guild_user\n" +
        "           LEFT JOIN guild g ON gu.guild = g.guild_id\n" +
        "      WHERE g.name = 'Pineapple Land'\n" +
        "      GROUP BY DATE_FORMAT(time_sent, '%w'), DATE_FORMAT(time_sent, '%H')) AS innerData\n" +
        "GROUP BY innerData.Day_Of_Week, innerData.Hour_Of_Day, innerData.Message_Count, innerData.Message_Count;";

    public static RawSql getRawSQL(){
        return RawSqlBuilder
            .parse(rawSqlForThis)
            .columnMapping("N_Day_Of_Week","dayOfWeek")
            .columnMapping("N_Hour_Of_Day","hourOfDay")
            .columnMapping("N_Message_Count","messageCount")
            .columnMapping("N_Days_Sampled","daysSampled")
            .create();
    }

    int dayOfWeek;
    int hourOfDay;
    int messageCount;
    int daysSampled;

}
