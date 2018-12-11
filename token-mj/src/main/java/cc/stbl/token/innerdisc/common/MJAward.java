package cc.stbl.token.innerdisc.common;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

/**
 * MJ奖励算法
 */
public class MJAward {

	/**
	 * 根据用户层级关系，计算指定用户当天释放的MJ+层级奖
	 * 
	 * @param users
	 *            指定用户及其下级用户(目前是最多20层)，从数据库查询用户信息时，查询条件 user_code like 'xxx%'
	 *            ,排序条件为order by user_code_level asc
	 * @return 指定用户当天释放的MJ+层级奖
	 */
	public static BigDecimal getAward(List<User> users) {
		BigDecimal bd = new BigDecimal("0");
		if (CollectionUtils.isEmpty(users)) {
			return bd;
		}

		User u0 = users.get(0);

		int firstLevel = u0.getUserLevel();
		int lastLevel = users.get(users.size() - 1).getUserLevel();
		// 计算用户与最低层用户的级差(最大20)
		int level = lastLevel - firstLevel;

		if (level == 0) { // 级差为0，即该用户没有下级
			bd = users.get(0).getMj();
		} else if (level == 1) { // 有一层下级
			BigDecimal mj1 = users.get(1).getMj();
			if (users.size() == 3) {
				mj1 = mj1.add(users.get(2).getMj());
			}
			bd = u0.getMj().add(mj1.multiply(u0.getL1Rate()));
		} else if (level == 2) { // 有两层下级
			Map<String, BigDecimal> map = new HashMap<>();
			map.put("a1", new BigDecimal("0"));
			users.stream().filter(u -> u.getUserLevel() == firstLevel + 1).forEach(u1 -> {
				List<User> users2 = users.stream()
						.filter(uu -> uu.getLft() >= u1.getLft() && uu.getLft() <= u1.getRgt())
						.collect(Collectors.toList());
				BigDecimal a1 = map.get("a1");
				map.put("a1", a1.add(getAward(users2)));
			});

			map.put("a2", new BigDecimal("0"));
			users.stream().filter(u -> u.getUserLevel() == firstLevel + 2).forEach(u -> {
				BigDecimal a2 = map.get("a2");
				map.put("a2", a2.add(u.getMj()));
			});
			bd = u0.getMj().add(map.get("a1").multiply(u0.getL1Rate())).add(map.get("a2").multiply(u0.getL2Rate()));
		} else if (level == 3) { // 有三层下级
			Map<String, BigDecimal> map = new HashMap<>();
			map.put("a1", new BigDecimal("0"));
			map.put("a2", new BigDecimal("0"));
			map.put("a3", new BigDecimal("0"));

			users.stream().filter(u -> u.getUserLevel() == firstLevel + 1).forEach(u1 -> {
				List<User> users2 = users.stream()
						.filter(uu -> uu.getLft() >= u1.getLft() && uu.getLft() <= u1.getRgt())
						.collect(Collectors.toList());
				BigDecimal a1 = map.get("a1");
				map.put("a1", a1.add(getAward(users2).multiply(u0.getL1Rate())));
			});

			users.stream().filter(u -> u.getUserLevel() == firstLevel + 2).forEach(u1 -> {
				List<User> users2 = users.stream()
						.filter(uu -> uu.getLft() >= u1.getLft() && uu.getLft() <= u1.getRgt())
						.collect(Collectors.toList());
				BigDecimal a2 = map.get("a2");
				map.put("a2", a2.add(getAward(users2).multiply(u0.getL2Rate())));
			});

			users.stream().filter(u -> u.getUserLevel() == firstLevel + 3).forEach(u -> {
				BigDecimal a3 = map.get("a3");
				map.put("a3", a3.add(u.getMj().multiply(u0.getL3Rate())));
			});

			bd = u0.getMj().add(map.get("a1")).add(map.get("a2")).add(map.get("a3"));
		} else if (level > 3) { // 大于三层下级
			Map<String, BigDecimal> map = new HashMap<>();
			map.put("a1", new BigDecimal("0"));
			map.put("a2", new BigDecimal("0"));
			map.put("a3", new BigDecimal("0"));

			users.stream().filter(u -> u.getUserLevel() == firstLevel + 1).forEach(u1 -> {
				List<User> users2 = users.stream()
						.filter(uu -> uu.getLft() >= u1.getLft() && uu.getLft() <= u1.getRgt())
						.collect(Collectors.toList());
				BigDecimal a1 = map.get("a1");
				map.put("a1", a1.add(getAward(users2).multiply(u0.getL1Rate())));
			});

			users.stream().filter(u -> u.getUserLevel() == firstLevel + 2).forEach(u1 -> {
				List<User> users2 = users.stream()
						.filter(uu -> uu.getLft() >= u1.getLft() && uu.getLft() <= u1.getRgt())
						.collect(Collectors.toList());
				BigDecimal a2 = map.get("a2");
				map.put("a2", a2.add(getAward(users2).multiply(u0.getL2Rate())));
			});

			users.stream().filter(u -> u.getUserLevel() == firstLevel + 3).forEach(u1 -> {
				List<User> users2 = users.stream()
						.filter(uu -> uu.getLft() >= u1.getLft() && uu.getLft() <= u1.getRgt())
						.collect(Collectors.toList());
				BigDecimal a3 = map.get("a3");
				map.put("a3", a3.add(getAward(users2).multiply(u0.getL3Rate())));
			});

			bd = u0.getMj().add(map.get("a1")).add(map.get("a2")).add(map.get("a3"));

		}
		return bd.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public static class User {
		private int userLevel;
		private int lft;
		private int rgt;
		/** 当天释放的MJ值 */
		private BigDecimal mj;
		/** 第一层层级奖比例 */
		private BigDecimal l1Rate;
		/** 第二层层级奖比例 */
		private BigDecimal l2Rate;
		/** 第三层层级奖比例 */
		private BigDecimal l3Rate;

		public User(int userLevel, int lft, int rgt, BigDecimal mj, BigDecimal l1Rate, BigDecimal l2Rate,
				BigDecimal l3Rate) {
			this.userLevel = userLevel;
			this.lft = lft;
			this.rgt = rgt;
			this.mj = mj;
			this.l1Rate = l1Rate;
			this.l2Rate = l2Rate;
			this.l3Rate = l3Rate;
		}

		public int getUserLevel() {
			return userLevel;
		}

		public void setUserLevel(int userLevel) {
			this.userLevel = userLevel;
		}

		public int getLft() {
			return lft;
		}

		public void setLft(int lft) {
			this.lft = lft;
		}

		public int getRgt() {
			return rgt;
		}

		public void setRgt(int rgt) {
			this.rgt = rgt;
		}

		public BigDecimal getMj() {
			return mj;
		}

		public void setMj(BigDecimal mj) {
			this.mj = mj;
		}

		public BigDecimal getL1Rate() {
			return l1Rate;
		}

		public void setL1Rate(BigDecimal l1Rate) {
			this.l1Rate = l1Rate;
		}

		public BigDecimal getL2Rate() {
			return l2Rate;
		}

		public void setL2Rate(BigDecimal l2Rate) {
			this.l2Rate = l2Rate;
		}

		public BigDecimal getL3Rate() {
			return l3Rate;
		}

		public void setL3Rate(BigDecimal l3Rate) {
			this.l3Rate = l3Rate;
		}

	}

}
