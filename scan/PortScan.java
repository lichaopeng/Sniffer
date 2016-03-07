package main.scan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PortScan implements ActionListener {
    // 创建主窗口
    public static JFrame mainFrame = new JFrame();
    public static JLabel labelIP = new JLabel("主机IP");
    public static JLabel labelPortStart = new JLabel("起始端口：");
    public static JLabel labelPortEnd = new JLabel("结束端口：");
    public static JLabel labelResult = new JLabel("扫描结果:");
    public static JLabel State = new JLabel("状态：");
    public static JLabel Scanning = new JLabel("未开始扫描");
    public static JTextField hostIP = new JTextField("127.0.0.1");
    public static JTextField PortStart = new JTextField("0");
    public static JTextField PortEnd = new JTextField("1024");
    // 文本区域，显示扫描结果
    public static JTextArea Result = new JTextArea();
    public static JScrollPane scrollPane = new JScrollPane(Result);
    public static JLabel DLGINFO = new JLabel("");
    public static JButton Start = new JButton("扫描");
    public static JButton Exit = new JButton("退出");
    // 错误提示对话框
    public static JDialog DLGError = new JDialog(mainFrame, "错误");
    public static JButton OK = new JButton("确定");

    public PortScan() {

        // 设置主窗体名称
        mainFrame.setTitle("端口扫描");
        // 设置主窗体位置和大小
        mainFrame.setBounds(380, 300, 550, 300);

        // 事件监听
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        });

        // 设置错误提示框
        Container dPanel = DLGError.getContentPane();
        dPanel.setLayout(null);
        dPanel.add(DLGINFO);
        dPanel.add(OK);
        OK.setActionCommand("ok");
        OK.addActionListener(this);

        // 在主窗体中添加其他组件
        mainFrame.setLayout(null);
        mainFrame.setResizable(false);
        mainFrame.add(Start);
        mainFrame.add(Exit);
        mainFrame.add(labelIP);
        mainFrame.add(hostIP);
        mainFrame.add(labelPortStart);
        mainFrame.add(labelPortEnd);
        mainFrame.add(PortStart);
        mainFrame.add(PortEnd);
        mainFrame.add(labelResult);
        mainFrame.add(scrollPane);
        mainFrame.add(State);
        mainFrame.add(Scanning);

        // 设置扫描按钮和退出按钮
        Start.setBounds(405, 232, 60, 30);
        Start.setActionCommand("Start");
        Start.addActionListener(this);
        Exit.setBounds(475, 232, 60, 30);
        Exit.setActionCommand("Exit");
        Exit.addActionListener(this);
        labelIP.setBounds(17, 13, 50, 20);
        hostIP.setBounds(67, 10, 92, 25);
        hostIP.setHorizontalAlignment(JTextField.CENTER);

        labelPortStart.setBounds(162, 13, 65, 20);
        PortStart.setBounds(227, 10, 45, 25);
        PortStart.setHorizontalAlignment(JTextField.CENTER);

        labelPortEnd.setBounds(292, 13, 65, 20);
        PortEnd.setBounds(357, 10, 45, 25);
        PortEnd.setHorizontalAlignment(JTextField.CENTER);


        labelResult.setBounds(1, 45, 55, 20);
        Result.setEditable(false);
        scrollPane.setBounds(1, 65, 542, 160);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        State.setBounds(17, 232, 60, 30);
        Scanning.setBounds(80, 232, 120, 30);
        mainFrame.setVisible(true);
    }

    /**
     * 事件处理
     *
     * */
    public void actionPerformed(ActionEvent e) {

        // 得到命令处理事件
        String cmd = e.getActionCommand();

        // 开始扫描
        if (cmd.equals("Start")) {
            String ipAddress;
            InetAddress inetAddress;
            try {
                ipAddress = PortScan.hostIP.getText();
                inetAddress = InetAddress.getByName(ipAddress);
            } catch (UnknownHostException e1) {
                DLGError.setBounds(300, 280, 160, 110);
                DLGINFO.setText("错误的IP地址/域名");
                DLGINFO.setBounds(25, 15, 100, 20);
                OK.setBounds(45, 40, 60, 30);
                DLGError.setVisible(true);
                return;
            }
            int minPort;
            int maxPort;

            // 获取输入数据
            try {
                minPort = Integer.parseInt(PortStart.getText());
                maxPort = Integer.parseInt(PortEnd.getText());
            } catch (NumberFormatException e1) {
                DLGError.setBounds(300, 280, 299, 120);
                DLGINFO.setText("错误的端口号!端口号必须为整数!");
                DLGINFO.setBounds(10, 20, 280, 20);
                OK.setBounds(110, 50, 60, 30);
                DLGError.setVisible(true);
                return;
            }

            // 输入信息错误处理
            if ((minPort < 0) || (maxPort > 65535) || (minPort > maxPort)) {
                DLGError.setBounds(300, 280, 295, 120);
                DLGINFO.setText("最小端口必须是0-65535并且小于最大端口的整数");
                DLGINFO.setBounds(10, 20, 280, 20);
                OK.setBounds(120, 50, 60, 30);
                DLGError.setVisible(true);
                return;
            }

            Result.append("正在扫描 " + hostIP.getText() + "\n");
            Scanning.setText("开始扫描 ...");
            Result.append("开始端口 " + minPort + " 结束端口 " + maxPort + " \n");

            //使用线程池管理任务
            ExecutorService executorService = Executors.newCachedThreadPool();
            for(int i=minPort;i<=maxPort;i++){
                executorService.execute(new Scan(inetAddress,i));
            }
            executorService.shutdown();
            while (true) {
                if (executorService.isTerminated()) {
                    Result.append("扫描完成!\n");
                    Scanning.setText("扫描完成！");
                    break;
                }
            }
        } else if (cmd.equals("ok")) {
            DLGError.dispose();
        } else if (cmd.equals("Exit")) {
            mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }
    }

    public static void main() {
        new PortScan();
    }

}

