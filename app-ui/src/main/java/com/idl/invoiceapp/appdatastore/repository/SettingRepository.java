package com.idl.invoiceapp.appdatastore.repository;

import com.idl.invoiceapp.appdatastore.models.Settings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingRepository extends JpaRepository<Settings, Long> {
}
