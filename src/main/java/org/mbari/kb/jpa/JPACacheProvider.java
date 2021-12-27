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

import org.mbari.kb.core.PersistenceCacheProvider;

import javax.persistence.Cache;
import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mbari.kb.core.VARSObject;


/**
 * Provides a method to clear the 2nd level cache used by JPA.
 *
 * @author brian
 */
public class JPACacheProvider implements PersistenceCacheProvider {

    private final EntityManagerFactory kbEmf;
    private final EntityManagerFactory miscEmf;
    private final Logger log = LoggerFactory.getLogger(getClass());



    public JPACacheProvider(EntityManagerFactory kbEmf,
            EntityManagerFactory miscEmf) {

        this.kbEmf = kbEmf;
        this.miscEmf = miscEmf;
    }

    /**
     * Clear the second level cache
     */
    public void clear() {
        
        Cache cache = kbEmf.getCache();
        cache.evictAll();

        cache = miscEmf.getCache();
        cache.evictAll();
        
    }


    public void evict(VARSObject entity) {
        evict(kbEmf.getCache(), entity);
    }

    private void evict(Cache cache, VARSObject entity) {
        try {
            cache.evict(entity.getClass(), entity.getPrimaryKey());
        }
        catch (Exception e) {
            log.info("Failed to evict " + entity + " from cache", e);
        }
    }

}
