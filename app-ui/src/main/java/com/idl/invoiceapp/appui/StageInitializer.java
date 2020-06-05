package com.idl.invoiceapp.appui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Log4j2
public class StageInitializer implements ApplicationListener<InvoiceApplication.StageReadyEvent> {

    @Value("classpath:/AppMainFXML.fxml")
    private Resource appMainResource;
    @Value(("classpath:/styles/appmainfxml.css"))
    private Resource appCss;

    @Override
    public void onApplicationEvent(InvoiceApplication.StageReadyEvent event) {
        try {
            log.info(appMainResource.getURL());
            FXMLLoader fxmlLoader = new FXMLLoader(appMainResource.getURL());
            Parent parent=fxmlLoader.load();

            Stage stage = event.getStage();
            log.info("On Application event");
            Scene scene = new Scene(parent, 800, 600);
            log.info(StageInitializer.class.getResource("/").getPath());
            scene.getStylesheets().add(StageInitializer.class.getResource("/styles/appmainfxml.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
