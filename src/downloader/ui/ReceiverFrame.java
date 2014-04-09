package downloader.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import downloader.object.share.RequestMessage;

/**
 * Inbox frame which list all the request received from clients.
 * @author Moon
 *
 */
public class ReceiverFrame extends JDialog{
	
	private JLabel numberLine;
	public ReceiverFrame(ArrayList<RequestMessage> messages, MainFrame main){
		super(main, "Inbox", true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	 	this.setLocationByPlatform(true);
		
		
		JPanel titlePane = new JPanel();
		titlePane.setLayout(new GridLayout(1,8));
		titlePane.add(new JLabel("No."));
		titlePane.add(new JLabel("Status"));
		titlePane.add(new JLabel("File Name"));
		titlePane.add(new JLabel("File Size"));
		titlePane.add(new JLabel("Addresser"));
		titlePane.add(new JLabel(""));
		titlePane.add(new JLabel("Handle"));
		titlePane.add(new JLabel(""));
		
		JPanel messagePane = new JPanel();
		messagePane.setLayout(new GridLayout(messages.size()+1,1));
		messagePane.add(titlePane);
		messagePane.setBorder(BorderFactory.createTitledBorder("Messages"));
		
		for(RequestMessage m: messages){
			//receiverThreads.add(new ReceiverThread(socketList.get(i)));
			messagePane.add(m);					
		}
		
		numberLine = new JLabel("	  "+messages.size()+"  files in Inbox.");
		numberLine.setBorder(BorderFactory.createEtchedBorder());
	
		
		JPanel mainPane = new JPanel(new BorderLayout());
		mainPane.add(numberLine , BorderLayout.SOUTH);
		mainPane.add(messagePane , BorderLayout.NORTH);
		
		
		this.setContentPane(mainPane);
		
		//this.setSize(650, 50*(socketList.size()+2));
		pack();
		this.setResizable(false);
		this.setVisible(true);
			}
	

}
