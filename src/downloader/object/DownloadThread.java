package downloader.object;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import javax.swing.JOptionPane;


/**
 * Task is separated to parts according to thread number
 * set by user.Download thread stand for each part which
 * run in its own thread and download data to its own file. 
 * @author Moon
 *
 */
public class DownloadThread implements Runnable , Serializable{

	/**
	 * Generate by system
	 */
	private static final long serialVersionUID = -7430419081946140L;
	private int begin= 0 ;
	private int end= 0 ;
	private int size = 0 ;
	private int threadID= 0 ;
	private String filePath;

	//private RandomAccessFile file;
	private int currentPosition= 0 ;

	private URL url;
	private final int BUFFER_SIZE =1024*8;

	private boolean isFinished= false;
	private boolean isPaused= false;
	private Task task;
	
	private int testTime;
	
	/**
	 * Constructs this class according specified task , beginning point,
	 * end point and ID(ID only used for debug).
	 * @param task  Task this thread derives from.
	 * @param begin Beginning point of file.
	 * @param end   End point of file.
	 * @param id	Thread ID.
	 */

	public DownloadThread(Task task, int begin, int end, int id) {
		super();

		this.task = task;
		this.url = this.task.getURL();
		this.begin = begin;
		this.end = end;

		this.size = end - begin;
		this.isFinished = false;
		this.isPaused = false;
		this.currentPosition = 0;
		this.threadID = id;
		this.filePath = task.getFilePath()+"\\" + UUID.randomUUID().toString()
				+ ".tmp";
		
		this.testTime= Setting.getTestTime();

		
		// this.test();
	}
	
	/**
	 * Starts this thread to download data
	 */
	public void startDownload(){
		Thread t = new Thread(this);
		t.start();
	}
	
	/**
	 * Implemented run function.
	 */
	public void run() {
		if (!isFinished) {
			// System.out.println("Begin to download in Thread"+this.threadID);
			download();
		}
		//System.out.print("Thread" +this.threadID+"Run ended");
	}

	/**
	 * Download function. Set request property about downloading position and
	 * get data from the server to write in random access file at the position
	 * of currentPosition.
	 */
	private void download() {
		RandomAccessFile file=null;
		try {
			file = new RandomAccessFile(this.filePath, "rwd");
			// System.out.println("File opened");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Range", "bytes=" + begin + "-" + end);

			conn.connect();
			BufferedInputStream bis = new BufferedInputStream(
					conn.getInputStream());
			byte[] buff = new byte[BUFFER_SIZE];

			file.seek(currentPosition);
			int length = 0;

			/* test code 
			 * System.out.println("InputStream started in Thread"+
			 this.threadID);
			System.out.println("Thread: "+ this.threadID+"\n"
					+"Length: "+ length+"\n"+
							"Begin : "+ begin+"\n"+
							"End: "+ end +"\n"
							+ "CurrentPosition" +this.currentPosition);
			*/
			while (!isFinished&& !isPaused && (length = bis.read(buff)) > 0
					&& begin < end) {
				//if(!isPaused){
					file.write(buff, 0, length);
					this.currentPosition += length;					
					this.begin += length;										
				//}
				//	if(isPaused) break;
			}
			/* test code 
			System.out.println("Thread: "+ this.threadID+"\n"
					+"Length: "+ length+"\n"+
							"Begin : "+ begin+"\n"+
							"End: "+ end +"\n"
							+ "CurrentPosition" +this.currentPosition);
			file.close();
			
			System.out.println("IsFinished is"+ isFinished);
			System.out.println("Begin is"+begin +" , End is "+ end);
			System.out.println("length is" + length);
			System.out.println("Loop condition is"+(!isFinished && (length = bis.read(buff)) > 0
					&& begin < end));
			System.out.println("IsPause is"+ isPaused);
			System.out.println("Thread"+this.threadID+"loop end");
			*/
			/**
			 * Test whether all threads are finished. If so, begin to merge all
			 * separated files.
			 */
			
			if (begin >= end) {
				file.close();
				isFinished = true;
				if (task.isReadyToMerge())
					task.merge();
			}

		} catch (IOException e) {
			//e.printStackTrace();
			if(this.testTime>0){

				this.testTime--;
				this.download();
			}
			else{
				if(!this.task.isError()){
					this.task.setError();
					int response = JOptionPane.showConfirmDialog(null, "Connection failed, try again?");
					if(response ==JOptionPane.YES_OPTION){
						this.testTime= 10;
						this.task.clearError();
						this.download();
					}
					else{						
						this.task.stop();						
					}				
				}

			}
		}
		
		
	}

	/**
	 * Tests whether thread is finished
	 * 
	 * @return true if thread is finished
	 */
	public boolean isFinished() {
		return isFinished;
	}

	/**
	 *Tests whether thread is paused
	 * 
	 * @return true if thread isPaused
	 */
	public boolean isPaused() {
		return isPaused;
	}

	/**
	 * Gets size of task file
	 * 
	 * @return size
	 */
	public int getSize() {
		return this.size;
	}

	/**
	 * Gets thread id
	 * 
	 * @return thread id
	 */
	public int getThreadID() {
		return this.threadID;
	}

	/**
	 * Gets temporary file path in this thread
	 * 
	 * @return file path
	 */
	public String getFilePath() {
		return this.filePath;
	}

	/**
	 * Gets size of finished data
	 * 
	 * @return current position
	 */
	public int getCurrentPos() {
		return this.currentPosition;
	}

	/**
	 * Sets pause state
	 * 
	 * @param isPaused
	 *            true to pause, false to resume
	 */
	public void setPause(boolean isPaused) {
		this.isPaused = isPaused;
	}
	
	
	/**
	 * print all elements for test
	 */
	/*
	 * public void test(){ System.out.println("Thread ID:" + this.threadID);
	 * System.out.println("FileName:"+this.filePath);
	 * System.out.println("Begin:"+this.begin);
	 * System.out.println("End:"+this.end);
	 * System.out.println("Size:"+this.size); }
	 */

}
