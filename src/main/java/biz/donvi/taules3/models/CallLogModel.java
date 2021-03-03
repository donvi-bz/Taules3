package biz.donvi.taules3.models;

import io.ebean.annotation.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity @Table(name = "call_log")
@Data @AllArgsConstructor
public class CallLogModel {
    @Id @NotNull int       id;
    @NotNull     int       guild_user;
    @NotNull     Timestamp time_joined;
    Timestamp time_left;
}