class Scan extends Thread {
    int port;
    public static InetAddress hostAddress;

    Scan(InetAddress inetAddress ,int port) {
        this.hostAddress = inetAddress;
        this.port = port;
    }

    public void run() {
        // 扫描指定端口
        PortScan.Scanning.setText("正在扫描" + hostAddress.getHostAddress() + " : " + port);
        try {
            // 根据主机名和端口号创建套接字地址
            SocketAddress sockaddr = new InetSocketAddress(hostAddress, port);
            Socket scans = new Socket();
            int timeoutMs = 200;
            // 将此套接字连接到具有指定超时值的服务器
            scans.connect(sockaddr, timeoutMs);
            // 关闭此套接字。
            scans.close();

            // 添加结果显示
            synchronized (PortScan.Result) {
                PortScan.Result.append("主机:" + hostAddress.getHostAddress() + " TCP端口:"
                        + port);
                switch (port) {
                    case 20:
                        PortScan.Result.append("(FTP Data)");
                        break;
                    case 21:
                        PortScan.Result.append("(FTP Control)");
                        break;
                    case 23:
                        PortScan.Result.append("(TELNET)");
                        break;
                    case 25:
                        PortScan.Result.append("(SMTP)");
                        break;
                    case 38:
                        PortScan.Result.append("(RAP)");
                        break;
                    case 53:
                        PortScan.Result.append("(DNS)");
                        break;
                    case 79:
                        PortScan.Result.append("FINGER");
                        break;
                    case 80:
                        PortScan.Result.append("(HTTP)");
                        break;
                    case 110:
                        PortScan.Result.append("(POP)");
                        break;
                    case 161:
                        PortScan.Result.append("(SNMP)");
                        break;
                    case 443:
                        PortScan.Result.append("(HTTPS)");
                        break;
                    case 1433:
                        PortScan.Result.append("(SqlServer)");
                        break;
                    case 3306:
                        PortScan.Result.append("(MySql)");
                        break;
                    case 8000:
                        PortScan.Result.append("(QQ)");
                        break;
                }
                PortScan.Result.append(" 开放\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return;
        }
    }
}