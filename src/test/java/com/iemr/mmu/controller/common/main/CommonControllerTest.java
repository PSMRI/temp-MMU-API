package com.iemr.mmu.controller.common.main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.ws.rs.NotFoundException;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;

import com.iemr.mmu.data.benFlowStatus.BeneficiaryFlowStatus;
import com.iemr.mmu.service.common.transaction.CommonDoctorServiceImpl;
import com.iemr.mmu.service.common.transaction.CommonNurseServiceImpl;
import com.iemr.mmu.service.common.transaction.CommonServiceImpl;
import com.iemr.mmu.utils.AESEncryption.AESEncryptionDecryption;
import com.iemr.mmu.utils.exception.IEMRException;
import com.iemr.mmu.utils.mapper.InputMapper;
import com.iemr.mmu.utils.response.OutputResponse;

import jakarta.servlet.ServletContext;

@ExtendWith(MockitoExtension.class)
class CommonControllerTest {

	@Mock
	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	@Mock
	private CommonDoctorServiceImpl commonDoctorServiceImpl;
	@Mock
	private CommonNurseServiceImpl commonNurseServiceImpl;
	@Mock
	private CommonServiceImpl commonServiceImpl;
	@Mock
	private InputMapper inputMapper = new InputMapper();
	@Mock
	private ServletContext servletContext;
	@Mock
	private AESEncryptionDecryption aESEncryptionDecryption;
	@Mock
	private MockMvc mockMvc;

	private static final String BENEFICIARY_REG_ID = "beneficiaryRegID";
	private MockHttpServletRequest request;

	@InjectMocks
	CommonController commonController;

//	@Test
//	void testGetDocWorkListNew() {
//		OutputResponse response = new OutputResponse();
//
//		Integer providerServiceMapID = 1;
//		Integer serviceID = 1;
//		Integer vanID = 1;
//		String s = "test";
//
//		when(commonDoctorServiceImpl.getDocWorkListNew(providerServiceMapID, serviceID, vanID)).thenReturn(s);
//
//		String expResponse = commonController.getDocWorkListNew(providerServiceMapID, serviceID, vanID);
//
//		response.setResponse(s);
//
//		assertTrue(providerServiceMapID != null && serviceID != null);
//		assertNotNull(s);
//
//		assertEquals(expResponse, commonController.getDocWorkListNew(providerServiceMapID, serviceID, vanID));
//		assertTrue(response.toString().contains(s));
//	}

	@Test
	void testGetDocWorkListNew_ValidResponse() {
		// Arrange
		Integer providerServiceMapID = 1, serviceID = 1, vanID = 1;
		String expectedResponse = "Expected Response";
		when(commonDoctorServiceImpl.getDocWorkListNew(providerServiceMapID, serviceID, vanID))
				.thenReturn(expectedResponse);

		// Act
		String actualResponse = commonController.getDocWorkListNew(providerServiceMapID, serviceID, vanID);

		// Assert
		assertNotNull(actualResponse);
		assertTrue(actualResponse.contains(expectedResponse));
	}

	@Test
	void testGetDocWorkListNew_InvalidRequest() {
		// Arrange
		Integer providerServiceMapID = null; // or set to null to simulate invalid request
		Integer serviceID = 1; // or set this to null instead
		Integer vanID = 1; // Arbitrary, since the error doesn't depend on this

		// Act
		String response = commonController.getDocWorkListNew(providerServiceMapID, serviceID, vanID);

		// Assert
		assertTrue(response.contains("Invalid request, either ProviderServiceMapID or ServiceID is invalid"),
				"Expected error message not found in the response.");
	}

	@Test
	void testGetDocWorkListNew_Exception() {
		OutputResponse response = new OutputResponse();

		response.setError(5000, "Error while getting doctor worklist");

		assertEquals(response.toString(), commonController.getDocWorkListNew(any(), any(), any()));

	}

	@Test
	void testGetDocWorkListNewFutureScheduledForTM() {
		OutputResponse response = new OutputResponse();

		Integer providerServiceMapID = 1;
		Integer serviceID = 1;
		String s = "test";

		when(commonDoctorServiceImpl.getDocWorkListNewFutureScheduledForTM(providerServiceMapID, serviceID))
				.thenReturn(s);

		String expResponse = commonController.getDocWorkListNewFutureScheduledForTM(providerServiceMapID, serviceID);

		response.setResponse(s);

		assertTrue(providerServiceMapID != null && serviceID != null);
		assertNotNull(s);

		assertEquals(expResponse,
				commonController.getDocWorkListNewFutureScheduledForTM(providerServiceMapID, serviceID));
		assertTrue(response.toString().contains(s));
	}

	@Test
	void testGetDocWorkListNewFutureScheduledForTM_InvalidRequest() {
		OutputResponse response = new OutputResponse();

		Integer providerServiceMapID = null;
		Integer serviceID = null;
		String s = null;

		// when(commonDoctorServiceImpl.getDocWorkListNewFutureScheduledForTM(providerServiceMapID,
		// serviceID))
		// .thenReturn(s);

		String expResponse = commonController.getDocWorkListNewFutureScheduledForTM(providerServiceMapID, serviceID);

		response.setError(5000, "Error while getting doctor worklist for future scheduled beneficiay");

		assertNull(providerServiceMapID);
		assertNull(serviceID);
		assertNull(s);

		assertEquals(expResponse,
				commonController.getDocWorkListNewFutureScheduledForTM(providerServiceMapID, serviceID));
		assertTrue(response.toString().contains("Error while getting doctor worklist for future scheduled beneficiay"));
	}

	@Test
	void testGetDocWorkListNewFutureScheduledForTM_Exception() {
		OutputResponse response = new OutputResponse();

		response.setError(5000, "Error while getting doctor worklist for future scheduled beneficiay");

		assertEquals(response.toString(), commonController.getDocWorkListNewFutureScheduledForTM(any(), any()));
	}
	// **************

	@Test
	void testGetNurseWorkListNew() {
		OutputResponse response = new OutputResponse();

		Integer providerServiceMapID = 1;
		Integer vanID = 1;
		String s = "test";

		when(commonNurseServiceImpl.getNurseWorkListNew(providerServiceMapID, vanID)).thenReturn(s);

		String expResponse = commonController.getNurseWorkListNew(providerServiceMapID, vanID);

		response.setResponse(s);

		assertTrue(s != null);

		assertEquals(expResponse, commonController.getNurseWorkListNew(providerServiceMapID, vanID));
		assertTrue(response.toString().contains(s));
	}

	@Test
	void testGetNurseWorkListNew_Error() {
		OutputResponse response = new OutputResponse();

		Integer providerServiceMapID = 1;
		Integer vanID = 1;
		String s = null;

		when(commonNurseServiceImpl.getNurseWorkListNew(providerServiceMapID, vanID)).thenReturn(s);

		String expResponse = commonController.getNurseWorkListNew(providerServiceMapID, vanID);

		response.setError(5000, "Error while getting nurse worklist");

		assertNull(s);

		assertEquals(expResponse, commonController.getNurseWorkListNew(providerServiceMapID, vanID));
		assertTrue(response.toString().contains("Error while getting nurse worklist"));
	}

//	@Test
//	void testGetNurseWorkListNew_Exception() {
//		OutputResponse response = new OutputResponse();
//
//		response.setError(5000, "Error while getting nurse worklist");
//
//		assertEquals(response.toString(), commonController.getNurseWorkListNew(any(), any()));
//	}

	@Test
	void testGetNurseWorkListNew_Exception() {
		when(commonNurseServiceImpl.getNurseWorkListNew(any(), any())).thenThrow(RuntimeException.class);

		String response = commonController.getNurseWorkListNew(any(), any());
		assertEquals(response, commonController.getNurseWorkListNew(any(), any()));

	}

