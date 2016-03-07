package main.unUse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleEdge;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Created by lenovo on 2016-01-10.
 */
public class LineChart {
    private DefaultCategoryDataset defaultCategoryDataset;
    private JFreeChart chart;

    public void creatCategoryDataset(){
        defaultCategoryDataset = new DefaultCategoryDataset();

        for(int i=0;i<30;i++){
            defaultCategoryDataset.addValue(Math.random()*1000,"吞吐率",i+"");
        }
    }

    public void creatChart(){
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

        chart = ChartFactory.createLineChart(
                "吞吐率   单位：Kbps",      //图表标题
                "",             //X轴标签
                "速率",       //Y轴标签
                defaultCategoryDataset,     //数据集
                PlotOrientation.VERTICAL,   //方向
                true,          //是否显示图例
                true,           //是否显示提示信息
                false           //是否使用urls
        );
        chart.setBackgroundPaint(Color.white);      //背景颜色
        chart.setAntiAlias(true);       //抗锯齿

        LegendTitle legendTitle = new LegendTitle(chart.getPlot());//创建图例
        legendTitle.setPosition(RectangleEdge.BOTTOM);  //设置图例的位置

        CategoryAxis categoryAxis = chart.getCategoryPlot().getDomainAxis();
        categoryAxis.setLowerMargin(0);        //设置分类轴左边距
        categoryAxis.setUpperMargin(0);         //设置分类轴右边距
        categoryAxis.setTickLabelsVisible(false);

        CategoryPlot categoryplot = (CategoryPlot) chart.getPlot();
        categoryplot.setBackgroundPaint(Color.lightGray);
        categoryplot.setRangeGridlinePaint(Color.white);
        categoryplot.setRangeGridlinesVisible(true);

        LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer) categoryplot.getRenderer();
        lineandshaperenderer.setDrawOutlines(true);
        lineandshaperenderer.setUseFillPaint(true);
        lineandshaperenderer.setBaseFillPaint(Color.white);
        lineandshaperenderer.setSeriesStroke(0, new BasicStroke(3.0F));
        lineandshaperenderer.setSeriesOutlineStroke(0, new BasicStroke(2.0F));
        lineandshaperenderer.setSeriesShape(0, new Ellipse2D.Double(-5.0, -5.0,
                10.0, 10.0));

        // 显示数据值
//        DecimalFormat decimalformat1 = new DecimalFormat("##.##");// 数据点显示数据值的格式
//        lineandshaperenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator(
//                "{2}", decimalformat1));//  设置数据项标签的生成器
//        lineandshaperenderer.setBaseItemLabelsVisible(true);// 基本项标签显示
//        lineandshaperenderer.setBaseShapesFilled(true);// 在数据点显示实心的小图标
        System.out.println("creat new chart");
    }

    public void refreshDateSet(double value){
//        CategoryPlot categoryplot = (CategoryPlot) chart.getPlot();
        defaultCategoryDataset.removeRow(0);
        defaultCategoryDataset.addValue(value,"吞吐率",29+"");
//        categoryplot.setDataset(defaultCategoryDataset);
    }

    public ChartPanel getChartPanel(){
        creatCategoryDataset();
        creatChart();
        return new ChartPanel(chart);
    }

    public ChartPanel refreshChartPanel(double value){
        refreshDateSet(value);
        creatChart();
        return new ChartPanel(chart);
    }

}
