package main.trafficMonitorChart;

import org.jfree.data.time.Millisecond;

import java.util.Date;

/**
 * Created by lenovo on 2016-01-11.
 */
public class tmp {
    public static void main(String args[]){
//        Map<Integer , Double> map = new LinkedHashMap<Integer, Double>();
//        int i=10;
//        while(i != 0){
//            int second = (int)(new Millisecond().getFirstMillisecond()/1000);
//            if (map.containsKey(second)) {
//                double value = map.get(second);
//                map.put(second,value+1.0);
//            } else {
//                map.put(second,1.0);
//            }
//            System.out.println(second);
//
////            System.out.println(new Millisecond().getFirstMillisecond()/1000);
////            System.out.println(new Date().getTime()/1000);
//            try {
//                Thread.sleep((long)(Math.random()*1500));
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            i--;
//        }
//        Iterator iterator = map.entrySet().iterator();
//        while(iterator.hasNext()){
//            Map.Entry entry = (Map.Entry)iterator.next();
//            System.out.println(entry.getKey() + " " + entry.getValue());
//        }

//        Timestamp timestamp = new Timestamp(new Date().getTime());
//        int t = (int) (new Date().getTime()/1000);
//        System.out.println(t);
        System.out.println(new Date((long)1452505969*1000));
        System.out.println(new Date());
        System.out.println(new Millisecond(new Date((long)1452505969*1000)));
    }
}
