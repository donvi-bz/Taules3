package biz.donvi.taules3.data.models;

import io.ebean.annotation.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity @Table(name = "bot_life")
@Data @NoArgsConstructor @AllArgsConstructor
public class BotLifeModel {
    @Id @NotNull Timestamp timeLogged;
}