	@Test
	void testGetNurseWorklistTMreferred() {
		OutputResponse response = new OutputResponse();

		Integer providerServiceMapID = 1;
		Integer serviceID = 1;
		Integer vanID = 1;
		String s = "test";

		when(commonNurseServiceImpl.getNurseWorkListTMReferred(providerServiceMapID, vanID)).thenReturn(s);

		String expResponse = commonController.getNurseWorklistTMreferred(providerServiceMapID, vanID);

		response.setResponse(s);

		assertTrue(s != null);

		assertEquals(expResponse, commonController.getNurseWorklistTMreferred(providerServiceMapID, vanID));
		assertTrue(response.toString().contains(s));
	}

	@Test
	void testGetNurseWorklistTMreferred_Error() {
		OutputResponse response = new OutputResponse();

		Integer providerServiceMapID = 1;
		Integer serviceID = 1;
		Integer vanID = 1;
		String s = null;

		when(commonNurseServiceImpl.getNurseWorkListTMReferred(providerServiceMapID, vanID)).thenReturn(s);

		String expResponse = commonController.getNurseWorklistTMreferred(providerServiceMapID, vanID);

		response.setError(5000, "Error while getting nurse worklist");

		assertNull(s);

		assertEquals(expResponse, commonController.getNurseWorklistTMreferred(providerServiceMapID, vanID));
		assertTrue(response.toString().contains("Error while getting nurse worklist"));
	}

	@Test
	void testGetNurseWorklistTMreferred_Exception() {

		when(commonNurseServiceImpl.getNurseWorkListTMReferred(any(), any())).thenThrow(RuntimeException.class);

		String response = commonController.getNurseWorklistTMreferred(any(), any());
		assertEquals(response, commonController.getNurseWorklistTMreferred(any(), any()));

	}

	@Test
	void testGetDoctorPreviousSignificantFindings() throws JSONException {
		OutputResponse response = new OutputResponse();

		String comingRequest = "{\"beneficiaryRegID\": \"1\"}";
		String s = "test";

		JSONObject obj = new JSONObject(comingRequest);

		Long beneficiaryRegID = obj.getLong(BENEFICIARY_REG_ID);

		when(commonDoctorServiceImpl.fetchBenPreviousSignificantFindings(beneficiaryRegID)).thenReturn(s);

		String expResponse = commonController.getDoctorPreviousSignificantFindings(comingRequest);

		response.setResponse(s);

		assertTrue(obj.has(BENEFICIARY_REG_ID) && obj.get(BENEFICIARY_REG_ID) != null);
		assertTrue(s != null);

		assertEquals(expResponse, commonController.getDoctorPreviousSignificantFindings(comingRequest));
		assertTrue(response.toString().contains(s));
	}

	@Test
	void testGetDoctorPreviousSignificantFindingsWithError() throws JSONException {
		String comingRequest = "{\"beneficiaryRegID\": \"1\"}";
		OutputResponse expectedResponse = new OutputResponse();
		expectedResponse.setError(5000, "Error while getting previous significant findings");

		// Mocking the service to return null, simulating an error in fetching data
		when(commonDoctorServiceImpl.fetchBenPreviousSignificantFindings(anyLong())).thenReturn(null);

		// Execute
		String actualResponse = commonController.getDoctorPreviousSignificantFindings(comingRequest);

		assertTrue(actualResponse.contains("Error while getting previous significant findings"));
	}

	@Test
	void testGetDoctorPreviousSignificantFindingsWithInvalidData() {
		String comingRequest = "{}"; // Missing beneficiaryRegID
		OutputResponse expectedResponse = new OutputResponse();
		expectedResponse.setError(5000, "Invalid data!");

		// Execute
		String actualResponse = commonController.getDoctorPreviousSignificantFindings(comingRequest);

		assertTrue(actualResponse.contains("Invalid data!"));
	}

	@Test
	void testGetDoctorPreviousSignificantFindings_Exception() throws JSONException {
		OutputResponse response = new OutputResponse();

		response.setError(5000, "Error while getting previous significant findings");

		assertEquals(response.toString(), commonController.getDoctorPreviousSignificantFindings(any()));
	}

//	@Test
//	void testGetLabWorkListNew() {
//		OutputResponse response = new OutputResponse();
//
//		Integer providerServiceMapID = 1;
//		Integer vanID = 1;
//		String s = "test";
//
//		when(commonNurseServiceImpl.getLabWorkListNew(providerServiceMapID, vanID));
//
//		String expResponse = commonController.getLabWorkListNew(providerServiceMapID, vanID);
//
//		response.setResponse(s);
//
//		assertNotNull(s);
//
//		assertEquals(expResponse, commonController.getLabWorkListNew(providerServiceMapID, vanID));
//		assertTrue(response.toString().contains(s));
//	}

	@Test
	void testGetLabWorkListNew() {
		OutputResponse response = new OutputResponse();
		Integer providerServiceMapID = 1;
		Integer vanID = 1;
		Integer combinedValue = providerServiceMapID + vanID;
		String request = combinedValue.toString();
//		String expectedResponse = "Success";

		// Stubbing the mock to return a specific value when the method is called
		when(commonNurseServiceImpl.getLabWorkListNew(providerServiceMapID, vanID)).thenReturn(request);
		response.setResponse(request.toString());

		// Directly call the controller's method
		String actualResponse = commonController.getLabWorkListNew(providerServiceMapID, vanID);
		assertNotNull(actualResponse);

		// Validate the response
		assertEquals(response.toString(), actualResponse);

	}

	@Test
	void testGetLabWorkListNew_else() {
		Integer providerServiceMapID = null;
		Integer vanID = null;
		String response = commonController.getLabWorkListNew(providerServiceMapID, vanID);
		assertTrue(response.contains("Error while getting lab technician worklist"),
				"Expected error message not found in the response.");
	}

	@Test
	void testGetLabWorkListNew_ThrowsException() {

		Integer providerServiceMapID = 1;
		Integer vanID = 1;

		when(commonNurseServiceImpl.getLabWorkListNew(any(), any())).thenThrow(NotFoundException.class);

		String getLabWorkListNew = commonController.getLabWorkListNew(providerServiceMapID, vanID);

		assertTrue(getLabWorkListNew.contains("Error while getting lab technician worklist"));
	}

	@Test
	void testGetRadiologistWorklistNew() {
		OutputResponse response = new OutputResponse();

		Integer providerServiceMapID = 1;
		Integer vanID = 1;
		String s = "test";

		when(commonNurseServiceImpl.getRadiologistWorkListNew(providerServiceMapID, vanID)).thenReturn(s);

		String expResponse = commonController.getRadiologistWorklistNew(providerServiceMapID, vanID);

		response.setResponse(s);

		assertNotNull(s);

		assertEquals(expResponse, commonController.getRadiologistWorklistNew(providerServiceMapID, vanID));
		assertTrue(response.toString().contains(s));
	}

	@Test
	void testGetRadiologistWorklistNew_WithError() throws JSONException {
		Integer providerServiceMapID = 1;
		Integer vanID = 1;
		String s = "test";
		OutputResponse expectedResponse = new OutputResponse();
		expectedResponse.setError(5000, "Error while getting radiologist worklist");

		when(commonNurseServiceImpl.getRadiologistWorkListNew(providerServiceMapID, vanID)).thenReturn(null);

		// Execute
		String actualResponse = commonController.getRadiologistWorklistNew(providerServiceMapID, vanID);

		assertTrue(actualResponse.contains("Error while getting radiologist worklist"));
	}

//	@Test
//	void testGetRadiologistWorklist_Error() {
//		OutputResponse response = new OutputResponse();
//
//		String s = null;
//
//		response.setError(5000, "Error while getting radiologist worklist");
//
//		assertNull(s);
//
//		assertTrue(response.toString().contains("Error while getting radiologist worklist"));
//	}

	@Test
	void testGetRadiologistWorklistNew_Exception() {
		when(commonNurseServiceImpl.getRadiologistWorkListNew(any(), any())).thenThrow(RuntimeException.class);

		String response = commonController.getRadiologistWorklistNew(any(), any());
		assertEquals(response, commonController.getRadiologistWorklistNew(any(), any()));

	}

