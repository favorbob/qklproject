package cc.stbl.token.innerdisc.common.remote;

public interface URI2L {
    /**
     * 将uri转换成url
     * @param uri
     * @return
     */
    String uri2Url(String uri);

    /**
     * 将url转换成uri
     * @param url
     * @return
     */
    String url2Uri(String url);
}
