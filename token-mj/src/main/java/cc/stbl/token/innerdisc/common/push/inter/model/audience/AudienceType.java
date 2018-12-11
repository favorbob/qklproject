package cc.stbl.token.innerdisc.common.push.inter.model.audience;

public enum AudienceType {
	TAG("tag"),
	TAG_AND("tag_and"),
	ALIAS("alias"),
;
	private final String value;
	private AudienceType(final String value) {
		this.value = value;
	}
	public String value() {
		return this.value;
	}
	
	
}
