package biz.donvi.taules3.data.models;

import io.ebean.annotation.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.dv8tion.jda.api.entities.User;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity @Table(name = "user")
@Data @AllArgsConstructor
public class UserModel {
    @Id @NotNull long   user_id;
    @NotNull     String name;
                 Short  num;

    public UserModel() { }

    public UserModel(User user) {
        this(user.getIdLong(), user.getName(), Short.parseShort(user.getDiscriminator()));
    }
}
