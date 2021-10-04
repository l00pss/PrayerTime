package org.namazvaxti.prayertimews.dataAccess.concretes;

import org.namazvaxti.prayertimews.core.utilities.exceptions.DataNotFoundException;
import org.namazvaxti.prayertimews.core.utilities.exceptions.NullValueException;
import org.namazvaxti.prayertimews.core.utilities.messages.error.ErrorMessages;
import org.namazvaxti.prayertimews.dataAccess.abstracts.CheckerCurrentData;
import org.namazvaxti.prayertimews.dataAccess.abstracts.ReadDataFromJson;
import org.namazvaxti.prayertimews.dataAccess.abstracts.TimeRepository;
import org.namazvaxti.prayertimews.dataAccess.helper.DayHelper;
import org.namazvaxti.prayertimews.entities.concretes.time.BaseTime;
import org.namazvaxti.prayertimews.entities.concretes.time.City;
import org.namazvaxti.prayertimews.entities.concretes.time.ExtraTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Date;
import java.util.List;

@Repository
public class TimeRepositoryManager implements TimeRepository {

    public static String DATA_PATH = "src/main/java/org/namazvaxti/prayertimews/dataAccess/data/json/";

    private CheckerCurrentData checkerCurrentData;
    private ReadDataFromJson readDataFromJson;
    private City city;
    private BaseTime baseTime;
    private ExtraTime extraTime;

    @Autowired
    public TimeRepositoryManager(CheckerCurrentData checkerCurrentData, ReadDataFromJson readDataFromJson,
                                 City city, BaseTime baseTime, ExtraTime extraTime) {
        this.checkerCurrentData = checkerCurrentData;
        this.readDataFromJson = readDataFromJson;
        this.city = city;
        this.baseTime = baseTime;
        this.extraTime = extraTime;
    }

    private JsonStructure getAllData(Integer indexOfCity) throws DataNotFoundException, NullValueException {
        if (indexOfCity == null)throw new NullValueException();
        if(checkerCurrentData.hasDataInLocalRepository(indexOfCity)
                && checkerCurrentData.isCurrentDataInLocalRepository(indexOfCity))
            return readDataFromJson.readAllDataFromLocalRepository(indexOfCity);
        else {
            throw new DataNotFoundException(ErrorMessages.DATA_NOT_FOUND.getValue());
        }
    }

    @Override
    public JsonStructure getAllDataAsJson(Integer indexOfCity) throws DataNotFoundException, NullValueException {
        return getAllData(indexOfCity);
    }

    @Override
    public JsonStructure getListOfCities() {
        try(
                Reader reader = new FileReader("src/main/java/org/namazvaxti/prayertimews/client/helper/ListOfCity.json");
                JsonReader jsonReader = Json.createReader(reader)
        ) {
            return jsonReader.read().asJsonArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public City getDatesOfDay(Integer idexOfCity) throws DataNotFoundException, NullValueException {
        return getCityBean(idexOfCity,DayHelper.getDayOfYear());
    }

    @Override
    public City getDatesOfDay(Integer idexOfCity, Date date) {
        return null;
    }

    @Override
    public List<City> getWeeklyDates(Integer idexOfCity) {
        return null;
    }

    @Override
    public List<City> getMonthlyDates(Integer idexOfCity) {
        return null;
    }


    private City getCityBean(int indexOfCity, int datOfYear) throws DataNotFoundException, NullValueException {
        var jsonStructure = getAllData(indexOfCity).asJsonObject().get("@attributes").asJsonObject();
        var times = getAllData(indexOfCity).asJsonObject().get("vakit").asJsonArray().get(datOfYear).asJsonObject();
        city.setDayOfYear(jsonStructure.getString("ID"));
        city.setCityOfName(jsonStructure.getString("cityNameEN"));
        city.setMagdeg(jsonStructure.getString("magdeg"));
        city.setQiblaAngle(jsonStructure.getString("qiblaangle"));
        city.setBaseTime(getBaseTime(times));
        city.setExtraTime(getExtraTime(times));
        return city;
    }

    private BaseTime getBaseTime(JsonObject times){
        baseTime.setDayOfYear(times.get("@attributes").asJsonObject().getString("gun"));
        baseTime.setTodayDate(times.get("@attributes").asJsonObject().getString("tarih"));
        baseTime.setTodayHijrahDate(times.get("@attributes").asJsonObject().getString("hicri"));

        baseTime.setImsaq(times.getString("imsak"));
        baseTime.setSabah(times.getString("sabah"));
        baseTime.setGunes(times.getString("gunes"));
        baseTime.setGunorta(times.getString("ogle"));
        baseTime.setIkindi(times.getString("ikindi"));
        baseTime.setAxsam(times.getString("aksam"));
        baseTime.setYatsi(times.getString("yatsi"));
        baseTime.setQiblaClock(times.getString("kible"));

        return baseTime;
    }

    private ExtraTime getExtraTime(JsonObject times){
        extraTime.setIsrak(times.getString("israk"));
        extraTime.setDahve(times.getString("dahve"));
        extraTime.setKerahat(times.getString("kerahet"));
        extraTime.setAsri_sani(times.getString("asrisani"));
        extraTime.setIsfirar(times.getString("isfirar"));
        extraTime.setIstibak(times.getString("istibak"));
        extraTime.setIsa_sani(times.getString("isaisani"));
        extraTime.setMidnight(times.getString("geceyarisi"));
        extraTime.setTeheccud(times.getString("teheccud"));
        extraTime.setSeher(times.getString("seher"));

        return extraTime;
    }




}
