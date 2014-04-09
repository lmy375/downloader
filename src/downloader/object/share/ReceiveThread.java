package downloader.object.share;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;

import downloader.object.Setting;
import downloader.object.Task;

/**
 * Receive thread. Using to handle remote request from clients.
 * @author Moon
 *
 */
public class ReceiveThread implements Runnable{
	private static final int BUFFER_SIZE = 1024*8;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	//private FileOutputStream fos ;
	private RandomAccessFile file;
	
	private Socket socket;
	private RequestMessage message;
	private Task task;

	private String fileName;
	private int fileSize;
	private int finishedSize;
	private String addresser;
	
	/**
	 * Constructor
	 * @param socket Socket for the request.
	 * @param message Message the thread belongs to .
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public ReceiveThread(Socket socket, RequestMessage message) throws IOException, ClassNotFoundException{		
		
		//try {
			this.socket = socket;
			this.message = message;
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			task =(Task) ois.readObject();
			fileName = task.getFileName();
			fileSize = task.getFileSize();
			addresser = socket.getInetAddress().getHostName();
			finishedSize = 0 ;
					
		//} catch (IOException | ClassNotFoundException e) {
		//	JOptionPane.showMessageDialog(null, "Error occurs in establishing InputStream");
		//}
	}
	
	/**
	 * Close server and all stream.
	 */
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
	
	/**
	 * Implements the Runnable interface. Receives data from
	 * addresser's PC in individual thread.
	 */
	public void run(){
		try {
			String s = (String) ois.readObject();
			if(s.equals("Error")){
				message.setStatus("Error");
				return;
			}
			if(s.equals("Ready")){
				file = new RandomAccessFile(Setting.getDefaultPath()+"\\Recevived-"+this.fileName, "rwd");
				
			//	fos = new FileOutputStream(task.getFileName());
				byte[] buff = new byte[BUFFER_SIZE];
				int len;
				message.setStatus("Receiving");
				while((len = ois.read(buff))>0){
					file.write(buff,0, len);
					this.finishedSize+= len;
					if(this.finishedSize>=this.fileSize) break;
					message.setFinishedSize(finishedSize);
				}
				oos.writeObject("Success");
				message.setStatus("Received");
				//fos.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				oos.writeObject("Failure");
				message.setStatus("Error");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			this.closeAll();
			message.setHandled();
		}
	}
	
	
	public Task getTask(){
		return this.task;
	}
	public int getFileSize(){
		return this.fileSize;
	}
	public int getFinishedSize(){
		return this.finishedSize;
	}
	public String getFileName(){
		return this.fileName;
	}
	public String getAddresser(){
		return this.addresser;
	}
	
	/**
	 * Accepts sending request. Thread starts to receive data.
	 * One-off function.
	 */
	public void accept(){
		try {
			oos.writeObject(new String("Accept"));
			message.setStatus("Accepted");			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		new Thread(this).start();
	}
	
	/**
	 * Picks link from current task and start a new task to download
	 * it from Internet.
	 */
	public void download(){
		try {
			if(!socket.isClosed()) oos.writeObject(new String("Download"));
			message.setStatus("Downloaded");
			this.closeAll();
			message.setHandled();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	/**
	 * Refuse current request.
	 */
	public void refuse(){
		try {
			oos.writeObject(new String("Refuse"));
			message.setStatus("Refused");
			this.closeAll();
			message.setHandled();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
