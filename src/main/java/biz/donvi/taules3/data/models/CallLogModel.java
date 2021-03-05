package biz.donvi.taules3.data.models;

import io.ebean.annotation.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Calendar;

@Entity @Table(name = "call_log")
@Data
public class CallLogModel {
    @Id @NotNull int       id;
    @NotNull     int       guild_user;
    @NotNull     Timestamp time_joined;
                 Timestamp time_left;

    public CallLogModel() {
        time_joined = new Timestamp(Calendar.getInstance().getTime().getTime());
    }

    public CallLogModel(int guildUserId) {
        guild_user = guildUserId;
        time_joined = new Timestamp(Calendar.getInstance().getTime().getTime());
    }
}
