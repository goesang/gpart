package com.forweaver.gpart;

import java.text.SimpleDateFormat;
import java.util.Date;

// 자식 피드로 읽어온 피드의 제목,내용,날짜,주소 및 
// 티커의 표시용으로 쓰일 x축, 길이, 클릭 여부가 저장되는 클래스
public class FeedChild {
	public int x;				// 티커에서 계산되는 피드의 위치
	public int width;			// 티커에서 계산되는 피드의 길이
	public String postTitle;	// 포스트의 제목
	public String postContent;	// 포스트의 내용
	public Date postDate;		// 포스트의 날짜
	public String postLink;		// 포스트의 주소
	public String parentTitle;		// 포스트의 주소
	public boolean clicked;		// 클릭 여부(이부분은 추후 개발 예정)
	
	public FeedChild(){
		this.x = 0;
		this.width = 0;
		this.postTitle = new String("");
		this.postContent = new String("");
		this.postDate = null;
		this.postLink = new String("");
		this.parentTitle=new String("");
		this.clicked = false;
	}
	
	public String getPostFormatDate() {
		return new SimpleDateFormat("yy년 MM월 dd일 a hh시mm분 ss초").format(this.postDate);
	}
}
