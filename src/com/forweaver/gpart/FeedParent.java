package com.forweaver.gpart;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// 부모 피드로 피드를 불러올 사이트의 제목, 주소를 담고 있으며
// 불러오는 횟수와 불러온 자식 피드들의 목록을 저장하고 있는 클래스이다.
public class FeedParent extends Thread {
	static int runNum =0; 	// 부모 피드들이 실행이 성공한 횟수를 저장하는 필드

	public String title;	// 사이트의 제목
	public String url;		// 사이트의 주소
	public int limit;		// 불러올 획수
	public ArrayList<FeedChild> feedChildList;	// 붑러온 자식들의 목록

	public FeedParent(String title,String url,int limit){
		this.title = title;
		this.url = url;
		this.limit = limit;
		this.feedChildList = new ArrayList<FeedChild>();
	}


	public void run() {	// 병렬화 하여 피드들읠 긁어 오는 메서드
		try{
			//RSS를 파싱하기 위한 초기화
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			URL url = new URL(this.url); 
			URLConnection conn = url.openConnection();
           conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:42.0) Gecko/20100101 Firefox/42.0");
			conn.connect();
			Document doc = builder.parse(conn.getInputStream(),"utf-8");
			doc.getDocumentElement().normalize();
			
			boolean isRss=false;
			
			if(doc.getElementsByTagName("rss").getLength() == 1) //RSS인지 판별
				isRss= true;
			
			NodeList itemLst;
			
			if(isRss)
				itemLst = doc.getElementsByTagName("item"); //RSS라면 item을
			else
				itemLst = doc.getElementsByTagName("entry"); //Atom이라면 entry를
			
			for(int i=0; (i < itemLst.getLength()) && (i < this.limit); i++){
				Node item = itemLst.item(i);
				Element itemTmp = (Element)item;

				FeedChild fc = new FeedChild();

				NodeList title = itemTmp.getElementsByTagName("title");
				NodeList link = itemTmp.getElementsByTagName("link");
				NodeList description;
				NodeList date;
				if(isRss){
					description = itemTmp.getElementsByTagName("description");
					date = itemTmp.getElementsByTagName("pubDate");
					if(date.getLength() == 0)
						date = itemTmp.getElementsByTagName("dc:date");
					fc.postLink = link.item(0).getTextContent();
				}else{
					description = itemTmp.getElementsByTagName("summary");
					if(description.getLength() == 0)
						description = itemTmp.getElementsByTagName("content");
					date = itemTmp.getElementsByTagName("published");
					if(date.getLength() == 0)
						date = itemTmp.getElementsByTagName("updated");
					Element element = (Element) link.item(0);
					fc.postLink = element.getAttribute("href");
				}				
				fc.postTitle = title.item(0).getTextContent();
				
				fc.postDate = date.item(0).getTextContent();
				fc.postContent = description.item(0).getTextContent();
				fc.postContent = fc.postContent.replaceAll("\\<[^>]*>","");
				fc.postContent= fc.postContent.replaceAll("\r|\n|&nbsp;","").trim();

				this.feedChildList.add(fc);

			}
		}
		catch(Exception e){}
		finally {runNum++;	} // 실행이 끝나면 횟수를 증가시킨다.
	}

	public void clearFeedChildList(){	// 자식피드를 초기화하는 메서드
		this.feedChildList.clear();	
	}


}
