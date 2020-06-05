package com.idl.invoiceapp.datastore.controller;

import com.idl.invoiceapp.datastore.model.SettingModel;
import com.idl.invoiceapp.datastore.repository.SettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
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


    public List<SettingModel> findByExample(Example<SettingModel> example) {
        return settingRepository.findAll(example);
    }

    @Transactional(propagation=Propagation.REQUIRED)
    public List<SettingModel> findAll() {
        return settingRepository.findAll();
    }

    public SettingModel findById(Long aLong) {
        return settingRepository.findById(aLong).get();
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public SettingModel save(SettingModel setting) {
        return settingRepository.saveAndFlush(setting);
    }

    public void delete(SettingModel setting) {
        settingRepository.delete(setting);
    }
}
