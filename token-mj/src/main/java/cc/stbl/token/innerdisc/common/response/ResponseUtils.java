package cc.stbl.token.innerdisc.common.response;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseUtils {

    public static void responseJson(HttpServletResponse response, Object object){
        try {
 //         response.setCharacterEncoding("GBK");
//          response.reset();
          response.setContentType("application/json;charset=utf-8");
//          response.setCharacterEncoding("utf-8");
//            response.setContentType("application/json");
            response.getWriter().print(JSON.toJSONString(object));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
