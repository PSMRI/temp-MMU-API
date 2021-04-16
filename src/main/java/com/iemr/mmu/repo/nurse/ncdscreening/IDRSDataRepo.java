package com.iemr.mmu.repo.nurse.ncdscreening;

import java.sql.Timestamp;
import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iemr.mmu.data.ncdScreening.IDRSData;

@Repository
public interface IDRSDataRepo extends CrudRepository<IDRSData, Long> {

	@Query("select a from IDRSData a where a.beneficiaryRegID = :beneficiaryRegID and  a.visitCode = :visitCode")
	public IDRSData getBenIdrsDetails(@Param("beneficiaryRegID") Long beneficiaryRegID,
			@Param("visitCode") Long visitCode);

	@Query(" SELECT id,beneficiaryRegID, benVisitID, providerServiceMapID, idrsQuestionID, idrsScore, question, answer, "
			+ "suspectedDisease, visitCode, diseaseQuestionType FROM IDRSData "
			+ " WHERE beneficiaryRegID = :benRegID AND deleted = false AND visitCode = :visitCode")
	public ArrayList<Object[]> getBenIdrsDetail(@Param("benRegID") Long benRegID, @Param("visitCode") Long visitCode);


	@Query(value="select a.* from t_idrsDetails a inner join i_ben_flow_outreach b on  a.visitcode=b.beneficiary_visit_code where (b.specialist_flag=9 OR b.doctor_Flag=9)  AND a.beneficiaryRegID = :beneficiaryRegID AND a.createdDate >= :tDate "
			+ " AND a.diseaseQuestionType "
			+ " IN ('Asthma', 'Malaria Screening', 'Tuberculosis Screening') "
			+ " ORDER BY createddate DESC, a.visitCode ",nativeQuery=true)
	public ArrayList<IDRSData> getBenIdrsDetailsLast_3_Month(@Param("beneficiaryRegID") Long beneficiaryRegID,
			@Param("tDate") Timestamp tDate);

	@Query(value="select count(a.idrsid) from t_idrsDetails a inner join i_ben_flow_outreach b on a.visitcode=b.beneficiary_visit_code where (b.specialist_flag=9 OR b.doctor_flag=9) and a.BeneficiaryRegID = :beneficiaryRegID and a.isDiabetic is true "
			,nativeQuery=true)
	public Integer isDiabeticCheck(@Param("beneficiaryRegID") Long beneficiaryRegID);

	@Query("select a from IDRSData a where a.beneficiaryRegID = :beneficiaryRegID AND a.diseaseQuestionType = 'Diabetes' "
			+ " ORDER BY Date(a.createdDate) DESC  ")
	public ArrayList<IDRSData> getBenPreviousDiabetesDetails(@Param("beneficiaryRegID") Long beneficiaryRegID);
	
	@Query( value=" SELECT t.VisitCode,r.CreatedDate,t.SuspectedDiseases from db_iemr.t_idrsdetails t " +
			" inner join db_iemr.t_benreferdetails r on r.VisitCode = t.VisitCode where t.beneficiaryRegID = :beneficiaryRegID " +
			" and t.deleted is false  and  t.SuspectedDiseases is not null group by t.visitcode ORDER BY t.createddate DESC ",nativeQuery=true)
	public ArrayList<Object[]> getBenPreviousReferredDetails(@Param("beneficiaryRegID") Long beneficiaryRegID);
	
	@Query(value="select count(a.idrsid) from t_idrsdetails a inner join i_ben_flow_outreach b on a.visitcode=b.beneficiary_visit_code  where (b.specialist_flag=9 OR b.doctor_flag=9) and a.BeneficiaryRegID= :beneficiaryRegID and a.SuspectedDiseases like '%vision%' ",nativeQuery=true)
	public Integer isDefectiveVisionCheck(@Param("beneficiaryRegID") Long beneficiaryRegID);
	
	@Query(value="select count(a.idrsid) from t_idrsdetails a inner join i_ben_flow_outreach b on a.visitcode=b.beneficiary_visit_code where (b.specialist_flag=9 OR b.doctor_flag=9) and a.BeneficiaryRegID= :beneficiaryRegID and a.SuspectedDiseases like '%epilepsy%' ",nativeQuery=true)
	public Integer isEpilepsyCheck(@Param("beneficiaryRegID") Long beneficiaryRegID);
	@Transactional
	@Modifying
	@Query("UPDATE IDRSData SET idrsScore = :idrsScore WHERE beneficiaryRegID = :beneficiaryRegID AND visitCode = :visitCode")
	public int updateIdrsScore(@Param("beneficiaryRegID") Long beneficiaryRegID,
			@Param("visitCode") Long visitCode, @Param("idrsScore") Integer idrsScore);

}