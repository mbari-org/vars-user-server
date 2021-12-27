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

package org.mbari.kb.core;

/**
 *
 * @author brian
 */
public interface UserAccount extends VARSObject {

    /**
     * Description of the Field
     */
    String PASSWORD_DEFAULT = "guest";

    /**
     * Description of the Field
     */
    String USERNAME_DEFAULT = "default";

    String PROP_FIRST_NAME = "firstName";
    String PROP_LAST_NAME = "lastName";
    String PROP_PASSWORD = "password";
    String PROP_USER_NAME = "userName";
    String PROP_ROLE = "role";
    String PROP_AFFILIATION = "affiliation";

    /**
     * Gets the password for this <code>UserAccount</code>.
     * @return     The password for this <code>UserAccount</code>.
     */
    String getPassword();

    /**
     * Gets the KB Maint <code>Role</code> for this <code>UserAccount</code>.
     * @return     The KB Maint <code>Role</code> for this <code>UserAccount</code>.
     */
    String getRole();

    /**
     * Gets the user name for this <code>UserAccount</code>.
     * @return     The user name for this <code>UserAccount</code>.
     */
    String getUserName();

    String getFirstName();

    String getLastName();

    String getAffiliation();

    String getEmail();

    boolean isAdministrator();


    boolean isMaintainer();


    boolean isReadOnly();

    /**
     * Sets the password for this <code>UserAccount</code>.
     * @param unencryptedPassword    The password for this <code>UserAccount</code>. Implementations
     *      of this method should encrypt the passwrd
     */
    void setPassword(String unencryptedPassword);

    /**
     * Sets the permissible <code>Role</code> for this <code>UserAccount</code>.
     * @param role  The role to set.
     */
    void setRole(String role);

    /**
     * Sets the username for this <code>UserAccount</code>.
     * @param userName  The new userName value
     * @uml.property  name="userName"
     */
    void setUserName(String userName);

    void setFirstName(String firstName);
    void setLastName(String lastName);
    void setAffiliation(String affiliation);

    void setEmail(String email);

    boolean authenticate(String unencryptedPassword);
}
