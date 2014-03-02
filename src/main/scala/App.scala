import net.mizchi.simple_di._

object App {
  def main(args: Array[String]) {
    class A(val x:Int)
    class B

    // Injector
    class C extends Inject {
      val a: A = injected[A]
      val b: B = injected[B]
      def run: Unit = println(a.x, b)
    }

    Injector.mapValue(new A(4))
    Injector.mapValue(new B)
    val c = new C
    c.run
  }
}
