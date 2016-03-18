package com.forweaver.gpart.gui;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.forweaver.gpart.Feed;
import com.forweaver.gpart.Messages;
import com.forweaver.gpart.Parser;
import com.forweaver.gpart.PreInfo;

//SWT로 구현한 프로그램의 환경설정창
public class PreferrenceWidget extends Dialog {
	private static PreferrenceWidget single = 
			new PreferrenceWidget(Shell.internal_new(Display.getCurrent(), SWT.NONE),SWT.NONE);


	private static int num;
	private Table table;

	protected Display display;
	protected Object result;
	protected Shell shell;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */

	public static PreferrenceWidget getInstance(){
		return single;
	}

	private PreferrenceWidget(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog"); //$NON-NLS-1$
		this.num = 0;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {

		Display display = getParent().getDisplay();

		if(num++ == 0) createContents();

		Monitor primary = display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();

		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;

		shell.setLocation(x, y);
		shell.open();
		shell.layout();
		shell.pack();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		num = 0;

		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {

		shell = new Shell(getParent(), SWT.ON_TOP | SWT.DIALOG_TRIM);
		shell.setSize(410, 539);
		shell.setText(getText());


		final Shell shlGpartPreferrence = shell;
		shlGpartPreferrence.setSize(410, 503);
		shlGpartPreferrence.setText(Messages.PreferrenceWidget_title);


		FormLayout fl_shlGpartPreferrence = new FormLayout();
		fl_shlGpartPreferrence.marginWidth = 3;
		fl_shlGpartPreferrence.marginTop = 3;
		fl_shlGpartPreferrence.marginRight = 3;
		fl_shlGpartPreferrence.marginLeft = 3;
		fl_shlGpartPreferrence.marginHeight = 3;
		fl_shlGpartPreferrence.marginBottom = 3;
		shlGpartPreferrence.setLayout(fl_shlGpartPreferrence);

		Composite composite = new Composite(shlGpartPreferrence, SWT.NONE);
		FormData fd_composite = new FormData();
		fd_composite.top = new FormAttachment(0, 5);
		fd_composite.left = new FormAttachment(0, 5);
		composite.setLayoutData(fd_composite);

		TabFolder tabFolder = new TabFolder(composite, SWT.NONE);
		tabFolder.setBounds(0, 0, 385, 427);
		// 첫번째 칼럼이 시작되는 코드 부분
		TabItem tbtmWidget = new TabItem(tabFolder, SWT.NONE);
		tbtmWidget.setText(Messages.PreferrenceWidget_widget);

		Composite compoWidget = new Composite(tabFolder, SWT.NONE);
		tbtmWidget.setControl(compoWidget);
		compoWidget.setLayout(null);
		// 티커의 x 축 설정	
		Label lableX = new Label(compoWidget, SWT.NONE);
		lableX.setBounds(30, 27, 71, 20);
		lableX.setText(Messages.PreferrenceWidget_x);

		final Spinner spinX = new Spinner(compoWidget, SWT.BORDER);
		spinX.setValues(PreInfo.getInstance().positionX, 0, 3000, 0, 1, 100);
		spinX.setBounds(240, 25, 120, 25);		
		// 티커의 y 축 설정
		Label labelY = new Label(compoWidget, SWT.NONE);
		labelY.setBounds(30, 58, 71, 20);
		labelY.setText(Messages.PreferrenceWidget_y);

		final Spinner spinY = new Spinner(compoWidget, SWT.BORDER);
		spinY.setValues(PreInfo.getInstance().positionY, 0, 3000, 0, 1, 100);
		spinY.setBounds(240, 58, 120, 25);
		// 티커의 길이 설정
		Label LabelWidth = new Label(compoWidget, SWT.NONE);
		LabelWidth.setBounds(30, 89, 62, 20);
		LabelWidth.setText(Messages.PreferrenceWidget_width);

		final Spinner spinWidth = new Spinner(compoWidget, SWT.BORDER);
		spinWidth.setValues(PreInfo.getInstance().width, 0, 3000, 0, 1, 100);
		spinWidth.setBounds(240, 89, 120, 25);
		// 티커가 숨겨져서 실행할지 설정
		Label labelHideTicker = new Label(compoWidget, SWT.NONE);
		labelHideTicker.setBounds(30, 250, 83, 20);
		labelHideTicker.setText(Messages.PreferrenceWidget_hide);

		final Button buttonHideTicker = new Button(compoWidget, SWT.CHECK);
		buttonHideTicker.setSelection(PreInfo.getInstance().hideTicker);
		buttonHideTicker.setBounds(240, 246, 18, 24);
		// 티커가 다른 위젯 보다 위에 배치될지 설정
		Label labelOnTop = new Label(compoWidget, SWT.NONE);
		labelOnTop.setBounds(30, 280, 82, 20);
		labelOnTop.setText(Messages.PreferrenceWidget_top);

		final Button buttonOnTop = new Button(compoWidget, SWT.CHECK);
		buttonOnTop.setBounds(240, 276, 18, 24);
		buttonOnTop.setSelection(PreInfo.getInstance().onTop);
		// 피드를 읽어오는 시간 설정
		Label labelUpdateInterval = new Label(compoWidget, SWT.NONE);
		labelUpdateInterval.setBounds(30, 312, 108, 20);
		labelUpdateInterval.setText(Messages.PreferrenceWidget_update);

		final Spinner spinUpdateInterval = new Spinner(compoWidget, SWT.BORDER);
		spinUpdateInterval.setValues(PreInfo.getInstance().updateInterval, 10, 80, 0, 10, 10);
		spinUpdateInterval.setBounds(240, 309, 120, 25);
		// 피드의 내용의 길이 조절
		Label labelDescLength = new Label(compoWidget, SWT.NONE);
		labelDescLength.setBounds(30, 343, 131, 20);
		labelDescLength.setText(Messages.PreferrenceWidget_desc);

		final Spinner spinDescLength = new Spinner(compoWidget, SWT.BORDER);
		spinDescLength.setLocation(240, 340);
		spinDescLength.setSize(120, 25);
		spinDescLength.setValues(PreInfo.getInstance().descLength, 0, 500, 0, 50, 100);


		// 피드의 유통 기한 조정
		Label labelFeedlimitDate = new Label(compoWidget, SWT.NONE);
		labelFeedlimitDate.setBounds(30, 373, 131, 20);
		labelFeedlimitDate.setText(Messages.PreferrenceWidget_feedLimitDate);

		final Spinner spinFeedlimitDate = new Spinner(compoWidget, SWT.BORDER);
		spinFeedlimitDate.setLocation(240, 370);
		spinFeedlimitDate.setSize(120, 25);
		spinFeedlimitDate.setValues(PreInfo.getInstance().limitDate, 0, 500, 0, 1, 10);

		// 글꼴 설정
		Label labelFontStyle = new Label(compoWidget, SWT.NONE);
		labelFontStyle.setLocation(30, 123);
		labelFontStyle.setSize(81, 20);
		labelFontStyle.setText(Messages.PreferrenceWidget_fontStyle);		

		final Button buttonFontStyle = new Button(compoWidget, SWT.NONE);
		buttonFontStyle.setLocation(240, 120);
		buttonFontStyle.setSize(120, 25);
		buttonFontStyle.setFont(new Font(display,new FontData(PreInfo.getInstance().fontStyle.getName(),9,PreInfo.getInstance().fontStyle.getStyle())));
		buttonFontStyle.setText(PreInfo.getInstance().fontStyle.getName()+" "+PreInfo.getInstance().fontStyle.getHeight()); //$NON-NLS-1$

		final FontData defaultFont = PreInfo.getInstance().fontStyle;

		buttonFontStyle.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FontDialog fd = new FontDialog(shlGpartPreferrence, SWT.NONE);
				fd.setText(Messages.PreferrenceWidget_selectFont);
				fd.setRGB(new RGB(0, 0, 255));
				fd.setFontData(defaultFont);
				FontData newFont = fd.open();
				if(newFont!=null){
					defaultFont.setHeight(newFont.getHeight());
					defaultFont.setName(newFont.getName());
					defaultFont.setStyle(newFont.getStyle());
					newFont.setHeight(9);
					buttonFontStyle.setFont(new Font(display,newFont));
					buttonFontStyle.setText(defaultFont.getName()+" "+defaultFont.getHeight()); //$NON-NLS-1$
				}
			}
		});

