package biz.donvi.taules3.data.models;

import io.ebean.annotation.NotNull;
import lombok.Data;
import net.dv8tion.jda.api.entities.Member;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity @Table(name = "guild_user")
@Data
public class GuildUserModel {
    @Id @NotNull int    id;
    @NotNull     long   guild;
    @NotNull     long   user;
    @NotNull     String display_name;
                 String last_color;

    public GuildUserModel() { }

    public GuildUserModel(long guild, long user, String display_name, String last_color) {
        this.guild = guild;
        this.user = user;
        this.display_name = display_name;
        this.last_color = last_color;
    }

    public GuildUserModel(long guildId, Member guildMember) {
        this(guildId,
             guildMember.getIdLong(),
             guildMember.getEffectiveName(),
             guildMember.getColor() != null
                 ? Integer.toHexString(guildMember.getColor().getRGB())
                 : "00000000");
    }
}
