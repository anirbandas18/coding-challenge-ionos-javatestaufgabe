package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.job.step;

import com.github.javafaker.Address;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.seed.kunde.job.data.KundeModelEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Random;

@Slf4j
public class KundeSeedProcessor implements ItemProcessor<KundeModelEntity, KundeModelEntity> {

    private String emailDomain;
    private List<String> landList;

    private Faker faker;

    @Value("#{'${s3export.seed.land.list}'.split(',')}")
    public void setLandList(List<String> landList) {
        this.landList = landList;
    }

    @Value("${s3export.seed.email.domain:example.com}")
    public void setEmailDomain(String emailDomain) {
        this.emailDomain = emailDomain;
    }

    @Autowired
    public void setFaker(Faker faker) {
        this.faker = faker;
    }

    @Override
    public KundeModelEntity process(KundeModelEntity item) throws Exception {
        Name name = faker.name();
        String email = String.join("@", name.username(), emailDomain);
        Random randomizer = new Random(System.currentTimeMillis());
        String land = landList.get(randomizer.nextInt(landList.size()));
        Address address = faker.address();
        item.setEmail(email);
        item.setFirmenName(faker.company().name());
        item.setVorName(name.firstName());
        item.setNachName(name.lastName());
        item.setLand(land);
        item.setStrasse(address.streetName());
        item.setStrassenZuSatz(address.secondaryAddress());
        item.setOrt(address.cityName());
        item.setPlz(address.zipCode());
        return item;
    }

}
