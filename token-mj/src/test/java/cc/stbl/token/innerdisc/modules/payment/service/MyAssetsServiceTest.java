package cc.stbl.token.innerdisc.modules.payment.service;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cogent.cache.CacheService;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.common.AbstractTestCase;
import cc.stbl.token.innerdisc.restapi.app.payment.MyAssetsProto;

public class MyAssetsServiceTest extends AbstractTestCase {

	@Autowired
	MyAssetsService service;

	@Autowired
	private CacheService cache;

	@Test
	public void testCache() {
		String userId = "80c5f203011a497299911f4347b7c626";
		System.out.println("====1=====" + service(userId));
		System.out.println("====11=====" + service(userId).getString("integral"));
		System.out.println("====2=====" + service(userId));
		System.out.println("====22=====" + service(userId).getString("restrictedAssets"));
		System.out.println("====22=====" + service(userId).getString("availableAssets"));
	}

	public JSONObject service(String userId) {
		// System.out.println(JSON.toJSONString(service.myAssetsHome()));
		String key = "MyAssetsHome:" + userId;

		String str = cache.get(key, String.class);
		if (StringUtils.isBlank(str)) {
			System.out.println("=======StringUtils.isBlank(str)");
			MyAssetsProto.ResponseMyAssetsSupports asset = service.myAssetsHome(userId).getData();
			str = JSON.toJSONString(asset);
			cache.put(key, str, 3 * 60 * 60);
		}
		JSONObject json = JSONObject.parseObject(str);
		return json;
	}

}
