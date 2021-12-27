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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mbari.kb.core.DAO;
import org.mbari.kb.core.MiscDAOFactory;
import org.mbari.kb.core.MiscFactory;
import org.mbari.kb.core.UserAccount;
import org.mbari.kb.core.UserAccountRoles;
import org.mbari.kb.jpa.DerbyTestDAOFactory;
import org.mbari.kb.jpa.Factories;

/**
 * Created by IntelliJ IDEA. User: brian Date: Aug 19, 2009 Time: 3:37:47 PM To
 * change this template use File | Settings | File Templates.
 */
public class UserAccountCrudTest {

    MiscFactory miscFactory;
    MiscDAOFactory daoFactory;

    public final Logger log = LoggerFactory.getLogger(getClass());

    @BeforeClass
    public void setup() {
        Factories factories = new Factories(DerbyTestDAOFactory.newEntityManagerFactory());

        miscFactory = factories.getMiscFactory();
        daoFactory = factories.getMiscDAOFactory();
    }

    @Test
    public void basicCrud() {
        log.info("---------- TEST: basicCrud ----------");
        String testString = "test";
        UserAccount userAccount = miscFactory.newUserAccount();
        userAccount.setPassword(testString);
        log.info("Password '" + testString + "' encrypted as '" + userAccount.getPassword() + "'");
        userAccount.setUserName(testString);
        userAccount.setRole(UserAccountRoles.ADMINISTRATOR.getRoleName());
        DAO dao = daoFactory.newUserAccountDAO();
        dao.startTransaction();
        dao.persist(userAccount);
        dao.endTransaction();
        assertNotNull(((JPAEntity) userAccount).getId());

        dao.startTransaction();
        userAccount = dao.findByPrimaryKey(UserAccountImpl.class, ((JPAEntity) userAccount).getId());
        log.info("Password stored in database as '" + userAccount.getPassword() + "'");
        assertEquals(testString, userAccount.getUserName(), "UserName wasn't stored correctly");
        assertEquals(UserAccountRoles.ADMINISTRATOR.getRoleName(), userAccount.getRole(),
                "Role wasn't stored correctly");

        dao.remove(userAccount);
        dao.endTransaction();

        assertNull("Primary key wasn't reset on delete", ((JPAEntity) userAccount).getId());
    }
}
