import scala.collection.mutable

/**
  * Created by vasilisck on 11/10/16.
  */
trait TestMethods {

  def oldFlatList(inList: List[AnyRef]): List[String] =
    inList.foldLeft(List[String]())((acc, elem) => elem match {
      case str: String => acc :+ str
      case list: List[AnyRef] => acc ++ oldFlatList(list)
    })

  def oldCoalesceDuplicates(inList: List[String]): List[String] =
    inList.foldLeft(List[String]())((acc, elem) => acc.lastOption match {
      case Some(`elem`) => acc
      case _ => acc :+ elem
    })


  def newFlatList(inList: List[AnyRef]): List[String] =
    inList.foldLeft(List.newBuilder[String])((acc, elem) => elem match {
      case str: String => acc += str
      case list: List[AnyRef] => acc ++= oldFlatList(list)
    }).result()

  def newFlatListOnList(inList: List[AnyRef]): List[String] =
    inList.foldLeft(List[String]())((acc, elem) => elem match {
      case str: String => str +: acc
      case list: List[AnyRef] => oldFlatList(list).reverse ++ acc
    }).reverse

  def newCoalesceDuplicates(inList: List[String]): List[String] = {
    type Acc = (scala.collection.mutable.Builder[String, List[String]], Option[String])
    val initial: Acc = List.newBuilder[String] -> None
    initial._1.sizeHint(inList.length)

    val (folded, _) = inList.foldLeft[Acc](initial) {
      case ((builder, Some(prev)), elem) if prev == elem => builder -> Some(prev)
      case ((builder, _), elem) => (builder += elem) -> Some(elem)
    }
    folded.result()
  }

  def newCoalesceDuplicatesOnList(inList: List[String]): List[String] = {
    inList.foldLeft(List[String]())((acc, elem) => acc.headOption match {
      case Some(`elem`) => acc
      case _ => elem +: acc
    }).reverse
  }

}
