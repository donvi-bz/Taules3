package biz.donvi.taules3.models;

import io.ebean.annotation.Cache;
import io.ebean.annotation.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity @Table(name = "user")
@Data @AllArgsConstructor
public class UserModel {
    @Id @NotNull long   user_id;
    @NotNull     String name;
}
