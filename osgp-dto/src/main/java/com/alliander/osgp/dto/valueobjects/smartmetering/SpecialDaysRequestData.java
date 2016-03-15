/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.dto.valueobjects.smartmetering;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class SpecialDaysRequestData implements Serializable {

    private static final long serialVersionUID = 2733945253731054997L;

    private final List<SpecialDay> specialDays;

    public SpecialDaysRequestData(final List<SpecialDay> specialDays) {
        super();
        this.specialDays = Collections.unmodifiableList(specialDays);
    }

    public List<SpecialDay> getSpecialDays() {
        return this.specialDays;
    }
}