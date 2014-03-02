import org.specs2.mutable._
import net.mizchi.simple_di._

class InjectorSpec extends Specification {
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