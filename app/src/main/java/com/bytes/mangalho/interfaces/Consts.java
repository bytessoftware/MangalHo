package com.bytes.mangalho.interfaces;

public interface Consts {
    String APP_NAME = "Mangal Ho";
    String BASE_URL = "https://www.mangalho.co.in/api/";
    //String BASE_URL = "https://phpstack-390430-1543668.cloudwaysapps.com/admin/api/";
    String TERMS_URL = BASE_URL+"termsConditionPage/en";
    String PRIVACY_URL = BASE_URL+"privacyPolicyPage/en";
    String FAQ_URL = BASE_URL+"faqPage/en";

    /*APIs*/

    String REGISTRATION = "signup";
    String LOGIN_API = "login";
    String FORGET_PASSWORD_API = "forgotPassword";
    String MY_PROFILE_API = "myProfile";
    String ALL_PROFILES_API = "matchesUser";
    String JUST_JOIN_API = "justJoin";
    String GET_EVENTS_API = "getNews";
    String GET_GALLARY_API = "getGallary";
    String SET_PROFILE_IMAGE_API = "setProfileImage";
    String SHORTLISTED_API = "getShortlist";
    String GET_VISITORS_API = "getVisitors";
    String SET_VISITOR_API = "addVisitors";
    String IMAGES = "imageUpdate";
    String GET_INTEREST_API = "getInterest";
    String SET_SHORTLISTED_API = "shortlist";
    String SEND_INTEREST_API = "sendInterest";
    String GET_MY_SUBSCRIPTION_API = "subscriptionCheck";
    String SEARCH_API = "searchUsers";
    String GET_CHAT_HISTORY_API = "getMessageHistory";
    String GET_CHAT_API = "getMessage";
    String GET_ALL_SETTING_VALUE_API = "getAllSettingValue";
    String GET_DISTRICTS_API = "getDistricts";
    String SET_MESSAGE = "setMessage";
    String DELETE_IMAGE_API = "deleteImage";
    String CHANGE_PASSWORD_API = "changePassword";
    String GET_ALL_PACKAGES_API = "getPackages";
    String SUBSCRIPTION_API = "userSubscribe";
    String GET_MY_SUBSCRIPTION_HISTORY_API = "subscribeHistory";
    String UPDATE_PROFILE_API = "userUpdate";
    String GET_CATEGORY_API = "getCategory";
    String GET_SERVICE_BY_CATEGORY_API = "getServiceByCategory";
    String DEACTIVATE_ACCOUNT_API = "deleteAccount";
    /*DTOs*/

    String DTO = "dto";
    String LOGIN_DTO = "login_dto";
    String SUBSCRIPTION_DTO = "subscription_dto";
    String SEARCH_PARAM = "search_param";
    String LANGUAGAE_CODE = "languagae_code";

    /*Project*/
    String IS_REGISTERED = "is_registered";
    String CAMERA_ACCEPTED = "camera_accepted";
    String MODIFY_AUDIO_ACCEPTED = "modify_audio_accepted";
    String CALL_PRIVILAGE = "call_privilage";
    String FINE_LOC = "fine_loc";
    String CORAS_LOC = "coras_loc";
    String CALL_PHONE = "call_phone";
    String STORAGE_ACCEPTED = "storage_accepted";
    String IMAGE_LIST = "image_list";
    String WEB_VIEW_FLAG = "web_view_flag";
    String LANGUAGE_SELECTION = "language_selection";
    String SELECTED_LANGUAGE = "selected_language";
    String TAG_PROFILE = "tag_profile";
    String LANGUAGE_PREF = "language_pref";
    String IS_SUBSCRIBE = "is_subscribe";


