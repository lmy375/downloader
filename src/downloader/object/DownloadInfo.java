package downloader.object;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.table.DefaultTableModel;

import downloader.ui.taskList.TaskList;
import downloader.ui.taskList.TaskRow;

/**
 * Download information Class with two static function to save or load task information
 * @author Moon
 *
 */
public class DownloadInfo {
		
	/**
	 * Saves all tasks picked from current TaskList in file "task.info". 
	 * @param taskList  The TaskList to save.
	 * @return 
	 * 			0 if saving successfully.
	 * 			Task status  that lead to saving exception if saving unsuccessfully.
	 * 			(only PREPARING status allowed in current version)
	 */
	public static int saveTasksInfo(TaskList taskList){
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream("tasks.info");
			oos = new ObjectOutputStream(fos);
			DefaultTableModel model = (DefaultTableModel) taskList.getModel();
			
			for (int i = 0; i < model.getRowCount(); i++) {				
				TaskRow row = (TaskRow)  model.getDataVector().elementAt(i);
				Task t  = row.getTask();
				if(t.getCurrentStatus()==Task.DOWNLOADING) t.pause();
				if(t.getCurrentStatus()==Task.PREPARING) return t.getCurrentStatus();
				//if(t.getCurrentStatus()==Task.MERGING) return t.getCurrentStatus();
			
				oos.writeObject(t);				
			}			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}finally{
			try {
				if(oos!=null)oos.close();
				if(fos!= null)fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
		}
		return 0 ;
	}
	
	/**
	 * Loads task information from file "task.info" to current task list in main frame
	 * @param taskList target taskList to load information 
	 */
	public static void loadTaskInfo(TaskList taskList){
		FileInputStream fis = null;
		ObjectInputStream ois = null;
			try {
				fis = new FileInputStream("tasks.info");
				ois = new ObjectInputStream(fis);
				Task t;
				while((t = (Task) ois.readObject())!=null){
					taskList.addTask(t);
					//if(t.getCurrentStatus()==Task.PAUSED) t.pause();
				}
				
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				
			}finally{
				try {
					if(fis!=null)fis.close();
					if(ois!= null)ois.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
						
			}
			
		
	
	}

}