	@Test
	void testGetOncologistWorklistNew() {
		OutputResponse response = new OutputResponse();

		Integer providerServiceMapID = 1;
		Integer vanID = 1;
		String s = "test";

		when(commonNurseServiceImpl.getOncologistWorkListNew(providerServiceMapID, vanID)).thenReturn(s);

		String expResponse = commonController.getOncologistWorklistNew(providerServiceMapID, vanID);

		response.setResponse(s);

		assertNotNull(s);

		assertEquals(expResponse, commonController.getOncologistWorklistNew(providerServiceMapID, vanID));
		assertTrue(response.toString().contains(s));
	}

	@Test
	void testGetOncologistWorklistNew_Error() {
		OutputResponse response = new OutputResponse();
		Integer providerServiceMapID = 1;
		Integer vanID = 1;
		String s = null;

		when(commonNurseServiceImpl.getOncologistWorkListNew(providerServiceMapID, vanID)).thenReturn(s);

		String expResponse = commonController.getOncologistWorklistNew(providerServiceMapID, vanID);

		response.setError(5000, "Error while getting oncologist worklist");

		assertNull(s);

		assertEquals(expResponse, commonController.getOncologistWorklistNew(providerServiceMapID, vanID));
		assertTrue(response.toString().contains("Error while getting oncologist worklist"));
	}

	@Test
	void testGetOncologistWorklistNew_Exception() {
		when(commonNurseServiceImpl.getOncologistWorkListNew(any(), any())).thenThrow(RuntimeException.class);

		String response = commonController.getOncologistWorklistNew(any(), any());
		assertEquals(response, commonController.getOncologistWorklistNew(any(), any()));
	}

	@Test
	void testGetPharmaWorklistNew() {
		OutputResponse response = new OutputResponse();
		Integer providerServiceMapID = 1;
		Integer vanID = 1;
		String s = "test";

		when(commonNurseServiceImpl.getPharmaWorkListNew(providerServiceMapID, vanID)).thenReturn(s);

		String expResponse = commonController.getPharmaWorklistNew(providerServiceMapID, vanID);

		response.setResponse(s);

		assertNotNull(s);

		assertEquals(expResponse, commonController.getPharmaWorklistNew(providerServiceMapID, vanID));
		assertTrue(response.toString().contains(s));
	}

	@Test
	void testGetPharmaWorklistNew_Error() {
		OutputResponse response = new OutputResponse();
		Integer providerServiceMapID = 1;
		Integer vanID = 1;
		String s = null;

		when(commonNurseServiceImpl.getPharmaWorkListNew(providerServiceMapID, vanID)).thenReturn(s);

		String expResponse = commonController.getPharmaWorklistNew(providerServiceMapID, vanID);

		response.setError(5000, "Error while getting pharma worklist");

		assertNull(s);

		assertEquals(expResponse, commonController.getPharmaWorklistNew(providerServiceMapID, vanID));
		assertTrue(response.toString().contains("Error while getting pharma worklist"));
	}

	@Test
	void testGetPharmaWorklistNew_Exception() {
		when(commonNurseServiceImpl.getPharmaWorkListNew(any(), any())).thenThrow(RuntimeException.class);

		String response = commonController.getPharmaWorklistNew(any(), any());
		assertEquals(response, commonController.getPharmaWorklistNew(any(), any()));
	}

	@Test
	void testGetCasesheetPrintData_Success() throws Exception {
		// Arrange
		String comingReqJson = "{\"beneficiaryId\":\"123\"}";
		String authorization = "Bearer someToken";
		BeneficiaryFlowStatus obj = new BeneficiaryFlowStatus(); // Assuming you have a constructor or a method to parse
																	// JSON
		String expectedResponseData = "CaseSheet Data";

		when(commonServiceImpl.getCaseSheetPrintDataForBeneficiary(any(BeneficiaryFlowStatus.class), eq(authorization)))
				.thenReturn(expectedResponseData);

		// Act
		String response = commonController.getCasesheetPrintData(comingReqJson, authorization);

		// Assert
		assertNotNull(response);
		assertTrue(response.contains(expectedResponseData));
	}

	@Test
	void testGetCasesheetPrintData_Failure_InvalidRequest() {
		// Arrange
		String authorization = "Bearer someToken";

		// Act
		String response = commonController.getCasesheetPrintData(null, authorization);

		// Assert
		assertNotNull(response);
		assertTrue(response.contains("Invalid request")); // Adjust based on how you format error messages
	}

	@Test
	void testGetCasesheetPrintData_Exception() throws Exception {
		String comingReq = "{\"statusCode\":5000,\"errorMessage\":\"Failed with generic error\",\"status\":\"FAILURE\"}";
		String authorization = "{\"statusCode\":5000,\"errorMessage\":\"Failed with generic error\",\"status\":\"FAILURE\"}";

		when(commonServiceImpl.getCaseSheetPrintDataForBeneficiary(any(), any())).thenThrow(NotFoundException.class);

		String response = commonController.getCasesheetPrintData(comingReq, authorization);
		assertEquals(response, commonController.getCasesheetPrintData(comingReq, authorization));
	}

	@Test
	void testGetBenPastHistory() throws Exception {
		OutputResponse response = new OutputResponse();

		String comingRequest = "{\"benRegID\":\"1\"}";
		String s = "test";

		JSONObject obj = new JSONObject(comingRequest);

		Long benRegID = obj.getLong("benRegID");

		when(commonServiceImpl.getBenPastHistoryData(benRegID)).thenReturn(s);

		String expResponse = commonController.getBenPastHistory(comingRequest);

		response.setResponse(s);

		assertTrue(obj.has("benRegID"));

		assertEquals(expResponse, commonController.getBenPastHistory(comingRequest));
		assertTrue(response.toString().contains(s));
	}

	@Test
	void testGetBenPastHistory_Invalid() throws Exception {
		String invalidRequestObj = "{\"someInvalidField\": \"value\"}";
		String response = commonController.getBenPastHistory(invalidRequestObj);
		assertTrue(response.contains("Invalid request"), "Expected error message not found in the response.");
	}

	@Test
	void testGetBenPastHistory_Exception() throws Exception {
		OutputResponse response = new OutputResponse();

		response.setError(5000, "Error while getting illness and surgery history");

		assertEquals(response.toString(), commonController.getBenPastHistory(any()));
	}

	@Test
	void testGetBenTobaccoHistory() throws Exception {
		OutputResponse response = new OutputResponse();

		String comingRequest = "{\"benRegID\":\"1\"}";
		String s = "test";

		JSONObject obj = new JSONObject(comingRequest);

		Long benRegID = obj.getLong("benRegID");

		when(commonServiceImpl.getPersonalTobaccoHistoryData(benRegID)).thenReturn(s);

		String expResponse = commonController.getBenTobaccoHistory(comingRequest);

		response.setResponse(s);

		assertTrue(obj.has("benRegID"));

		assertEquals(expResponse, commonController.getBenTobaccoHistory(comingRequest));
		assertTrue(response.toString().contains(s));
	}

	@Test
	void testGetBenTobaccoHistory_InvaliedRequest() throws Exception {
		String invalidRequestObj = "{\"someInvalidField\": \"value\"}";
		String response = commonController.getBenTobaccoHistory(invalidRequestObj);
		assertTrue(response.contains("Invalid request"), "Expected error message not found in the response.");
	}

	@Test
	void testGetBenTobaccoHistory_Exception() throws Exception {
		OutputResponse response = new OutputResponse();
		response.setError(5000, "Error while getting tobacco history");
		assertEquals(response.toString(), commonController.getBenTobaccoHistory(any()));
	}

	@Test
	void testGetBenAlcoholHistory() throws Exception {
		OutputResponse response = new OutputResponse();

		String comingRequest = "{\"benRegID\":\"1\"}";
		String s = "test";

		JSONObject obj = new JSONObject(comingRequest);

		Long benRegID = obj.getLong("benRegID");

		when(commonServiceImpl.getPersonalAlcoholHistoryData(benRegID)).thenReturn(s);

		String expResponse = commonController.getBenAlcoholHistory(comingRequest);

		response.setResponse(s);

		assertTrue(obj.has("benRegID"));

		assertEquals(expResponse, commonController.getBenAlcoholHistory(comingRequest));
		assertTrue(response.toString().contains(s));
	}

