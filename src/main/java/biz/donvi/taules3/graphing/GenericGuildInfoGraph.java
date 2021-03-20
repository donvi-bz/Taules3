//package biz.donvi.taules3.graphing;
//
//import biz.donvi.gnuPlotter.Plotter;
//import biz.donvi.taules3.data.DataManager;
//
//public abstract class GenericGuildInfoGraph {
//
//    static String plottingRoot = "plot/";
//
//    Plotter plotter;
//
//    public GenericGuildInfoGraph(int plotFileNum, String plotFileName, int dataFileNum, String dataFileName) {
//        plotter = new Plotter()
//            .usePlotFile(plottingRoot + plotFileName, plotFileNum)
//            .useDataFile(plottingRoot + dataFileName, dataFileNum);
//    }
//
//    public abstract void generatePlot(DataManager dataManager, String guildName, int averageOver);
//
//    public abstract void displayPlot();
//
//    public static double[] getRoundingCurve(int size) {
//        double[] curve = new double[size];
//        final int start = size / 2;
//        for (int i = start; i < size; i++) {
//            double pos = ((double) start - i) / start;
//            curve[i] = curve[size - i - 1] = (Math.cos(Math.PI * pos) + 1) / size;
//        }
//        double sum = 0;
//        for (double d : curve) sum += d;
//        double inv = 1 / sum;
//        for (int i = 0, curveLength = curve.length; i < curveLength; i++)
//            curve[i] *= inv;
//        return curve;
//    }
//}