		// 글 색상 설정
		Label labelFontColor = new Label(compoWidget, SWT.NONE);
		labelFontColor.setLocation(30, 156);
		labelFontColor.setSize(81, 20);
		labelFontColor.setText(Messages.PreferrenceWidget_fontColor);

		final Canvas canvasFontColor = new Canvas(compoWidget, SWT.NONE);
		canvasFontColor.setLocation(240, 153);
		canvasFontColor.setSize(60, 25);
		canvasFontColor.setBackground(new Color(display, PreInfo.getInstance().fontColor));

		Button btnFontColor = new Button(compoWidget, SWT.NONE);
		btnFontColor.setLocation(300, 153);
		btnFontColor.setSize(60, 25);
		btnFontColor.setText(Messages.PreferrenceWidget_color);

		btnFontColor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ColorDialog cd = new ColorDialog(shlGpartPreferrence);
				cd.setText(Messages.PreferrenceWidget_selectColor);
				cd.setRGB(canvasFontColor.getBackground().getRGB());
				RGB newColor = cd.open();
				if(newColor != null)
					canvasFontColor.setBackground(new Color(display, newColor));
			}
		});

		// 티커 색상 설정
		Label labelBackgroundColor = new Label(compoWidget, SWT.NONE);
		labelBackgroundColor.setLocation(30, 188);
		labelBackgroundColor.setSize(120, 20);
		labelBackgroundColor.setText(Messages.PreferrenceWidget_bgColor);

		final Canvas canvasBackGroundColor = new Canvas(compoWidget, SWT.NONE);
		canvasBackGroundColor.setLocation(240, 184);
		canvasBackGroundColor.setSize(60, 25);
		canvasBackGroundColor.setBackground(new Color(display, PreInfo.getInstance().backgroundColor));

		Button btnBackGroundColor = new Button(compoWidget, SWT.NONE);
		btnBackGroundColor.setLocation(300, 184);
		btnBackGroundColor.setSize(60, 25);
		btnBackGroundColor.setText(Messages.PreferrenceWidget_color);

		btnBackGroundColor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ColorDialog cd = new ColorDialog(shlGpartPreferrence);
				cd.setText(Messages.PreferrenceWidget_selectColor);
				cd.setRGB(canvasBackGroundColor.getBackground().getRGB());
				RGB newColor = cd.open();

				if(newColor != null)
					canvasBackGroundColor.setBackground(new Color(display, newColor));

			}
		});

		// 티커에 표시되는 피드들의 간격을 설정
		Label labelSpacing = new Label(compoWidget, SWT.NONE);
		labelSpacing.setLocation(30, 220);
		labelSpacing.setSize(81, 20);
		labelSpacing.setText(Messages.PreferrenceWidget_spacing);

		final Spinner spinSpacing = new Spinner(compoWidget, SWT.BORDER);
		spinSpacing.setLocation(240, 215);
		spinSpacing.setSize(120, 25);
		spinSpacing.setValues(PreInfo.getInstance().spacing, 10, 100, 0, 1, 10);
		// 두번째 칼럼이 시작되는 코드 부분
		TabItem tbtmFeedItem = new TabItem(tabFolder, SWT.NONE);
		tbtmFeedItem.setText(Messages.PreferrenceWidget_feedItem);

		Composite compoFeedItem = new Composite(tabFolder, SWT.NONE);
		tbtmFeedItem.setControl(compoFeedItem);

		TableViewer tableViewer = new TableViewer(compoFeedItem, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setBounds(10, 10, 361, 304);
		table.setHeaderVisible(true);
		table.setToolTipText(Messages.PreferrenceWidget_tableTooltip);

		final TableEditor editor = new TableEditor(table);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;

		table.addMouseListener(new MouseAdapter() {
			//테이블을 두번 클릭했을때 이벤트
			//다음 프로그래머들의 소스를 참고함
			//http://www.java2s.com/Code/Java/SWT-JFace-Eclipse/DemonstratesTableEditor.htm
			//Rob Warner (rwarner@interspatial.com)
			//Robert Harris (rbrt_harris@yahoo.com)
			public void mouseDoubleClick(MouseEvent event) {
				Control old = editor.getEditor();
				if (old != null) 
					old.dispose();

				Point pt = new Point(event.x, event.y);

				final TableItem item = table.getItem(pt);

				if (item != null) {
					int column = -1;
					for (int i = 0, n = table.getColumnCount(); i < n; i++) {
						Rectangle rect = item.getBounds(i);
						if (rect.contains(pt)) {
							column = i;
							break;
						}
					}

					if (column == 2) {	// 두번째 칼럼을 클릭했을때
						final Spinner spin = new Spinner(table, SWT.NONE);
						spin.setValues(Integer.parseInt(item.getText(column)), 0, 30, 0, 1, 5);
						// spinner를 생성한다.
						editor.minimumWidth = 48;
						table.getColumn(column).setWidth(editor.minimumWidth);

						spin.setFocus();
						editor.setEditor(spin, item, column);

						final int col = column;
						spin.addSelectionListener(new SelectionAdapter() {
							public void widgetSelected(SelectionEvent event) {
								item.setText(col, spin.getText());
							}
						});

						spin.addFocusListener(new FocusListener() {

							public void focusLost(FocusEvent arg0) {
								// TODO Auto-generated method stub
								spin.setVisible(false);
							}

							public void focusGained(FocusEvent arg0) {
								// TODO Auto-generated method stub

							}
						});

					}else{
						final Text text = new Text(table, SWT.NONE);
						text.setForeground(item.getForeground());
						text.setText(item.getText(column));
						text.setForeground(item.getForeground());
						text.selectAll();
						text.setFocus();

						editor.setEditor(text, item, column);

						final int col = column;
						text.addModifyListener(new ModifyListener() {
							public void modifyText(ModifyEvent event) {

								item.setText(col, text.getText());
							}
						});
						text.addFocusListener(new FocusListener() {

							public void focusLost(FocusEvent e) {
								// TODO Auto-generated method stub
								text.setVisible(false);
							}

							public void focusGained(FocusEvent e) {
								// TODO Auto-generated method stub

							}
						});
					}
				}
			}
		});

		TableColumn tblclmnTitle = new TableColumn(table, SWT.NONE);
		tblclmnTitle.setWidth(100);
		tblclmnTitle.setText(Messages.PreferrenceWidget_feedTitle);

		TableColumn tblclmnUrl = new TableColumn(table, SWT.NONE);
		tblclmnUrl.setWidth(210);
		tblclmnUrl.setText(Messages.PreferrenceWidget_feedUrl);

		TableColumn tblclmnLimit = new TableColumn(table, SWT.CENTER);
		tblclmnLimit.setWidth(47);
		tblclmnLimit.setText(Messages.PreferrenceWidget_feedLimit);

		int i = 0;

		for(String strTmp : PreInfo.getInstance().feedItem){
			TableItem tableItem = new TableItem(table, i++);
			tableItem.setText(strTmp.split("@w@")); //$NON-NLS-1$
		}

		final Text textURL = new Text(compoFeedItem, SWT.BORDER);
		textURL.setBounds(47, 320, 324, 25);

		Label labelUrl = new Label(compoFeedItem, SWT.NONE);
		labelUrl.setBounds(10, 323, 38, 20);
		labelUrl.setText(Messages.PreferrenceWidget_url);

		Button btnRemove = new Button(compoFeedItem, SWT.NONE);
		btnRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				table.remove(table.getSelectionIndices());
			}
		});
		btnRemove.setBounds(296, 351, 77, 27);
		btnRemove.setText(Messages.PreferrenceWidget_remove);

		Button btnAdd = new Button(compoFeedItem, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() { //피드를 추가하는 부분
			@Override
			public void widgetSelected(SelectionEvent e) {

				String urlStr = textURL.getText();
				textURL.setText("");
				try{
					String title = "";

					if(urlStr.startsWith("https://www.facebook.com") && !urlStr.contains("/feeds/") || Parser.isNumber(urlStr)) // facebook
						title = Parser.getTitleInFacebookURL(urlStr);
					if(urlStr.startsWith("@") || urlStr.startsWith("#")) // twitter
						title = Parser.getTitleInTwitter(urlStr);
					else
						title = Parser.getTitleInXMLURL(urlStr); // rss or atom

					if(title.length() > 0){
						TableItem tableItem = new TableItem(table, table.getItemCount());
						tableItem.setText((title +"@w@"+urlStr+"@w@15").split("@w@"));
					}

				}//try
				catch(MalformedURLException ex){
					MessageDialog.openError(shlGpartPreferrence, "Error", Messages.PreferrenceWidget_urlError); 
				}
				catch(Exception ex) {
					MessageDialog.openError(shlGpartPreferrence, "Error", Messages.PreferrenceWidget_feedError); 
				}
			}
		});
		btnAdd.setBounds(213, 351, 77, 27);
		btnAdd.setText(Messages.PreferrenceWidget_add);

		Button btnUp = new Button(compoFeedItem, SWT.NONE);
		btnUp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int nextIndex = table.getSelectionIndex();
				int preIndex = nextIndex - 1;

				if( nextIndex == 0) return;

				String[] preStr = {table.getItem(preIndex).getText(0),table.getItem(preIndex).getText(1),table.getItem(preIndex).getText(2)};
				String[] nextStr = {table.getItem(nextIndex).getText(0),table.getItem(nextIndex).getText(1),table.getItem(nextIndex).getText(2)};

				table.getItem(preIndex).setText(nextStr);
				table.getItem(nextIndex).setText(preStr);
				table.setSelection(preIndex);
			}
		});
		btnUp.setBounds(47, 351, 77, 27);
		btnUp.setText(Messages.PreferrenceWidget_up);

		Button btnDown = new Button(compoFeedItem, SWT.NONE);
		btnDown.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int preIndex = table.getSelectionIndex();
				int nextIndex = preIndex + 1;

				if( preIndex == table.getItemCount()-1) return;
				String[] preStr = {table.getItem(preIndex).getText(0),table.getItem(preIndex).getText(1),table.getItem(preIndex).getText(2)};
				String[] nextStr = {table.getItem(nextIndex).getText(0),table.getItem(nextIndex).getText(1),table.getItem(nextIndex).getText(2)};

				table.getItem(preIndex).setText(nextStr);
				table.getItem(nextIndex).setText(preStr);
				table.setSelection(nextIndex);

			}
		});
		btnDown.setText(Messages.PreferrenceWidget_down);
		btnDown.setBounds(130, 351, 77, 27);

		Composite compoButton = new Composite(shlGpartPreferrence, SWT.NONE);
		FormData fd_compoButton = new FormData();
		fd_compoButton.right = new FormAttachment(composite, 0, SWT.RIGHT);
		fd_compoButton.bottom = new FormAttachment(100, -6);
		fd_compoButton.top = new FormAttachment(0, 431);
		fd_compoButton.left = new FormAttachment(0, 187);
		compoButton.setLayoutData(fd_compoButton);

		RowLayout rl_compoButton = new RowLayout(SWT.HORIZONTAL);
		compoButton.setLayout(rl_compoButton);
		// 변경된 내용을 저장하는 부분
		Button btnOk = new Button(compoButton, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final ArrayList<String> feedItemList = new ArrayList<String>();
				for(int i = 0;i<table.getItemCount();i++){
					feedItemList.add(table.getItem(i).getText(0)+"@w@"+table.getItem(i).getText(1)+"@w@"+table.getItem(i).getText(2)); 
				}// 글꼴과 글의 크기를 설정시 세로길이를 측정 하여 저장
				Text text = new Text(shlGpartPreferrence, SWT.NONE);
				text.setText("AAA");
				text.setFont(new Font(display, defaultFont));

				PreInfo.getInstance().save(	spinX.getSelection(), 
						spinY.getSelection(), 				    						
						spinWidth.getSelection(),								    	
						text.getLineHeight(),		
						spinSpacing.getSelection(),				
						spinDescLength.getSelection(),		
						spinUpdateInterval.getSelection(), 			
						buttonHideTicker.getSelection(),		
						buttonOnTop.getSelection(),
						defaultFont, 
						canvasFontColor.getBackground().getRGB(), 
						canvasBackGroundColor.getBackground().getRGB(),
						spinFeedlimitDate.getSelection(),
						feedItemList);
				text.dispose();
				shlGpartPreferrence.dispose();
				Feed.getInstance().load();
				TickerWidget.getInstance().recreateContents();
				TrayWidget.getInstance().recreateMenuRSS();
			}
		});
		btnOk.setLayoutData(new RowData(95, SWT.DEFAULT));
		btnOk.setText(Messages.PreferrenceWidget_ok);

		Button btnCancel = new Button(compoButton, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				shlGpartPreferrence.close();
			}
		});
		btnCancel.setLayoutData(new RowData(95, SWT.DEFAULT));
		btnCancel.setText(Messages.PreferrenceWidget_cancel);

	}
}
