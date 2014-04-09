package downloader.object.share;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import downloader.object.Task;
import downloader.ui.ShareFrame;


/**
 * Sending thread which establish a socket to send data to server.
 * @author Moon
 *
 */
public class SendThread implements Runnable{
	private static final int BUFFER_SIZE = 1024*8;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Socket socket;
	private Task task;
	private ShareFrame shareFrame;
	
	
	public SendThread(Task task, ShareFrame shareFrame) {
		
		this.task = task;
		this.shareFrame = shareFrame;		
		new Thread(this).start();
		
		
	}	
	
	private void sendData() {
		File file = new File(task.getFilePath()+"\\"+task.getFileName());
		if(!file.exists()){
			shareFrame.setInfo("Fail to send. File "+task.getFileName()+ " not found.");
			try {
				oos.writeObject("Error");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		} else
			try {
				oos.writeObject("Ready");
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(
					new FileInputStream(file));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		byte[] buff = new byte[BUFFER_SIZE];
		
		BufferedOutputStream bos = new BufferedOutputStream(oos);

		
		int length = 0;
		int currentLength = 0 ;

		try {
			while ((length = bis.read(buff)) > 0) {			
					bos.write(buff, 0, length);
					bos.flush();
					currentLength += length;	
					shareFrame.setInfo(" Sending "+currentLength/1024+
							"/"+task.getFileSize()/1024+"K"+
							currentLength*100/task.getFileSize()+"% ");
					if(currentLength>= task.getFileSize()) break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			shareFrame.setInfo("Error occurs in IO of sending");
		}
		
		try {
			String s = (String) ois.readObject();
			if(s.equals("Success")) {
				shareFrame.setInfo("Target received your data successfully.");				
			}
			if(s.equals("Failure")){
				shareFrame.setInfo("Error occurs in target computer.");
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	private void connect() {
		try {
			shareFrame.setInfo("Connecting to target computer...");
			socket = new Socket(shareFrame.getTargetIP(), shareFrame.getTargetPort());
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			
			shareFrame.setInfo("Connected.");
			oos.writeObject(task);
			
			try {
				String s = (String) ois.readObject();
				if(s.equals("Accept")) {
					shareFrame.setInfo("Target accepted your sending request");
					this.sendData();
					
				}
				if(s.equals("Download")){
					shareFrame.setInfo("Target is downloading your share himself.");
				}
				if(s.equals("Refuse")){
					shareFrame.setInfo("Target refused your sending request");
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null,"Illegal port number.");
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
			JOptionPane.showMessageDialog(null,"Unknow Host. "+e1.getMessage());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "Network Error.  "+e1.getMessage());
		}
		//this.dispose();
	}

	private void closeAll(){		
		try {
			if(oos!=null) oos.close();
			if(ois!=null)ois.close();
			if(socket!=null) socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

	@Override
	/**
	 * Connect to server and send data to server if request is permitted.
	 */
	public void run() {
		// TODO Auto-generated method stub
		connect();
		this.closeAll();
	}
	
	
	

}
