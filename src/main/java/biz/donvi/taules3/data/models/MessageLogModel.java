package biz.donvi.taules3.data.models;

import io.ebean.annotation.NotNull;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Calendar;

@Entity @Table(name = "message_log")
@Data
public class MessageLogModel {
    @Id @NotNull int       id;
    @NotNull     int       guild_user;
    @NotNull     Timestamp time_sent;

    public MessageLogModel() { }

    public MessageLogModel(int guild_user) {
        this.guild_user = guild_user;
        time_sent = new Timestamp(Calendar.getInstance().getTime().getTime());
    }
}
