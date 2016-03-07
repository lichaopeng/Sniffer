package main;

import jpcap.JpcapCaptor;
import jpcap.PacketReceiver;
import jpcap.packet.Packet;

public class Netcaptor {
 
    JpcapCaptor jpcap = null;
    JFrameMain frame;
    boolean showStatisticalFigure = false;
      
    public void setJFrame(JFrameMain frame){
        this.frame=frame;
    }

    //根据对话框中的配置信息获取jpcap对象
    public void capturePacketsFromDevice() {
 
        if(jpcap!=null)
            jpcap.close();

        jpcap = Jcapturedialog.getJpcap(frame);
        showStatisticalFigure = Jcapturedialog.isCheckBoxFigureSelected();      //是否显示监控图

        if (jpcap != null) {
            startCaptureThread();
        }
 
    }

    private Thread captureThread;

    //启动捕获线程
    private void startCaptureThread(){

        if(captureThread != null)
            return;
        captureThread = new Thread(new Runnable(){
            public void run(){
                while(captureThread != null){
                    jpcap.processPacket(-1, handler);              //捕获数据包
                }
            }
        });
        captureThread.setPriority(Thread.MIN_PRIORITY);
        captureThread.start();
    }

    void stopCaptureThread(){
        captureThread = null;
    }

    public void stopCapture(){
        stopCaptureThread();
    }

    //接包处理器
    private PacketReceiver handler=new PacketReceiver(){
        public void receivePacket(Packet packet) {
            frame.dealPacket(packet);
        }
    };
}
