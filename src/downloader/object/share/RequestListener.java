package downloader.object.share;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import downloader.object.Setting;
import downloader.ui.MainFrame;

/**
 * Listener to listen whether there is request from clients.
 * @author Moon
 *
 */
public class RequestListener implements Runnable{
	private ServerSocket server;
	private ArrayList<RequestMessage> messages;	
	private boolean running;
	private MainFrame main;

	public RequestListener(MainFrame main) {
		super();
		this.main = main;
		messages = new ArrayList<RequestMessage>();
		//this.start();
	}
	
	/**
	 * Starts server according to the port number in setting.
	 * @return true if success else false
	 */
	public boolean startServer() {
		try {
			server = new ServerSocket(Setting.getPortNumber());
			running = true;
		} catch (BindException e) {
			//e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					" Current port ("+Setting.getPortNumber()+
					") used. Change a new port number in Options-Setting.");
			return false;
			//Setting.setPortNumber(JOptionPane.showInputDialog(
			//		"Current port ("+Setting.getPortNumber()+") used. Change a new port number"));
			//startServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block			
			e.printStackTrace();
			return false;
		}
		
		new Thread(this).start();
		return true;

	}
	
	/**
	 * Closes the server.
	 * @return true if success else false.
	 */
	public boolean closeServer() {
		try {
			if (server != null)
				server.close();
			running = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	
	public void run(){
		listen();
	}
	
	/**
	 * Listens whether server gets new request.
	 */
	private void listen() {
		int id = 0;
		while (server != null && !server.isClosed()) {
			try {
				Socket socket = server.accept();
				messages.add(new RequestMessage(socket, ++id, main));
				//new ReceiverFrame(new ReceiverThread(socket));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
	}
	
	public ArrayList<RequestMessage> getMessages(){
		return this.messages;
	}
	/**
	 * Tests whether listener is running.
	 * @return true if running
	 */
	public boolean isRunning(){
		return this.running;
	}
}