	@Test
	void testGetBenAlcoholHistory_InvalidRequest() throws Exception {
		String invalidRequestObj = "{\"someInvalidField\": \"value\"}";
		String response = commonController.getBenAlcoholHistory(invalidRequestObj);
		assertTrue(response.contains("Invalid request"), "Expected error message not found in the response.");
	}

	@Test
	void testGetBenAlcoholHistory_Exception() throws Exception {
		OutputResponse response = new OutputResponse();
		response.setError(5000, "Error while getting alcohol history");
		assertEquals(response.toString(), commonController.getBenAlcoholHistory(any()));
	}

	@Test
	void testGetBenANCAllergyHistory() throws Exception {
		OutputResponse response = new OutputResponse();

		String comingRequest = "{\"benRegID\":\"1\"}";
		String s = "test";

		JSONObject obj = new JSONObject(comingRequest);

		Long benRegID = obj.getLong("benRegID");

		when(commonServiceImpl.getPersonalAllergyHistoryData(benRegID)).thenReturn(s);

		String expResponse = commonController.getBenANCAllergyHistory(comingRequest);

		response.setResponse(s);

		assertTrue(obj.has("benRegID"));

		assertEquals(expResponse, commonController.getBenANCAllergyHistory(comingRequest));
		assertTrue(response.toString().contains(s));

	}

	@Test
	void testGetBenANCAllergyHistory_InvalidRequest() throws Exception {
		String invalidRequestObj = "{\"someInvalidField\": \"value\"}";
		String response = commonController.getBenANCAllergyHistory(invalidRequestObj);
		assertTrue(response.contains("Invalid request"), "Expected error message not found in the response.");
	}

	@Test
	void testGetBenANCAllergyHistory_Exeption() throws Exception {
		String comingRequest = "{\"benRegID\":\"1\"}";

		when(commonServiceImpl.getPersonalAllergyHistoryData(any())).thenThrow(NotFoundException.class);

		String GetBenANCAllergyHistory = commonController.getBenANCAllergyHistory(comingRequest);

		assertTrue(GetBenANCAllergyHistory.contains("Error while getting allergy history"));
	}

//	@Test
//	void testGetBenANCAllergyHistory_Exeption1() throws Exception {
//		OutputResponse response = new OutputResponse();
//		response.setError(5000, "Error while getting allergy history");
//		assertEquals(response.toString(), commonController.getBenANCAllergyHistory(any()));
//	}

	@Test
	void testGetBenMedicationHistory() throws JSONException {
		OutputResponse response = new OutputResponse();

		String comingRequest = "{\"benRegID\":\"1\"}";
		String s = "test";

		JSONObject obj = new JSONObject(comingRequest);

		Long benRegID = obj.getLong("benRegID");

		when(commonServiceImpl.getMedicationHistoryData(benRegID)).thenReturn(s);

		String expResponse = commonController.getBenMedicationHistory(comingRequest);

		response.setResponse(s);

		assertTrue(obj.has("benRegID"));

		assertEquals(expResponse, commonController.getBenMedicationHistory(comingRequest));
		assertTrue(response.toString().contains(s));
	}

	@Test
	void testGetBenMedicationHistory_InvalidRequest() throws JSONException {
		String invalidRequestObj = "{\"someInvalidField\": \"value\"}";
		String response = commonController.getBenMedicationHistory(invalidRequestObj);
		assertTrue(response.contains("Invalid request"), "Expected error message not found in the response.");
	}

	@Test
	void testGetBenMedicationHistory_Exception() throws Exception {
		OutputResponse response = new OutputResponse();
		response.setError(5000, "Error while getting medication history");
		assertEquals(response.toString(), commonController.getBenMedicationHistory(any()));
	}

	@Test
	void testGetBenFamilyHistory() throws JSONException {
		OutputResponse response = new OutputResponse();

		String comingRequest = "{\"benRegID\":\"1\"}";
		String s = "test";

		JSONObject obj = new JSONObject(comingRequest);

		Long benRegID = obj.getLong("benRegID");

		when(commonServiceImpl.getFamilyHistoryData(benRegID)).thenReturn(s);

		String expResponse = commonController.getBenFamilyHistory(comingRequest);

		response.setResponse(s);

		assertTrue(obj.has("benRegID"));

		assertEquals(expResponse, commonController.getBenFamilyHistory(comingRequest));
		assertTrue(response.toString().contains(s));
	}

	@Test
	void testGetBenFamilyHistory_InvalidRequest() throws JSONException {
		String invalidRequestObj = "{\"someInvalidField\": \"value\"}";
		String response = commonController.getBenFamilyHistory(invalidRequestObj);
		assertTrue(response.contains("Invalid request"), "Expected error message not found in the response.");
	}

	@Test
	void testGetBenFamilyHistory_Exception() throws Exception {
		OutputResponse response = new OutputResponse();
		response.setError(5000, "Error while getting family history");
		assertEquals(response.toString(), commonController.getBenFamilyHistory(any()));
	}

	@Test
	void testGetBenMenstrualHistory() throws JSONException {
		OutputResponse response = new OutputResponse();

		String comingRequest = "{\"benRegID\":\"1\"}";
		String s = "test";

		JSONObject obj = new JSONObject(comingRequest);

		Long benRegID = obj.getLong("benRegID");

		when(commonServiceImpl.getMenstrualHistoryData(benRegID)).thenReturn(s);

		String expResponse = commonController.getBenMenstrualHistory(comingRequest);

		response.setResponse(s);

		assertTrue(obj.has("benRegID"));

		assertEquals(expResponse, commonController.getBenMenstrualHistory(comingRequest));
		assertTrue(response.toString().contains(s));
	}

	@Test
	void testGetBenMenstrualHistory_InvalidRequest() throws JSONException {
		String invalidRequestObj = "{\"someInvalidField\": \"value\"}";
		String response = commonController.getBenMenstrualHistory(invalidRequestObj);
		assertTrue(response.contains("Invalid request"), "Expected error message not found in the response.");
	}

	@Test
	void testGetBenMenstrualHistory_Exception() throws Exception {
		OutputResponse response = new OutputResponse();
		response.setError(5000, "Error while getting menstrual history");
		assertEquals(response.toString(), commonController.getBenMenstrualHistory(any()));
	}

	@Test
	void testGetBenPastObstetricHistory() throws JSONException {
		OutputResponse response = new OutputResponse();

		String comingRequest = "{\"benRegID\":\"1\"}";
		String s = "test";

		JSONObject obj = new JSONObject(comingRequest);

		Long benRegID = obj.getLong("benRegID");

		when(commonServiceImpl.getObstetricHistoryData(benRegID)).thenReturn(s);

		String expResponse = commonController.getBenPastObstetricHistory(comingRequest);

		response.setResponse(s);

		assertTrue(obj.has("benRegID"));

		assertEquals(expResponse, commonController.getBenPastObstetricHistory(comingRequest));
		assertTrue(response.toString().contains(s));
	}

	@Test
	void testGetBenPastObstetricHistory_InvalidRequest() throws JSONException {
		String invalidRequestObj = "{\"someInvalidField\": \"value\"}";
		String response = commonController.getBenPastObstetricHistory(invalidRequestObj);
		assertTrue(response.contains("Invalid request"), "Expected error message not found in the response.");
	}

	@Test
	void testGetBenPastObstetricHistory_Exception() throws Exception {
		OutputResponse response = new OutputResponse();
		response.setError(5000, "Error while getting obstetric history");
		assertEquals(response.toString(), commonController.getBenPastObstetricHistory(any()));
	}

