package cc.stbl.token.innerdisc.modules.basic.entity;

import lombok.Data;

@Data
public class VrUserInfoNode {

	private VrUserInfo root;
	private VrUserInfoNode left;
	private VrUserInfoNode right;
}
