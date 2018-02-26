package lscsCrawler;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class LSCSCrawler {
	/**图像保存位置*/
	private static final String IMG_LOCATION = "E:\\upload\\";
	/**允许最大图像40M*/
	private static final long MAX_DOWNLOAD_SIZE = 40L * 1024 * 1024;
	/**UA*/
	static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36";

	public static void main(String[] args) throws Exception {
		downloadInfo();
		downloadImg();
	}
	public static void downloadImg() throws Exception{
		CardDao dao = new CardDao();
		List<String> urlList = dao.getAllImgUrl();
		for(int i=0;i<urlList.size();i++){
			String name = urlList.get(i).split("\\|")[0];
			File file1 = new File(IMG_LOCATION + name + ".png");
			if(file1.exists()&&file1.length()>0){
				continue;
			}
			String imgUrl = urlList.get(i).split("\\|")[1].replaceAll(" ", "%20");
			byte[] img = download(imgUrl, 100000);
			FileUtils.byte2File(img, IMG_LOCATION, name+".png");
			System.out.println(name+"下载完成");
		}
	}
	public static void downloadInfo(){
		String[] zhiyes = new String[]{"druid","hunter","mage","neutral","paladin","priest","rogue","shaman","warlock","warrior"};
		String url = "http://hs.blizzard.cn/action/cards/query";
		for(String zhiye:zhiyes){
			int pagecount=2;
			for(int i=1;i<=pagecount;i++){
				Map<String, String> conditions = new HashMap<String, String>();
				conditions.put("cardClass", zhiye);
				conditions.put("golden", "0");
				conditions.put("t", System.currentTimeMillis() + "");
				conditions.put("p", i + "");
				String codeStr = new HttpClientUtil().doPost(url, conditions, "utf-8");
				codeStr = codeStr.replace("null", "\"\"");
				JsonObject object = new JsonParser().parse(codeStr).getAsJsonObject();
				pagecount = object.get("totalPage").getAsInt();
				JsonArray cards = object.getAsJsonArray("cards");
				for(int j=0;j<cards.size();j++){
					JsonObject card = cards.get(j).getAsJsonObject();
					String id = card.get("id").getAsString();
					String artist = card.get("artist").getAsString();
					String attack = card.get("attack").getAsString();
					String background = card.get("background").getAsString();
					String cardClass = card.get("cardClass").getAsString();
					String cardEffect = card.get("cardEffect").getAsString();
					String cardRace = card.get("cardRace").getAsString();
					String cardRarity = card.get("cardRarity").getAsString();
					String cardSet = card.get("cardSet").getAsString();
					String cardType = card.get("cardType").getAsString();
					String code = card.get("code").getAsString();
					String consume = card.get("consume").getAsString();
					String cost = card.get("cost").getAsString();
					String createTime = card.get("createTime").getAsString();
					String description = card.get("description").getAsString();
					String durability = card.get("durability").getAsString();
					String gain = card.get("gain").getAsString(); 
					String golden = card.get("golden").getAsString();
					String health = card.get("health").getAsString();
					String imageUrl = card.get("imageUrl").getAsString();
					String name = card.get("name").getAsString();
					String neutralClass = card.get("neutralClass").getAsString();
					String updateTime = card.get("updateTime").getAsString();
					try {
						new CardDao().save(id, artist, attack, background, cardClass, cardEffect, cardRace, 
								cardRarity, cardSet, cardType, code, consume, cost, createTime, description, durability,
								gain, golden, health, imageUrl, name, neutralClass, updateTime);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	/**
	 * 读取网络图像为byte的格式，后面设置下载时间
	 *
	 * @param webUrl
	 *            要下载的web图像的url
	 * @return byte数组
	 * 
	 *
	 */
	private static byte[] download(String webUrl, int timeOut) {
		HttpURLConnection connection = null;
		long start = System.currentTimeMillis();
		try {
			URL url = new URL(webUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(timeOut);
			connection.setReadTimeout(timeOut);
			connection.setRequestProperty("User-Agent", USER_AGENT);
			int len = connection.getContentLength();
			if (len >= MAX_DOWNLOAD_SIZE) {
				return null;
			}
			if (len == -1) {// 长度为-1的情况特殊处理，http://chuantu.biz/t5/35/1474453734x3340469630.jpg
							// 返回文件长度为-1
				try (InputStream in = connection.getInputStream()) {
					return IOUtils.toByteArray(connection.getInputStream());
				}
			} else {
				byte[] data = new byte[len];
				byte[] buffer = new byte[4096 * 2];// 8k读取缓冲，读取nos文件测试发现比4k稍微快一点
				int count = 0, sum = 0;
				try (InputStream in = connection.getInputStream()) {
					while ((count = in.read(buffer)) > 0) {
						long elapse = System.currentTimeMillis() - start;
						if (elapse >= timeOut) {// 每读取一次数据，检测一次超时
							data = null;
							break;
						}
						System.arraycopy(buffer, 0, data, sum, count);
						sum += count;
					}
				}
				return data;
			}
		} catch (Exception e) {
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}

class HttpClientUtil {  
    public String doPost(String url,Map<String,String> map,String charset){  
        HttpClient httpClient = null;  
        HttpPost httpPost = null;  
        String result = null;  
        try{  
            httpClient = HttpClients.createDefault();  
            httpPost = new HttpPost(url);  
            //设置参数  
            List<NameValuePair> list = new ArrayList<NameValuePair>();  
            Iterator<Entry<String, String>> iterator = map.entrySet().iterator();  
            while(iterator.hasNext()){  
                Entry<String,String> elem = iterator.next();  
                list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));  
            }  
            if(list.size() > 0){  
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,charset);  
                httpPost.setEntity(entity);  
            }  
            HttpResponse response = httpClient.execute(httpPost);  
            if(response != null){  
                HttpEntity resEntity = response.getEntity();  
                if(resEntity != null){  
                    result = EntityUtils.toString(resEntity,charset);  
                }  
            }  
        }catch(Exception ex){  
            ex.printStackTrace();  
        }  
        return result;  
    }  
}  
