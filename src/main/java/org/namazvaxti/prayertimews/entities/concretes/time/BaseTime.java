package org.namazvaxti.prayertimews.entities.concretes.time;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseTime {

    private int dayOfYear;

    private String imsaq;
    private String sabah;
    private String gunes;
    private String gunorta;
    private String ikindi;
    private String axsam;
    private String yatsi;
}