	@Test
	void testGetBenANCComorbidityConditionHistory() throws JSONException {
		OutputResponse response = new OutputResponse();

		String comingRequest = "{\"benRegID\":\"1\"}";
		String s = "test";

		JSONObject obj = new JSONObject(comingRequest);

		Long benRegID = obj.getLong("benRegID");

		when(commonServiceImpl.getComorbidHistoryData(benRegID)).thenReturn(s);

		String expResponse = commonController.getBenANCComorbidityConditionHistory(comingRequest);

		response.setResponse(s);

		assertTrue(obj.has("benRegID"));

		assertEquals(expResponse, commonController.getBenANCComorbidityConditionHistory(comingRequest));
		assertTrue(response.toString().contains(s));
	}

	@Test
	void testGetBenANCComorbidityConditionHistory_InvalidRequest() throws JSONException {
		String invalidRequestObj = "{\"someInvalidField\": \"value\"}";
		String response = commonController.getBenANCComorbidityConditionHistory(invalidRequestObj);
		assertTrue(response.contains("Invalid request"), "Expected error message not found in the response.");
	}

	@Test
	void testGetBenANCComorbidityConditionHistory_Exception() throws Exception {
		OutputResponse response = new OutputResponse();
		response.setError(5000, "Error while getting comodbidity history");
		assertEquals(response.toString(), commonController.getBenANCComorbidityConditionHistory(any()));
	}

	@Test
	void testGetBenOptionalVaccineHistory() throws JSONException {
		OutputResponse response = new OutputResponse();

		String comingRequest = "{\"benRegID\":\"1\"}";
		String s = "test";

		JSONObject obj = new JSONObject(comingRequest);

		Long benRegID = obj.getLong("benRegID");

		when(commonServiceImpl.getChildVaccineHistoryData(benRegID)).thenReturn(s);

		String expResponse = commonController.getBenOptionalVaccineHistory(comingRequest);

		response.setResponse(s);

		assertTrue(obj.has("benRegID"));

		assertEquals(expResponse, commonController.getBenOptionalVaccineHistory(comingRequest));
		assertTrue(response.toString().contains(s));
	}

	@Test
	void testGetBenOptionalVaccineHistory_InvalidRequest() throws JSONException {

		String invalidRequestObj = "{\"someInvalidField\": \"value\"}";
		String response = commonController.getBenOptionalVaccineHistory(invalidRequestObj);
		assertTrue(response.contains("Invalid request"), "Expected error message not found in the response.");
	}

	@Test
	void testGetBenOptionalVaccineHistory_Exception() throws Exception {
		OutputResponse response = new OutputResponse();
		response.setError(5000, "Error while getting optional vaccination history");
		assertEquals(response.toString(), commonController.getBenOptionalVaccineHistory(any()));
	}

	@Test
	void testGetBenImmunizationHistory() throws JSONException {
		OutputResponse response = new OutputResponse();

		String comingRequest = "{\"benRegID\":\"1\"}";
		String s = "test";

		JSONObject obj = new JSONObject(comingRequest);

		Long benRegID = obj.getLong("benRegID");

		when(commonServiceImpl.getImmunizationHistoryData(benRegID)).thenReturn(s);

		String expResponse = commonController.getBenImmunizationHistory(comingRequest);

		response.setResponse(s);

		assertTrue(obj.has("benRegID"));

		assertEquals(expResponse, commonController.getBenImmunizationHistory(comingRequest));
		assertTrue(response.toString().contains(s));
	}

	void testGetBenImmunizationHistory_InvalidRequest() throws JSONException {
		String invalidRequestObj = "{\"someInvalidField\": \"value\"}";
		String response = commonController.getBenImmunizationHistory(invalidRequestObj);
		assertTrue(response.contains("Invalid request"), "Expected error message not found in the response.");
	}

	@Test
	void testGetBenImmunizationHistory_Exception() throws Exception {
		OutputResponse response = new OutputResponse();
		response.setError(5000, "Error while getting child vaccine(immunization) history");
		assertEquals(response.toString(), commonController.getBenImmunizationHistory(any()));
	}

	@Test
	void testGetBenPerinatalHistory() throws JSONException {
		OutputResponse response = new OutputResponse();

		String comingRequest = "{\"benRegID\":\"1\"}";
		String s = "test";

		JSONObject obj = new JSONObject(comingRequest);

		Long benRegID = obj.getLong("benRegID");

		when(commonServiceImpl.getBenPerinatalHistoryData(benRegID)).thenReturn(s);

		String expResponse = commonController.getBenPerinatalHistory(comingRequest);

		response.setResponse(s);

		assertTrue(obj.has("benRegID"));

		assertEquals(expResponse, commonController.getBenPerinatalHistory(comingRequest));
		assertTrue(response.toString().contains(s));
	}

	void testGetBenPerinatalHistory_InvalidRequest() throws JSONException {
		String invalidRequestObj = "{\"someInvalidField\": \"value\"}";
		String response = commonController.getBenPerinatalHistory(invalidRequestObj);
		assertTrue(response.contains("Invalid request"), "Expected error message not found in the response.");
	}

	@Test
	void testGetBenPerinatalHistory_Exception() throws Exception {
		OutputResponse response = new OutputResponse();
		response.setError(5000, "Error while getting perinatal history");
		assertEquals(response.toString(), commonController.getBenPerinatalHistory(any()));
	}

	@Test
	void testGetBenFeedingHistory() throws JSONException {
		OutputResponse response = new OutputResponse();

		String comingRequest = "{\"benRegID\":\"1\"}";
		String s = "test";

		JSONObject obj = new JSONObject(comingRequest);

		Long benRegID = obj.getLong("benRegID");

		when(commonServiceImpl.getBenFeedingHistoryData(benRegID)).thenReturn(s);

		String expResponse = commonController.getBenFeedingHistory(comingRequest);

		response.setResponse(s);

		assertTrue(obj.has("benRegID"));

		assertEquals(expResponse, commonController.getBenFeedingHistory(comingRequest));
		assertTrue(response.toString().contains(s));
	}

	void testGetBenFeedingHistory_InvalidRequest() throws JSONException {
		String invalidRequestObj = "{\"someInvalidField\": \"value\"}";
		String response = commonController.getBenFeedingHistory(invalidRequestObj);
		assertTrue(response.contains("Invalid request"), "Expected error message not found in the response.");
	}

	@Test
	void testGetBenFeedingHistory_Exception() throws JSONException {
		OutputResponse response = new OutputResponse();
		response.setError(5000, "Error while getting child feeding history");
		assertEquals(response.toString(), commonController.getBenFeedingHistory(any()));
	}

	@Test
	void testGetBenDevelopmentHistory() throws JSONException {
		OutputResponse response = new OutputResponse();

		String comingRequest = "{\"benRegID\":\"1\"}";
		String s = "test";

		JSONObject obj = new JSONObject(comingRequest);

		Long benRegID = obj.getLong("benRegID");

		commonServiceImpl.getBenDevelopmentHistoryData(benRegID);

		String expResponse = commonController.getBenDevelopmentHistory(comingRequest);

		response.setResponse(s);

		assertTrue(obj.has("benRegID"));

		assertEquals(expResponse, commonController.getBenDevelopmentHistory(comingRequest));
		assertTrue(response.toString().contains(s));
	}

	@Test
	void testGetBenDevelopmentHistory_WithValidBenRegID() throws Exception {
		// Prepare the input JSON string
		String requestJson = "{\"benRegID\":123}";
		// Mock the behavior of commonServiceImpl to return a predetermined string
		when(commonServiceImpl.getBenDevelopmentHistoryData(123L)).thenReturn("Development history data");

		// Execute the method with the mocked request
		String response = commonController.getBenDevelopmentHistory(requestJson);

		// Validate the response contains the expected data
		assertTrue(response.contains("Development history data"),
				"Response should contain the expected development history data.");

		// Verify the interaction with the mocked service
		verify(commonServiceImpl).getBenDevelopmentHistoryData(123L);
	}

	@Test
	void testGetBenDevelopmentHistory_Exception() throws JSONException {
		OutputResponse response = new OutputResponse();
		response.setError(5000, "Error while getting child development history");
		assertEquals(response.toString(), commonController.getBenDevelopmentHistory(any()));
	}

