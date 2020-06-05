package com.idl.invoiceapp.dataclient;

import com.idl.invoiceapp.datastore.controller.SettingController;
import com.idl.invoiceapp.datastore.model.SettingModel;
import reactor.core.publisher.Flux;

public class ClientSettingClient {

    private SettingController settingClient;

    public ClientSettingClient(SettingController settingClient) {
        this.settingClient = settingClient;
    }

    public Flux<SettingModel> createSettingFor(String key, String value) {
        SettingModel setting = new SettingModel();
        setting.setSetKey(key);
        setting.setSetVal(String.valueOf(1));
        settingClient.save(setting);
        return Flux.fromArray(new SettingModel[0]);
    }
}
