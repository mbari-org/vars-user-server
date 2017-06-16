package org.mbari.vars.userserver.dao.jpa

import java.util.concurrent.TimeUnit

import org.mbari.vars.userserver.Constants
import org.mbari.vars.userserver.dao.PrefNodeDAO
import org.mbari.vars.userserver.model.PrefNode
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration.{Duration => SDuration}
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * @author Brian Schlining
  * @since 2017-06-16T09:55:00
  */
class PrefNodeDAOImplSpec extends FlatSpec with Matchers with BeforeAndAfterAll {

  private[this] val daoFactory = Constants.DAOFactory
  private[this] val timeout = SDuration(2, TimeUnit.SECONDS)
  private[this] val dao = daoFactory.newPrefNodeDAO()
  def run[R](fn: PrefNodeDAO => R): R = Await.result(dao.runTransaction(fn), timeout)
  private[this] val prefNodes = Seq(PrefNode("/brian/foo", "bar", "baz"),
    PrefNode("/brian", "tab1", "clam"),
    PrefNode("/brian", "tab2", "shrimp"),
    PrefNode("/schlin", "tab1", "Benthic"))
  private[this] val fooNode = PrefNode("/foo", "bar", "baz")


  "PrefNodeDAOImpl" should "create" in {
    prefNodes.foreach(n => run(_.create(n)))
  }

  it should "findByNodeNameAndKey" in {
    prefNodes.foreach(n => {
      val opt = run(_.findByNodeNameAndKey(n.name, n.key))
      opt.isDefined should be (true)
      opt.foreach(m => {
        m.name should be (n.name)
        m.key should be (n.key)
        m.value should be (n.value)
      })
    })
  }

  it should "findByNodeName" in {
    val nodes = run(_.findByNodeName("/brian"))
    nodes.size should be (2)
  }

  it should "findByNodeNameLike" in {
    val nodes = run(_.findByNodeNameLike("/brian"))
    nodes.size should be (3)
  }

  it should "update" in {
    val node0 = prefNodes.last.copy(value = "Pelagic")
    val opt1 = run(_.update(node0))
    opt1 should be (defined)
    opt1.get should be (node0)
    val opt2 = run(_.findByNodeNameAndKey(node0.name, node0.key))
    opt2 should be (defined)
    opt2.get should be (node0)
  }

  it should "insert a non-persisted node on update" in {
    val opt1 = run(_.update(fooNode))
    //opt1.isEmpty should be (true)
    val opt2 = run(_.findByNodeNameAndKey(fooNode.name, fooNode.key))
    opt2 should be (defined)
    opt2.get should be (fooNode)
  }

  it should "delete" in {
    run(_.delete(fooNode))
    val opt1 = run(_.findByNodeNameAndKey(fooNode.name, fooNode.key))
    opt1 should not be defined

    prefNodes.foreach(n => run(_.delete(n)))
    val nodes = run(_.findByNodeNameLike("/brian"))
    nodes.size should be (0)
  }


}
