package downloader.ui.taskList;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

import downloader.object.Task;
import downloader.ui.MainFrame;


public class TaskList extends JTable implements Runnable {
	
	
	private static final long serialVersionUID = 1L;
	private DefaultTableModel model;
	private Thread runner;
	public final static int UPDATE_INTERVAL = 1000;

	public TaskList(MainFrame main) {
		super();
		/**
		 * Set UI.
		 */
		model = new DefaultTableModel(){
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
	                return false;
	            }
		};
		model.setColumnIdentifiers(new Object[] { "Name", "Size", "Status",
				"Speed", "Time Used", "Time Remaining" });
		
		// this.getColumnModel().getColumn(3).setCellRenderer(new
		// DownloadProgressBar());
		model.setRowCount(0);
		this.setModel(model);
		this.setAutoCreateRowSorter(true);
		this.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		this.setShowGrid(false);
		this.setAutoscrolls(true);
		this.getTableHeader().setReorderingAllowed(false);
		
		
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

		runner = new Thread(this);
		runner.start();
		
	}
	
	/**
	 * Add item to the list .
	 * @param t Task to add.
	 */
	public void addTask(Task t) {
		model.addRow(new TaskRow(t));		
		this.getColumnModel().getColumn(2).setCellRenderer(new DownloadProgressBar());
		
	}
//	public void getModel()

	@Override
	public void run() {
		
		while (true) {
			try {
				Thread.sleep(UPDATE_INTERVAL);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int i = 0; i < model.getRowCount(); i++) {				
				TaskRow row = (TaskRow) model.getDataVector().elementAt(i);
			//	System.out.println("Updating");
				row.updateData();
			}
			this.repaint();
		}
	}

}
