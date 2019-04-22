package repeile.thridversion;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.New;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


/**
 * 
 * @ClassName: FirstReptile
 * @Description: 能够实现从多个个网页中不断抓取文字和图片信息
 * @author Xavier Xu
 * @date 2019年4月
 *
 */
public class ThridReptile {

	/**
	* 
	* @Title: saveHtml
	* @Description: 将抓取的数据保存在本地或者json文件中
	* @param @param url    
	* @return void   
	* @author Xavier Xu 
	* @throws
	*/
    public static void saveHtml(String url) {
        try {
        	if()
            // 这是将首页的信息存入到一个html文件中 为了后面分析html文件里面的信息做铺垫
            File dest = new File("src/temp/reptile.html");
            // 接收字节输入流
            InputStream is;
            // 字节输出流
            FileOutputStream fos = new FileOutputStream(dest);
            URL temp = new URL(url);
            // 这个地方需要加入头部 避免大部分网站拒绝访问
            // 这个地方是容易忽略的地方所以要注意
            URLConnection uc = temp.openConnection();
            // 因为现在很大一部分网站都加入了反爬虫机制 这里加入这个头信息
            uc.addRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 "
                            + "(iPad; U; CPU OS 4_3_3 like Mac OS X; en-us) "
                            + "AppleWebKit/533.17.9"
                            + " (KHTML, like Gecko) Version/5.0.2 Mobile/8J2 Safari/6533.18.5");
            is = temp.openStream();
            // 为字节输入流加入缓冲
            BufferedInputStream bis = new BufferedInputStream(is);
            // 为字节输出流加入缓冲
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            int length;
            byte[] bytes = new byte[1024 * 20];
            while ((length = bis.read(bytes, 0, bytes.length)) != -1) {
                fos.write(bytes, 0, length);
            }
            bos.close();
            fos.close();
            bis.close();
            is.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /**
	 * 
	 * @Title: getLocalHtml
	 * @Description: 解析本地的html文件获得数据
	 * @param @param path    
	 * @return void   
	 * @author Xavier Xu 
	 * @throws
	 */
        public static void getLocalHtml(String path) {
            // 读取本地的html文件
            File file = new File(path);
            // 获取这个路径下的所有html文件
            File[] files = file.listFiles();
            List<New> news = new ArrayList<New>();
            HttpServletResponse response = null;
            HttpServletRequest request = null;
            int tmp=1;
            // 循环解析所有的html文件
            try {
                for (int i = 0; i < files.length; i++) {

                    // 首先先判断是不是文件
                    if (files[i].isFile()) {
                        // 获取文件名
                        String filename = files[i].getName();
                        // 开始解析文件

                        Document doc = Jsoup.parse(files[i], "UTF-8");
                        // 获取所有内容 获取新闻内容
                        org.jsoup.select.Elements contents =  doc.getElementsByTag("img");
                        for (Element element : contents) {
                            org.jsoup.select.Elements e1 = element.getElementsByTag("a");
                            for (Element element2 : e1) {
                                // System.out.print(element2.attr("href"));
                                // 根据href获取新闻的详情信息
                                String newText = desGetUrl(element2.attr("href"));
                                // 获取新闻的标题
                                String newTitle = element2.text();                                                      
                                exportFile(newTitle, newText);
                                System.out.println("抓取成功。。。"+(tmp));
                                tmp++;
                                
                            }
                        }
                    }

                }
                
                //excelExport(news, response, request);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    	/**
    	 * 
    	 * @Title: desGetUrl
    	 * @Description: 根据url获取详情信息
    	 * @param @param url
    	 * @param @return    
    	 * @return String   
    	 * @author Xavier Xu 
    	 * @throws
    	 */
        public static String desGetUrl(String url) {
            String newText="";
            try {
                Document doc = Jsoup
                        .connect(url)
                        .userAgent(
                                "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; MALC)")
                        .get();
                // System.out.println(doc);
                // 得到html下的所有东西
                //Element content = doc.getElementById("article");
                org.jsoup.select.Elements contents = doc.getElementsByClass("article");
                if(contents != null && contents.size() >0){
                    Element content = contents.get(0);
                    newText = content.text();
                }
                //System.out.println(content);
                //return newText;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return newText;
        }
        /**
    	 * 
    	 * @Title: exportFile
    	 * @Description: 将新闻标题和内容写入txt文件中
    	 * @param @param title
    	 * @param @param content    
    	 * @return void   
    	 * @author Xavier Xu 
    	 * @throws
    	 */
        public static void exportFile(String title,String content){
            
            try {
                File file = new File("D:/replite/xinwen.txt");
                
                if (!file.getParentFile().exists()) {//判断路径是否存在，如果不存在，则创建上一级目录文件夹
                    file.getParentFile().mkdirs();
                }
                FileWriter fileWriter=new FileWriter(file, true); 
                fileWriter.write(title+"----------");
                fileWriter.write(content+"\r\n");
                fileWriter.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }  
        /**
    	 * 
    	 * @Title: getImage
    	 * @Description: 从html文件中拿到url地址
    	 * @param @param htmlPath    
    	 * @return void   
    	 * @author Xavier Xu 
    	 * @throws
    	 */
        public static void getImage(String htmlPath) {
        	 String filePath ="D:/Reptiles/image";
        	 File file = new File(htmlPath);
             // 获取这个路径下的所有html文件
             File[] files = file.listFiles();
             System.out.println("测试");
             List<New> news = new ArrayList<New>();
             int tmp=1;
             // 循环解析所有的html文件
             try {
                 for (int i = 0; i < files.length; i++) {

                     // 首先先判断是不是文件
                     if (files[i].isFile()) {
                         // 获取文件名
                         String filename = files[i].getName();
                         // 开始解析文件
                          System.out.println("解析成功");
                         Document doc = Jsoup.parse(files[i], "UTF-8");
                         // 获取所有内容 获取新闻内容
                         org.jsoup.select.Elements contents =  doc.getElementsByTag("img");
       
                             for(Element element1 : contents){
                            	  System.out.println("解析成功2");
                                 String imgSrc = element1.attr("src");
                                 if (!"".equals(imgSrc) && (imgSrc.startsWith("http://") || imgSrc.startsWith("https://"))) {
                                     // 判断imgSrc是否为空且是否以"http://"开头
                                     System.out.println("正在下载的图片的地址：" + imgSrc);
                           dowloadImage(filePath,imgSrc);
                                 
                             }
                         }
                     }

                 }
                 
                 //excelExport(news, response, request);
             } catch (IOException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
             }
        }
        /**
    	 * 
    	 * @Title: dowloadImage
    	 * @Description: 下载图片到指定的文件夹中
    	 * @param @param filePath
    	 * @param @param imageUrl    
    	 * @return void   
    	 * @author Xavier Xu 
    	 * @throws
    	 */
        public static void dowloadImage(String filePath,String imageUrl) {
        	
        	// 截取图片的名称
            String fileName = imageUrl.substring(imageUrl.lastIndexOf("/"));
            System.out.println(fileName+"ffds");
            //创建文件的目录结构
            File files = new File(filePath);
            if(!files.exists()){// 判断文件夹是否存在，如果不存在就创建一个文件夹
                files.mkdirs();
            }
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream is = connection.getInputStream();
                // 创建文件
                File file = new File(filePath+fileName);
                FileOutputStream out = new FileOutputStream(file);
                int i = 0;
                while((i = is.read()) != -1){
                    out.write(i);
                }
                is.close();
                out.close();
                
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /**
         * 
         * @Title: getUrl
         * @Description: 获得当前链接所链接的所有url
         * @param htmlPath @param Url   
         * @return void   
         * @author Xavier Xu 
         * @throws
         */
        public static void getUrl(String htmlPath,String cururl) {
        	String filePath ="D:/Reptiles/image";
       	 File file = new File(htmlPath);
            // 获取这个路径下的所有html文件
            File[] files = file.listFiles();
            System.out.println("测试");
            List<New> news = new ArrayList<New>();
            //保存所有网站的链接地址
            UrlResult result=new UrlResult();
            //设置当前访问网站作为所有将要爬的链接网站的起始网站
            result.setUrl(cururl);
            int tmp=1;
            // 循环解析所有的html文件
            try {
                for (int i = 0; i < files.length; i++) {

                    // 首先先判断是不是文件
                    if (files[i].isFile()) {
                        // 获取文件名
                        String filename = files[i].getName();
                        // 开始解析文件
                         System.out.println("解析成功");
                        Document doc = Jsoup.parse(files[i], "UTF-8");
                       
                        // 获取该网站的链接url地址
                        org.jsoup.select.Elements contents =  doc.getElementsByTag("a");
      
                            for(Element element1 : contents){         	  
                                String url = element1.attr("href");
                                if (!"".equals(url) && (url.startsWith("http://") || url.startsWith("https://"))) {
                                   
                                    System.out.println("该网站所连接的网页地址：" + url);
                                      result.list.add(url);
                                   
                                
                            }
                        }
                    }

                }
                //不断爬 爬两次
                
                //excelExport(news, response, request);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        public static void main(String[] args) {
            String url = "https://cs.58.com/";  
            String htmlPath ="src/temp" ;  //html保存地址
//          saveHtml(url);
            // 解析本地html文件
//            getLocalHtml("src/temp");     
//            getImage(htmlPath);
            getUrl(htmlPath,url);
            result
        }
    }
	

       

