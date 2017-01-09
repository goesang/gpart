package com.forweaver.gpart;
import org.eclipse.osgi.util.NLS;
// 프로그램에 표시될 텍스트들
public class Messages extends NLS {
	public static String AboutWidget_about;
	public static String AboutWidget_close;
	public static String AboutWidget_copyright;
	public static String AboutWidget_credits;
	public static String AboutWidget_desc;
	public static String AboutWidget_gpl;
	public static String AboutWidget_license;
	public static String AboutWidget_link;
	public static String AboutWidget_name;
	public static String AboutWidget_title;
	public static String AboutWidget_url;
	public static String AboutWidget_version;
	public static String AboutWidget_written;
	public static String PreferrenceWidget_add;
	public static String PreferrenceWidget_bgColor;
	public static String PreferrenceWidget_blankError;
	public static String PreferrenceWidget_cancel;
	public static String PreferrenceWidget_color;
	public static String PreferrenceWidget_desc;
	public static String PreferrenceWidget_feedError;
	public static String PreferrenceWidget_feedItem;
	public static String PreferrenceWidget_feedLimit;
	public static String PreferrenceWidget_feedLimitDate;
	public static String PreferrenceWidget_feedTitle;
	public static String PreferrenceWidget_feedUrl;
	public static String PreferrenceWidget_fontColor;
	public static String PreferrenceWidget_fontStyle;
	public static String PreferrenceWidget_hide;
	public static String PreferrenceWidget_ok;
	public static String PreferrenceWidget_remove;
	public static String PreferrenceWidget_selectColor;
	public static String PreferrenceWidget_selectFont;
	public static String PreferrenceWidget_spacing;
	public static String PreferrenceWidget_tableTooltip;
	public static String PreferrenceWidget_title;
	public static String PreferrenceWidget_top;
	public static String PreferrenceWidget_update;
	public static String PreferrenceWidget_url;
	public static String PreferrenceWidget_urlError;
	public static String PreferrenceWidget_widget;
	public static String PreferrenceWidget_width;
	public static String PreferrenceWidget_x;
	public static String PreferrenceWidget_y;
	public static String TrayWidget_about;
	public static String TrayWidget_exit;
	public static String TrayWidget_preferrence;
	public static String TrayWidget_rssFeed;
	public static String TrayWidget_rssUpdate;
	public static String TrayWidget_systemError;
	public static String TrayWidget_title;
	public static String Alert;
	public static String Alert_Create_Direcitory;
	public static String Error;
	public static String Error_Create_Direcitory;
	static {
		// initialize resource bundle
		if(System.getProperty("user.language").indexOf("ko") == 0)
			NLS.initializeMessages("language/ko", Messages.class);
		else
			NLS.initializeMessages("language/en", Messages.class);
		
	}

	private Messages() {
	}
}
