package biz.donvi.taules3.models;

import io.ebean.annotation.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity @Table(name = "guild")
@Data @AllArgsConstructor
public class GuildModel {
    @Id @NotNull long   guild_id;
    @NotNull     String name;
}
