package parser.cvs

trait CValue

case class CString(value: String) extends CValue {
  override def equals(obj: Any): Boolean = {
    obj.isInstanceOf[CString] && value.equals(obj.asInstanceOf[CString].value)
  }
}

case class CArray(value: List[CValue]) extends CValue {
  def getVal: Seq[CValue] = value

  override def equals(obj: Any): Boolean = obj match {
    case obj: CArray =>
      value.length == obj.getVal.length &&
        !value.zipWithIndex.exists {
          case (v, index) => !obj.getVal(index).equals(v)
        }
    case _ => false
  }
}
