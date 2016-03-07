package main.trafficMonitorChart;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ComputeTraffic {
    private Map<Integer , Integer> map;
    private Integer packetLen;

    public ComputeTraffic(){
        packetLen = 0;
        map = new LinkedHashMap<Integer, Integer>();        //用map存储每秒内的流量
    }

    //每捕获一个数据包就更新一次流量数据
    public void putData(final Integer time , final Integer len){
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (map){
                    if (map.containsKey(time)) {
                        int value = map.get(time);
                        map.put(time, len + value);
                    } else {
                        map.put(time, len);
                    }
                }
            }
        }).start();
    }

    //刷新数据
    public void refreshData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (map){
                    Iterator iterator = map.entrySet().iterator();
                    if(iterator.hasNext()){
                        Map.Entry entry = (Map.Entry)iterator.next();
                        packetLen = (Integer)entry.getValue();
                        iterator.remove();
                    }
                    else{
                        packetLen = 0;
                    }
                }
            }
        }).start();
    }

    //获取刷新后的数据
    public Integer getPacketLen(){
        if(packetLen != null){
            return packetLen;
        } else {
            return null;
        }
    }
}
