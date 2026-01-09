package com.bytes.mangalho.Models.LoginWithOtp;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data implements Serializable {

	@SerializedName("occupation")
	private String occupation;

	@SerializedName("mother_name")
	private String motherName;

	@SerializedName("language")
	private String language;

	@SerializedName("device_type")
	private String deviceType;

	@SerializedName("user_avtar")
	private String userAvtar;

	@SerializedName("permanent_address")
	private String permanentAddress;

	@SerializedName("about_me")
	private String aboutMe;

	@SerializedName("work_place")
	private String workPlace;

	@SerializedName("family_city")
	private String familyCity;

	@SerializedName("pin")
	private String pin;

	@SerializedName("dietary")
	private String dietary;

	@SerializedName("body_type")
	private String bodyType;

	@SerializedName("shortlisted")
	private int shortlisted;

	@SerializedName("family_pin")
	private String familyPin;

	@SerializedName("state")
	private String state;

	@SerializedName("sister")
	private String sister;

	@SerializedName("height")
	private String height;

	@SerializedName("end_subscription_date")
	private String endSubscriptionDate;

	@SerializedName("family_status")
	private String familyStatus;

	@SerializedName("drinking")
	private String drinking;

	@SerializedName("caste")
	private String caste;

	@SerializedName("weight")
	private String weight;

	@SerializedName("birth_place")
	private String birthPlace;

	@SerializedName("organisation_name")
	private String organisationName;

	@SerializedName("profile_for")
	private String profileFor;

	@SerializedName("current_address")
	private String currentAddress;

	@SerializedName("qualification")
	private String qualification;

	@SerializedName("user_imgs")
	private List<Object> userImgs;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("father_name")
	private String fatherName;

	@SerializedName("dob")
	private String dob;

	@SerializedName("family_value")
	private String familyValue;

	@SerializedName("district")
	private String district;

	@SerializedName("s_no")
	private String sNo;

	@SerializedName("name")
	private String name;

	@SerializedName("blood_group")
	private String bloodGroup;

	@SerializedName("brother")
	private String brother;

	@SerializedName("area_of_interest")
	private String areaOfInterest;

	@SerializedName("status")
	private String status;

	@SerializedName("income")
	private String income;

	@SerializedName("gender")
	private String gender;

	@SerializedName("critical")
	private String critical;

	@SerializedName("city")
	private String city;

	@SerializedName("grand_father_name")
	private String grandFatherName;

	@SerializedName("family_state")
	private String familyState;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("mother_occupation")
	private String motherOccupation;

	@SerializedName("family_type")
	private String familyType;

	@SerializedName("family_income")
	private String familyIncome;

	@SerializedName("maternal_grand_father_name_address")
	private String maternalGrandFatherNameAddress;

	@SerializedName("challenged")
	private String challenged;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("mobile2")
	private String mobile2;

	@SerializedName("smoking")
	private String smoking;

	@SerializedName("working")
	private String working;

	@SerializedName("aadhaar")
	private String aadhaar;

	@SerializedName("email")
	private String email;

	@SerializedName("whatsapp_no")
	private String whatsappNo;

	@SerializedName("father_occupation")
	private String fatherOccupation;

	@SerializedName("gotra")
	private String gotra;

	@SerializedName("gotra_nanihal")
	private String gotraNanihal;

	@SerializedName("family_district")
	private String familyDistrict;

	@SerializedName("complexion")
	private String complexion;

	@SerializedName("mobile")
	private String mobile;

	@SerializedName("manglik")
	private String manglik;

	@SerializedName("marital_status")
	private String maritalStatus;

	@SerializedName("hobbies")
	private String hobbies;

	@SerializedName("family_address")
	private String familyAddress;

	@SerializedName("device_token")
	private String deviceToken;

	@SerializedName("interests")
	private String interests;

	@SerializedName("birth_time")
	private String birthTime;

	public void setOccupation(String occupation){
		this.occupation = occupation;
	}

	public String getOccupation(){
		return occupation;
	}

	public void setMotherName(String motherName){
		this.motherName = motherName;
	}

	public String getMotherName(){
		return motherName;
	}

	public void setLanguage(String language){
		this.language = language;
	}

	public String getLanguage(){
		return language;
	}

	public void setDeviceType(String deviceType){
		this.deviceType = deviceType;
	}

	public String getDeviceType(){
		return deviceType;
	}

	public void setUserAvtar(String userAvtar){
		this.userAvtar = userAvtar;
	}

	public String getUserAvtar(){
		return userAvtar;
	}

	public void setPermanentAddress(String permanentAddress){
		this.permanentAddress = permanentAddress;
	}

	public String getPermanentAddress(){
		return permanentAddress;
	}

	public void setAboutMe(String aboutMe){
		this.aboutMe = aboutMe;
	}

	public String getAboutMe(){
		return aboutMe;
	}

	public void setWorkPlace(String workPlace){
		this.workPlace = workPlace;
	}

	public String getWorkPlace(){
		return workPlace;
	}

	public void setFamilyCity(String familyCity){
		this.familyCity = familyCity;
	}

	public String getFamilyCity(){
		return familyCity;
	}

	public void setPin(String pin){
		this.pin = pin;
	}

	public String getPin(){
		return pin;
	}

	public void setDietary(String dietary){
		this.dietary = dietary;
	}

	public String getDietary(){
		return dietary;
	}

	public void setBodyType(String bodyType){
		this.bodyType = bodyType;
	}

	public String getBodyType(){
		return bodyType;
	}

	public void setShortlisted(int shortlisted){
		this.shortlisted = shortlisted;
	}

	public int getShortlisted(){
		return shortlisted;
	}

	public void setFamilyPin(String familyPin){
		this.familyPin = familyPin;
	}

	public String getFamilyPin(){
		return familyPin;
	}

	public void setState(String state){
		this.state = state;
	}

	public String getState(){
		return state;
	}

	public void setSister(String sister){
		this.sister = sister;
	}

	public String getSister(){
		return sister;
	}

	public void setHeight(String height){
		this.height = height;
	}

	public String getHeight(){
		return height;
	}

	public void setEndSubscriptionDate(String endSubscriptionDate){
		this.endSubscriptionDate = endSubscriptionDate;
	}

	public String getEndSubscriptionDate(){
		return endSubscriptionDate;
	}

	public void setFamilyStatus(String familyStatus){
		this.familyStatus = familyStatus;
	}

	public String getFamilyStatus(){
		return familyStatus;
	}

	public void setDrinking(String drinking){
		this.drinking = drinking;
	}

	public String getDrinking(){
		return drinking;
	}

	public void setCaste(String caste){
		this.caste = caste;
	}

	public String getCaste(){
		return caste;
	}

	public void setWeight(String weight){
		this.weight = weight;
	}

	public String getWeight(){
		return weight;
	}

	public void setBirthPlace(String birthPlace){
		this.birthPlace = birthPlace;
	}

	public String getBirthPlace(){
		return birthPlace;
	}

	public void setOrganisationName(String organisationName){
		this.organisationName = organisationName;
	}

	public String getOrganisationName(){
		return organisationName;
	}

	public void setProfileFor(String profileFor){
		this.profileFor = profileFor;
	}

	public String getProfileFor(){
		return profileFor;
	}

	public void setCurrentAddress(String currentAddress){
		this.currentAddress = currentAddress;
	}

	public String getCurrentAddress(){
		return currentAddress;
	}

	public void setQualification(String qualification){
		this.qualification = qualification;
	}

	public String getQualification(){
		return qualification;
	}

	public void setUserImgs(List<Object> userImgs){
		this.userImgs = userImgs;
	}

	public List<Object> getUserImgs(){
		return userImgs;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setFatherName(String fatherName){
		this.fatherName = fatherName;
	}

	public String getFatherName(){
		return fatherName;
	}

	public void setDob(String dob){
		this.dob = dob;
	}

	public String getDob(){
		return dob;
	}

	public void setFamilyValue(String familyValue){
		this.familyValue = familyValue;
	}

	public String getFamilyValue(){
		return familyValue;
	}

	public void setDistrict(String district){
		this.district = district;
	}

	public String getDistrict(){
		return district;
	}

	public void setSNo(String sNo){
		this.sNo = sNo;
	}

	public String getSNo(){
		return sNo;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setBloodGroup(String bloodGroup){
		this.bloodGroup = bloodGroup;
	}

	public String getBloodGroup(){
		return bloodGroup;
	}

	public void setBrother(String brother){
		this.brother = brother;
	}

	public String getBrother(){
		return brother;
	}

	public void setAreaOfInterest(String areaOfInterest){
		this.areaOfInterest = areaOfInterest;
	}

	public String getAreaOfInterest(){
		return areaOfInterest;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public void setIncome(String income){
		this.income = income;
	}

	public String getIncome(){
		return income;
	}

	public void setGender(String gender){
		this.gender = gender;
	}

	public String getGender(){
		return gender;
	}

	public void setCritical(String critical){
		this.critical = critical;
	}

	public String getCritical(){
		return critical;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		return city;
	}

	public void setGrandFatherName(String grandFatherName){
		this.grandFatherName = grandFatherName;
	}

	public String getGrandFatherName(){
		return grandFatherName;
	}

	public void setFamilyState(String familyState){
		this.familyState = familyState;
	}

	public String getFamilyState(){
		return familyState;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setMotherOccupation(String motherOccupation){
		this.motherOccupation = motherOccupation;
	}

	public String getMotherOccupation(){
		return motherOccupation;
	}

	public void setFamilyType(String familyType){
		this.familyType = familyType;
	}

	public String getFamilyType(){
		return familyType;
	}

	public void setFamilyIncome(String familyIncome){
		this.familyIncome = familyIncome;
	}

	public String getFamilyIncome(){
		return familyIncome;
	}

	public void setMaternalGrandFatherNameAddress(String maternalGrandFatherNameAddress){
		this.maternalGrandFatherNameAddress = maternalGrandFatherNameAddress;
	}

	public String getMaternalGrandFatherNameAddress(){
		return maternalGrandFatherNameAddress;
	}

	public void setChallenged(String challenged){
		this.challenged = challenged;
	}

	public String getChallenged(){
		return challenged;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setMobile2(String mobile2){
		this.mobile2 = mobile2;
	}

	public String getMobile2(){
		return mobile2;
	}

	public void setSmoking(String smoking){
		this.smoking = smoking;
	}

	public String getSmoking(){
		return smoking;
	}

	public void setWorking(String working){
		this.working = working;
	}

	public String getWorking(){
		return working;
	}

	public void setAadhaar(String aadhaar){
		this.aadhaar = aadhaar;
	}

	public String getAadhaar(){
		return aadhaar;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setWhatsappNo(String whatsappNo){
		this.whatsappNo = whatsappNo;
	}

	public String getWhatsappNo(){
		return whatsappNo;
	}

	public void setFatherOccupation(String fatherOccupation){
		this.fatherOccupation = fatherOccupation;
	}

	public String getFatherOccupation(){
		return fatherOccupation;
	}

	public void setGotra(String gotra){
		this.gotra = gotra;
	}

	public String getGotra(){
		return gotra;
	}

	public void setGotraNanihal(String gotraNanihal){
		this.gotraNanihal = gotraNanihal;
	}

	public String getGotraNanihal(){
		return gotraNanihal;
	}

	public void setFamilyDistrict(String familyDistrict){
		this.familyDistrict = familyDistrict;
	}

	public String getFamilyDistrict(){
		return familyDistrict;
	}

	public void setComplexion(String complexion){
		this.complexion = complexion;
	}

	public String getComplexion(){
		return complexion;
	}

	public void setMobile(String mobile){
		this.mobile = mobile;
	}

	public String getMobile(){
		return mobile;
	}

	public void setManglik(String manglik){
		this.manglik = manglik;
	}

	public String getManglik(){
		return manglik;
	}

	public void setMaritalStatus(String maritalStatus){
		this.maritalStatus = maritalStatus;
	}

	public String getMaritalStatus(){
		return maritalStatus;
	}

	public void setHobbies(String hobbies){
		this.hobbies = hobbies;
	}

	public String getHobbies(){
		return hobbies;
	}

	public void setFamilyAddress(String familyAddress){
		this.familyAddress = familyAddress;
	}

	public String getFamilyAddress(){
		return familyAddress;
	}

	public void setDeviceToken(String deviceToken){
		this.deviceToken = deviceToken;
	}

	public String getDeviceToken(){
		return deviceToken;
	}

	public void setInterests(String interests){
		this.interests = interests;
	}

	public String getInterests(){
		return interests;
	}

	public void setBirthTime(String birthTime){
		this.birthTime = birthTime;
	}

	public String getBirthTime(){
		return birthTime;
	}
}