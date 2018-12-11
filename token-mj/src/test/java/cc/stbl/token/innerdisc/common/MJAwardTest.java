package cc.stbl.token.innerdisc.common;

import static cc.stbl.token.innerdisc.common.MJAward.getAward;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cc.stbl.token.innerdisc.common.MJAward.User;

public class MJAwardTest {

	@Test
	public void testSingle() {
		List<User> users = new ArrayList<>();
		User user0 = new User(1,1,10, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user1 = new User(2,2,9, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user2 = new User(3,3,8, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user3 = new User(4,4,7, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user4 = new User(5,5,6, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));

		users.add(user0);
		users.add(user1);
		users.add(user2);
		users.add(user3);
		users.add(user4);
		System.out.println("testSingle:"+getAward(users));
	}
	
	@Test
	public void test0Level() {
		List<User> users = new ArrayList<>();
		User user0 = new User(1,1,2, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		users.add(user0);

		System.out.println("test0Level:"+getAward(users));
	}
	
	@Test
	public void test1Level() {
		List<User> users = new ArrayList<>();
		User user0 = new User(1,1,6, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user1 = new User(2,2,3, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user2 = new User(2,4,5, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		users.add(user0);
		users.add(user1);
		users.add(user2);

		System.out.println("test1Level:"+getAward(users));
	}

	@Test
	public void test2Level() {
		List<User> users = new ArrayList<>();
		User user0 = new User(1,1,14, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user1 = new User(2,2,7, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user2 = new User(2,8,13, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user3 = new User(3,3,4, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user4 = new User(3,5,6, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user5 = new User(3,9,10, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user6 = new User(3,11,12, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));

		users.add(user0);
		users.add(user1);
		users.add(user2);
		users.add(user3);
		users.add(user4);
		users.add(user5);
		users.add(user6);
		System.out.println("test2Level:"+getAward(users));
	}

	
	
	@Test
	public void test3Level() {
		List<User> users = new ArrayList<>();
		User user0 = new User(1,1,30, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));

		User user1 = new User(2,2,15, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));

		User user2 = new User(2,16,29, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		
		User user3 = new User(3,3,8, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user4 = new User(3,9,14, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user5 = new User(3,17,22, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user6 = new User(3,23,28, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));

		User user7 = new User(4,4,5, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user8 = new User(4,6,7, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));

		User user9 = new User(4,10,11, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user10 = new User(4,12,13, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user11 = new User(4,18,19, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));

		User user12 = new User(4,20,21, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user13 = new User(4,24,25, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user14 = new User(4,26,27, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));

		users.add(user0);
		users.add(user1);
		users.add(user2);
		users.add(user3);
		users.add(user4);
		users.add(user5);
		users.add(user6);
		users.add(user7);
		users.add(user8);
		users.add(user9);
		users.add(user10);
		users.add(user11);
		users.add(user12);
		users.add(user13);
		users.add(user14);

		System.out.println("test3Level:"+getAward(users));
	}
	
	@Test
	public void test4Level() {
		List<User> users = new ArrayList<>();
		User user0 = new User(1,1,62, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));

		User user1 = new User(2,2,31, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));

		User user2 = new User(2,32,61, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		
		User user3 = new User(3,3,16, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user4 = new User(3,17,30, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user5 = new User(3,33,46, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user6 = new User(3,47,60, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));

		User user7 = new User(4,4,9, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user8 = new User(4,10,15, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));

		User user9 = new User(4,18,23, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user10 = new User(4,24,29, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user11 = new User(4,34,39, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));

		User user12 = new User(4,40,45, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user13 = new User(4,48,53, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user14 = new User(4,54,59, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		
		User user15 = new User(5,5,6, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user16 = new User(5,7,8, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user17 = new User(5,11,12, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user18 = new User(5,13,14, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user19 = new User(5,19,20, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user20 = new User(5,21,22, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user21 = new User(5,25,26, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user22 = new User(5,27,28, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user23 = new User(5,35,36, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user24 = new User(5,37,38, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user25 = new User(5,41,42, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user26 = new User(5,43,44, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user27 = new User(5,49,50, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user28 = new User(5,51,52, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user29 = new User(5,55,56, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));
		User user30 = new User(5,57,58, new BigDecimal("100"), new BigDecimal("0.3"), new BigDecimal("0.2"),
				new BigDecimal("0.1"));

		users.add(user0);
		users.add(user1);
		users.add(user2);
		users.add(user3);
		users.add(user4);
		users.add(user5);
		users.add(user6);
		users.add(user7);
		users.add(user8);
		users.add(user9);
		users.add(user10);
		users.add(user11);
		users.add(user12);
		users.add(user13);
		users.add(user14);
		users.add(user15);
		users.add(user16);
		users.add(user17);
		users.add(user18);
		users.add(user19);
		users.add(user20);
		users.add(user21);
		users.add(user22);
		users.add(user23);
		users.add(user24);
		users.add(user25);
		users.add(user26);
		users.add(user27);
		users.add(user28);
		users.add(user29);
		users.add(user30);

		System.out.println("test4Level:"+getAward(users));
	}
}
