package com.idl.invoiceapp.datastore;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.MariaDB4jService;
import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;
import com.idl.invoiceapp.datastore.controller.SettingController;
import com.idl.invoiceapp.datastore.model.SettingModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.idl.invoiceapp.datastore.controller",
        "com.idl.invoiceapp.datastore.configs",
        "com.idl.invoiceapp.datastore.model",
        "com.idl.invoiceapp.datastore.repository"
})
@EnableJpaRepositories(basePackages = {"com.idl.invoiceapp.datastore.repository"})
@EntityScan(basePackages = {"com.idl.invoiceapp.datastore.model"})
@Log4j2
public class DataStoreApplication implements ExitCodeGenerator {

    private final MariaDB4jSpringService mariaDB4j;

    final
    SettingController settingController;

    @Autowired
    public DataStoreApplication(MariaDB4jSpringService mariaDB4j, SettingController settingController) throws ManagedProcessException {
        this.mariaDB4j = mariaDB4j;

        this.settingController = settingController;

        test();
    }

    public static void main(String[] args) throws IOException, ManagedProcessException {
        SpringApplication app=new SpringApplication(DataStoreApplication.class);
        ConfigurableApplicationContext ctx=app.run(args);

        MariaDB4jService.waitForKeyPressToCleanlyExit();
        ctx.stop();
        ctx.close();
    }

    private void test(){
        SettingModel sm=new SettingModel();
        sm.setSetKey("TEST");
        sm.setSetVal("TEST");
        settingController.save(sm);

        settingController.findAll().stream().forEach(k->log.info(k));
    }

    @Override
    public int getExitCode() {
        log.error(mariaDB4j.getLastException());
        return mariaDB4j.getLastException() == null ? 0 : -1;
    }

}
