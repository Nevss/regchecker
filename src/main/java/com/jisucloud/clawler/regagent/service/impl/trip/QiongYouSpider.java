package com.jisucloud.clawler.regagent.service.impl.trip;


import com.jisucloud.clawler.regagent.interfaces.PapaSpider;
import com.jisucloud.clawler.regagent.interfaces.PapaSpiderConfig;

import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

import java.util.Map;

import java.util.UUID;


@Slf4j
@PapaSpiderConfig(
		home = "qyer.com", 
		message = "穷游网由肖异于2004年在德国留学期间创立，至今已经发展为国内领先的出境旅行服务平台。", 
		platform = "qyer", 
		platformName = "穷游网", 
		tags = { "旅游" , "酒店" , "美食" , "o2o" }, 
		testTelephones = { "18210538000", "18212345678" })
public class QiongYouSpider extends PapaSpider {

	

	public boolean checkTelephone(String account) {
		try {
			String url = "https://passport.qyer.com/qcross/passport/register/mobile/checkmobile?ajaxID="+UUID.randomUUID().toString().replaceAll("\\-", "");
			FormBody formBody = new FormBody
	                .Builder()
	                .add("mobile", "86-" + account)
	                .build();
			Request request = new Request.Builder().url(url)
					.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:56.0) Gecko/20100101 Firefox/56.0")
					.addHeader("Host", "passport.qyer.com")
					.addHeader("Referer", "https://passport.qyer.com/register/mobile?ref=https%3A%2F%2Fwww.qyer.com%2F")
					.post(formBody)
					.build();
			Response response = okHttpClient.newCall(request).execute();
			String res = response.body().string();
			if (res.contains("已经被注册") || res.contains("\\u5df2\\u7ecf\\u88ab\\u6ce8\\u518c")) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
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
