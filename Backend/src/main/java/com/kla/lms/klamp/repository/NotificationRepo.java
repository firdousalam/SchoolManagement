package com.kla.lms.klamp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kla.lms.klamp.entity.Notification;

@Repository
public interface NotificationRepo extends CrudRepository<Notification, Long>,JpaSpecificationExecutor<Notification> {

	@Query(nativeQuery = true, value = "Select * from klamp.notification where status =:status")
	public List<Notification> findNotificationByStatus(@Param("status") String status);
}