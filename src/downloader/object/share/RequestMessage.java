package downloader.object.share;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import downloader.object.Setting;
import downloader.ui.MainFrame;

/**
 * Message is context about request get from clients.
 * @author Moon
 *
 */
public class RequestMessage extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = -9059550475124067463L;

	MainFrame main;
	
	Socket socket;
	int messageID;
	boolean handled = false;
	
	//ObjectOutputStream oos;
	//ObjectInputStream ois;
	JButton accept;
	JButton download;
	JButton refuse;
	JLabel status;
	JLabel size;
	
	ReceiveThread thread;
	
	public RequestMessage(Socket socket, int id , MainFrame main){
		super();
		this.socket= socket;
		this.messageID= id;
		this.main= main;
		
		this.setLayout(new GridLayout(1,8));
		this.add(new JLabel(""+this.messageID));
		accept = new JButton("Accept");
		accept.addActionListener(this);
		download = new JButton("Download");
		download.addActionListener(this);
		refuse = new JButton("Refuse");
		refuse.addActionListener(this);
		
		status = new JLabel("Unhandled");
		size = new JLabel();
		
		try {
			thread = new ReceiveThread(socket , this);
			
			this.add(status);			
			this.add(new JLabel(thread.getFileName()));
			size.setText(" 0/"+thread.getFileSize()/1024+"K");
			this.add(size);
			this.add(new JLabel(thread.getAddresser()));			
			
			
		} catch (IOException e) {
			//e.printStackTrace();
			this.setStatus("Error");
			this.setHandled();
		} catch (ClassNotFoundException e) {
			//e.printStackTrace();
			this.setStatus("Error");
			this.setHandled();
		}
		this.add(accept);
		this.add(download);
		this.add(refuse);
		
		this.setVisible(true);
		
	}
	
	@Override
	/**
	 * Actions for buttons in the pane.
	 * To accept , download or refuse the request from cliens.
	 */
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object src = e.getSource();
		if(src==accept){		
			
			int response = JOptionPane.showConfirmDialog(null,
					"Receive data from "+thread.getAddresser()+ " ?" +
					"\n File \"Received-"+thread.getFileName()+"\"" +
					" will be saved in default path \n"+ Setting.getDefaultPath());
			if (response==JOptionPane.YES_OPTION){			
				thread.accept();
			}		
			
			
		}
		if(src==download){
			main.getNewTaskFrame();
			thread.getTask();
			main.getNewTaskFrame().addNewTask(thread.getTask());
			thread.download();
			//System.out.println(main.getNewTaskFrame()==null);			
			//System.out.println(thread.getTask()==null);
			//this.setHandled();			
		}
		if(src==refuse){			
			thread.refuse();			
		}
	}
	
	/**
	 * Sets accept and refuse button disable.
	 */
	public void setHandled(){
		this.handled = true;
		this.accept.setEnabled(false);
		this.refuse.setEnabled(false);
		//this.download.setEnabled(false);
	}
	
	
	public void setStatus(String s){
		this.status.setText(s);
	}
	public void setFinishedSize(int finishedSize){
		size.setText(" "+finishedSize/1024 + "/"+thread.getFileSize()/1024+"K");
	}
	

}
