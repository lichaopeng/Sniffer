package main.test;

/**
 * Created by lenovo on 2016-01-07.
 */
import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;

public class Test {
    public static void main(String args[]) {
        try {
            // 获取本机上的网络接口对象数组
            final NetworkInterface[] devices = JpcapCaptor.getDeviceList();
            for (int i = 0; i < devices.length; i++) {
                NetworkInterface nc = devices[i];
                // 一块卡上可能有多个地址:
                String address = "";
                for (int t = 0; t < nc.addresses.length; t++) {
                    address += "|addresses[" + t + "]: "
                            + nc.addresses[t].address.toString();
                }
                // 打印说明:
                System.out.println("第" + i + "个接口:" + "|name: " + nc.description
                        + "|loopback: " + nc.loopback + "\r\naddress: "
                        + address);
            }
        } catch (Exception ef) {
            ef.printStackTrace();
            System.out.println("显示网络接口数据失败:  " + ef);
        }
    }
}