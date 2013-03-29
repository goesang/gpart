import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolTip;

// SWT로 구현된 티커 클래스
public class TickerWidget extends Dialog {
	// 싱글톤으로 구현
	private static TickerWidget single = 
		new TickerWidget(Shell.internal_new(Display.getCurrent(), SWT.NONE),SWT.NONE);
	
	public boolean hide;	// 티커를 숨길지 여부
	
	private final int TIMER_INTERVAL = 30; 	// 티커의 에니메이션 업데이트 간격 ( 리눅스의 경우 50이 적당합니다  그 이상은 cpu 부하 )
	private boolean autoScroll;				// 자동 스크롤 여부
	private boolean resize;					// 크기 재정의 여부
	private Point origin;		
	private Font font;
	private Canvas canvas;
	private Object result;
	private Shell shell;
	
	public static TickerWidget getInstance(){
		return single;
	}
	
	public TickerWidget(Shell parent, int style) {
		super(parent, style);
		setText("GPART Ticker");
	}
	
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		// 설정된 내용에 따라 티커를 보여줄지 결정
		if( PreInfo.getInstance().hideTicker )	
			this.hide();
		else	
			this.show();

		final Display display = Display.getDefault();
		
		Runnable runnable = new Runnable() {
			// 위젯을 새로그려 에니메이션을 실행
			public void run() {
				canvas.redraw();
				display.timerExec(TIMER_INTERVAL, this);
			}
		};
		display.timerExec(TIMER_INTERVAL, runnable);
		
