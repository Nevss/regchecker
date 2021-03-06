package com.jisucloud.clawler.regagent.service.impl.borrow;

import com.deep077.spiderbase.selenium.mitm.AjaxHook;
import com.deep077.spiderbase.selenium.mitm.ChromeAjaxHookDriver;
import com.deep077.spiderbase.selenium.mitm.HookTracker;

import com.jisucloud.clawler.regagent.interfaces.PapaSpider;
import com.jisucloud.clawler.regagent.interfaces.PapaSpiderConfig;
import com.jisucloud.clawler.regagent.util.OCRDecode;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

import java.util.Map;


import org.openqa.selenium.WebElement;

@Slf4j
@PapaSpiderConfig(
		home = "jujinziben.com", 
		message = "聚金资本贷是河南聚金金融服务股份公司推出的个人对个人网络借贷服务平台，为有资金需求的借款人和有理财需求的出借人搭建了一个轻松、便捷、安全、透明的网络互动平台。聚金金融以其诚信、透明、安全、高效、创新的... ", 
		platform = "jujinziben", 
		platformName = "聚金资本", 
		tags = { "p2p", "借贷" }, 
		testTelephones = { "18212345678", "15161509916" })
public class JuJinZiBenSpider extends PapaSpider implements AjaxHook {

	private ChromeAjaxHookDriver chromeDriver;
	private boolean checkTel = false;
	private boolean vcodeSuc = false;//验证码是否正确
	
	
	private String getImgCode() {
		for (int i = 0 ; i < 3; i++) {
			try {
				WebElement img = chromeDriver.findElementByCssSelector(".img-box");
				chromeDriver.mouseClick(img);
				byte[] body = chromeDriver.screenshot(img);
				return OCRDecode.decodeImageCode(body);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}


	public boolean checkTelephone(String account) {
		try {
			chromeDriver = ChromeAjaxHookDriver.newChromeInstance(false, true);
			chromeDriver.get("https://www.jujinziben.com/login");
			smartSleep(2000);
			chromeDriver.addAjaxHook(this);
			chromeDriver.findElementByCssSelector("input[name='username']").sendKeys(account);
			chromeDriver.findElementByCssSelector("input[name='password']").sendKeys("xas129asad0b");
			for (int i = 0; i < 5; i++) {
				if (chromeDriver.checkElement(".img-box")) {
					String code = getImgCode();
					WebElement webElement = chromeDriver.findElementByCssSelector("input[name='code']");
					webElement.click();
					webElement.sendKeys(code);
				}
				chromeDriver.findElementByCssSelector("input.btn-common").click();
				smartSleep(3000);
				if (vcodeSuc) {
					break;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (chromeDriver != null) {
				chromeDriver.quit();
			}
		}
		return checkTel;
	}

	@Override
	public boolean checkEmail(String account) {
		return false;
	}

	@Override
	public Map<String, String> getFields() {
		return null;
	}

	@Override
	public HookTracker getHookTracker() {
		return HookTracker.builder().addUrl("api/user/login").isPost().build();
	}

	@Override
	public HttpResponse filterRequest(HttpRequest request, HttpMessageContents contents, HttpMessageInfo messageInfo) {
		return null;
	}

	@Override
	public void filterResponse(HttpResponse response, HttpMessageContents contents, HttpMessageInfo messageInfo) {
		if (!contents.getTextContents().contains("验证码")) {
			vcodeSuc = true;
		}
		checkTel = contents.getTextContents().contains("锁定");
	}

}
