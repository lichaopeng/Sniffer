package main;

import jpcap.packet.*;
import main.scan.PortScan;
import main.trafficMonitorChart.ComputeTraffic;
import main.trafficMonitorChart.TrafficMonitor;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Timestamp;
import java.util.Vector;

public class JFrameMain extends javax.swing.JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JMenuItem exitMenuItem;
	private JSeparator jSeparator;
	private JMenuItem portScan;
	private JMenuItem stopMenuItem;
	private JMenuItem startMenuItem;
	private JMenu Menu;
	private JMenuBar jMenuBar;

	private JTable tableDisplay = null;
	private Vector rows, columns;
	private DefaultTableModel tabModel;
	private JScrollPane scrollPane;
    private JScrollBar scrollBar;

    private JPanel bottomPanel;             //中间容器，置于主面板底部显示数据包详细信息
    private JTextArea frameDetailInfo;      //置于底部中间容器中显示详细信息
	private JLabel statisticalData;         //置于底部中间容器中显示统计信息

	private Netcaptor captor = new Netcaptor();     //实例化捕获器
    private Vector<Packet> vectorPacket = new Vector<Packet>(); //存储捕获到的数据包

    private TrafficMonitor trafficMonitor;
    private ComputeTraffic computeTraffic;

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		JFrameMain inst = new JFrameMain();
        inst.setTitle("Java网络分析工具");
		inst.setVisible(true);
        inst.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent windowevent){
                System.exit(0);
            }
        });
	}

	public JFrameMain() {
		super();
		initGUI();
	}

	private void initGUI() {
		try {
			setSize(800, 500);
			{
				jMenuBar = new JMenuBar();
				setJMenuBar(jMenuBar);
				{
					Menu = new JMenu();
					jMenuBar.add(Menu);
					Menu.setText("菜单");
					Menu.setPreferredSize(new java.awt.Dimension(35, 21));
					{
						startMenuItem = new JMenuItem();
						Menu.add(startMenuItem);
						startMenuItem.setText("开始");
						startMenuItem.setActionCommand("start");
						startMenuItem.addActionListener(this);
					}
					{
						stopMenuItem = new JMenuItem();
						Menu.add(stopMenuItem);
						stopMenuItem.setText("停止");
						stopMenuItem.setActionCommand("stop");
						stopMenuItem.addActionListener(this);
					}
					{
						portScan = new JMenuItem();
						Menu.add(portScan);
						portScan.setText("端口扫描");
                        portScan.setActionCommand("scan");
                        portScan.addActionListener(this);

					}
                    {
                        jSeparator = new JSeparator();
                        Menu.add(jSeparator);
                    }
					{
						exitMenuItem = new JMenuItem();
						Menu.add(exitMenuItem);
						exitMenuItem.setText("Exit");
						exitMenuItem.setActionCommand("exit");
						exitMenuItem.addActionListener(this);
					}
				}
			}

			rows = new Vector();
			columns = new Vector();

			columns.addElement("数据报时间");
			columns.addElement("首部长度");
			columns.addElement("数据长度");
			columns.addElement("源IP地址");
			columns.addElement("目的IP地址");
			columns.addElement("是否分段");
			columns.addElement("分段偏移量");
			columns.addElement("上层协议");

            //实例化表格组件，显示数据包信息
			tabModel = new DefaultTableModel();
			tabModel.setDataVector(rows, columns);
			tableDisplay = new JTable(tabModel);

            //设置表格列宽
            tableDisplay.getColumnModel().getColumn(0).setPreferredWidth(180);
            tableDisplay.getColumnModel().getColumn(1).setPreferredWidth(70);
            tableDisplay.getColumnModel().getColumn(2).setPreferredWidth(70);
            tableDisplay.getColumnModel().getColumn(3).setPreferredWidth(120);
            tableDisplay.getColumnModel().getColumn(4).setPreferredWidth(120);

			scrollPane = new JScrollPane(tableDisplay);
			this.getContentPane().add(scrollPane, BorderLayout.CENTER);
            scrollBar = scrollPane.getVerticalScrollBar();          //获取滚动条

            //实例化底部中间容器
            bottomPanel = new JPanel();
            bottomPanel.setLayout(new BorderLayout());
            this.getContentPane().add(bottomPanel,BorderLayout.SOUTH);

            //实例化底部中间容器的文本域
            frameDetailInfo = new JTextArea();
            JScrollPane scroll = new JScrollPane(frameDetailInfo);
            scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scroll.setPreferredSize(new Dimension(0, 180));
            frameDetailInfo.setEditable(false);
            bottomPanel.add(scroll,BorderLayout.NORTH);

            //实例化底部中间容器的标签
            statisticalData = new JLabel("已捕获数据包：0 个");
			bottomPanel.add(statisticalData, BorderLayout.SOUTH);


            //添加表格组件监听事件，选中某一行后在底部中间容器中显示当前行数据包的详细信息
            tableDisplay.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
                public void valueChanged(ListSelectionEvent event) {
                    tableRowSelectionListener(tableDisplay.getSelectedRow());
                }
            });

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    //监听事件
	public void actionPerformed(ActionEvent event) {
		String cmd = event.getActionCommand();
		if (cmd.equals("start")) {
			captor.capturePacketsFromDevice();
			captor.setJFrame(this);
            if(captor.showStatisticalFigure){
                trafficMonitor = new TrafficMonitor("吞吐率  单位：B/s","","吞吐率");
                this.computeTraffic = trafficMonitor.getComputeTraffic();
                showMonitorChart();
            }

		} else if (cmd.equals("stop")) {
			captor.stopCapture();
		} else if(cmd.equals("scan")){
            PortScan.main();
        } else if (cmd.equals("exit")) {
			System.exit(0);
		}
	}

    //显示流量监控图
    public void showMonitorChart(){
        JFrame frame=new JFrame("流量监控");
        frame.getContentPane().add(trafficMonitor,new BorderLayout().CENTER);
        frame.pack();
        frame.setVisible(true);
        (new Thread(trafficMonitor)).start();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    //解析选中的数据包
    public void tableRowSelectionListener(int rowIndex){
        Packet packet = vectorPacket.get(rowIndex);
        String detailInfo = null;

        if(packet instanceof jpcap.packet.ARPPacket){
            ARPPacket p=(ARPPacket)packet;
            //Returns the hardware address (MAC address) of the sender
            Object saa = p.getSenderHardwareAddress();
            Object taa = p.getTargetHardwareAddress();
            detailInfo="数据包类型：ARP\n源MAC地址："+saa
                    +"\n目的MAC地址"+taa
                    +"\n数据包长度："+p.len;
        }
        else if(packet instanceof jpcap.packet.ICMPPacket){
            ICMPPacket p=(ICMPPacket)packet;
            detailInfo="数据包类型：ICMP"
                    +"\nTYPE："+p.type
                    +"\n目的IP地址："+p.dst_ip
                    +"\n源IP地址："+p.src_ip
                    +"\nTTL："+p.hop_limit
                    +"\n上层协议："+p.protocol
                    +"\n长度"+p.len
                    +"\n校验和：0x"+Integer.toHexString(p.checksum & 0xffff);
        }
        else if (packet instanceof jpcap.packet.TCPPacket){
            TCPPacket p=(TCPPacket)packet;
            detailInfo="数据包类型：TCP\n目的IP地址:端口："+p.dst_ip+" : "+p.dst_port
                    +"\n源IP地址:端口："+p.src_ip+" : "+p.src_port
                    +"\nTTL："+p.hop_limit
                    +"\n上层协议："+p.protocol
                    +"\n长度："+p.len
                    +"\n窗口："+p.window
                    +"\n数据内容："+new String(p.data);
        }
        else if (packet instanceof jpcap.packet.UDPPacket){
            UDPPacket p=(UDPPacket)packet;
            detailInfo="数据包类型：UDP\n目的IP地址:端口："+p.dst_ip+":"+p.dst_port
                    +"\n源IP地址:端口："+p.src_ip+" : "+p.src_port
                    +"\nTTL："+p.hop_limit
                    +"\n上层协议："+p.protocol
                    +"\n长度："+p.len
                    +"\n数据内容："+new String(p.data);
        }
        frameDetailInfo.setText(detailInfo);
    }

    //将数据包基本信息显示在表格中
	public void dealPacket(Packet packet) {
		try {
            if (captor.showStatisticalFigure) {
                computeTraffic.putData((int)packet.sec,packet.len);
            }

            vectorPacket.add(packet);
            Vector v = new Vector();
			Timestamp timestamp = new Timestamp((packet.sec * 1000) + (packet.usec / 1000));

			v.addElement(timestamp.toString()); // 数据报时间
			v.addElement(packet.header.length); // 首部长度
			v.addElement(packet.data.length); // 数据长度

            if(packet instanceof IPPacket){
                IPPacket p = (IPPacket)packet;
                v.addElement(p.src_ip.toString()); // 源IP地址
                v.addElement(p.dst_ip.toString()); // 目的IP地址
                v.addElement(p.dont_frag == true ? "分段" : "不分段"); // 是否分段
                v.addElement(p.offset); // 分段偏移量
                v.addElement(p.protocol);
            } else{
                v.addElement("-");
                v.addElement("-");
                v.addElement("-");
                v.addElement("-");
                v.addElement("-");
            }

			rows.addElement(v);

            statisticalData.setText("已捕获数据包：" + vectorPacket.size() + " 个");

			tableDisplay.addNotify();
            scrollBar.setValue(scrollBar.getMaximum());         //滚动条自动滚动到底部

		} catch (Exception e) {

		}
	}
}