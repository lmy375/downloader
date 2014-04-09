package downloader.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import downloader.object.Setting;
import downloader.object.Task;
import downloader.object.share.SendThread;

/**
 * Frame for user to fill IP and port to send request to server.
 * @author Moon
 *
 */
public class ShareFrame extends JDialog implements ActionListener{
	private JTextField targetIP;	
	private JTextField targetPort;	
		
	private JButton send;
	private JButton close;
	
	private JLabel info;	
	
	private Task task;
	
	public ShareFrame(Task t , MainFrame main){
		super(main,"New Share",false);
		this.task = t;
		//this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(400,150);
		this.setLocationByPlatform(true);
		this.setResizable(false);
		
		targetIP = new JTextField("");		
		targetPort = new JTextField("");
	
		send = new JButton("OK");
		send.addActionListener(this);
		close = new JButton ("Cancel");
		close.addActionListener(this);

		JPanel labelPane = new JPanel(new GridLayout(3,1));
		labelPane.add(new JLabel("File Name :  "));
		labelPane.add(new JLabel("Target IP :  "));
		labelPane.add(new JLabel("Target Port :  "));

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout());
		buttonPane.add(send);
		buttonPane.add(close);

		JPanel textPane = new JPanel(new GridLayout(3,1));
		textPane.add(new JLabel(t.getFileName()));		
		textPane.add(targetIP);
		textPane.add(targetPort);
		
		JPanel pane = new JPanel ();
		pane.setLayout(new BorderLayout());		
		
		pane.add(labelPane,BorderLayout.WEST);
		pane.add(textPane, BorderLayout.CENTER);
		pane.add(buttonPane,BorderLayout.SOUTH);

		info = new JLabel(" ");
		
		JPanel mainPane = new JPanel(new BorderLayout());

		mainPane.add(new JLabel(" "),BorderLayout.NORTH);
		mainPane.add(info,BorderLayout.SOUTH);
		mainPane.add(new JLabel("     "),BorderLayout.WEST);
		mainPane.add(new JLabel("     "),BorderLayout.EAST);
		mainPane.add(pane,BorderLayout.CENTER);
		
		this.setContentPane(mainPane);
		this.setVisible(true);
				
	}
	
	
	/**
	 * Set information showed on the bottom of the dialog.
	 * @param s Information to set.
	 */
	public void setInfo(String s){
		this.info.setText(s);
	}
	
	/**
	 * Gets IP filled by user
	 * @return IP
	 */
	public String getTargetIP(){
		return this.targetIP.getText();
	}
	
	/**
	 * Gets port number filled by user.
	 * @return Port number 
	 * @throws NumberFormatException
	 */
	public int getTargetPort ()throws  NumberFormatException{
		String s = this.targetPort.getText();
		return Integer.parseInt(s);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object src = e.getSource();
		if(src==send){
			if(task.getCurrentStatus()==Task.FINISHED) 
				new SendThread(task, this);
			else{
				JOptionPane.showMessageDialog(null,"Error to share. Task is not finished.");
			}
			
		}
		if(src==close){
			this.dispose();
		}
	}

}
