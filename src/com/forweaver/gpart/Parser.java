package com.forweaver.gpart;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Group;
import facebook4j.Page;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.auth.AccessToken;

public class Parser {

	private static String accessTokenString = "";
	private static String oauthConsumerKey = "";
	private static String oauthConsumerSecret = "";
	private static String oauthAccessToken = "";
	private static String oauthAccessTokenSecret = "";

	public static String getTitleInTwitter(String urlStr) throws TwitterException{
		if(urlStr.startsWith("#"))
			return "Twitter - "+urlStr;
		if(!urlStr.startsWith("@"))
			return "";
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey(oauthConsumerKey)
		.setOAuthConsumerSecret(oauthConsumerSecret)
		.setOAuthAccessToken(oauthAccessToken)
		.setOAuthAccessTokenSecret(oauthAccessTokenSecret);
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		User user = twitter.showUser(urlStr.substring(1));

		return "Twitter - @"+user.getScreenName();
	}

	public static String getTitleInFacebookURL(String urlStr) throws FacebookException{
		Facebook facebook = new FacebookFactory().getInstance();
		facebook.setOAuthAppId("", "");
		AccessToken at = new AccessToken(accessTokenString);
		facebook.setOAuthAccessToken(at);
		Group group = null;
		Page page = null;
		if(urlStr.startsWith("https://www.facebook.com")){
			if(urlStr.split("/")[3].equals("groups")){
				group = facebook.getGroup(urlStr.split("/")[4]);
				return group.getName();
			}else{
				page = facebook.getPage(urlStr.split("/")[3]);
				return page.getName();
			}

		}
		group = facebook.getGroup(urlStr);
		if(group != null)
			return group.getName();

		page = facebook.getPage(urlStr);
		return page.getName();			
	}

	public static String getTitleInXMLURL(String urlStr) throws MalformedURLException,Exception{
		//사이트의 정보를 읽어와서 이것이 RSS가 구독이 가능한지 여부를 판별하고 
		//http://sirini.net/grboard2/blog/view/573

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		URL url = new URL(urlStr); 
		URLConnection conn = url.openConnection();
		conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:42.0) Gecko/20100101 Firefox/42.0");
		conn.connect();
		Document doc = builder.parse(conn.getInputStream(),"utf-8");
		doc.getDocumentElement().normalize();
		NodeList channel,itemlist;
		if(doc.getElementsByTagName("rss").getLength()==1){ // rss의 경우
			channel = doc.getElementsByTagName("channel"); //rss에서 channel 부분 파싱
			itemlist = doc.getElementsByTagName("item"); //rss에서 item 부분 파싱
		}else{
			channel = doc.getElementsByTagName("feed"); //atom에서 feed 부분 파싱
			itemlist = doc.getElementsByTagName("entry"); //atom에서 entry 부분 파싱
		}
		if(itemlist.getLength() == 0) // 읽어온 내용이 없으면 에러
			throw new Exception(Messages.PreferrenceWidget_blankError);

		Node item = channel.item(0);

		if(item.getNodeType() == Node.ELEMENT_NODE){
			Element itemTmp = (Element)item;
			NodeList title = itemTmp.getElementsByTagName("title");
			return title.item(0).getTextContent();
		}

		return "";
	}

	public static void getFeedInTwitter(FeedParent fp) throws TwitterException{
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey(oauthConsumerKey)
		.setOAuthConsumerSecret(oauthConsumerSecret)
		.setOAuthAccessToken(oauthAccessToken)
		.setOAuthAccessTokenSecret(oauthAccessTokenSecret);
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();

		List<Status> statusList = null;

		if(fp.url.startsWith("#")){
			Query query = new Query(fp.url);
			query.count(fp.limit);
			QueryResult result = twitter.search(query);
			statusList = result.getTweets();

		}

		if(fp.url.startsWith("@"))
			statusList = twitter.getUserTimeline(fp.url.substring(1), new Paging(1, fp.limit));


		for (Status status :  statusList)
			if(status.getText() != null){
				FeedChild fc = new FeedChild();

				fc.postDate =status.getCreatedAt().toString();
				fc.postContent = "@" + status.getUser().getScreenName() + " - " + status.getText();
				fc.parentTitle = fp.title;
				fc.postContent = fc.postContent.replaceAll("\\<[^>]*>","");
				fc.postContent= fc.postContent.replaceAll("\r|\n|&nbsp;","").trim();
				fc.postTitle = fc.postContent;
				fc.postLink = "https://twitter.com/"+status.getUser().getScreenName()+"/status/"+status.getId();
				fp.feedChildList.add(fc);
			}
	}

	public static void getFeedInFacebookURl(FeedParent fp) throws FacebookException{
		Facebook facebook = new FacebookFactory().getInstance();
		facebook.setOAuthAppId("", "");
		AccessToken at = new AccessToken(accessTokenString);
		facebook.setOAuthAccessToken(at);
		List<Post> posts = null;
		String urlStr = getNameInFacebookURL(fp.url);
		try{
			posts = facebook.getFeed(urlStr,new Reading().limit(fp.limit));
		}catch(FacebookException e){
			posts = facebook.getGroupFeed(urlStr,new Reading().limit(fp.limit));
		}
		for(Post feed:posts)
			if(feed.getMessage() != null){
				FeedChild fc = new FeedChild();
				if(feed.getCreatedTime()!=null)
					fc.postDate =feed.getCreatedTime().toString();
				else
					fc.postDate =feed.getUpdatedTime().toString();
				fc.postContent = feed.getMessage();
				fc.parentTitle = fp.title;
				fc.postContent = fc.postContent.replaceAll("\\<[^>]*>","");
				fc.postContent= fc.postContent.replaceAll("\r|\n|&nbsp;","").trim();
				fc.postTitle = fc.postContent;
				fc.postLink = "https://www.facebook.com/"+feed.getId();
				fp.feedChildList.add(fc);
			}


	}

	public static void getFeedInXMLURL(FeedParent fp) throws Exception{
		//RSS를 파싱하기 위한 초기화
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		URL url = new URL(fp.url); 
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

		for(int i=0; (i < itemLst.getLength()) && (i < fp.limit); i++){
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
			fc.parentTitle = fp.title;
			fc.postDate = date.item(0).getTextContent();
			fc.postContent = description.item(0).getTextContent();
			fc.postContent = fc.postContent.replaceAll("\\<[^>]*>","");
			fc.postContent= fc.postContent.replaceAll("\r|\n|&nbsp;","").trim();

			fp.feedChildList.add(fc);
		}

	}
	public static String getNameInFacebookURL(String url){

		if(url.startsWith("https://www.facebook.com")){
			if(url.split("/")[3].equals("groups")){
				return url.split("/")[4];
			}
			if(url.split("/")[3].equals("feeds")){
				return "";
			}

			return url.split("/")[3];

		} 

		if(isNumber(url))
			return url;

		return "";
	}

	public static boolean isNumber(String str){        
		try{
			Double.parseDouble(str) ;
			return true ;
		}catch(Exception e){}
		return false ;
	}
}
