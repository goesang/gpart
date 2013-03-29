package murunmo;
import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

//프로그램 제작자 소개 및 저작권 관련 소개 창
public class AboutWidget extends Dialog {

	// 싱글톤으로 만듬
	private static AboutWidget single = 
		new AboutWidget(Shell.internal_new(Display.getCurrent(), SWT.NONE),SWT.NONE);
	
	protected Object result;
	protected Shell shell;
	
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public static AboutWidget getInstance(){
		return single;
	}
	
	public AboutWidget(Shell parent, int style) {
		super(parent, style);
		setText(Messages.AboutWidget_title);
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		shell.pack();
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM);
		shell.setLayout(null);
		shell.setSize(273, 344);
		shell.setText(Messages.AboutWidget_about);
		Monitor primary = Display.getDefault().getPrimaryMonitor();
	 	Rectangle bounds = primary.getBounds();
	 	Rectangle rect = shell.getBounds();
	    
	 	int x = bounds.x + (bounds.width - rect.width) / 2;
	 	int y = bounds.y + (bounds.height - rect.height) / 2;
	    
	 	shell.setLocation(x, y);
	 	
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(0, 10, 265, 226);
		
		Label lblGpart = new Label(composite, SWT.NONE);
		lblGpart.setAlignment(SWT.CENTER);
		lblGpart.setBounds(10, 116, 245, 30);
		lblGpart.setText(Messages.AboutWidget_version);
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setAlignment(SWT.CENTER);
		lblNewLabel.setBounds(10, 152, 245, 22);
		lblNewLabel.setText(Messages.AboutWidget_desc);
		
		Label lblNewLabel_1 = new Label(composite, SWT.NONE);
		lblNewLabel_1.setAlignment(SWT.CENTER);
		lblNewLabel_1.setBounds(10, 180, 245, 15);
		lblNewLabel_1.setText(Messages.AboutWidget_copyright);
		
		Link link = new Link(composite, SWT.NONE);
		link.setBounds(48, 201, 185, 15);
		link.setText(Messages.AboutWidget_url);
		link.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event event) {
		    	  Program.launch(Messages.AboutWidget_link);
		      }
		    });
		String strPath = "resources"+File.separator+"img"+File.separator+"gpart.png";
		final Image image = new Image(shell.getDisplay(),strPath);
		Canvas canvas = new Canvas(composite, SWT.NONE);
		canvas.setBounds(85, 10, 95, 95);
		canvas.addPaintListener(new PaintListener() {
		      public void paintControl(PaintEvent e) {
		        e.gc.drawImage(image, 0, 0);
		        
		      }
		    });
		
		Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setBounds(0, 242, 265, 50);
		
		Button btnNewButton = new Button(composite_1, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				createContentsCredits();
			}
		});
		btnNewButton.setBounds(10, 10, 77, 27);
		btnNewButton.setText(Messages.AboutWidget_credits);
		
		Button btnNewButton_1 = new Button(composite_1, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				createContentsLicense();
			}
		});
		btnNewButton_1.setBounds(93, 10, 77, 27);
		btnNewButton_1.setText(Messages.AboutWidget_license);
		
		Button btnNewButton_2 = new Button(composite_1, SWT.NONE);
		btnNewButton_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		btnNewButton_2.setBounds(176, 10, 77, 27);
		btnNewButton_2.setText(Messages.AboutWidget_close);

	}
	private void createContentsCredits(){
		final Shell shlCredits = new Shell(shell,SWT.DIALOG_TRIM);
		shlCredits.setSize(249, 250);
		shlCredits.setText(Messages.AboutWidget_credits);
		Monitor primary = Display.getDefault().getPrimaryMonitor();
	 	Rectangle bounds = primary.getBounds();
	 	Rectangle rect = shlCredits.getBounds();
	    
	 	int x = bounds.x + (bounds.width - rect.width) / 2;
	 	int y = bounds.y + (bounds.height - rect.height) / 2;
	    
	 	shlCredits.setLocation(x, y);
		
		TabFolder tabFolder = new TabFolder(shlCredits, SWT.NONE);
		tabFolder.setBounds(10, 10, 224, 170);
		
		TabItem tbtmWritten = new TabItem(tabFolder, SWT.NONE);
		tbtmWritten.setText(Messages.AboutWidget_written);
		
		Link link = new Link(tabFolder, SWT.NONE);
		tbtmWritten.setControl(link);
		link.setText(Messages.AboutWidget_name);
		
		Button btnNewButton = new Button(shlCredits, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlCredits.close();
			}
		});
		btnNewButton.setBounds(157, 186, 77, 27);
		btnNewButton.setText(Messages.AboutWidget_close);
		shlCredits.open();
	}
	
	private void createContentsLicense(){
		final Shell shlLicense = new Shell(shell,SWT.DIALOG_TRIM);
		shlLicense.setSize(430, 347);
		shlLicense.setText(Messages.AboutWidget_license);
		Monitor primary = Display.getDefault().getPrimaryMonitor();
	 	Rectangle bounds = primary.getBounds();
	 	Rectangle rect = shlLicense.getBounds();
	    
	 	int x = bounds.x + (bounds.width - rect.width) / 2;
	 	int y = bounds.y + (bounds.height - rect.height) / 2;
	    
	 	shlLicense.setLocation(x, y);
		
		Button btnNewButton = new Button(shlLicense, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlLicense.close();
			}
		});
		btnNewButton.setBounds(335, 279, 77, 27);
		btnNewButton.setText(Messages.AboutWidget_close);
		
		Text text1 = new Text(shlLicense, SWT.MULTI | SWT.BORDER);
		text1.setText(Messages.AboutWidget_gpl);
		text1.setBounds(10, 10, 402, 263);
		shlLicense.open();
	}
	
}
