package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.model.service;

import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.model.data.KundeModelException;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.model.data.KundeModelForm;
import com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.kunde.model.data.KundeModelVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface KundeModelService {

    public Set<KundeModelVo> retrieveAllByNaturalOrdering();

    public KundeModelVo retrieveDetailsById(Long id) throws KundeModelException;

    public List<KundeModelVo> retrieveAllMatchingDetailsByEmail(String email) throws KundeModelException;

    public Long createKundeModel(KundeModelForm form) throws KundeModelException;

}
