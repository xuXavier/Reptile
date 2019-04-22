/**
 * 
 */
package repeile.thridversion;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: urlResult
 * @Description: 主要就是拿来保存抓取到的url的
 * @author Xavier Xu
 * @date 2019年4月
 *
 */
public class UrlResult {
	/*
	 * url为当前网站，即所有将要爬取网站的起始网站
	 */
	private String url;
	
	
	public UrlResult() {
		list=new ArrayList();
	}
	
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/*
	  * 保存该url的网址所链接的所有url
	 */
	public  List<String>  list;
	
	
}
