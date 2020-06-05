package com.idl.invoiceapp.appdatastore.controller;

import com.idl.invoiceapp.appdatastore.models.Settings;
import com.idl.invoiceapp.appdatastore.repository.SettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Controller
@Transactional
public class SettingController  {//implements ControllerInterface<SettingModel, Long>

    private SettingRepository settingRepository;

    @Autowired
    public SettingController(SettingRepository repo) {
        this.settingRepository = repo;
    }


    public List<Settings> findByExample(Example<Settings> example) {
        return settingRepository.findAll(example);
    }

    @Transactional(propagation=Propagation.REQUIRED)
    public List<Settings> findAll() {
        return settingRepository.findAll();
    }

    public Settings findById(Long aLong) {
        return settingRepository.findById(aLong).get();
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public Settings save(Settings setting) {
        return settingRepository.saveAndFlush(setting);
    }

    public void delete(Settings setting) {
        settingRepository.delete(setting);
    }
}
