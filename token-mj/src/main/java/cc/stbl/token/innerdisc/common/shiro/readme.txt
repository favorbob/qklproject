shiro权限的集成
    1.存储用户的时候，密码使用(com.t9team.common.shiro.authc.PasswordEncoder.encoder)加密，salt值必须保存好(相当于用户表有密码和salt两必须字段),PasswordEncoder的实例通过spring 注入的方式获取
    2.需实现com.t9team.common.shiro.service.ShiroAdapter接口,使用spring管理其生命周期 （app用户登录：LoginApiShiroAdapter，后台用户登录:SysUserService）
    3.采用token的方式认证权限，用过com.t9team.boxcode.restapi.LoginController.login登录系统，认证成功后返回token
    4.以后的每次请求需要在请求头设值 access-token 的值
    5.关于获取登录用户的Id通过 com.t9team.common.shiro.session.ShiroUtils.getLoginUserId
