package com.example.demo.repo;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Events;

import jakarta.transaction.Transactional;

public interface EventsRepo extends JpaRepository<Events, Long>{
	@Query(value = "select e.eventid, e.id from events e where e.eventid = :eventid", nativeQuery = true)
	List<Map<String, Object>> getEventById(@Param("eventid") long eventid);
	
	@Modifying
	@Transactional
	@Query(value = "insert into userEvents(eventid, userid) values(:eventid, :userid)", nativeQuery = true)
	void updateEvent(@Param("eventid") long eventid, @Param("userid") long userid);
	
	@Query(value = "select u.userid, u.firstName, u.lastName, u.email, u.city from users u where u.userid in (select ue.userid from userevents ue where ue.eventid = :eventid)", nativeQuery = true)
	List<Map<String, Object>> getUsersOfEvent(@Param("eventid") long eventid);
	
	@Query(value = "select ue.userid, ue.eventid from userEvents ue where ue.userid = :userid", nativeQuery = true)
	List<Map<String, Long>> getEventsOfUser(@Param("userid") long userid);
}
