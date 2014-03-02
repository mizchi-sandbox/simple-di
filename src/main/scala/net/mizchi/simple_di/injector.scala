package net.mizchi.simple_di

import scala.reflect.{classTag, ClassTag}
object Injector {
  private val rootInjector =  new Injector(None)
  def createInjector =  rootInjector.createChild(rootInjector)
  def mapValue[T:ClassTag](instance:T): Unit =
    rootInjector.mapValue[T](instance)
  def clear = rootInjector.clear
}

class Injector(val parent:Option[Injector]) {
  private val valueMap =
    scala.collection.mutable.Map[ClassTag[_], Any]()

  def clear = valueMap.clear
  def createChild:Injector = new Injector(Some(this))
  def createChild(parent:Injector):Injector = new Injector(Some(parent))

  private def hasInjectedValue(cTag:ClassTag[_]):Boolean = {
    valueMap.get(cTag) match {
      case Some(_) => true
      case None => false
    }
  }

  def mapValue[T:ClassTag](instance:T): Unit = {
    valueMap += classTag[T] -> instance
  }

  def getInstance[T:ClassTag]: T =
    valueMap.get(classTag[T]) match {
      case Some(i) => i.asInstanceOf[T]
      case None => {
        parent match {
          case Some(j) => j.getInstance[T]
          case None => throw new Error("no injected target error")
        }
      }
    }
}

trait Inject {
  var injector:Injector = Injector.createInjector
  def injected[T:ClassTag]: T = injector.getInstance[T]
}

trait CustomInject {
  val injector:Injector
  def injected[T:ClassTag]: T = injector.getInstance[T]
}