		return result;
	}


	
	private void draw(){
	// 티커에서 피드 내용의 움직임을 구현하는 메서드
		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent event) {
				Image image = new Image(shell.getDisplay(), canvas.getBounds());
				GC gcImage = new GC(image);
				// 티커의 배경 및 글꼴 성정
				// 더블 버퍼링으로 구현
				gcImage.setBackground(event.gc.getBackground());
				gcImage.setForeground(event.gc.getForeground());
				gcImage.fillRectangle(image.getBounds());
				gcImage.setFont(font);
				gcImage.setClipping(0, 0, shell.getSize().x,shell.getSize().y);
				
				for(FeedChild fc : Feed.getInstance().getFlattenChildList()){
					// 피드의 내용을 확인하여 티커창 안에 피드가 위치하면 화면에 그림
			   		if(fc.x>-fc.width && fc.x <shell.getSize().x)
			   			gcImage.drawText(fc.postTitle, fc.x,3);
			   		// 피드를 전체적으로 왼쪽으로 옮김
			   		if(autoScroll) fc.x-=2;
	    	  		// 피드가 화면에서 사라지면 위치 재정의
	    	  		if(fc.x <= -fc.width ) fc.x += Feed.getInstance().totalWidth;
			    	  
				}
			   	event.gc.drawImage(image, 0, 0);
			   	image.dispose();
			   	gcImage.dispose();
			}
			
		});
	}
	
	private void mouse(){	// 마우스 이벤트 구현 메서드
		
		final ToolTip tip=new ToolTip(shell, SWT.BALLOON);
		// 손가락 모양 커서
		final Cursor cursorHand = new Cursor(Display.getDefault(), SWT.CURSOR_HAND);
		// 일반 모양 커서
		final Cursor cursorArrow = new Cursor(Display.getDefault(), SWT.CURSOR_ARROW);
		// 티커 이동시 커서
		final Cursor cursorSizeAll = new Cursor(Display.getDefault(), SWT.CURSOR_SIZEALL);
		// 티커 크기 조절시 커서
		final Cursor cursorSizeEE = new Cursor(Display.getDefault(), SWT.CURSOR_SIZEE);
		
		canvas.addMouseMoveListener(new MouseMoveListener() {
			@Override
			public void mouseMove(MouseEvent e) {
				// TODO Auto-generated method stub
				if (origin != null) {	// 티커 이동 및 티커 조절을 위한 Point 생성
					Point p = Display.getDefault().map(shell, null, e.x, e.y);
					shell.setLocation(p.x - origin.x, p.y - origin.y);
		            }
				
				if (resize) {	// 티커 크기 조절
					shell.setSize(e.x+5,shell.getSize().y);
		            }
			}
		});
			canvas.addMouseListener(new MouseListener() { // 마우스 클릭시 이벤트
				public void mouseDown(MouseEvent e) {	
					autoScroll = false;
					
					if(e.button == 1){ // 마우스 왼쪽 클릭시
						if(e.x >= shell.getSize().x -10){ // 오른쪽 부분을 클릭하면 크기 조절
							resize = true;
							canvas.setCursor(cursorSizeEE);
						}else{ // 내부를 클릭하면 클릭한 피드의 주소에 따라 링크 
							for(FeedChild fc : Feed.getInstance().getFlattenChildList()){
								
								if( e.x >= fc.x && 
									e.x <= fc.x+fc.width && 
									e.x <= shell.getSize().x -10 )
									Program.launch(fc.postLink);
							}
						}
					}
					else if(e.button == 3)	{ // 마우스 왼쪽 클릭시
						origin = new Point(e.x, e.y);	// 커서가 이동 커서로 바뀌고 티커 이동
						canvas.setCursor(cursorSizeAll);
					}
	        }
				public void mouseUp(MouseEvent e) { // 마우스 클릭 후
					origin = null;
	          }
				public void mouseDoubleClick(MouseEvent e) { }
			});
	    
			canvas.addMouseWheelListener(new MouseWheelListener() {
				public void mouseScrolled(MouseEvent e) { // 마우스 스크롤 발생시
					autoScroll = false;		// 티커 자동 스크롤 중지
					
					for(FeedChild fc : Feed.getInstance().getFlattenChildList()){
						if(e.count == -3) { // 마우스 스크롤 다운 시
							fc.x -= 12;		// 왼쪽으로 피드들이 이동함
						}
						else if(e.count == 3) { // 마우스 스크롤 업 시
												// 오른쪽으로 피드들이 이동함
							if(fc.x >= Feed.getInstance().totalWidth - fc.width) 
								fc.x -= Feed.getInstance().totalWidth;
							
							fc.x += 12; 
						}
					}
	          }
	        });
			canvas.addMouseTrackListener(new MouseTrackListener() {
				// 마우스가 티커 안으로 들어올시
				public void mouseEnter(MouseEvent e) {
					resize = false;
				// 티커 맨 오른쪽에 있을 경우 사이즈 변경 커서 변함
					if(e.x >= shell.getSize().x-10)
						canvas.setCursor(cursorSizeEE);
	    	  	}
				
				public void mouseExit(MouseEvent e) {
					autoScroll = true;
					resize = false;
					tip.setVisible(false);
					canvas.setCursor(cursorArrow);
	    		}
	    		public void mouseHover(MouseEvent e) {
	    			// 티커안에 마우스가 떠있는 경우
	    			autoScroll = false; // 자동 스크롤을 멈춤
	    			tip.setVisible(false);
	    			canvas.setCursor(cursorArrow);
	    		
	    			for(FeedChild fc : Feed.getInstance().getFlattenChildList()){
	    			// 마우스의 위치에 맞는 피드의 내용을 툴팁 형식으로 보여줌
	    				if(e.x >= fc.x && 
	    						e.x <= fc.x+fc.width && 
	    						e.x<=shell.getSize().x-10 &&
	    						fc.postContent.length() >= 3 ){	 
		    	  			
	    					tip.setMessage("");
	    					tip.setText("");
	    					tip.setText(fc.postDate);
	    					canvas.setCursor(cursorHand);
		    	  			
	    					if(shell.getLocation().y <= 750)
	    						tip.setLocation(shell.getLocation().x+e.x, 
	    										shell.getLocation().y+
	    										PreInfo.getInstance().fontHeight+10);
	    					else 
	    						tip.setLocation(shell.getLocation().x+e.x, 
	    										shell.getLocation().y-6);
		    	  			
	    					tip.setMessage(fc.postContent);
	    					tip.setVisible(true);
	    					canvas.setFocus();
	    				}
	    			}
	    		}
			});
	}
	
	public void hide(){ // 티커 숨김
		shell.setVisible(false);
		hide = true;
	}
	
	public void show(){ // 티커를 보여줌
		shell.setVisible(true);
		shell.forceActive();
		hide = false;
	}
	
	private void createContents() { // 티커를 생성하는 메서드
		this.autoScroll = true;

		PreInfo.getInstance().load(); // 티커를 설정하는 정보를 불러옴
		
		if(PreInfo.getInstance().onTop)
			shell = new Shell(getParent(), SWT.ON_TOP);
		else
			shell = new Shell(getParent(), SWT.NONE);
		
		this.shell.setSize(PreInfo.getInstance().width, PreInfo.getInstance().fontHeight+8);
		this.shell.setLocation(PreInfo.getInstance().positionX, PreInfo.getInstance().positionY);
		this.shell.setText(getText());
		this.shell.setLayout(new FillLayout());

		this.canvas = new Canvas(shell, SWT.DOUBLE_BUFFERED); // 더블 버퍼 옵션
		this.canvas.setBackground(new Color(Display.getDefault(),PreInfo.getInstance().backgroundColor));
		this.canvas.setForeground(new Color(Display.getDefault(),PreInfo.getInstance().fontColor));
			
		this.font = new Font(Display.getDefault(),PreInfo.getInstance().fontStyle);

		this.draw();
		this.mouse();
	}
	
	public void recreateContents(){ // 티커를 재구성하는 메서드
		PreInfo.getInstance().load();
		Shell shellTmp,delTmp;

		if(PreInfo.getInstance().onTop)	
			shellTmp = new Shell(getParent(), SWT.ON_TOP);
		else			
			shellTmp = new Shell(getParent(), SWT.NONE);
		
		delTmp = shell;
		shell = shellTmp;
		delTmp.dispose();
		
		this.shell.setSize(PreInfo.getInstance().width, PreInfo.getInstance().fontHeight+8);
		this.shell.setLocation(PreInfo.getInstance().positionX, PreInfo.getInstance().positionY);
		this.shell.setText(getText());
		this.shell.setLayout(new FillLayout());
	
		
		this.canvas.dispose();
		this.canvas = new Canvas(this.shell, SWT.DOUBLE_BUFFERED);
		this.canvas.setBackground(new Color(Display.getDefault(),PreInfo.getInstance().backgroundColor));
		this.canvas.setForeground(new Color(Display.getDefault(),PreInfo.getInstance().fontColor));
		
		this.font.dispose();
		this.font = new Font(Display.getDefault(),PreInfo.getInstance().fontStyle);
		
		if(PreInfo.getInstance().hideTicker)		this.hide();
		else		this.show();
		
		this.draw();
		this.mouse();
	}
	
	public int getTickerWidth(){
		return shell.getSize().x;
	}
	public int getTickerX(){
		return shell.getLocation().x;
	}
	public int getTickerY(){
		return shell.getLocation().y;
	}

}
