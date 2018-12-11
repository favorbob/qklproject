package cc.stbl.token.innerdisc.restapi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/debug/response")
public class ResponseCodeController {

    @RequestMapping(value = "/codeDesc",method = {RequestMethod.GET})
    public List<CodeDesc> responseCode(){
        List<CodeDesc> codeDescs = new ArrayList<>();
        for (ResponseCode responseCode : ResponseCode.values()) {
            CodeDesc cd = new CodeDesc();
            cd.setCode(responseCode.code);
            cd.setMessage(responseCode.msg);
            codeDescs.add(cd);
        }
        return codeDescs;
    }


    public static class CodeDesc{
        private Integer code;
        private String message;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
