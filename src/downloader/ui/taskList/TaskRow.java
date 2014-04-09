package downloader.ui.taskList;

import java.text.DecimalFormat;
import java.util.Vector;

import downloader.object.Task;


public class TaskRow extends Vector<Object>{
	
	private static final long serialVersionUID = 1L;

	private Task task;

	private String timeUsed;
	private int lastSize;
	private int finishedSize;
	private String speed;
	private String timeRemaining;
	private DecimalFormat df;
	
	/**
	 * Generates a new row for Task list according to the new task
	 * @param t The task
	 */
	public TaskRow(Task t){
		super();
		task = t;
		
		/**
		 * sets accuracy of numbers displayed
		 */
		df= new DecimalFormat();
		df.setMaximumFractionDigits(2);
		
		/**
		 * add components to vector
		 */
		this.add(" "+t.getFileName());		
		this.add(df.format((double)t.getFileSize()/(1024*1024.0))+" MB");
		this.add(t);
		this.add(new String("0 K/s"));
		this.add(new String("0 s"));
		this.add(new String(" "));
		
		lastSize = finishedSize = t.getFinishedSize();
		updateData();
	}
	
	/**
	 * Updates the data in this row according to the current data of task.
	 */
	public void updateData(){		
		
		speed = (String) this.get(3);
		timeUsed = (String) this.get(4);
		timeRemaining = (String) this.get(5);
		
		
		
		finishedSize = task.getFinishedSize();
		double speedInt = (finishedSize-lastSize)/1024.0/(TaskList.UPDATE_INTERVAL/1000.0);
		speed = df.format(speedInt) +" K/s";
		lastSize = finishedSize;
		
		timeUsed = task.getTimeUsed()+" s";
		if(speedInt!=0)
			timeRemaining = df.format((task.getFileSize()-task.getFinishedSize())/1024/speedInt)+" s";
		else timeRemaining = "¡Þ";
		
		//this.setElementAt(bar, 2);
		this.setElementAt(speed, 3);
		this.setElementAt(timeUsed, 4);
		this.setElementAt(timeRemaining, 5);
		
	}
	
	/**
	 * gets task contained by the row
	 * @return task in the row
	 */
	public Task getTask(){
		return this.task;
	}
}
