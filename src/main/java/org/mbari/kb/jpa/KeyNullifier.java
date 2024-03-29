/*
 * Copyright 2017 Monterey Bay Aquarium Research Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mbari.kb.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PostRemove;
import java.lang.reflect.Method;

/**
 * An EntityListener that sets the primary key of an Entity object to null after it's been
 * deleted. This is handy since a null key indicates the object is not a persisted object.
 */
public class KeyNullifier {

    public static final Logger log = LoggerFactory.getLogger(KeyNullifier.class);

    @PostRemove
    public void nullifyKey(Object object) {

        /*
         * We use reflection to locate the setId method since it's not defined in
         * any of the base interfaces of the VARS JPAEntities. This is deliberate
         * we don't normally want developers playing with the setId method.
         */
        if (object instanceof JPAEntity) {
            Class params[] = {Long.class};
            Object args[] = {null};
            try {
                Method method = object.getClass().getDeclaredMethod("setId", params);
                method.invoke(object, args);
            }
            catch (Exception e) {
                log.error("Failed to set primary key on " + object + " to null", e);
            }
        }
    }
}
