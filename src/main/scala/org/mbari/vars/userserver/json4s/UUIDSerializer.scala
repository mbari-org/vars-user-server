package org.mbari.vars.userserver.json4s

import java.util.UUID

import org.json4s._

/**
  * @author Brian Schlining
  * @since 2017-03-01T14:29:00
  */
case object UUIDSerializer
  extends CustomSerializer[UUID](
    format =>
      (
        {
          case JString(s) => UUID.fromString(s)
          case JNull => null
          case JNothing => null
        }, {
        case x: UUID => JString(x.toString)
      }))

