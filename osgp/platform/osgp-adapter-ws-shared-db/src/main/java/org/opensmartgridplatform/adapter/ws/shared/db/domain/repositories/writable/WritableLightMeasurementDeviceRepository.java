package org.opensmartgridplatform.adapter.ws.shared.db.domain.repositories.writable;

import org.opensmartgridplatform.domain.core.entities.LightMeasurementDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WritableLightMeasurementDeviceRepository extends JpaRepository<LightMeasurementDevice, Long> {
    LightMeasurementDevice findByDeviceIdentification(String deviceIdentification);
}
