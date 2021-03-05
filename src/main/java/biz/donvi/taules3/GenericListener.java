package biz.donvi.taules3;

import biz.donvi.taules3.data.models.CallLogModel;
import biz.donvi.taules3.data.models.GuildUserModel;
import biz.donvi.taules3.data.models.MessageLogModel;
import biz.donvi.taules3.data.models.UserModel;
import biz.donvi.taules3.data.models.query.QCallLogModel;
import biz.donvi.taules3.data.models.query.QGuildUserModel;
import io.ebean.DB;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

import java.security.Timestamp;
import java.util.Calendar;
import java.util.List;

public class GenericListener {
    private final Taules taules;

    public GenericListener(Taules taules) { this.taules = taules; }

    @SubscribeEvent
    public void onReady(ReadyEvent readyEvent) {
        taules.dataManager.updateGuilds();
        taules.dataManager.updateAllCallRecords();
    }

    public void periodicUpdate(){
        taules.dataManager.updateGuilds();
        taules.dataManager.updateUsers();
    }

    @SubscribeEvent
    public void onMessageReceived(GuildMessageReceivedEvent event) {
        // If we get a bot or a non-member, dip
        if (event.getAuthor().isBot() || event.getMember() == null) return;
        // Logic
        GuildUserModel guildUser = taules.dataManager.getGuildUserModel(event.getGuild().getIdLong(), event.getMember());
        DB.save(new MessageLogModel(guildUser.getId()));
    }

    @SubscribeEvent
    public void onVoiceJoined(GuildVoiceJoinEvent event) {
        // If we get a bot, dip
        if (event.getMember().getUser().isBot()) return;
        // Logic
        GuildUserModel guildUser = taules.dataManager.getGuildUserModel(event.getGuild().getIdLong(), event.getMember());
        DB.save(new CallLogModel(guildUser.getId()));
    }

    @SubscribeEvent
    public void onVoiceLeft(GuildVoiceLeaveEvent event) {
        // If we get a bot, dip
        if (event.getMember().getUser().isBot()) return;
        // Logic
        GuildUserModel guildUser = taules.dataManager.getGuildUserModel(event.getGuild().getIdLong(), event.getMember());
        List<CallLogModel> callLogs = new QCallLogModel()
            .guild_user.eq(guildUser.getId())
            .time_left.eq(null)
            .order("time_joined DESC")
            .findList();
        if (callLogs.size() >= 1) {
            callLogs.get(0).setTime_left(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
            DB.save(callLogs.get(0));
            if (callLogs.size() > 1){
                for(int i = 1; i < callLogs.size(); i++){
                    callLogs.get(i).setTime_left(callLogs.get(i).getTime_joined());
                    DB.save(callLogs.get(i));
                }
            }
        }

    }
}
