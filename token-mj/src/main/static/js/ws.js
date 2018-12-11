var stompClient = null;
//var url = "http://127.0.0.1:9801/endpointAIIC";
var url = "http://aiic.fun/backend_uat/endpointAIIC";
//var userId = "169baece8175459ca62547e9488971ce"; //当前登录用户的id，或者其他唯一标识
var userId = window.location.href.split("?")[1];
if(!userId){
	userId = "169baece8175459ca62547e9488971ce"; //若请求url没送userId过来，设置默认值
}

//加载完浏览器后，打开双通道
$(function() {
	connect();
})

//连接WebSocket服务端，成功后回调方法
var connectSuccess_callBack = function(frame) {
	console.log('Connect success:' + frame);
	//订阅
	stompQueue();
}

//连接WebSocket服务端，失败后回调方法
var connectError_callBack = function(error) {
	console.log('Connect error:' + error);
	//连接失败，尝试重新连接
	connect();
}

//打开双通道
function connect() {
	var socket = new SockJS(url); 
	stompClient = Stomp.over(socket);//使用STMOP子协议的WebSocket客户端
	stompClient.connect({},connectSuccess_callBack, connectError_callBack);
}

//强制关闭浏览器  调用websocket.close（）,进行正常关闭
window.onunload = function() {
	disconnect();
}

//关闭双通道
function disconnect() {
	if (stompClient != null) {
		stompClient.disconnect();
	}
	console.log("Disconnected");
}

//订阅
function stompQueue() {
	stompClient.subscribe('/user/' + userId + '/response', function(response) {
//		var message = JSON.parse(response.body);
		var message = JSON.stringify(response.body);
		//展示一对一的接收的内容接收
		var msg2 = $("#msg2");
		var currentTime = new Date().Format("yyyy-MM-dd hh:mm:ss");
		msg2.append("<p><span>" + currentTime + " 接收到服务端发来的信息:" + message + "</span></p>");
	});
}

//向服务器发送消息，用户激活或复投后调用，通知服务端处理数据
function sendMessage() {
	var $type = $("#type");
	var type = $type.val();
	
	var postValue = {};
	postValue.type = type;
	postValue.userId = userId;
	stompClient.send("/wsRequest", {}, JSON.stringify(postValue));
	var currentTime = new Date().Format("yyyy-MM-dd hh:mm:ss");
	$("#msg2").append("<p><span>" + currentTime + " 用户向服务器</span><span> 发送：" + JSON.stringify(postValue)
			+ "</span></p>");
	$type.val("");
}

Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, // 月份
        "d+": this.getDate(), // 日
        "h+": this.getHours(), // 小时
        "m+": this.getMinutes(), // 分
        "s+": this.getSeconds(), // 秒
        "q+": Math.floor((this.getMonth() + 3) / 3), // 季度
        "S": this.getMilliseconds() // 毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
}
