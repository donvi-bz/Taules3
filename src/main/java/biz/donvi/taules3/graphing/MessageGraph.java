package biz.donvi.taules3.graphing;

import biz.donvi.taules3.data.DataManager;
import biz.donvi.taules3.data.models.MessageActivityAggregate;
import io.ebean.DB;
import io.ebean.Query;

import java.util.List;

public class MessageGraph extends GenericGuildInfoGraph {

    public MessageGraph(int plotFile, int dataFile) {
        super(plotFile, "messagePlot.sh", dataFile, "messageData.txt");
    }

    @Override
    public void generatePlot(DataManager dataManager, String guildName, int averageOver) {
        Query<MessageActivityAggregate> query = DB.find(MessageActivityAggregate.class);
        query.setRawSql(MessageActivityAggregate.getRawSQL(guildName));
        List<MessageActivityAggregate> items = query.findList();
        // We need one point for every minute of the day plus one extra
        final int recordCount = 24 * 60;
        double[][] data = new double[recordCount + 1][];
        // Eight data points, one for each day of the week + one for average
        for (int i = 0; i < data.length; i++)  data[i] = new double[8];

        final int leftHalf = averageOver / 2;
        final int rightHalf  = averageOver - leftHalf;
        double[] smoothingCurve = getRoundingCurve(averageOver);

        for(MessageActivityAggregate ma : items) {
            int dayOfWeek = ma.getDayOfWeek();
            int minute = ma.getTimeOfDay();
            double messageAvgAtTime = (double) ma.getMessageCount() / ma.getDaysSampled();
            for (int i = minute - leftHalf, j = 0; i < minute + rightHalf; i++, j++) {
                try {
                    int plotDay = dayOfWeek + 7;
                    if (i >= recordCount) plotDay++;
                    else if (i < 0) plotDay--;
                    plotDay %= 7;
                    data[(i + recordCount) % recordCount][plotDay] += 60 * messageAvgAtTime * smoothingCurve[j];
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        // Better looking wrap around
        for(int i = 0; i < 6; i++)
            data[recordCount][i] = data[0][(i + 8) % 7];

        // Adding the averaging
        for (int i = 0; i < data.length; i++) {
            double sum = 0;
            for(int j = 0; j < 7; j++)
                sum += data[i][j];
            data[i][7] = sum / 7;
        }

        double[][] xAxis = new double[recordCount + 1][];
        for (int i = 0; i < xAxis.length; i++) {
            xAxis[i] = new double[]{(double)i/60};
        }
        plotter.writeData(xAxis, data);
        plotter.plot(true);
    }

    @Override
    public void displayPlot() {
        plotter.plot();
    }

}
