package cc.stbl.token.innerdisc.common.push.inter.model;

public class Token {

    private boolean use=false;
    private String token;

    private Token(){
    }

    public Token(String token, boolean use) {
        this.token = token;
        this.use = use;
    }

    public static Token disuseToken(){
        return new Token(null,false);
    }

    public static Token useToken(String token){
        return new Token(token,true);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isUse() {
        return use;
    }

    public void setUse(boolean use) {
        this.use = use;
    }
}


