package net.mizchi.simple_di

import scala.reflect.{classTag, ClassTag}
object Injector {
  private val rootInjector =  new Injector(None)
  def createInjector =  rootInjector.createChild(rootInjector)

  def mapValue[T:ClassTag](instance:T, key:String = ""): Unit =
    rootInjector.mapValue[T](instance, key)
  def clear = rootInjector.clear
}

class Injector(val parent:Option[Injector]) {
  private val valueMap =
    scala.collection.mutable.Map[String, scala.collection.mutable.Map[ClassTag[_], Any]]()

  def clear = valueMap.clear
  def createChild:Injector = new Injector(Some(this))
  def createChild(parent:Injector):Injector = new Injector(Some(parent))
  def mapValue[T:ClassTag](instance:T, key:String): Unit =
    valueMap.get(key) match {
      case Some(m) =>
        m += classTag[T] -> instance
      case None =>
        valueMap += key -> scala.collection.mutable.Map[ClassTag[_], Any]()
        mapValue[T](instance, key)
    }

  def getInstance[T:ClassTag]: T = this.getInstance[T]("")
  def getInstance[T:ClassTag](key:String): T =
    valueMap.get(key) match {
      case Some(map) =>
        map.get(classTag[T]) match {
          case Some(instance) => instance.asInstanceOf[T]
          case None => parent match {
            case Some(j:Injector) => j.getInstance[T](key)
            case None => throw new Error("no injected target error(instance map)" + key)
          }
        }
      case None => {
        parent match {
          case Some(k) => k.getInstance[T](key)
          case None =>{
            throw new Error("no injected target error(key map)" + key)
          }
        }
      }
    }
}

trait Inject {
  var injector:Injector = Injector.createInjector
  def injected[T:ClassTag](key:String): T = injector.getInstance[T](key)
  def injected[T:ClassTag]: T = this.injected[T]("")
}

trait CustomInject {
  val injector:Injector
  def injected[T:ClassTag](key:String): T = injector.getInstance[T](key)
  def injected[T:ClassTag]: T = this.injected[T]("")
}
