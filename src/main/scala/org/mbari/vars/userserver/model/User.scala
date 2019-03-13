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

package org.mbari.vars.userserver.model

import vars.UserAccount

/**
 * @author Brian Schlining
 * @since 2017-06-05T10:41:00
 */
case class User(
    username: String,
    password: String,
    role: String = "ReadOnly",
    affiliation: Option[String] = None,
    firstName: Option[String] = None,
    lastName: Option[String] = None,
    email: Option[String] = None,
    id: Option[Long] = None,
    isEncrypted: Boolean = true
) {

}

object User {
  def apply(userAccount: UserAccount): User = User(
    userAccount.getUserName,
    userAccount.getPassword,
    userAccount.getRole,
    Option(userAccount.getAffiliation),
    Option(userAccount.getFirstName),
    Option(userAccount.getLastName),
    Option(userAccount.getEmail),
    Option(userAccount.getPrimaryKey).map(_.asInstanceOf[Long])
  )

}
