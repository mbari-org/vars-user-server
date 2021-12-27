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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.mbari.kb.core.UserAccount;
import org.mbari.kb.core.UserAccountDAO;

/**
 * Created by IntelliJ IDEA.
 * User: brian
 * Date: Aug 19, 2009
 * Time: 3:09:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserAccountDAOImpl extends DAO implements UserAccountDAO {

    /**
     * Constructs ...
     *
     * @param entityManager
     */
    public UserAccountDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    public Collection<UserAccount> findAll() {
        return findByNamedQuery("UserAccount.findAll", new HashMap<String, Object>());
    }

    public Collection<UserAccount> findAllByFirstName(String firstName) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("firstName", firstName);
        return findByNamedQuery("UserAccount.findByFirstName", params);
    }

    public Collection<UserAccount> findAllByLastName(String lastName) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("lastName", lastName);
        return findByNamedQuery("UserAccount.findByLastName", params);
    }

    public Collection<UserAccount> findAllByRole(String role) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("role", role);
        return findByNamedQuery("UserAccount.findByRole", params);
    }

    /**
     * Search for the matching username
     * @param userName The username to search for
     * @return the match, or null if no match is found
     */
    public UserAccount findByUserName(String userName) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userName", userName);
        List<UserAccount> accounts = findByNamedQuery("UserAccount.findByUserName", params);
        return (accounts.size() == 0) ? null : accounts.get(0);
    }
}
