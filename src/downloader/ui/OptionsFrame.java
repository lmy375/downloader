package downloader.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import downloader.object.Setting;
import downloader.object.share.RequestListener;

/**
 * Options frame which shows all the setting and users may change
 * setting on the frame.
 * @author Moon
 *
 */
public class OptionsFrame extends JFrame implements ActionListener, ItemListener{
		
	
	MainFrame main;
	
	SkinButton skinButton[];
	
	JLabel portText;
	JButton changePort;
	
	JLabel listenerStatus;
	JButton startServer;
	JButton closeServer;
	JCheckBox startRequestListenerWhenStarts;
	JCheckBox startTasksWhenStarts;
	
	
	JLabel path;
	JButton choosePath;
	
	public OptionsFrame(MainFrame main){
		super("Options");
		this.main= main;
		setSize(300, 300);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
	
//		this.setResizable(false);
		
		/**
		 * Skin pane
		 */
		UIManager.LookAndFeelInfo[] laf = UIManager.getInstalledLookAndFeels();
		skinButton = new SkinButton[laf.length];
		JPanel skinPane = new JPanel();
		skinPane.setLayout(new GridLayout(1,laf.length));
		skinPane.setBorder(BorderFactory.createTitledBorder("Themes"));
		for(int i = 0 ;i< laf.length;i++){
			skinButton[i] = new SkinButton(laf[i]);
			skinButton[i].addActionListener(this);
			skinPane.add(skinButton[i]);
		}
		
		/**
		 * Send port pane
		 */
		JPanel ipPane = new JPanel(new BorderLayout());
		ipPane.add(new JLabel("LocalHost IP:   "), BorderLayout.WEST);
		try {
			InetAddress ip = InetAddress.getLocalHost();
			ipPane.add(new JLabel(ip.getHostAddress().toString()));
		} catch (UnknownHostException e) {
			ipPane.add(new JLabel("Unavilable"),BorderLayout.EAST);
		}
		
		JPanel portPane =  new JPanel(new BorderLayout());
		portPane.add(new JLabel("Port:  "),BorderLayout.WEST);
		
		portText = new JLabel(Setting.getPortNumber()+"  ");
		changePort = new JButton("Change Port Number");
		changePort.addActionListener(this);
		
		portPane.add(portText,BorderLayout.CENTER);
		portPane.add(changePort,BorderLayout.EAST);
		
		JPanel shareSettingPane  = new JPanel(new GridLayout(5,1));
		shareSettingPane.add(ipPane,BorderLayout.NORTH);
		shareSettingPane.add(portPane,BorderLayout.SOUTH);
		
		//JPanel serverPane = new JPanel(new FlowLayout());
		
		startServer = new JButton("Start Listening Request");
		startServer.addActionListener(this);
		closeServer = new JButton("Stop  Listening Request");
		closeServer.addActionListener(this);
		
		listenerStatus = new JLabel("Listener Status: "+
				(main.getRequestListener().isRunning()? "Running":"Closed"));
		shareSettingPane.add(listenerStatus);
		
		JPanel buttonPane = new JPanel(new GridLayout(1,2));
		buttonPane.add(startServer);
		buttonPane.add(closeServer);
		shareSettingPane.add(buttonPane);
		
		
		JPanel checkPane = new JPanel(new GridLayout(1,2));
		startRequestListenerWhenStarts = new JCheckBox("Start Listening when starting");
		if(Setting.startRequestListenerWhenStarts) 
			this.startRequestListenerWhenStarts.setSelected(true);
		startRequestListenerWhenStarts.addItemListener(this);
		checkPane.add(startRequestListenerWhenStarts);
		
		startTasksWhenStarts = new JCheckBox("Start all tasks when starting");
		if(Setting.startTasksWhenStarts) 
			this.startTasksWhenStarts.setSelected(true);
		startTasksWhenStarts.addItemListener(this);
		checkPane.add(startTasksWhenStarts);
		shareSettingPane.add(checkPane);
	
		JPanel pathPane = new JPanel(new BorderLayout());
		path = new JLabel(Setting.getDefaultPath());
		choosePath = new JButton("Change path");
		choosePath.addActionListener(this);
		pathPane.add(path , BorderLayout.WEST);
		pathPane.add(choosePath, BorderLayout.EAST);
		
		shareSettingPane.add(pathPane);
		
		shareSettingPane.setBorder(BorderFactory.createTitledBorder("Share & Task Setting"));
		
		/**
		 * main pane
		 */
		JPanel mainPane= new JPanel();
		mainPane.setLayout(new BorderLayout());
		mainPane.add(skinPane,BorderLayout.NORTH);
		mainPane.add(shareSettingPane, BorderLayout.CENTER);
		mainPane.add(pathPane, BorderLayout.SOUTH);
		
		setContentPane(mainPane);
		pack();
		setVisible(true);

		
		
		
	}
	
