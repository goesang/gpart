package com.forweaver.gpart;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Shell;

// 티커의 정보를 관리하는 클래스
public class PreInfo {
	// 싱글톤으로 구현
	private static PreInfo single = new PreInfo();

	public int positionX;
	public int positionY;
	public int width;
	public int fontHeight;
	public int spacing;
	public int descLength;
	public int updateInterval;
	public boolean hideTicker;
	public boolean onTop;
	public FontData fontStyle;
	public RGB fontColor;
	public RGB backgroundColor;
	public int limitDate;

	public ArrayList<String> feedItem; // 읽어 올 정보들을 담은 목록

	private String filePath;

	public static PreInfo getInstance(){
		return single;
	}


	private PreInfo(){
		this.positionX = 0;
		this.positionY = 0;
		this.width = 0;
		this.fontHeight = 0;
		this.spacing = 0;
		this.descLength = 0;
		this.updateInterval = 0;
		this.hideTicker = false;
		this.onTop = false;
		this.fontStyle = new FontData();
		this.fontColor = new RGB(0,0,0);
		this.backgroundColor = new RGB(0,0,0);
		this.feedItem = new ArrayList<String>();
		this.limitDate = 0;
		File dir;
		//만일 리눅스일 경우 사용자 디렉토리에 설정 파일을 생성한다
		if(System.getProperty("os.name").indexOf("Linux") == 0){
			this.filePath = File.separator + "home" + 
					File.separator + System.getProperty("user.name")+
					File.separator +".gpart";
			dir = new File(filePath);

		}//만일 윈도우일 경우 유저 이름으로 폴더를 만들고 설정 파일을 생성한다
		else{
			this.filePath = "C:"+File.separator+"Users"+File.separator+System.getProperty("user.name")+File.separator+"gpart";
			dir = new File(filePath);
		}

		if(dir.isDirectory())
			this.filePath += File.separator;
		else{
			MessageDialog.openError(new Shell(), Messages.Alert, Messages.Alert_Create_Direcitory+" "+filePath); 
			if (!dir.mkdirs())
				MessageDialog.openError(new Shell(), Messages.Error, Messages.Error); 
			this.filePath += File.separator;

			ArrayList<String> feedItem = new ArrayList<String>();
			feedItem.add("Stack Overflow@w@http://stackoverflow.com/feeds@w@15");
			feedItem.add("google news korea@w@https://news.google.co.kr/news?cf=all&hl=ko&pz=1&ned=kr&output=rss@w@15");

			this.save(0, 0, 900, 17, 
					30, 300, 30, false, 
					false, new FontData("Sans",10,0),
					new RGB(255,255,255), 
					new RGB(77,77,77),
					7,
					feedItem);
		}
	}



	public void load(){ // 파일에서 정보를 가져옴
		try {
			FileInputStream fileInputStream = new FileInputStream(filePath+"gpartSet");
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "MS949");
			BufferedReader bufferReader = new BufferedReader(inputStreamReader);

			String str = bufferReader.readLine();
			String strArray[] = str.split("@w@");

			this.positionX = Integer.parseInt(strArray[0]);
			this.positionY = Integer.parseInt(strArray[1]);
			this.width = Integer.parseInt(strArray[2]);
			this.fontHeight = Integer.parseInt(strArray[3]);
			this.spacing = Integer.parseInt(strArray[4]);
			this.descLength = Integer.parseInt(strArray[5]);
			this.updateInterval = Integer.parseInt(strArray[6]);
			this.hideTicker = Boolean.parseBoolean(strArray[7]);
			this.onTop = Boolean.parseBoolean(strArray[8]);
			this.fontStyle = new FontData(strArray[9].split("#")[0],
					Integer.parseInt(strArray[9].split("#")[1]),
					Integer.parseInt(strArray[9].split("#")[2]));
			this.fontColor = new RGB(	Integer.parseInt(strArray[10].split(" ")[0]),
					Integer.parseInt(strArray[10].split(" ")[1]),
					Integer.parseInt(strArray[10].split(" ")[2]));
			this.backgroundColor = new RGB(	Integer.parseInt(strArray[11].split(" ")[0]),
					Integer.parseInt(strArray[11].split(" ")[1]),
					Integer.parseInt(strArray[11].split(" ")[2]));
			this.limitDate = Integer.parseInt(strArray[12]);

			fileInputStream.close();
			inputStreamReader.close();
			bufferReader.close();

			if (this.feedItem.size() != 0)
				this.feedItem.clear();
			
			fileInputStream = new FileInputStream(filePath+"gpartFeedItem");
			inputStreamReader = new InputStreamReader(fileInputStream, "MS949");
			bufferReader = new BufferedReader(inputStreamReader);
			
			String strTmp = bufferReader.readLine();
			
			while( !(strTmp == null)) {
				this.feedItem.add(strTmp);
				strTmp = bufferReader.readLine();
			} 
			
			fileInputStream.close();
			inputStreamReader.close();
			bufferReader.close();

		}catch(Exception e){
			MessageDialog.openError(new Shell(), Messages.Error, e.getMessage());
		}
	}
	// 수정된 정보를 파일에 다시 저장함
	public void save(int positionX,
			int positionY,
			int width,
			int fontHeight,
			int spacing,
			int descLength,
			int updateInterval,
			boolean hideTicker,
			boolean onTop,
			FontData fontStyle,
			RGB fontColor,
			RGB backgroundColor,
			int limitDate,
			ArrayList<String> feedItem
			){
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(filePath+"gpartSet");
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "MS949");
			BufferedWriter bufferWriter = new BufferedWriter(outputStreamWriter);

			bufferWriter.write(	positionX+"@w@"+
					positionY+"@w@"+
					width+"@w@"+
					fontHeight+"@w@"+
					spacing+"@w@"+
					descLength+"@w@"+
					updateInterval+"@w@"+
					hideTicker+"@w@"+
					onTop+"@w@"+
					fontStyle.getName()+"#"+
					fontStyle.getHeight()+"#"+
					fontStyle.getStyle()+"@w@"+
					fontColor.red+" "+
					fontColor.green+" "+
					fontColor.blue+"@w@"+
					backgroundColor.red+" "+
					backgroundColor.green+" "+
					backgroundColor.blue+"@w@"+
					limitDate+"@w@");
			bufferWriter.close();
			outputStreamWriter.close();
			fileOutputStream.close();
			
			
			fileOutputStream = new FileOutputStream(filePath+"gpartFeedItem");
			outputStreamWriter = new OutputStreamWriter(fileOutputStream, "MS949");
			bufferWriter = new BufferedWriter(outputStreamWriter);
			
			for (String str : feedItem)
				bufferWriter.write(str+"\n");
			
			bufferWriter.close();
			outputStreamWriter.close();
			fileOutputStream.close();
			
			
		}catch(Exception e){
			MessageDialog.openError(new Shell(), Messages.Error, e.getMessage());
		}
	}

	// 수정된 정보를 파일에 다시 저장함
	public void save(int positionX,
			int positionY,
			int width){
		this.load();
		this.save(positionX, positionY, width, fontHeight,spacing,descLength,updateInterval, hideTicker, onTop, fontStyle, fontColor, backgroundColor, limitDate, feedItem);
	}


}