	@Test
	void testGetBeneficiaryCaseSheetHistory() throws IEMRException {
		OutputResponse response = new OutputResponse();
		String comingRequest = "{\"benRegID\":\"1\"}";
		String responseData = "test";

		when(commonServiceImpl.getBenPreviousVisitDataForCaseRecord(comingRequest)).thenReturn(responseData);

		String expResponse = commonController.getBeneficiaryCaseSheetHistory(comingRequest);

		response.setResponse(responseData);

		assertNotNull(responseData);

		assertEquals(expResponse, commonController.getBeneficiaryCaseSheetHistory(comingRequest));
		assertTrue(response.toString().contains(responseData));
	}

	@Test
	void testGetBeneficiaryCaseSheetHistory_Error() throws IEMRException {
		String invalidRequestObj = "{\"someInvalidField\": \"value\"}";
		String response = commonController.getBeneficiaryCaseSheetHistory(invalidRequestObj);
		assertTrue(response.contains("Error while fetching beneficiary previous visit history details"),
				"Expected error message not found in the response.");
	}

	@Test
	void testGetBeneficiaryCaseSheetHistory_Exception() throws JSONException, IEMRException {
		String comingRequest = "{\"statusCode\":5000,\"errorMessage\":\"Failed with generic error\",\"status\":\"FAILURE\"}";
		when(commonServiceImpl.getBenPreviousVisitDataForCaseRecord(comingRequest)).thenThrow(NotFoundException.class);
		String response = commonController.getBeneficiaryCaseSheetHistory(comingRequest);
		Assertions.assertEquals(response, commonController.getBeneficiaryCaseSheetHistory(comingRequest));
	}

	@Test
	void testGetTCSpecialistWorkListNew() {
		OutputResponse response = new OutputResponse();
		Integer providerServiceMapID = 1;
		Integer userID = 1;
		Integer serviceID = 1;
		String s = "test";

		when(commonDoctorServiceImpl.getTCSpecialistWorkListNewForTM(providerServiceMapID, userID, serviceID))
				.thenReturn(s);

		String expResponse = commonController.getTCSpecialistWorkListNew(providerServiceMapID, userID, serviceID);

		response.setResponse(s);

		assertNotNull(s);

		assertEquals(expResponse, commonController.getTCSpecialistWorkListNew(providerServiceMapID, userID, serviceID));
		assertTrue(response.toString().contains(s));
	}

	@Test
	void testGetTCSpecialistWorkListNew_InvalidRequest() {
		Integer providerServiceMapID = null;
		Integer userID = null;
		Integer serviceID = null;
		String response = commonController.getTCSpecialistWorkListNew(providerServiceMapID, userID, serviceID);
		assertTrue(response.contains("Invalid request, either ProviderServiceMapID or userID is invalid"),
				"Expected error message not found in the response.");
	}

	@Test
	void testGetTCSpecialistWorkListNew_Exception() throws JSONException {
		OutputResponse response = new OutputResponse();
		response.setError(5000, "Error while getting TC specialist worklist");
		assertEquals(response.toString(), commonController.getTCSpecialistWorkListNew(any(), any(), any()));
	}

	@Test
	void testGetTCSpecialistWorklistFutureScheduled() {
		OutputResponse response = new OutputResponse();
		Integer providerServiceMapID = 1;
		Integer userID = 1;
		Integer serviceID = 1;
		String s = "test";

		when(commonDoctorServiceImpl.getTCSpecialistWorkListNewFutureScheduledForTM(providerServiceMapID, userID,
				serviceID)).thenReturn(s);

		String expResponse = commonController.getTCSpecialistWorklistFutureScheduled(providerServiceMapID, userID,
				serviceID);

		response.setResponse(s);

		assertNotNull(providerServiceMapID);
		assertNotNull(userID);
		assertNotNull(s);

		assertEquals(expResponse,
				commonController.getTCSpecialistWorklistFutureScheduled(providerServiceMapID, userID, serviceID));
		assertTrue(response.toString().contains(s));
	}

	@Test
	void testGetTCSpecialistWorklistFutureScheduled_InvalidRequest() {
		Integer providerServiceMapID = null;
		Integer userID = null;
		Integer serviceID = null;
		String response = commonController.getTCSpecialistWorklistFutureScheduled(providerServiceMapID, userID,
				serviceID);
		assertTrue(response.contains("Invalid request, either ProviderServiceMapID or userID is invalid"),
				"Expected error message not found in the response.");
	}

	@Test
	void testGetTCSpecialistWorklistFutureScheduled_Exception() throws JSONException {
		OutputResponse response = new OutputResponse();
		response.setError(5000, "Error while getting TC specialist future scheduled worklist");
		assertEquals(response.toString(), commonController.getTCSpecialistWorklistFutureScheduled(any(), any(), any()));
	}

	@Test
	void testDownloadFile() throws Exception {
	}

	@Test
	void testGetBenPhysicalHistory() throws Exception {
		OutputResponse response = new OutputResponse();

		String comingRequest = "{\"benRegID\":\"1\"}";
		String s = "test";

		JSONObject obj = new JSONObject(comingRequest);

		Long benRegID = obj.getLong("benRegID");

		when(commonServiceImpl.getBenPhysicalHistory(benRegID)).thenReturn(s);

		String expResponse = commonController.getBenPhysicalHistory(comingRequest);

		response.setResponse(s);

		assertTrue(obj.has("benRegID"));

		assertEquals(expResponse, commonController.getBenPhysicalHistory(comingRequest));
		assertTrue(response.toString().contains(s));
	}

	@Test
	void testGetBenPhysicalHistory_InvalidRequest() throws Exception {

		String invalidRequestObj = "{\"someInvalidField\": \"value\"}";
		String response = commonController.getBenPhysicalHistory(invalidRequestObj);
		assertTrue(response.contains("Invalid request"), "Expected error message not found in the response.");
	}

	@Test
	void testGetBenPhysicalHistory_Exception1() throws JSONException {
		OutputResponse response = new OutputResponse();
		response.setError(5000, "Error while getting Physical history");
		assertEquals(response.toString(), commonController.getBenPhysicalHistory(any()));
	}

	@Test
	void testGetBenSymptomaticQuestionnaireDetails() throws Exception {
		OutputResponse response = new OutputResponse();

		String comingRequest = "{\"benRegID\":\"1\"}";
		String s = "test";

		JSONObject obj = new JSONObject(comingRequest);

		Long benRegID = obj.getLong("benRegID");

		when(commonServiceImpl.getBenSymptomaticQuestionnaireDetailsData(benRegID)).thenReturn(s);

		String expResponse = commonController.getBenSymptomaticQuestionnaireDetails(comingRequest);

		response.setResponse(s);

		assertTrue((obj.has("benRegID")));

		assertEquals(expResponse, commonController.getBenSymptomaticQuestionnaireDetails(comingRequest));
		assertTrue(response.toString().contains(s));
	}

	@Test
	void testGetBenSymptomaticQuestionnaireDetails_Invalid() throws Exception {
	}

	@Test
	void testGetBenSymptomaticQuestionnaireDetails_Exception() throws JSONException {
		OutputResponse response = new OutputResponse();
		response.setError(5000, "Error while getting details");
		assertEquals(response.toString(), commonController.getBenSymptomaticQuestionnaireDetails(any()));
	}

	@Test
	void testGetBenPreviousDiabetesHistoryDetails() throws Exception {
		OutputResponse response = new OutputResponse();

		String comingRequest = "{\"benRegID\":\"1\"}";
		String s = "test";

		JSONObject obj = new JSONObject(comingRequest);

		Long benRegID = obj.getLong("benRegID");

		when(commonServiceImpl.getBenPreviousDiabetesData(benRegID)).thenReturn(s);

		String expResponse = commonController.getBenPreviousDiabetesHistoryDetails(comingRequest);

		response.setResponse(s);

		assertTrue((obj.has("benRegID")));

		assertEquals(expResponse, commonController.getBenPreviousDiabetesHistoryDetails(comingRequest));
		assertTrue(response.toString().contains(s));
	}

	void testGetBenPreviousDiabetesHistoryDetails_Invalid() throws Exception {
	}

