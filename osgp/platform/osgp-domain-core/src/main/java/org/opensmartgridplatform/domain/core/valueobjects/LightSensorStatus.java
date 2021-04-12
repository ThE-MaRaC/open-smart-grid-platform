/**
 * Copyright 2020 Alliander N.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.domain.core.valueobjects;

import java.io.Serializable;

public class LightSensorStatus implements Serializable {

    private static final long serialVersionUID = -6385082207732463078L;

    private final boolean dark;

    public LightSensorStatus(final boolean dark) {
        this.dark = dark;
    }

    public boolean isDark() {
        return this.dark;
    }
}
