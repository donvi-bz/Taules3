package biz.donvi.taules3;

import biz.donvi.taules3.models.GuildModel;
import biz.donvi.taules3.models.GuildUserModel;
import biz.donvi.taules3.models.MessageLogModel;
import biz.donvi.taules3.models.UserModel;
import biz.donvi.taules3.models.query.QGuildUserModel;
import io.ebean.DB;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

import java.util.Calendar;

public class GenericListener {
    private final Taules taules;

    public GenericListener(Taules taules) { this.taules = taules; }

    @SubscribeEvent
    public void onReady(ReadyEvent readyEvent) {
        for (Guild guild : taules.jda.getGuilds()) {
            GuildModel guildModel = new GuildModel(guild.getIdLong(), guild.getName());
            if (DB.find(GuildModel.class, guildModel.getGuild_id()) == null)
                 DB.save(guildModel);
            else DB.update(guildModel);
        }
    }

    @SubscribeEvent
    public void onMessageReceived(GuildMessageReceivedEvent event) {
        final long guildId = event.getGuild().getIdLong();
        final long userId  = event.getAuthor().getIdLong();
        if (event.getAuthor().isBot() || event.getMember() == null) return;
        if (DB.find(UserModel.class, userId) == null) try {
            DB.save(new UserModel(userId, event.getAuthor().getName()));
        } catch (Throwable t) {
            System.out.println(Calendar.getInstance().getTime().toString());
            System.out.println("UserId: " + userId);
            System.out.println("Author Name: " + event.getAuthor().getName());
            t.printStackTrace();
        }
        GuildUserModel gum = new QGuildUserModel()
            .guild.equalTo(guildId)
            .user.equalTo(userId)
            .findOne();
        if (gum == null) {
            DB.save(new GuildUserModel(guildId, event.getMember()));
            gum = new QGuildUserModel()
                .guild.equalTo(guildId)
                .user.equalTo(userId)
                .findOne();
        }
        assert gum != null;
        DB.save(new MessageLogModel(gum.getId()));
    }
}
