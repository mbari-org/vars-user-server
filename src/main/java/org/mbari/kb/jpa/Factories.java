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

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.mbari.kb.core.*;
import org.mbari.kb.jpa.*;

import javax.persistence.EntityManagerFactory;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * @author Brian Schlining
 * @since 2019-08-21T14:33:00
 */
public class Factories {

    private final EntityManagerFactory entityManagerFactory;
    private final MiscDAOFactory miscDAOFactory;
    private final PersistenceCache persistenceCache;


    public Factories(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        miscDAOFactory = new MiscDAOFactoryImpl(entityManagerFactory);
        persistenceCache = new PersistenceCache(getPersistenceCacheProvider());
    }


    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }


    public MiscDAOFactory getMiscDAOFactory() {
        return miscDAOFactory;
    }


    public MiscFactory getMiscFactory() {
        return new MiscFactoryImpl();
    }


    public VarsUserPreferencesFactory getVarsUserPreferencesFactory() {
        return new VarsUserPreferencesFactoryImpl(entityManagerFactory);
    }

    public PersistenceCache getPersistenceCache() {
        return persistenceCache;
    }

    public PersistenceCacheProvider getPersistenceCacheProvider() {
        return new JPACacheProvider(entityManagerFactory, entityManagerFactory);
    }


    public SimpleDateFormat getDateFormatISO() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'") {{
            setTimeZone(TimeZone.getTimeZone("UTC"));
        }};
    }

    /**
     * Builds the factories for a given environment
     * @param environment Can be 'production' or anything else (although
     *                    'development' is expected.
     * @return
     */
    public static Factories build(String environment) {
        String nodeName = environment.equalsIgnoreCase("production") ?
                "org.mbari.vars.knowledgebase.database.production" :
                "org.mbari.vars.knowledgebase.database.development";

        EntityManagerFactory entityManagerFactory =
                EntityManagerFactories.newEntityManagerFactory(nodeName);
        return new Factories(entityManagerFactory);
    }

    public static Factories build() {
        Config config = ConfigFactory.load();
        String environment = config.getString("database.environment");
        return build(environment);
    }
}
