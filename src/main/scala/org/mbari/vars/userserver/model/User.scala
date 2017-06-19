package org.mbari.vars.userserver.model

import vars.UserAccount
import vars.jpa.UserAccountImpl

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
