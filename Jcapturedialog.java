package main;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Jcapturedialog extends javax.swing.JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	static JpcapCaptor jpcap = null;
	private JRadioButton wholeRadioButton;
	private JPanel buttonPanel;
	private JButton cancelButton;
	private JButton okButton;
	private JRadioButton userRadioButton;
	private JRadioButton headRadioButton;
	private JPanel netPanel;
	private JTextField caplenTextField;
	private JPanel caplenPanel;
	private static JTextField textFieldFilter;
	private JPanel checkBoxPanel;
	private JPanel filterPanel;
	private JCheckBox checkBoxMix;
	private static JCheckBox checkBoxFigure;
	private JComboBox netJComboBox;
	private JPanel jPanelEast;
	private JPanel jPanelWest;

    final  NetworkInterface[] devices = JpcapCaptor.getDeviceList();        //获取网卡接口

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		Jcapturedialog inst = new Jcapturedialog(frame);
		inst.setVisible(true);
	}

	public Jcapturedialog(JFrame frame) {
		super(frame, "选择要检测的网卡并设置参数", true);

		try {
			BoxLayout thisLayout = new BoxLayout(getContentPane(), javax.swing.BoxLayout.X_AXIS);
			getContentPane().setLayout(thisLayout);
			{
				jPanelWest = new JPanel();
				jPanelWest.setLayout(new BoxLayout(jPanelWest, BoxLayout.Y_AXIS));
				getContentPane().add(jPanelWest);
				{
					netPanel = new JPanel();
					FlowLayout netPanelLayout = new FlowLayout();
					netPanelLayout.setAlignOnBaseline(true);
					netPanel.setBorder(BorderFactory.createTitledBorder("选择网卡"));
					netPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
					jPanelWest.add(netPanel);
					netPanel.setLayout(netPanelLayout);
					{
						if (devices == null) {
							JOptionPane.showMessageDialog(frame, "没有找到网卡");
							dispose();
							return;
						} else {
							String[] names = new String[devices.length];
							for (int i = 0; i < names.length; i++) {
								names[i] = (devices[i].description == null ? devices[i].name
										: devices[i].description  + "    " + devices[i].addresses[1].address);

                            }
							netJComboBox = new JComboBox(names);
						}
						netPanel.add(netJComboBox);
					}
				}
				{
                    checkBoxPanel = new JPanel();
                    checkBoxPanel.setLayout(new BorderLayout());
                    checkBoxPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    FlowLayout CheckBoxLayout = new FlowLayout();
                    CheckBoxLayout.setAlignOnBaseline(true);
                    jPanelWest.add(checkBoxPanel);

					checkBoxMix = new JCheckBox();
					checkBoxMix.setText("设置为混杂模式");
                    checkBoxMix.setSelected(true);
					checkBoxMix.setLayout(null);
                    checkBoxPanel.add(checkBoxMix,BorderLayout.WEST);

                    checkBoxFigure = new JCheckBox();
                    checkBoxFigure.setText("用图表显示统计数据");
                    checkBoxFigure.setLayout(null);
                    checkBoxPanel.add(checkBoxFigure,BorderLayout.EAST);
				}
				{
					filterPanel = new JPanel();
					filterPanel.setBorder(BorderFactory.createTitledBorder("捕获过滤器"));
					filterPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
					FlowLayout filterPanelLayout = new FlowLayout();
					filterPanelLayout.setAlignment(FlowLayout.LEFT);
					filterPanelLayout.setAlignOnBaseline(true);
					jPanelWest.add(filterPanel);
					filterPanel.setLayout(filterPanelLayout);
					{
						textFieldFilter = new JTextField(20);
						filterPanel.add(textFieldFilter);
					}
				}
			}
			{
				jPanelEast = new JPanel();
				jPanelEast.setLayout(new BoxLayout(jPanelEast, BoxLayout.Y_AXIS));
				getContentPane().add(jPanelEast);

				{
					caplenPanel = new JPanel();
					caplenPanel.setBorder(BorderFactory.createTitledBorder("最长字长"));
					caplenPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
					jPanelEast.add(caplenPanel);
					caplenPanel.setLayout(new BoxLayout(caplenPanel, BoxLayout.Y_AXIS));

					{
						caplenTextField = new JTextField(20);
						caplenPanel.add(caplenTextField);
						caplenTextField.setText("1514");
						caplenTextField.setEnabled(false);
					}
					{
						wholeRadioButton = new JRadioButton();
						FlowLayout userRadioButtonLayout = new FlowLayout();
						userRadioButtonLayout.setAlignOnBaseline(true);
						caplenPanel.add(wholeRadioButton);
						wholeRadioButton.setText("整个数据报");
						wholeRadioButton.setSelected(true);

						wholeRadioButton.addActionListener(this);
					}
					{
						headRadioButton = new JRadioButton();
						caplenPanel.add(headRadioButton);
						headRadioButton.setText("仅首部");

						headRadioButton.addActionListener(this);
					}
					{
						userRadioButton = new JRadioButton();
						caplenPanel.add(userRadioButton);
						userRadioButton.setText("其他部分");

						userRadioButton.addActionListener(this);
					}
					ButtonGroup group = new ButtonGroup();
					group.add(wholeRadioButton);
					wholeRadioButton.setActionCommand("Whole");
					group.add(headRadioButton);
					headRadioButton.setActionCommand("Head");
					group.add(userRadioButton);
					userRadioButton.setActionCommand("user");
				}
				{
					buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
					jPanelEast.add(buttonPanel);

					{
						okButton = new JButton();
						buttonPanel.add(okButton);
						FlowLayout cancelButtonLayout = new FlowLayout();
						cancelButtonLayout.setAlignOnBaseline(true);
						okButton.setText("确定");

						okButton.setActionCommand("ok");
						okButton.addActionListener(this);
					}
					{
						cancelButton = new JButton();
						buttonPanel.add(cancelButton);
						cancelButton.setText("取消");

						cancelButton.setActionCommand("cancel");
						cancelButton.addActionListener(this);
					}
				}
			}
			getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));

			getContentPane().add(jPanelWest);

			getContentPane().add(jPanelEast);

			pack();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent evt) {
		String cmd = evt.getActionCommand();

		if (cmd.equals("Whole")) {
			caplenTextField.setText("1514");
			caplenTextField.setEnabled(false);
		} else if (cmd.equals("Head")) {
			caplenTextField.setText("68");
			caplenTextField.setEnabled(false);
		} else if (cmd.equals("user")) {
			caplenTextField.setText("");
			caplenTextField.setEnabled(true);
			caplenTextField.requestFocus();
		} else if (cmd.equals("ok")) {
			try {
				int caplen = Integer.parseInt(caplenTextField.getText());       //获取捕获长度

				if (caplen < 68 || caplen > 1514) {
					JOptionPane.showMessageDialog(null, "捕获长度必须介于 68 和 1514之间");
					return;
				}

                //根据所选配置构建jpcap对象
				jpcap = JpcapCaptor.openDevice(
						devices[netJComboBox.getSelectedIndex()], caplen,
						checkBoxMix.isSelected(), 50);

                //设置过滤器
				if (textFieldFilter.getText() != null
						&& textFieldFilter.getText().length() > 0) {
					jpcap.setFilter(textFieldFilter.getText(), true);
				}
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "捕获长度必须是正整数");
			} catch (java.io.IOException e) {
				JOptionPane.showMessageDialog(null, e.toString());
				jpcap = null;
			} finally {
				dispose();
			}

		} else if (cmd.equals("cancel")) {
			dispose();
		}
	}

	public static JpcapCaptor getJpcap(JFrame parent) {
		new Jcapturedialog(parent).setVisible(true);
		return jpcap;
	}

    public static boolean isCheckBoxFigureSelected(){
        return checkBoxFigure.isSelected();
    }

}