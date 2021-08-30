package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.download.bucket.data;

import com.teenthofabud.core.common.data.form.LOVForm;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class BucketForm {

    private String prefix;
    private String name;
    private String country;
    private Date date;

}