	private JFileChooser createFileChooser(){
		JFileChooser filechooser= new JFileChooser();
		filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		filechooser.setDialogTitle("Choose New Path");
		filechooser.setFileHidingEnabled(true);
		//FileFilter f = filechooser.getFileFilter();
		
		return filechooser;
	}
	
	
	
	public void actionPerformed(ActionEvent ae){
		Object src = ae.getSource();		
		/**
		 * Set look and feel according to the system
		 */
		for(SkinButton sb: skinButton){
			if(src==sb){
				try{
					Setting.setDefaultLookAndFeel(sb.getClassName());
					UIManager.setLookAndFeel(sb.getClassName());					
					SwingUtilities.updateComponentTreeUI(this);
					SwingUtilities.updateComponentTreeUI(main);
					SwingUtilities.updateComponentTreeUI(main.getNewTaskFrame());
					pack();
				}catch(Exception e){
					
				}
			}
		}
		
		/**
		 * Change the port number
		 */
		if (src==changePort){
			String port = JOptionPane.showInputDialog("Enter the port number");
		//	System.out.print(port);
			if(port!=null) Setting.setPortNumber(port);
			portText.setText(Setting.getPortNumber()+"");
			
		}
		
		if(src == startServer){
			RequestListener r = main.getRequestListener();
			if(r.isRunning()) 
				JOptionPane.showMessageDialog(null,"Server is already running.");
			else {
				if(r.startServer()){
					JOptionPane.showMessageDialog(null,"Server is started.");
					this.listenerStatus.setText("Listener Status: "+
							(main.getRequestListener().isRunning()? "Running":"Closed"));
				}
			}
		}
		if(src == closeServer){
			RequestListener r = main.getRequestListener();
			if(r.isRunning()){
				if(r.closeServer()){
					JOptionPane.showMessageDialog(null,"Server is closed.");
					this.listenerStatus.setText("Listener Status: "+
							(main.getRequestListener().isRunning()? "Running":"Closed"));
				}
			}
			else
				JOptionPane.showMessageDialog(null, "Server is already closed.");
			
		}
		if(src == choosePath){
			JFileChooser filePath = this.createFileChooser();
			int returnVal =filePath.showOpenDialog(this);
			if(returnVal==JFileChooser.APPROVE_OPTION){
				Setting.setDefaultPath(filePath.getSelectedFile().toString());
				path.setText(Setting.getDefaultPath());
			}
				
		
		}
		
	}



	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		Object src = e.getSource();
		if(src == this.startRequestListenerWhenStarts){
			if(e.getStateChange()==ItemEvent.SELECTED) Setting.startRequestListenerWhenStarts=true;
			else if(e.getStateChange()==ItemEvent.DESELECTED) Setting.startRequestListenerWhenStarts=false;
			
		}
		if(src == this.startTasksWhenStarts){
			if(e.getStateChange()==ItemEvent.SELECTED) Setting.startTasksWhenStarts = true;
			else if(e.getStateChange()==ItemEvent.DESELECTED) Setting.startTasksWhenStarts = false;
		}
	}
	
	
	
}

/**
 * class skin button 
 * @author Moon
 *
 */
class SkinButton extends JButton{
	private String className;
	public SkinButton(UIManager.LookAndFeelInfo laf){
		super(laf.getName());
		className = laf.getClassName();
	}
	public String getClassName(){
		return this.className;
	}
}
