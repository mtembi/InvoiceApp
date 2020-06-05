package com.idl.invoiceapp.appui;

import com.idl.invoiceapp.datastore.DataStoreApplication;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.idl.invoiceapp.datastore.controller",
        "com.idl.invoiceapp.datastore.configs",
        "com.idl.invoiceapp.datastore.model",
        "com.idl.invoiceapp.datastore.repository"
})
@Import(DataStoreApplication.class)
public class JavafxUiApplication {


    public static void main(String[] args) {
        Application.launch(InvoiceApplication.class, args);
    }

}
