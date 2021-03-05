package biz.donvi.taules3.data;

import biz.donvi.taules3.Taules;
import biz.donvi.taules3.data.models.CallLogModel;
import biz.donvi.taules3.data.models.GuildModel;
import biz.donvi.taules3.data.models.GuildUserModel;
import biz.donvi.taules3.data.models.UserModel;
import biz.donvi.taules3.data.models.query.QCallLogModel;
import biz.donvi.taules3.data.models.query.QGuildUserModel;
import io.ebean.DB;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class DataManager {

    private final Taules taules;

    public DataManager(Taules taules) { this.taules = taules; }

    public void updateGuilds(){
        // Updates `guild` table
        for (Guild guild : taules.jda.getGuilds()) {
            GuildModel guildModel = new GuildModel(guild.getIdLong(), guild.getName());
            if (DB.find(GuildModel.class, guildModel.getGuild_id()) == null)
                DB.save(guildModel);
            else DB.update(guildModel);
        }
    }

    public void updateUsers(){
        // Updates `user` table
        for(User user : taules.jda.getUsers()){
            UserModel userM = new UserModel(user);
            if (DB.find(UserModel.class, userM.getUser_id()) == null)
                DB.save(userM);
            else DB.update(userM);
        }
        //  Updates `guild_user` table
        for(Guild guild : taules.jda.getGuilds()) {
            long guildId = guild.getIdLong();
            for (Member member : guild.getMembers()) {
                if (member.getUser().isBot()) continue;
                GuildUserModel gUserM = new GuildUserModel(guildId, member);
                GuildUserModel gum = new QGuildUserModel()
                    .guild.equalTo(guildId)
                    .user.equalTo(member.getIdLong())
                         .findOne();
                if (gum == null)
                    DB.save(gUserM);
                else {
                    gUserM.setId(gum.getId());
                    DB.update(gUserM);
                }
            }
        }
    }

    public boolean addUserIfMissing(User user){
        if (DB.find(UserModel.class, user.getIdLong()) == null) {
            DB.save(new UserModel(user));
            return true;
        } else return false;
    }

    public GuildUserModel findGuildUserModel(long guildId, User user) {
        addUserIfMissing(user);
        return new QGuildUserModel()
            .guild.equalTo(guildId)
            .user.equalTo(user.getIdLong())
                 .findOne();
    }

    public GuildUserModel getGuildUserModel(long guildId, Member user){
        GuildUserModel potential = findGuildUserModel(guildId, user.getUser());
        if (potential != null)
            return potential;
        DB.save(new GuildUserModel(guildId, user));
        return findGuildUserModel(guildId, user.getUser());
    }

    public void updateAllCallRecords() {
        // Every record with no `time_left` (ideally every record corresponds to a person currently in a vc)
        List<CallLogModel> callLogs = new QCallLogModel().time_left.eq(null).findList();
        // For each member in each guild...
        for(Guild guild : taules.jda.getGuilds()) for(Member member : guild.getMembers())
            // If the user IS IN a voice chat
            if (member.getVoiceState() == null || member.getVoiceState().inVoiceChannel()){
                final GuildUserModel guildUser = getGuildUserModel(guild.getIdLong(), member);
                // Make sure we do NOT clear their current record
                boolean removed = callLogs.removeIf(callLogModel -> callLogModel.getGuild_user() == guildUser.getId());
                // If they are in a voice chat and there is no record to be removed, then they are missing their record
                if (!removed) DB.save(new CallLogModel(guildUser.getId()));
            }
        for(CallLogModel callLog : callLogs){
            callLog.setTime_left(callLog.getTime_joined());
            DB.save(callLog);
        }
    }
}