    /*registration params*/
    String NAME = "name";
    String EMAIL = "email";
    String GENDER = "gender";
    String PROFILE_FOR = "profile_for";
    String MARITAL_STATUS = "marital_status";
    String AADHAAR = "aadhaar";
    String PASSWORD = "password";
    String BLOOD_GROUP = "blood_group";
    String DOB = "dob";
    String BIRTH_PLACE = "birth_place";
    String BIRTH_TIME = "birth_time";
    String HEIGHT = "height";
    String CASTE = "caste";
    String MANGLIK = "manglik";
    String QUALIFICATION = "qualification";
    String OCCUPATION = "occupation";
    String INCOME = "income";
    String WORK_PLACE = "work_place";
    String STATE = "state";
    String DISTRICT = "district";
    String CITY = "city";
    String GOTRA = "gotra";
    String GOTRA_NANIHAL = "gotra_nanihal";
    String ORGANIZATION = "organisation_name";
    String WORKING = "working";
    String FIREBASE_TOKEN = "firebase_token";
    String DEVICE_TYPE = "device_type";
    String DEVICE_TOKEN = "device_token";


    /*Life Style*/
    String DIETARY = "dietary";
    String DRINKING = "drinking";
    String SMOKING = "smoking";
    String LANGUAGE = "language";
    String HOBBIES = "hobbies";
    String INTERESTS = "interests";

    /*About Family*/
    String MOTHER_OCCUPATION = "mother_occupation";
    String FATHER_OCCUPATION = "father_occupation";
    String FAMILY_INCOME = "family_income";
    String FAMILY_STATUS = "family_status";
    String FAMILY_TYPE = "family_type";
    String FAMILY_VALUE = "family_value";
    String FAMILY_STATE = "family_state";
    String FAMILY_DISTRICT = "family_district";
    String FAMILY_CITY = "family_city";
    String FAMILY_PIN = "family_pin";
    String BROTHER = "brother";
    String SISTER = "sister";
    String GRAND_FATHER_NAME = "grand_father_name";
    String MATERNAL_GRAND_FATHER_NAME_ADDRESS = "maternal_grand_father_name_address";
    String FATHER_NAME = "father_name";
    String MOTHER_NAME = "mother_name";
    String PERMANENT_ADDRESS = "permanent_address";
    String WHATSAPP_NO = "whatsapp_no";
    String MOBILE2 = "mobile2";

    /*About Me*/
    String ABOUT_ME = "about_me";
    String WEIGHT = "weight";
    String BODY_TYPE = "body_type";
    String COMPLEXION = "complexion";
    String CHALLENGED = "challenged";

    /*Get All Profile*/
    String USER_ID = "user_id";
    String FROM_USER_ID = "from_user_id";
    String TO_USER_ID = "to_user_id";
    String CHAT_LIST_DTO = "chat_list_dto";
    String FLAG = "flag";

    /*Critical*/

    String CRITICAL = "critical";

    /*shortListed*/

    String SHORT_LISTED_ID = "short_listed_id";

    /*setVisitor*/
    String VISITOR_ID = "visitor_id";
    /*setProfilePic*/
    String MEDIA_ID = "media_id";


    /*send interest*/
    String REQUESTED_ID = "requested_id";

    /*change password*/
    String NEW_PASSWORD = "new_password";

    /*get interest*/
    String TYPE = "type";
    String SENT = "0";
    String RECIEVED = "1";

    /*Buy Package*/
    String PACKAGES_ID = "packages_id";
    String TXN_ID = "txn_id";
    String ORDER_ID = "order_id";
    String PRICE = "price";


    String MESSAGE = "message";
    String BROADCAST = "broadcast";
    String IMAGE_URI_CAMERA = "image_uri_camera";
    String IMAGE = "image";
    String MEDIA = "media";
    String USER_AVTAR = "user_avtar";

    /*Notification*/
    String CHAT_NOTIFICATION = "7003";//both
    String USER_SUBSCRIPTION = "70005";//both
    String SEND_INTEREST = "7005";//both
    String UPDATE_INTEREST = "70002";//both
    String CANCEL_INTEREST = "70003";//both

    String FILES = "files[]";
    String SCREEN_TAG = "screen_tag";
    String CATEGORY_ID = "category_id";
    String STATE_ID = "state_id";
    String REASONS = "reasons";


    String CALL_TYPE = "call_type";
    String RECEIVER_NAME = "receiver_name";
    String RECEIVER_IMAGE = "receiver_image";
    String CALL_TYPE_VIDEO = "3";
    String CALL_TYPE_AUDIO = "4";



}
