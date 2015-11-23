package com.forweaver.gpart.gui;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

import com.forweaver.gpart.Feed;
import com.forweaver.gpart.FeedChild;
import com.forweaver.gpart.FeedParent;
import com.forweaver.gpart.Messages;
import com.forweaver.gpart.PreInfo;
// SWT로 구현된 티커를 관리할 트레이 클래스 
public class TrayWidget extends Dialog {
	// 싱글톤으로 구현
	private static TrayWidget single = 
		new TrayWidget(Shell.internal_new(Display.getCurrent(), SWT.NONE),SWT.NONE);
	// 분단위 읽어오기를 위한 상수
	final static int TIME = 1000 * 60;
	
	private MenuItem menuItemRSS;
	private int menuItemRSSIndex = 3;
	protected Object result;
	protected Shell shell;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	
	public static TrayWidget getInstance(){
		return single;
	}
	
	private TrayWidget(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		
		shell.layout();
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {	
		shell = new Shell(getParent(), getStyle());
		shell.setSize(450, 300);
		shell.setText(getText());
		// 트레이의 아이콘을 불러옴
		Image image = new Image(shell.getDisplay(),this.getClass().getResourceAsStream("/img/gpart.png"));

		final Tray tray = shell.getDisplay().getSystemTray();
		if (tray == null) {
			 MessageDialog.openError(new Shell(), Messages.Error, Messages.TrayWidget_systemError);
		} else {
			final TrayItem item = new TrayItem(tray, SWT.NONE);
			item.setToolTipText(Messages.TrayWidget_title);

			item.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					if (TickerWidget.getInstance().hide)			
						TickerWidget.getInstance().show();
					else TickerWidget.getInstance().hide();
				}
			});
			final Menu menu = new Menu(shell, SWT.POP_UP);

			MenuItem miInfo = new MenuItem(menu, SWT.POP_UP);
			miInfo.setText(Messages.TrayWidget_about);
			miInfo.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					AboutWidget.getInstance().open();
				}
			});
			
			
			MenuItem miPre = new MenuItem(menu, SWT.POP_UP);
			miPre.setText(Messages.TrayWidget_preferrence);
			miPre.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					PreferrenceWidget.getInstance().open();
				}
			});

			MenuItem miUp = new MenuItem(menu, SWT.POP_UP);
			miUp.setText(Messages.TrayWidget_rssUpdate);
			miUp.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					Feed.getInstance().load();
					recreateMenuRSS();
					System.gc();
				}
			});

			createMenuRSS(menu);
			MenuItem miSep = new MenuItem(menu, SWT.SEPARATOR);
			MenuItem miExit = new MenuItem(menu, SWT.POP_UP);
			miExit.setText(Messages.TrayWidget_exit);
			miExit.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					System.exit(0);
				}
			});

			item.addListener(SWT.MenuDetect, new Listener() {
				public void handleEvent(Event event) {
					menu.setVisible(true);
				}
			});
			item.setImage(image);
		}

		Runnable runnable = new Runnable() { // 일정 시간이 되면 피드를 다시 읽어들임
			public void run() {
				PreInfo.getInstance().load();
				Feed.getInstance().load();
				recreateMenuRSS();
				System.gc();
				shell.getDisplay().timerExec(TIME*PreInfo.getInstance().updateInterval, this);
			}
		};
		PreInfo.getInstance().load();
		shell.getDisplay().timerExec(TIME*PreInfo.getInstance().updateInterval, runnable);
	}

	private void createMenuRSS(Menu menu) {	// 피드의 내용을 메뉴 형식으로 보여주는 메서드
		if(menuItemRSSIndex == 4) menuItemRSSIndex--;
		this.menuItemRSS = new MenuItem(menu, SWT.CASCADE,menuItemRSSIndex++);
		menuItemRSS.setText(Messages.TrayWidget_rssFeed);
	
		Menu newMenu = new Menu(menu);
		menuItemRSS.setMenu(newMenu);
		// 부모 피드와 자식 피드을 불러와 메뉴를 구성한다.
		for (FeedParent feedParent : Feed.getInstance().getParentList()) {
			MenuItem rssMenuItem = new MenuItem(newMenu, SWT.CASCADE);
			if(feedParent.title.length()>50) // 제목이 길면 제한을 둠
				rssMenuItem.setText(feedParent.title.substring(0, 50)+"...");
			else
				rssMenuItem.setText(feedParent.title);
			Menu newMenu2 = new Menu(newMenu);
			rssMenuItem.setMenu(newMenu2);

			for (FeedChild feedChild : feedParent.feedChildList) {
				MenuItem rssChild = new MenuItem(newMenu2, SWT.PUSH);
				rssChild.setText(feedChild.postTitle);
				
				final String postLink = feedChild.postLink;
				rssChild.addSelectionListener(new SelectionListener() {
					
					public void widgetSelected(SelectionEvent e) {
						// TODO Auto-generated method stub
						Program.launch(postLink);
					}
					
					public void widgetDefaultSelected(SelectionEvent e) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		}
		
	}
	
	public void recreateMenuRSS(){ // RSS 메뉴 재구성 메서드
		Menu menuTmp = this.menuItemRSS.getMenu().getParentMenu();
		this.menuItemRSS.dispose();
		createMenuRSS(menuTmp);
	}

}
