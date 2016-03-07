package main.test;

/**
 * Created by lenovo on 2016-01-09.
 */
import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.PacketReceiver;
import jpcap.packet.*;
/**
 * 使用jpcap显示网络上的各种数据包
 * @author www.NetJava.cn
 */
public class DispalyNetPacket {

    //程序启动主方法
    public static void main(String args[]){
        try{
            //获取本机上的网络接口对象数组
            final  NetworkInterface[] devices = JpcapCaptor.getDeviceList();
//            for(int i=0;i<devices.length;i++){
                NetworkInterface nc=devices[5];
                //创建某个卡口上的抓取对象,最大为2000个
                JpcapCaptor jpcap = JpcapCaptor.openDevice(nc, 68, true, 20);
                startCapThread(jpcap);
                System.out.println("开始抓取第"+5+"个卡口上的数据");
//            }
        }catch(Exception ef){
            ef.printStackTrace();
            System.out.println("启动失败:  "+ef);
        }

    }
    //将每个Captor放到独立线程中运行
    public static void startCapThread(final JpcapCaptor jpcap ){
        JpcapCaptor jp=jpcap;
        java.lang.Runnable rnner=new Runnable(){
            public void run(){
                //使用接包处理器循环抓包
                jpcap.loopPacket(-1, new TestPacketReceiver());
            }
        };
        new Thread(rnner).start();//启动抓包线程
    }
}

/**
 * 抓包监听器,实现PacketReceiver中的方法:打印出数据包说明
 * @author www.NetJava.cn
 */
class TestPacketReceiver  implements PacketReceiver {
    /**
     * 实现的接包方法:
     */
    public void receivePacket(Packet packet) {
        //Tcp包,在java Socket中只能得到负载数据
        if(packet instanceof jpcap.packet.TCPPacket){
            TCPPacket p=(TCPPacket)packet;
            String s="TCPPacket:| dst_ip "+p.dst_ip+":"+p.dst_port
                    +"|src_ip "+p.src_ip+":"+p.src_port
                    +" |len: "+p.len;
            System.out.println(s);
        }
        //UDP包,开着QQ,你就会看到:它是tcp+udp
        else if(packet instanceof jpcap.packet.UDPPacket){
            UDPPacket p=(UDPPacket)packet;
            String s="UDPPacket:| dst_ip "+p.dst_ip+":"+p.dst_port
                    +"|src_ip "+p.src_ip+":"+p.src_port
                    +" |len: "+p.len;
            System.out.println(s);
        }
        //如果你要在程序中构造一个ping报文,就要构建ICMPPacket包
        else if(packet instanceof jpcap.packet.ICMPPacket){
            ICMPPacket p=(ICMPPacket)packet;
            //ICMP包的路由链
            String router_ip="";
            for(int i=0;i<p.router_ip.length;i++){
                router_ip+=" "+p.router_ip[i].getHostAddress();
            }
            String s="@ @ @ ICMPPacket:| router_ip "+router_ip
                    +" |redir_ip: "+p.redir_ip
                    +" |mtu: "+p.mtu
                    +" |length: "+p.len;
            System.out.println(s);
        }
        //是否地址转换协议请求包
        else if(packet instanceof jpcap.packet.ARPPacket){
            ARPPacket p=(ARPPacket)packet;
            //Returns the hardware address (MAC address) of the sender
            Object  saa=   p.getSenderHardwareAddress();
            Object  taa=p.getTargetHardwareAddress();
            String s="* * * ARPPacket:| SenderHardwareAddress "+saa
                    +"|TargetHardwareAddress "+taa
                    +" |len: "+p.len;
            System.out.println(s);

        }
        //取得链路层数据头 :如果你想局网抓包或伪造数据包，嘿嘿
        DatalinkPacket datalink  =packet.datalink;
        //如果是以太网包
        if(datalink instanceof jpcap.packet.EthernetPacket){
            EthernetPacket ep=(EthernetPacket)datalink;
            String s="  datalink layer packet: "
                    +"|DestinationAddress: "+ep.getDestinationAddress()
                    +"|SourceAddress: "+ep.getSourceAddress();
            System.out.println(s);
        }
    }

}