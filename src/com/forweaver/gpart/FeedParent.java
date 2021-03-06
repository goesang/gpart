package com.forweaver.gpart;
import java.util.ArrayList;

// 부모 피드로 피드를 불러올 사이트의 제목, 주소를 담고 있으며
// 불러오는 횟수와 불러온 자식 피드들의 목록을 저장하고 있는 클래스이다.
public class FeedParent extends Thread {
	static int runNum =0; 	// 부모 피드들이 실행이 성공한 횟수를 저장하는 필드

	public String title;	// 사이트의 제목
	public String url;		// 사이트의 주소
	public int limit;		// 불러올 갯수
	public ArrayList<FeedChild> feedChildList;	// 붑러온 자식들의 목록

	public FeedParent(String title,String url,int limit){
		this.title = title;
		this.url = url;
		this.limit = limit;
		this.feedChildList = new ArrayList<FeedChild>();
	}


	public void run() {	// 병렬화 하여 피드들을 긁어 오는 메서드
		try{
			if((this.url.startsWith("https://www.facebook.com")&&!this.url.contains("/feeds/"))
					|| Parser.isNumber(this.url))
				Parser.getFeedInFacebookURl(this);
			if(this.url.startsWith("@") || this.url.startsWith("#"))
				Parser.getFeedInTwitter(this);
			else
				Parser.getFeedInXMLURL(this);
		}
		catch(Exception e){}
		finally {runNum++;	} // 실행이 끝나면 횟수를 증가시킨다.
	}

	public void clearFeedChildList(){	// 자식피드를 초기화하는 메서드
		this.feedChildList.clear();	
	}


}
