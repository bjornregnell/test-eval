package example


case class ServerConfig(port: Int)

trait Constr 
case class XGTY(x: Var, y: Var) extends Constr
case class XLTY(x: Var, y: Var) extends Constr


case class Var(id: String):
  def >(other: Var): XGTY = XGTY(this, other)
  def <(other: Var): XLTY = XLTY(this, other)
  
def testEvalTyped(): Unit = 
  import com.eed3si9n.eval.Eval
  val x = Eval[ServerConfig]("example.ServerConfig(port = 8080)")
  println(s"  x.port == ${x.port}")

def testEvalInfer(): Unit = 
  import com.eed3si9n.eval.Eval
  val eval = Eval()
  val code = """
    import example.*
    val c1 = Var("x") > Var("y")
    val c2 = Var("x") < Var("y")
    Seq(c1, c2) -> c1
  """
  val result = eval.evalInfer(code)
  val value = result.getValue(this.getClass.getClassLoader)
  println("  result.getvalue " + value)
  println("  result.tpe      " + result.tpe)
  val c1: Constr = value.asInstanceOf[(Seq[_], XGTY)]._2
  println("  c1              " + c1)

def testEvalError() = 
  import com.eed3si9n.eval.{Eval, EvalResult, EvalException}
  val eval = Eval()
  val code = """badCode"""
  val result: EvalResult | String = try eval.evalInfer(code) catch
    case e: EvalException =>
      println(e.getMessage()) 
      println(e.getStackTrace().mkString("\n"))
      e.getMessage().dropWhile(_ != '$')
  println(result)
  

@main def main(): Unit =
  testEvalTyped()
  testEvalInfer()
  testEvalError()