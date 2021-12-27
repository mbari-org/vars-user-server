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
import org.eclipse.persistence.config.TargetDatabase;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Brian Schlining
 * @since 2016-11-16T11:08:00
 */
public class DerbyTestDAOFactory {

    private static Config config = ConfigFactory.load();

    public static EntityManagerFactory newEntityManagerFactory() {
        String driver = config.getString("org.mbari.vars.knowledgebase.database.derby.driver");
        String url = config.getString("org.mbari.vars.knowledgebase.database.derby.url");
        String user = config.getString("org.mbari.vars.knowledgebase.database.derby.user");
        String password = config.getString("org.mbari.vars.knowledgebase.database.derby.password");

        Map<String, String> props = new HashMap<>();
        props.put("eclipselink.connection-pool.default.initial", "2");
        props.put("eclipselink.connection-pool.default.max", "16");
        props.put("eclipselink.connection-pool.default.min", "2");
        props.put("eclipselink.logging.level", "INFO");
        props.put("eclipselink.logging.session", "false");
        props.put("eclipselink.logging.thread", "false");
        props.put("eclipselink.logging.timestamp", "false");
        props.put("eclipselink.target-database", TargetDatabase.Derby);
        props.put("javax.persistence.database-product-name", TargetDatabase.Derby);
        props.put("javax.persistence.schema-generation.database.action", "create");
        props.put("javax.persistence.schema-generation.scripts.action", "drop-and-create");
        props.put("javax.persistence.schema-generation.scripts.create-target", "test-database-create.ddl");
        props.put("javax.persistence.schema-generation.scripts.drop-target", "test-database-drop.ddl");


        props.put("javax.persistence.jdbc.url", url);
        props.put("javax.persistence.jdbc.user", user);
        props.put("javax.persistence.jdbc.password", password);
        props.put("javax.persistence.jdbc.driver", driver);
        return Persistence.createEntityManagerFactory("vars-jpa-knowledgebase", props);
    }
}
