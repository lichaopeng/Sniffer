package main.unUse;

import javax.swing.*;
import java.awt.*;

/**
 * Created by lenovo on 2016-01-10.
 */
public class JFrameFigure extends JFrame implements Runnable{
    private LineChart lineChart;
    private JPanel chartPanel;

    public JFrameFigure(){
        setSize(800,500);
        lineChart = new LineChart();
        chartPanel = lineChart.getChartPanel();
        this.getContentPane().add(chartPanel, BorderLayout.NORTH);
    }

    public void showFigure() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrameFigure frameFigure = new JFrameFigure();
                frameFigure.setTitle("流量监控");
                frameFigure.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frameFigure.setVisible(true);
            }
        });
    }

    @Override
    public void run() {
        while (true){
            System.out.println(Math.random()*1000);
            this.remove(chartPanel);
//            this.getContentPane().add(lineChart.refreshChartPanel(Math.random()*1000), BorderLayout.NORTH);
//            chartPanel.addNotify();
//            this.getContentPane().addNotify();
//            this.addNotify();
//            this.pack();
//            this.validate();
//            this.repaint();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
