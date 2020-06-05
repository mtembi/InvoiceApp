package com.idl.invoiceapp.datastore.repository;

import com.idl.invoiceapp.datastore.model.SettingModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingRepository extends JpaRepository<SettingModel, Long> {
}
