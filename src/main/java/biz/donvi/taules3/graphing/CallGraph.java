package biz.donvi.taules3.graphing;

import biz.donvi.taules3.data.DataManager;
import biz.donvi.taules3.data.models.CallLogModel;
import io.ebean.DB;
import io.ebean.RawSqlBuilder;

import java.time.LocalDateTime;
import java.util.List;

public class CallGraph extends GenericGuildInfoGraph{

    public CallGraph(int plotFile, int dataFile) {
        super(plotFile, dataFile);
    }

    @Override
    public void generatePlot(DataManager dataManager, String guildName, int averageOver) {
        List<CallLogModel> callLogList = DB.find(CallLogModel.class)
            .setRawSql(
                RawSqlBuilder.parse(
                    "SELECT call_log.id,\n" +
                    "       call_log.guild_user,\n" +
                    "       call_log.time_joined,\n" +
                    "       call_log.time_left\n" +
                    "FROM call_log\n" +
                    "     INNER JOIN guild_user gu ON call_log.guild_user = gu.id\n" +
                    "     INNER JOIN guild g ON gu.guild = g.guild_id\n" +
                    "where g.name = '" + guildName + "';") // Lol sql injection issue here
                .columnMapping("call_log.id","id")
                .columnMapping("call_log.guild_user","guild_user")
                .columnMapping("call_log.time_joined","time_joined")
                .columnMapping("call_log.time_left","time_left")
                .create())
            .findList();
        final int recordCount = 24 * 60;
        double[][] data = new double[recordCount + 1][];
        for (int i = 0; i < data.length; i++)  data[i] = new double[8];

        LocalDateTime dateTime;
        for(CallLogModel callLog : callLogList){
            final int startMinute, endMinute, dayOfWeek;
            // Prep
            dateTime = callLog.getTime_joined().toLocalDateTime();
            startMinute = dateTime.getHour() * 60 + dateTime.getMinute();
            dayOfWeek = dateTime.getDayOfWeek().ordinal() ; // Why must week start on monday?
            if (callLog.getTime_left() != null)
                 dateTime = callLog.getTime_left().toLocalDateTime();
            else dateTime = LocalDateTime.now();
            endMinute = dateTime.getHour() * 60 + dateTime.getMinute();;
            // Point dumping
            for (int minuteInGraph = startMinute; minuteInGraph <= endMinute; minuteInGraph++)
                ++data[minuteInGraph % data.length][(dayOfWeek + (minuteInGraph >= data.length ? 0 : 1)) % 7];

        }
        // Adding average
        for (int i = 0; i < data.length; i++) {
            double sum = 0;
            for(int j = 0; j < 7; j++)
                sum += data[i][j];
            data[i][7] = sum / 7;
        }
        data[recordCount] = data[0];

        double[][] xAxis = new double[recordCount + 1][];
        for (int i = 0; i < xAxis.length; i++) {
            xAxis[i] = new double[]{(double)i/60};
        }
        plotter.writeData(xAxis, data);
        plotter.plot();
    }

    @Override
    public void displayPlot() {

    }
}