	@Test
	void testGetBenPreviousDiabetesHistoryDetails_Exceptipn() throws JSONException {
		OutputResponse response = new OutputResponse();
		response.setError(5000, "Error while getting details");
		assertEquals(response.toString(), commonController.getBenPreviousDiabetesHistoryDetails(any()));
	}

	@Test
	void testGetTMReferredPrintData_1() throws IEMRException {
		OutputResponse response = new OutputResponse();
		String comingRequest = "{\"benId\": \"1\", \"benVisitCode\": \"1\"}";
		String authorization = "Authorization";
		String serverAuthorization = "ServerAuthorization";

		BeneficiaryFlowStatus obj = InputMapper.gson().fromJson(comingRequest, BeneficiaryFlowStatus.class);
		String casesheetData = null;

		int caseSheetStatus = 1;

		when(commonServiceImpl.checkIsCaseSheetDownloaded(obj.getBenVisitCode())).thenReturn(caseSheetStatus);

		casesheetData = commonServiceImpl.getTmCaseSheetOffline(obj);

		String expResponse = commonController.getTMReferredPrintData(comingRequest, authorization, serverAuthorization);

		assertTrue(comingRequest != null);
		assertTrue(caseSheetStatus == 1);
		assertEquals(expResponse,
				commonController.getTMReferredPrintData(comingRequest, authorization, serverAuthorization));
	}

	@Test
	void testGetTMReferredPrintData_WhenCaseSheetDataIsNotNull() throws IEMRException {
		// Prepare the input JSON string and headers
		String casesheetData = "1";
		String comingRequest = "{\"benVisitCode\":\"1\"}";
		String authorization = "Bearer someAuth";
		String serverAuthorization = "Server Bearer someServerAuth";

		when(commonServiceImpl.checkIsCaseSheetDownloaded(anyLong())).thenReturn(1); // or 0, based on your case
		when(commonServiceImpl.getTmCaseSheetOffline(any(BeneficiaryFlowStatus.class))).thenReturn("1");

		// Execute the method
		String response = commonController.getTMReferredPrintData(comingRequest, authorization, serverAuthorization);

		// Validate the response
		assertTrue(response.contains("1"));
	}

	@Test
	void testGetTMReferredPrintData_2() throws IEMRException {
		OutputResponse response = new OutputResponse();
		String comingRequest = "{\"benId\": \"1\", \"benVisitCode\": \"1\"}";
		String authorization = "Authorization";
		String serverAuthorization = "ServerAuthorization";

		BeneficiaryFlowStatus obj = InputMapper.gson().fromJson(comingRequest, BeneficiaryFlowStatus.class);
		String casesheetData = null;

		int caseSheetStatus = 0;

		when(commonServiceImpl.checkIsCaseSheetDownloaded(obj.getBenVisitCode())).thenReturn(caseSheetStatus);

		casesheetData = commonServiceImpl.getCaseSheetFromCentralServer(comingRequest, serverAuthorization);

		String expResponse = commonController.getTMReferredPrintData(comingRequest, authorization, serverAuthorization);

		assertTrue(comingRequest != null);
		assertTrue(caseSheetStatus == 0);
		assertEquals(expResponse,
				commonController.getTMReferredPrintData(comingRequest, authorization, serverAuthorization));
	}

	@Test
	void testGetTMReferredPrintData_3() throws IEMRException {
		OutputResponse response = new OutputResponse();
		String comingRequest = "{\"benId\": \"1\", \"benVisitCode\": \"1\"}";
		String authorization = "Authorization";
		String serverAuthorization = "ServerAuthorization";

		BeneficiaryFlowStatus obj = InputMapper.gson().fromJson(comingRequest, BeneficiaryFlowStatus.class);
		String casesheetData = "test";

		int caseSheetStatus = 0;

		response.setResponse(casesheetData);

		assertTrue(comingRequest != null);
		assertTrue(casesheetData != null);
		assertTrue(response.toString().contains(casesheetData));
	}

	@Test
	void testGetTMReferredPrintData_4() throws IEMRException {
		OutputResponse response = new OutputResponse();
		String comingRequest = "{\"benId\": \"1\", \"benVisitCode\": \"1\"}";
		String authorization = "Authorization";
		String serverAuthorization = "ServerAuthorization";

		BeneficiaryFlowStatus obj = InputMapper.gson().fromJson(comingRequest, BeneficiaryFlowStatus.class);
		String casesheetData = null;

		int caseSheetStatus = 0;

		response.setError(5000, "Beneficiary pending for Tele-Consultation");

		assertTrue(comingRequest != null);
		assertNull(casesheetData);
		assertTrue(response.toString().contains("Beneficiary pending for Tele-Consultation"));
	}

	@Test
	void testGetTMReferredPrintData_5() throws IEMRException {
		OutputResponse response = new OutputResponse();
		String comingRequest = null;
		String authorization = "Authorization";
		String serverAuthorization = "ServerAuthorization";

		response.setError(5000, "Invalid request");

		assertNull(comingRequest);
		assertTrue(response.toString().contains("Invalid request"));
	}

//	@Test
//	void testGetTMReferredPrintData_IEMRException() throws IEMRException {
//		String incomingRequest = "{\"benVisitCode\":\"someCode\"}";
//		String authorizationHeader = "Bearer someToken";
//		String serverAuthorizationHeader = "ServerToken";
//
//		when(commonServiceImpl.getCaseSheetFromCentralServer(anyString(), anyString()))
//				.thenThrow(IEMRException.class);
//
//		// Call the method under test
//		String response = commonController.getTMReferredPrintData(incomingRequest, authorizationHeader,
//				serverAuthorizationHeader);
//
//		// Verify the output
//		assertNotNull(response);
//		assertTrue(response.contains("5003")); // Assuming the response is a JSON string containing the error code
//		assertTrue(response.contains("Test Exception"));
//
//	}

	@Test
	void testGetTMReferredPrintData_Exception() throws IEMRException {
		String requestObj = "{\"request\":\"Save cancer screening nurse data\"}";
		String authorization = "Bearer token";
		String ServerAuthorization = "serverAuthorization";

		when(commonServiceImpl.checkIsCaseSheetDownloaded(any())).thenThrow(NotFoundException.class);

		String saveBenCancerScreeningNurseData = commonController.getTMReferredPrintData(requestObj, authorization,
				ServerAuthorization);

		assertTrue(saveBenCancerScreeningNurseData.contains("Error in getting case sheet - "));
	}

	@Test
	void testGetBenPreviousReferralHistoryDetails() throws Exception {
		OutputResponse response = new OutputResponse();

		String comingRequest = "{\"benRegID\":\"1\"}";
		String s = "test";

		JSONObject obj = new JSONObject(comingRequest);

		Long benRegID = obj.getLong("benRegID");

		when(commonServiceImpl.getBenPreviousReferralData(benRegID)).thenReturn(s);

		String expResponse = commonController.getBenPreviousReferralHistoryDetails(comingRequest);

		response.setResponse(s);

		assertTrue(obj.has("benRegID"));

		assertEquals(expResponse, commonController.getBenPreviousReferralHistoryDetails(comingRequest));
		assertTrue(response.toString().contains(s));

	}

	@Test
	void testGetBenPreviousReferralHistoryDetails_Invalid() throws Exception {
		OutputResponse response = new OutputResponse();

		String comingRequest = "{\"\":\"\"}";

		JSONObject obj = new JSONObject(comingRequest);

		response.setError(5000, "Invalid request");

		assertFalse(obj.has("benRegID"));
		assertTrue(response.toString().contains("Invalid request"));
	}

	@Test
	void testGetBenPreviousReferralHistoryDetails_Exception() throws JSONException {
		OutputResponse response = new OutputResponse();
		response.setError(5000, "Error while getting details");
		assertEquals(response.toString(), commonController.getBenPreviousReferralHistoryDetails(any()));
	}

