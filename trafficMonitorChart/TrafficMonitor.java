package main.trafficMonitorChart;

/**
 * Created by lenovo on 2016-01-10.
 */

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class TrafficMonitor extends ChartPanel implements Runnable{
    private static TimeSeries timeSeries;
    private ComputeTraffic computeTraffic;

    public TrafficMonitor(String chartContent, String title, String yaxisName){
        super(createChart(chartContent,title,yaxisName));
        this.computeTraffic = new ComputeTraffic();
    }

    public ComputeTraffic getComputeTraffic() {
        return computeTraffic;
    }

    private static JFreeChart createChart(String chartContent,String title,String yaxisName){
        //创建主题样式
        StandardChartTheme chartTheme = new StandardChartTheme("CN");
        //设置标题字体
        chartTheme.setExtraLargeFont(new Font("Microsoft Yahei", Font.BOLD, 18));
        //设置轴向字体
        chartTheme.setLargeFont(new Font("Microsoft Yahei", Font.CENTER_BASELINE, 14));
        //设置图例字体
        chartTheme.setRegularFont(new Font("Microsoft Yahei", Font.CENTER_BASELINE, 14));
        //应用主题样式
        ChartFactory.setChartTheme(chartTheme);

        //创建时序图对象
        timeSeries = new TimeSeries(chartContent);
        TimeSeriesCollection timeseriescollection = new TimeSeriesCollection(timeSeries);
        JFreeChart jfreechart = ChartFactory.createTimeSeriesChart(title,"",yaxisName,
                timeseriescollection,true,true,false);
        XYPlot xyplot = jfreechart.getXYPlot();
        //纵坐标设定
        ValueAxis valueaxis = xyplot.getDomainAxis();
        //自动设置数据轴数据范围
        valueaxis.setAutoRange(true);
        //数据轴固定数据范围 30s
        valueaxis.setFixedAutoRange(30000D);

        xyplot.getRangeAxis();

        ValueAxis valueAxis = jfreechart.getXYPlot().getDomainAxis();
        valueAxis.setLowerMargin(0);        //设置分类轴左边距
        valueAxis.setUpperMargin(0);         //设置分类轴右边距
        valueAxis.setTickLabelsVisible(false);

        //渲染线条
        XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) jfreechart.getXYPlot().getRenderer();
        xylineandshaperenderer.setDrawOutlines(true);
        xylineandshaperenderer.setUseFillPaint(true);
        xylineandshaperenderer.setBaseFillPaint(Color.white);
        xylineandshaperenderer.setSeriesStroke(0, new BasicStroke(3.0F));
        xylineandshaperenderer.setSeriesOutlineStroke(0, new BasicStroke(2.0F));
        xylineandshaperenderer.setSeriesShape(0, new Ellipse2D.Double(-5.0, -5.0, 10.0, 10.0));

        return jfreechart;
    }

    //设置定时器，每隔1秒刷新一次数据
    public void run(){
        Integer value;
        while(true){
            try{
                computeTraffic.refreshData();
                value = computeTraffic.getPacketLen();
                timeSeries.addOrUpdate(new Millisecond(), value);
                Thread.sleep(1000);
            }
            catch (InterruptedException e){
            }
        }
    }
}