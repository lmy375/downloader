package downloader.ui.taskList;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import downloader.object.Task;



/**
 * DownloadProgressBar shows the current status of task especially the
 * finished percentage. Background color varies with variable status.
 * @author Moon
 *
 */
public class DownloadProgressBar extends JProgressBar implements TableCellRenderer {
	//private Task task;
	public DownloadProgressBar(){
		super(0,100);
		this.setStringPainted(true);
	}

	//public DownloadProgressBar(Task t) {
	//	super(0, 100);
	//	this.setStringPainted(true);
	//	this.setBackground(Color.blue);
	//	this.task = t;
	//	this.updateValue();

	//}

	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Task task = (Task)value;
		this.setValue(task.getFinishedSize() *100/ task.getFileSize());		
		int state = task.getCurrentStatus();
		
		switch (state) {
		
		case Task.DOWNLOADING:
			this.setForeground(new Color(0,191,255));
			this.setString(this.getValue() + "%");
			break;
		case Task.FINISHED:
			this.setString("Finished");
			this.setForeground(new Color(0,127,127));
			break;
		case Task.MERGING:
			this.setString("Merging");
			this.setForeground(new Color(186,85,211));
			break;
		case Task.PAUSED:
			this.setForeground(new Color(255,69,0));
			this.setString("Paused");
			break;
		case Task.PREPARING:
			this.setForeground(new Color(0,245,255));
			this.setString("Preparing");
			break;
		case Task.STOPPED:
			this.setForeground(Color.red);
			this.setString("Stopped");
		default:
			this.setForeground(Color.blue);
			this.setString(this.getValue() + "%");
			break;
		}
		return this;
	}
}