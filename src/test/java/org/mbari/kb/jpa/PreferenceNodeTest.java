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

import org.junit.*;
import static org.junit.Assert.*;
import org.mbari.kb.core.VarsUserPreferencesFactory;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mbari.kb.core.MiscDAOFactory;
import org.mbari.kb.core.MiscFactory;
import org.mbari.kb.core.PersistenceCache;
import org.mbari.kb.jpa.DerbyTestDAOFactory;
import org.mbari.kb.jpa.Factories;

/**
 *
 * @author brian
 */
public class PreferenceNodeTest {

    MiscFactory miscFactory;
    MiscDAOFactory daoFactory;
    VarsUserPreferencesFactory prefsFactory;
    PersistenceCache cache;

    public final Logger log = LoggerFactory.getLogger(getClass());

    @BeforeClass
    public void setup() {
        Factories factories = new Factories(DerbyTestDAOFactory.newEntityManagerFactory());
        miscFactory = factories.getMiscFactory();
        daoFactory = factories.getMiscDAOFactory();
        prefsFactory = factories.getVarsUserPreferencesFactory();
        cache = factories.getPersistenceCache();
    }

    @Test
    public void test01() {

        int testOrder = 0;
        String testName = "test-button";

        // Create nodes
        Preferences root = prefsFactory.userRoot("test");
        log.info("Absolutepath is " + root.absolutePath());
        Preferences test01 = root.node("test01");
        Preferences buttonNode = test01.node("abutton");
        buttonNode.putInt("buttonOrder", testOrder);
        buttonNode.put("buttonName", testName);
        Preferences buttonNode2 = test01.node("bbutton");
        buttonNode2.putInt("buttonOrder", testOrder + 1);
        buttonNode2.put("buttonName", testName + "-not");

        // Clear cache
        cache.clear();

        // Read nodes
        root = prefsFactory.userRoot("test");
        test01 = root.node("test01");
        buttonNode = test01.node("abutton");
        int buttonOrder = buttonNode.getInt("buttonOrder", 1000);
        String buttonName = buttonNode.get("buttonName", "boom");
        try {
            // Clean up
            root.removeNode();
        } catch (BackingStoreException ex) {
            fail(ex.getMessage());
        }

        assertEquals(testName, buttonName);
        assertTrue(testOrder == buttonOrder);

    }

}
