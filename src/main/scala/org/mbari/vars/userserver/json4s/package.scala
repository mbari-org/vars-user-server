package org.mbari.vars.userserver

import org.json4s.{DefaultFormats, Formats}

/**
  * @author Brian Schlining
  * @since 2017-06-05T10:07:00
  */
package object json4s {

  val CustomFormats: Formats = DefaultFormats + UUIDSerializer
}
