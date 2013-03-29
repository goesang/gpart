package murunmo;
import java.net.URL;
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
			Document doc = builder.parse(url.openStream(),"utf-8");
			doc.getDocumentElement().normalize();
			
			NodeList itemLst = doc.getElementsByTagName("item");
    			
    		for(int i=0; (i < itemLst.getLength()) && (i < this.limit); i++){
    			Node item = itemLst.item(i);
    			Element itemTmp = (Element)item;
    			
    			FeedChild fc = new FeedChild();
    				
    			NodeList title = itemTmp.getElementsByTagName("title");
    			NodeList link = itemTmp.getElementsByTagName("link");
    			NodeList description = itemTmp.getElementsByTagName("description");
    			NodeList date = itemTmp.getElementsByTagName("pubDate");
    				
    			if(date.getLength() == 0)
    				date = itemTmp.getElementsByTagName("dc:date");        
    			
    			fc.postTitle = title.item(0).getTextContent();
    			fc.postLink = link.item(0).getTextContent();
    			fc.postDate = date.item(0).getTextContent();
    			fc.postContent = // 공백을 제거하고 내용을 불러온다/
    				description.item(0).getTextContent().replaceAll("\\<[^>]*>","").replaceAll("\r|\n|&nbsp;","");

    			this.feedChildList.add(fc);
    				
    		}
    	}
		catch(Exception e){	}
		finally {	runNum++;	} // 실행이 끝나면 횟수를 증가시킨다.
	}
	
	public void clearFeedChildList(){	// 자식피드를 초기화하는 메서드
		this.feedChildList.clear();	
	}

	
}