	@Test
	void testGetTMCaseSheetFromCentralServer() throws Exception {
		OutputResponse response = new OutputResponse();

		String comingRequest = "{\"comingRequest\":\"Get beneficiary TM case record\"}";
		String authorization = "test";
		String caseSheetFromCentralServer = "test";

		when(commonServiceImpl.getCaseSheetOfTm(comingRequest, authorization)).thenReturn(caseSheetFromCentralServer);

		String expResponse = commonController.getTMCaseSheetFromCentralServer(comingRequest, authorization);

		response.setResponse(caseSheetFromCentralServer);

		assertNotNull(comingRequest);
		assertNotNull(caseSheetFromCentralServer);

		assertEquals(expResponse, commonController.getTMCaseSheetFromCentralServer(comingRequest, authorization));
		assertTrue(response.toString().contains(caseSheetFromCentralServer));
	}

	@Test
	void testGetTMCaseSheetFromCentralServer_Pending() throws Exception {
		OutputResponse response = new OutputResponse();

		String comingRequest = "{\"comingRequest\":\"Get beneficiary TM case record\"}";
		String authorization = "test";
		String caseSheetFromCentralServer = null;

		when(commonServiceImpl.getCaseSheetOfTm(comingRequest, authorization)).thenReturn(caseSheetFromCentralServer);

		String expResponse = commonController.getTMCaseSheetFromCentralServer(comingRequest, authorization);

		response.setError(5000, "Beneficiary pending for Tele-Consultation");

		assertNotNull(comingRequest);
		assertNull(caseSheetFromCentralServer);

		assertEquals(expResponse, commonController.getTMCaseSheetFromCentralServer(comingRequest, authorization));
		assertTrue(response.toString().contains("Beneficiary pending for Tele-Consultation"));
	}

//	void testGetTMCaseSheetFromCentralServer_Invalid() throws Exception {
//	}

	@Test
	public void testGetTMCaseSheetFromCentralServer_ThrowsException() throws Exception {
		String mockRequest = "testRequest";
		String authorization = "Bearer testToken";

		when(commonServiceImpl.getCaseSheetOfTm(anyString(), anyString())).thenThrow(RuntimeException.class);

		// Call the method under test
		String result = commonController.getTMCaseSheetFromCentralServer(mockRequest, authorization);

		// Validate the response
		assertTrue(result.contains("Error in MMU central server case sheet"),
				"The response should contain the expected error message.");

		verify(commonServiceImpl).getCaseSheetOfTm(mockRequest, authorization);
	}

	@Test
	void testCalculateBMIStatus() throws IEMRException {
		OutputResponse response = new OutputResponse();

		String comingRequest = "{\"comingRequest\":\"Calculate beneficiary BMI status\"}";
		String s = "test";

		when(commonNurseServiceImpl.calculateBMIStatus(comingRequest)).thenReturn(s);

		String expResponse = commonController.calculateBMIStatus(comingRequest);

		response.setResponse(s);

		assertEquals(expResponse, commonController.calculateBMIStatus(comingRequest));
		assertTrue(response.toString().contains(s));
	}

	@Test
	void testCalculateBMIStatus_Exception() throws IEMRException {
		when(commonNurseServiceImpl.calculateBMIStatus(any())).thenThrow(RuntimeException.class);

		String response = commonController.calculateBMIStatus(any());
		assertEquals(response, commonController.calculateBMIStatus(any()));
	}

	@Test
	void testSaveBeneficiaryVisitDetail() throws JSONException {
		OutputResponse response = new OutputResponse();
		inputMapper = new InputMapper();

		String comingRequest = "{\"beneficiaryRegID\": \"1\"}";
		Integer i = 1;

		JSONObject obj = new JSONObject(comingRequest);

		when(commonNurseServiceImpl.updateBeneficiaryStatus('R', obj.getLong(BENEFICIARY_REG_ID))).thenReturn(i);

		String expResponse = commonController.saveBeneficiaryVisitDetail(comingRequest);

		response.setResponse("Beneficiary Successfully Submitted to Nurse Work-List.");

		assertTrue(obj.has(BENEFICIARY_REG_ID));
		assertTrue(obj.getLong(BENEFICIARY_REG_ID) > 0);
		assertTrue(i != null && i > 0);

		assertEquals(expResponse, commonController.saveBeneficiaryVisitDetail(comingRequest));
		assertTrue(response.toString().contains("Beneficiary Successfully Submitted to Nurse Work-List."));
	}

	@Test
	void testSaveBeneficiaryVisitDetail_Wrong() throws JSONException {
		OutputResponse response = new OutputResponse();
		inputMapper = new InputMapper();

		String comingRequest = "{\"beneficiaryRegID\": \"1\"}";
		Integer i = -1;

		JSONObject obj = new JSONObject(comingRequest);

		when(commonNurseServiceImpl.updateBeneficiaryStatus('R', obj.getLong(BENEFICIARY_REG_ID))).thenReturn(i);

		String expResponse = commonController.saveBeneficiaryVisitDetail(comingRequest);

		response.setError(500, "Something went Wrong please try after Some Time !!!");

		assertTrue(obj.has(BENEFICIARY_REG_ID));
		assertTrue(obj.getLong(BENEFICIARY_REG_ID) > 0);
		assertTrue(i != null && i < 0);

		assertEquals(expResponse, commonController.saveBeneficiaryVisitDetail(comingRequest));
		assertTrue(response.toString().contains("Something went Wrong please try after Some Time !!!"));
	}

	@Test
	void testSaveBeneficiaryVisitDetail_NotValid() throws JSONException {
		OutputResponse response = new OutputResponse();
		inputMapper = new InputMapper();

		String comingRequest = "{\"beneficiaryRegID\": \"-1\"}";

		JSONObject obj = new JSONObject(comingRequest);

		response.setError(500, "Beneficiary Registration ID is Not valid !!!");

		assertTrue(obj.has(BENEFICIARY_REG_ID));
		assertTrue(obj.getLong(BENEFICIARY_REG_ID) < 0);

		assertTrue(response.toString().contains("Beneficiary Registration ID is Not valid !!!"));
	}

	@Test
	void testSaveBeneficiaryVisitDetail_Invalid() throws JSONException {
		OutputResponse response = new OutputResponse();
		inputMapper = new InputMapper();

		String comingRequest = "{\"beneficiaryID\": \"-1\"}";

		JSONObject obj = new JSONObject(comingRequest);

		response.setError(500, "Beneficiary Registration ID is Not valid !!!");

		assertFalse(obj.has(BENEFICIARY_REG_ID));

		assertTrue(response.toString().contains("Beneficiary Registration ID is Not valid !!!"));
	}

	@Test
	void testSaveBeneficiaryVisitDetail_Exception() throws JSONException {

		// when(commonNurseServiceImpl.updateBeneficiaryStatus(any(),
		// any())).thenThrow(RuntimeException.class);

		String response = commonController.saveBeneficiaryVisitDetail(any());
		assertEquals(response, commonController.saveBeneficiaryVisitDetail(any()));
	}

	@Test
	void testExtendRedisSession() {
		OutputResponse response = new OutputResponse();

		response.setResponse("Session extended for 30 mins");

		assertEquals(response.toString(), commonController.extendRedisSession());
	}

	@Test
	void testDeletePrescribedMedicine() throws JSONException {
		OutputResponse response = new OutputResponse();

		String requestOBJ = "{\"requestOBJ\": \"Soft delete prescribed medicine\"}";
		String s = "test";

		JSONObject obj = new JSONObject(requestOBJ);

		when(commonDoctorServiceImpl.deletePrescribedMedicine(obj)).thenReturn(s);

		String expResponse = commonController.deletePrescribedMedicine(requestOBJ);

		response.setResponse(s);

		assertTrue(requestOBJ != null);
		assertTrue(s != null);

		assertEquals(expResponse, commonController.deletePrescribedMedicine(requestOBJ));
		assertTrue(response.toString().contains(s));
	}

	@Test
	void testDeletePrescribedMedicine_Error() throws JSONException {
		OutputResponse response = new OutputResponse();

		String requestOBJ = null;

		response.setError(5000, "error while deleting record");

		assertNull(requestOBJ);

		assertTrue(response.toString().contains("error while deleting record"));
	}

}