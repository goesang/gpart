package com.forweaver.gpart;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


// 티커에 표시될 피드들을 전체적으로 관리하는 부분
public class Feed {
	//싱글톤으로 구현
	private static Feed single = 	new Feed();
	  
	public int totalWidth; // 티커에 표시될 전체 길이를 저장
	
	private ArrayList<FeedParent> feedParentList; 	// 실제 정보를 담는 피드들의 목록
													//(피드 부모와 피드 자식으로 나눔)
	
	public static Feed getInstance(){
		return single;
	}
	
	private Feed(){
		this.totalWidth = 0;
		this.feedParentList = new ArrayList<FeedParent>();
	}
	
	public void load(){
		this.totalWidth = 0; // 피드의 길이를 0으로 초기화

		PreInfo.getInstance().load(); // 티커의 설정하는 정보를 불러옴
		
		for(FeedParent fp:feedParentList) // 피드의 부모들의 자식 목록을 초기화
			fp.clearFeedChildList();
	
		this.getFlattenChildList().clear(); // 피드 자식들만 담는 목록 초기화
		this.feedParentList.clear(); // 피드 부모들의 목록 초기화

		for(String strTmp : PreInfo.getInstance().feedItem ) 
			//피드 아이템(gpartFeedItem 파일의 내용)을 피드 부모 리스트에 하니씩 추가
			this.feedParentList.add(new FeedParent(strTmp.split("@w@")[0],
					strTmp.split("@w@")[1],
					Integer.parseInt(strTmp.split("@w@")[2])));
		ExecutorService indexLoadService = //쓰레드 풀 생성
			Executors.newFixedThreadPool(this.feedParentList.size());
			
		try{
			for(FeedParent fp:this.feedParentList)	// 쓰레드 시작
				indexLoadService.execute(fp);		
				
			indexLoadService.shutdown();			// 쓰레드 중지
		
			while(true){
				if(FeedParent.runNum == this.feedParentList.size())	{
					FeedParent.runNum = 0;
					break;	//쓰레드의 갯수를 세면서 대기(쓰레드가 모두 실행 되었다면 대기를 푼다) 
				}
				Thread.sleep(100);
			}
			
			this.compute();		// 티커의 표시될 피드의 길이를 계산함
			
		}catch (Exception e) {
			MessageDialog.openError(new Shell(), "Error", "Feed Load Error");
		}
	}		
	// 부모 피드들의 자식 피드 목록만 따로 이어 붙여 하나의 목록으로 만드는 메서드
	public ArrayList<FeedChild> getFlattenChildList(){		
		ArrayList<FeedChild> flattenChildList = new ArrayList<FeedChild>();
			
		for(FeedParent feedParent : this.feedParentList){
			flattenChildList.addAll(feedParent.feedChildList);
		}
		
		return flattenChildList;
	}
	// 부모 목록을 제공하는 메서드
	public ArrayList<FeedParent> getParentList(){
		
		return feedParentList;
	}
	// 실제 티커에 표시될 피드들의 크기를 계산하는 메서드
	private void compute(){
		// 자식 피드들의 목록을 받음
		ArrayList<FeedChild> flattenChildList = getFlattenChildList();
		// 계산하는데 쓰일 SWT Text
		Text text = 
			new Text(Shell.internal_new(Display.getCurrent(), SWT.NONE), SWT.NONE);
		
		for(final FeedChild feedChild : flattenChildList){
			//피드의 제목이 너무 길면 짜름
			if(feedChild.postTitle.length() > 50)
				feedChild.postTitle =	feedChild.
				postTitle.
				substring(0, 50)+"...";
			//피드의 내용이 너무 길면 짜름	
			if(feedChild.postContent.length() > PreInfo.getInstance().descLength)
				feedChild.postContent = feedChild.
				postContent.
				substring(0, PreInfo.getInstance().descLength)+"...";
		    //Text에 제목을 넣고 글꼴과 글 크기를 설정
			text.setText(feedChild.postTitle);
			text.setFont(new Font(Display.getCurrent(), PreInfo.getInstance().fontStyle));	
		    //현재 자식 피드의 위치를 설정
			feedChild.x = this.totalWidth;
			//자식 피드의 크기를 계산함
			feedChild.width = text.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
			this.totalWidth += feedChild.width+PreInfo.getInstance().spacing;
		}
		text.dispose();
	}

}

