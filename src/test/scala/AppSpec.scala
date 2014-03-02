import org.specs2.mutable._
import net.mizchi.simple_di._

class InjectorSpec extends Specification {
  Injector.clear

  class A(val x:Int)
  class B
  class C extends Inject {
    val a: A = injected[A]
    val b: B = injected[B]
  }

  Injector.mapValue(new A(4))
  Injector.mapValue(new B)
  val c = new C

  override def is = s2"""
  C must have injected instance with its property ${ c.a.x mustEqual 4 }
  """
}

class Injector2Spec extends Specification {
  Injector.clear
  class A(val x:Int)

  class I extends Inject {
    val a: A = injected[A]("foo")
  }
  Injector.mapValue(new A(3))
  Injector.mapValue(new A(4), "foo")
  val i = new I

  //i must have injected instance by keyword ${ i.a.x mustEqual 4 }
  override def is = s2"""
  I must have injected instance with its property ${ i.a.x mustEqual 4 }
  """
}
