package murunmo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

// 실제 실행을 담당하는 main 메서드
public class Main {

  public static void main(String[] args) {
    Display display = new Display();
    Shell shell = Shell.internal_new(display, 0);
	Feed.getInstance().load();
	TickerWidget.getInstance().open();
	TrayWidget.getInstance().open();

	
	while (!shell.isDisposed()) {
	      if (!display.readAndDispatch())
	        display.sleep();
	    }
	    display.dispose();

  }
  
}