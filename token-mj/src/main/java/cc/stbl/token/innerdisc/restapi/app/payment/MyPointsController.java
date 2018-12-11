package cc.stbl.token.innerdisc.restapi.app.payment;

import cc.stbl.token.innerdisc.restapi.PathPrefix;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {PathPrefix.API_PATH + "/myPoints"})
@Api(description = "我的积分")
public class MyPointsController {

    //积分明细

}
