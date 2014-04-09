package downloader;
import downloader.object.Setting;
import downloader.ui.MainFrame;


/**
 * Main Class to execute this program , loadSetting and Initial the main frame.
 * @author Moon
 *
 */
public class Downloader {
	public static void main(String[] args){
		
		
		Setting.loadSetting();
		new MainFrame();
		// test code
		//Setting.saveSetting();
		//rl.closeServer();
//	NewTaskFrame n = new NewTaskFrame();
//	n.filePath.showOpenDialog(null);
//	Task t = new Task("http://www.baidu.com/img/baidu_sylogo1.gif","1.gif");
	//t.beginDownload();
	
	}
}
