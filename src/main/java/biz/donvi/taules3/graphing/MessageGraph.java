package biz.donvi.taules3.graphing;

import biz.donvi.gnuPlotter.Plotter;
import biz.donvi.taules3.data.DataManager;
import biz.donvi.taules3.data.models.MessageAggregate;
import io.ebean.DB;
import io.ebean.Query;
import io.ebean.RawSql;
import io.ebean.RawSqlBuilder;

import java.util.List;

public class MessageGraph {

    static final String plotLocation = "/plot/";

    Plotter plotter;

    public MessageGraph(int plotFile, int dataFile) {
        plotter = new Plotter();
    }

    public void generatePlot(DataManager dataManager) {
        Query<MessageAggregate> query = DB.find(MessageAggregate.class);
        query.setRawSql(MessageAggregate.getRawSQL());
        List<MessageAggregate> items = query.findList();
        double[][] data = new double[24 + 1][];
        for (int i = 0; i < data.length; i++) {
            data[i] = new double[8];
        }
        for(MessageAggregate ma : items) {
            data[ma.getHourOfDay()][ma.getDayOfWeek()] = (double) ma.getMessageCount() / ma.getDaysSampled();
        }
        for (int i = 0; i < data.length; i++) {
            double sum = 0;
            for(int j = 0; j < 7; j++)
                sum += data[i][j];
            data[i][7] = sum / 7;
        }
        data[24] = data[0];

        plotter.writeData(data);
        plotter.plot();
    }

    public void displayPlot() {
        plotter.plot(true);
    }


}
