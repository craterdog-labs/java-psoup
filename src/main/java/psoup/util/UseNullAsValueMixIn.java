/************************************************************************
 * Copyright (c) Crater Dog Technologies(TM).  All Rights Reserved.     *
 ************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.        *
 *                                                                      *
 * This code is free software; you can redistribute it and/or modify it *
 * under the terms of The MIT License (MIT), as published by the Open   *
 * Source Initiative. (See http://opensource.org/licenses/MIT)          *
 ************************************************************************/
package psoup.util;

import com.fasterxml.jackson.annotation.JsonValue;


/**
 * This interface defines a mixin that can be used to specify to
 * the jackson mappers specific class types whose values should always
 * be mapped to null.
 *
 * @author Derk Norton
 */
public interface UseNullAsValueMixIn {

    @JsonValue
    public default Object toNull() { return null; }

}
