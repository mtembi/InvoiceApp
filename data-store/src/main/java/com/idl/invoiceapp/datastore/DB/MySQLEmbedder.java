package com.idl.invoiceapp.datastore.DB;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.exec.ManagedProcessListener;
import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import com.idl.invoiceapp.datastore.DataStoreApplication;
import org.jboss.logging.Logger;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;

public class MySQLEmbedder {
    private final Logger log = Logger.getLogger(MySQLEmbedder.class);
    private File file = null;
    private final String dataFolder = "/data";
    private final String baseFolder = "/mysql";
    private final String userName = "dbAdmin";
    private final String password = "dbAdmin";
    private final String dbName = "invoices";
    private final int port = 3308;
    private DB db;

    public MySQLEmbedder() {
        getFolderURI();
        init();
    }

    public void init() {
        log.info("Booting dbms");
        try {
            if (!checkDBFolder() || !checkDBMSFolder()) {
                throw new Exception("Error creating system database. Consult administrator");
            }
            startDatabase();
        } catch (Exception e) {
            log.error(e);
        }
    }

    private void getFolderURI() {
        try {
            String path = DataStoreApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            File file = new File(path).getParentFile();
            String decodedPath = URLDecoder.decode(path, "UTF-8");
            System.out.println(decodedPath);
        } catch (UnsupportedEncodingException ex) {
            log.error(ex);
        }
    }

    private boolean checkDBFolder() {
        boolean isExist = true;

        try {
            if (file == null) {
                file = new File(DataStoreApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile();
            }
            File dbFolder = new File(file + dataFolder);
            if (!dbFolder.exists() || !dbFolder.isDirectory()) {
                dbFolder.mkdir();
            }
        } catch (SecurityException e) {
            log.info(e.getMessage(), e);
            isExist = false;
        }
        return isExist;
    }

    private boolean checkDBMSFolder() {
        boolean isExist = true;
        try {
            if (file == null) {
                file = new File(DataStoreApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            }
            File dbFolder = new File(file + baseFolder);
            if (!dbFolder.exists() || !dbFolder.isDirectory()) {
                dbFolder.mkdir();
            }
        } catch (SecurityException | URISyntaxException e) {
            log.error(e.getMessage(), e);
            isExist = false;
        }
        return isExist;
    }

    private void startDatabase() throws Exception {
        DBConfigurationBuilder dcb = DBConfigurationBuilder.newBuilder();

        dcb.setProcessListener(new ManagedProcessListener() {
            @Override
            public void onProcessComplete(int i) {
                System.out.println("Process complete");
            }

            @Override
            public void onProcessFailed(int i, Throwable thrwbl) {
                System.out.println("Process failed");
            }
        });
        dcb.setPort(port);
        dcb.setDataDir(new File(file + dataFolder).getPath());
        dcb.setDeletingTemporaryBaseAndDataDirsOnShutdown(false);

        dcb.setBaseDir(new File(file + baseFolder).getPath());
        db = DB.newEmbeddedDB(dcb.build());

        db.start();
        db.createDB(dbName, userName, password);
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getDbName() {
        return dbName;
    }

    public int getPort() {
        return port;
    }

    public void closeConnection() throws ManagedProcessException {
        db.stop();
    }
}
