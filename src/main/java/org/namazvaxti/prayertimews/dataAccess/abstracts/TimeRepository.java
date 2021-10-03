package org.namazvaxti.prayertimews.dataAccess.abstracts;

import org.namazvaxti.prayertimews.core.utilities.exceptions.DataNotFoundException;
import org.namazvaxti.prayertimews.entities.concretes.time.CityBean;

import javax.json.JsonStructure;
import java.util.Date;
import java.util.List;

public interface TimeRepository {
    JsonStructure getAllDataAsJson(int indexOfCity) throws DataNotFoundException;
    CityBean getToDayDates(int idexOfCity) throws DataNotFoundException;
    CityBean getChangeDayDates(int idexOfCity, Date date);
    List<CityBean> getWeeklyDates(int idexOfCity);
    List<CityBean> getMonthlyDates(int idexOfCity);
}
