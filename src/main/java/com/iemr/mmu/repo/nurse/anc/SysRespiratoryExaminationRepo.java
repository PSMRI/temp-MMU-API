package com.iemr.mmu.repo.nurse.anc;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iemr.mmu.data.anc.SysRespiratoryExamination;

@Repository
public interface SysRespiratoryExaminationRepo extends CrudRepository<SysRespiratoryExamination, Long>{
	@Query(" SELECT u FROM SysRespiratoryExamination u WHERE u.beneficiaryRegID = :benRegID AND u.benVisitID = :benVisitID ")
	public SysRespiratoryExamination getSysRespiratoryExaminationData(@Param("benRegID") Long benRegID,
			@Param("benVisitID") Long benVisitID);
}
