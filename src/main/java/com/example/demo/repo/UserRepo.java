package com.example.demo.repo;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.User;

import jakarta.transaction.Transactional;

public interface UserRepo extends JpaRepository<User, Long>{
	@Modifying
	@Transactional
	@Query(value = "insert into userInterest(interestid, userid) values(:interestid, :userid)", nativeQuery = true)
	void updateInterest(@Param("interestid") long interestid, @Param("userid") long userid);
	
	@Query(value = "select i.interestid, i.interestname from interest i where i.interestid in (select ui.interestid from userInterest ui where ui.userid = :userid)", nativeQuery = true)
	List<Map<String, Object>> getInterestsOfUser(@Param("userid") long userid);
	
	@Query(value = "select u.userid, u.firstName, u.lastName, u.email, u.city from users u where u.userid = :userid", nativeQuery = true)
	List<Map<String, Object>> getUsersById(@Param("userid") long userid);
	
	@Query(value = "select i.interestid, i.interestname from interest i where i.interestid = :interestid", nativeQuery = true)
	List<Map<String, Object>> getInterestById(@Param("interestid") long interestid);
	
	@Modifying
	@Transactional
	@Query(value = "delete from userInterest ui where ui.userid = :userid and ui.interestid = :interestid", nativeQuery = true)
	void deleteInterestOfUser(@Param("userid") long userid, @Param("interestid") long interestid);
	
	@Query(value = "Select u.userid, u.email, u.password, u.firstName, u.lastName, u.city from users u where u.email = :email and u.password = :password", nativeQuery = true)
	Map<String, Object> validateUser(@Param("email") String email, @Param("password") String password);
	
	@Query(value = "Select u.userid, u.email from users u where u.email = :email", nativeQuery = true)
	Map<String, Object> emailExits(@Param("email") String email);
	
	@Query(value = "select ui.userid, ui.interestid from userInterest ui where ui.userid = :userid and ui.interestid = :interestid", nativeQuery = true)
	Map<String, Long> userInterestExists(@Param("userid") long userid, @Param("interestid") long interestid);
	
	@Modifying
	@Transactional
	@Query(value = "insert into userFriends(friendid, userid) values(:friendid, :userid)", nativeQuery = true)
	void addUserFriends(@Param("friendid") long friendid, @Param("userid") long userid);
	
	@Query(value = "select uf.userid, uf.friendid from userFriends uf where uf.userid = :userid and uf.friendid = :friendid", nativeQuery = true)
	Map<String, Long> userFriendExists(@Param("userid") long userid, @Param("friendid") long friendid);
	
	@Query(value = "select u.userid, u.firstName, u.lastName, u.email, u.city from users u where u.userid in (select uf.friendid from userFriends uf where uf.userid = :userid)", nativeQuery = true)
	List<Map<String, Object>> getFriendsOfUser(@Param("userid") long userid);
	
	@Modifying
	@Transactional
	@Query(value = "delete from userFriends uf where uf.userid = :userid and uf.friendid = :friendid", nativeQuery = true)
	void deleteFriendOfUser(@Param("userid") long userid, @Param("friendid") long friendid);
	
	@Query(value = "select u.userid, u.firstName, u.lastName, u.email, u.city from users u where u.userid in (select ui.userid from userInterest ui where ui.interestid = :interestid)", nativeQuery = true)
	List<Map<String, Object>> getUsersOfInterest(@Param("interestid") long interestid);
	
}

