package com.iemr.mmu.repo.login;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iemr.mmu.data.login.Users;

@Repository
public interface UserLoginRepo extends CrudRepository<Users, Long> {

	@Query(" SELECT u FROM Users u WHERE u.userID = :userID AND u.deleted = false ")
	public Users getUserByUserID(@Param("userID") Long userID);

}
