package com.iemr.mmu.controller.anc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.iemr.mmu.service.anc.ANCServiceImpl;
import com.iemr.mmu.utils.response.OutputResponse;

import io.swagger.annotations.ApiOperation;

/**
 * 
 * @author NA874500
 * @Objective Saving ANC data for Nurse.
 * @Date 19-01-2018
 *
 */
@CrossOrigin
@RestController
@RequestMapping(value =  "/ANC", headers = "Authorization")
public class ANCCreateController {
	private Logger logger = LoggerFactory.getLogger(ANCCreateController.class);

	private ANCServiceImpl ancServiceImpl;

	@Autowired
	public void setAncServiceImpl(ANCServiceImpl ancServiceImpl) {
		this.ancServiceImpl = ancServiceImpl;
	}

	/**
	 * 
	 * @return
	 */

	@CrossOrigin
	@ApiOperation(value = "Save ANC nurse data..", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = { "/save/nurseData" }, method = { RequestMethod.POST })
	public String saveBenANCNurseData(@RequestBody String requestObj) {
		OutputResponse response = new OutputResponse();
		try {
			logger.info("Request object for ANC nurse data saving :" + requestObj);

			JsonObject jsnOBJ = new JsonObject();
			JsonParser jsnParser = new JsonParser();
			JsonElement jsnElmnt = jsnParser.parse(requestObj);
			jsnOBJ = jsnElmnt.getAsJsonObject();

			if (jsnOBJ != null) {
				Long ancRes = ancServiceImpl.saveANCNurseData(jsnOBJ);
				if (null != ancRes && ancRes > 0) {
					response.setResponse("ANC Nurse Entered Details stored successfully.");
				} else {
					response.setResponse("Failed to store ANC Details.");
				}

			} else {
				response.setError(5000, "Invalid Request !!!");
			}

		} catch (Exception e) {
			logger.error("Exception occurs in ANC nurse data saving :" + e);
			response.setError(e);
		}
		return response.toString();
	}

	@CrossOrigin
	@ApiOperation(value = "Save ANC doctor data..", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = { "/save/doctorData" }, method = { RequestMethod.POST })
	public String saveBenANCDoctorData(@RequestBody String requestObj) {
		OutputResponse response = new OutputResponse();
		try {
			logger.info("Request object for ANC doctor data saving :" + requestObj);

			JsonObject jsnOBJ = new JsonObject();
			JsonParser jsnParser = new JsonParser();
			JsonElement jsnElmnt = jsnParser.parse(requestObj);
			jsnOBJ = jsnElmnt.getAsJsonObject();
			if (jsnOBJ != null) {
				Long r = ancServiceImpl.saveANCDoctorData(jsnOBJ);
				if (r != null && r > 0) {

				} else {
					// soething went wrong
				}
			} else {
				// data is null}
			}

		} catch (Exception e) {
			logger.error("Exception occurs in ANC nurse data saving :" + e);
			response.setError(e);
		}
		return response.toString();
	}

}
