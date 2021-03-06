package com.jisucloud.clawler.regagent.service.impl.music;


import com.jisucloud.clawler.regagent.interfaces.PapaSpider;
import com.jisucloud.clawler.regagent.interfaces.PapaSpiderConfig;

import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

import java.util.Map;



@Slf4j
@PapaSpiderConfig(
		home = "lrts.com", 
		message = "懒人听书是3.2亿用户选择的综合性有声阅读交流平台。热门IP入驻,知名主播云集,原创小说、经典文学、海量精品栏目共筑有声阅读生态圈,解放双眼,畅听世界。", 
		platform = "lrts", 
		platformName = "懒人听书", 
		tags = { "听书", "生活休闲" }, 
		testTelephones = { "15985260000", "18212345678" })
public class LanRenTingShuSpider extends PapaSpider {

	


	public boolean checkTelephone(String account) {
		if (account.length() != 11) {
			return false;
		}
		try {
			String url = "http://www.lrts.me/checkPhone";
			FormBody formBody = new FormBody
	                .Builder()
	                .add("phone", account)
	                .build();
			Request request = new Request.Builder().url(url)
					.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:56.0) Gecko/20100101 Firefox/56.0")
					.addHeader("Host", "www.lrts.me")
					.addHeader("Referer", "http://www.lrts.me/signupByPhone")
					.post(formBody)
					.build();
			Response response = okHttpClient.newCall(request)
					.execute();
			if (response.body().string().contains("errCode\":\"0001")) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
		}
		return false;
	}

	@Override
	public boolean checkEmail(String account) {
		return false;
	}

	@Override
	public Map<String, String> getFields() {
		return null;
	}

}
