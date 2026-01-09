package com.bytes.mangalho;

import static com.bytes.mangalho.utils.ProjectUtils.TAG;

import android.content.Context;
import android.util.Log;

import com.bytes.mangalho.Models.CommanDTO;

import java.util.ArrayList;

public class SysApplication {
    private static Context mContext;
    private static SysApplication INSTANCE = null;

    // other instance variables can be here

    private SysApplication() {
    }

    public static synchronized SysApplication getInstance(Context context) {

        if (INSTANCE == null) {
            mContext = context;
            INSTANCE = new SysApplication();

        }
        return (INSTANCE);
    }
    // other instance methods can follow

    ArrayList<CommanDTO> bodytypelist = new ArrayList<>();
    ArrayList<CommanDTO> complexionlist = new ArrayList<>();
    ArrayList<CommanDTO> challengedlist = new ArrayList<>();
    ArrayList<CommanDTO> dietaryList = new ArrayList<>();
    ArrayList<CommanDTO> drinkingList = new ArrayList<>();
    ArrayList<CommanDTO> fatheroccupationList = new ArrayList<>();
    ArrayList<CommanDTO> motheroccupationList = new ArrayList<>();
    ArrayList<CommanDTO> brotherList = new ArrayList<>();
    ArrayList<CommanDTO> Famailystatuslist = new ArrayList<>();
    ArrayList<CommanDTO> Familytypelist = new ArrayList<>();
    ArrayList<CommanDTO> Familyvalueslist = new ArrayList<>();
    ArrayList<CommanDTO> casteList = new ArrayList<>();
    ArrayList<CommanDTO> manglikList = new ArrayList<>();
    ArrayList<CommanDTO> occupationList = new ArrayList<>();
    ArrayList<CommanDTO> incomeList = new ArrayList<>();
    ArrayList<CommanDTO> bloodList = new ArrayList<>();
    ArrayList<CommanDTO> heightList = new ArrayList<>();
    ArrayList<CommanDTO> stateList = new ArrayList<>();
    ArrayList<CommanDTO> maritalList = new ArrayList<>();
    ArrayList<CommanDTO> lookingForList = new ArrayList<>();
    ArrayList<CommanDTO> lanuageList = new ArrayList<>();
    ArrayList<CommanDTO> hobbiesList = new ArrayList<>();
    ArrayList<CommanDTO> interestsList = new ArrayList<>();

    public ArrayList<CommanDTO> getCasteList() {
        if (casteList.size() > 0) {
            return casteList;

        } else {
            return casteList;

        }

    }
    public ArrayList<CommanDTO> getStateList() {
        if (stateList.size() > 0) {
            return stateList;

        } else {
            return stateList;

        }

    }
    public ArrayList<CommanDTO> getHeightList() {
        if (heightList.size() > 0) {
            return heightList;

        } else {
            return heightList;

        }

    }

    public ArrayList<CommanDTO> getIncomeList() {
        if (incomeList.size() > 0) {
            return incomeList;

        } else {
            return incomeList;
        }
    }

    public ArrayList<CommanDTO> getBloodList() {
        if (bloodList.size() > 0) {
            Log.e(TAG,"bloodList::"+bloodList);
            return bloodList;
        } else {
            return bloodList;
        }
    }

    public ArrayList<CommanDTO> getMaritalList() {

        if (maritalList.size() > 0) {
            return maritalList;

        } else {
            return maritalList;
        }
    } public ArrayList<CommanDTO> getLookingFor() {

        if (lookingForList.size() > 0) {
            return lookingForList;

        } else {
            return lookingForList;
        }
    }

    public ArrayList<CommanDTO> getBodyType() {

        if (bodytypelist.size() > 0) {
            return bodytypelist;

        } else {
            return bodytypelist;
        }
    }

    public ArrayList<CommanDTO> getComplexion() {
        if (complexionlist.size() > 0) {
            return complexionlist;

        } else {
            return complexionlist;
        }
    }

    public ArrayList<CommanDTO> getChallenged() {
        if (challengedlist.size() > 0) {
            return challengedlist;
        } else {
            return challengedlist;
        }
    }


    public ArrayList<CommanDTO> getManglikList() {
        if (manglikList.size() > 0) {
            return manglikList;
        } else {
            return manglikList;
        }
    }

    public ArrayList<CommanDTO> getFamilyStatus() {
        if (Famailystatuslist.size() > 0) {
            return Famailystatuslist;
        } else {
            return Famailystatuslist;
        }
    }

    public ArrayList<CommanDTO> getFamilyType() {
        if (Familytypelist.size() > 0) {
            return Familytypelist;
        } else {

            return Familytypelist;
        }
    }

    public ArrayList<CommanDTO> getFamilyValues() {
        if (Familyvalueslist.size() > 0) {
            return Familyvalueslist;
        } else {
            return Familyvalueslist;
        }
    }

    public ArrayList<CommanDTO> getOccupationList() {
        if (occupationList.size() > 0) {
            return occupationList;
        } else {
            return occupationList;
        }
    }


    public ArrayList<CommanDTO> getMotherOccupationList() {
        if (motheroccupationList.size() > 0) {
            return motheroccupationList;
        } else {
            return motheroccupationList;
        }
    }

    public ArrayList<CommanDTO> getFatherOccupationList() {
        if (fatheroccupationList.size() > 0) {
            return fatheroccupationList;
        } else {
            return fatheroccupationList;
        }
    }

    public ArrayList<CommanDTO> getHobbiesList() {
        if (hobbiesList.size() > 0) {
            return hobbiesList;

        } else {
            return hobbiesList;
        }
    }

    public ArrayList<CommanDTO> getInterestsList() {
        if (interestsList.size() > 0) {
            return interestsList;
        } else {

            return interestsList;
        }
    }

    public ArrayList<CommanDTO> getDietary() {
        if (dietaryList.size() > 0) {
            return dietaryList;
        } else {

            return dietaryList;
        }
    }

    public ArrayList<CommanDTO> getHabitsDrink() {
        if (drinkingList.size() > 0) {
            return drinkingList;
        } else {
            return drinkingList;
        }
    }


    public ArrayList<CommanDTO> getLanguage() {
        if (lanuageList.size() > 0) {
            return lanuageList;
        } else {
            return lanuageList;
        }
    }

    public ArrayList<CommanDTO> getBrother() {
        if (brotherList.size() > 0) {
            return brotherList;
        } else {
            return brotherList;
        }
    }

}