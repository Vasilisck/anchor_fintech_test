import org.scalatest.FlatSpec

/**
  * Created by vasilisck on 11/10/16.
  */
class TestMethodsSpec extends FlatSpec with TestMethods {

  val listOfStrings = List("a", "b", "c", "d", "e", "f")
  val listOfStringWithListOfStrings = List("a", List("b", "c"), "d")
  val listOfStringsWithMergedDuplicates = List("a", "b", "c", "c", "d", "c", "e", "f")

  "old flat list" should "don't change list of strings" in {
    assertResult(listOfStrings)(oldFlatList(listOfStrings))
  }

  it should "flat list of string with list of strings" in {
    assertResult(List("a", "b", "c", "d"))(oldFlatList(listOfStringWithListOfStrings))
  }

  "old coalesce duplicates" should "do not remove change list of strings w/o merged duplicates" in {
    assertResult(listOfStrings)(oldCoalesceDuplicates(listOfStrings))
  }

  it should "remove merged duplicates" in {
    assertResult(List("a", "b", "c", "d", "c", "e", "f"))(oldCoalesceDuplicates(listOfStringsWithMergedDuplicates))
  }

  "new flat list" should "don't change list of strings" in {
    assertResult(listOfStrings)(newFlatList(listOfStrings))
  }

  it should "flat list of string with list of strings" in {
    assertResult(List("a", "b", "c", "d"))(newFlatList(listOfStringWithListOfStrings))
  }

  "new flat list on list" should "don't change list of strings" in {
    assertResult(listOfStrings)(newFlatListOnList(listOfStrings))
  }

  it should "flat list of string with list of strings" in {
    assertResult(List("a", "b", "c", "d"))(newFlatListOnList(listOfStringWithListOfStrings))
  }

  "new coalesce duplicates" should "do not remove change list of strings w/o merged duplicates" in {
    assertResult(listOfStrings)(newCoalesceDuplicates(listOfStrings))
  }

  it should "remove merged duplicates" in {
    assertResult(List("a", "b", "c", "d", "c", "e", "f"))(newCoalesceDuplicates(listOfStringsWithMergedDuplicates))
  }

  "new coalesce duplicates on list" should "do not remove change list of strings w/o merged duplicates" in {
    assertResult(listOfStrings)(newCoalesceDuplicatesOnList(listOfStrings))
  }

  it should "remove merged duplicates" in {
    assertResult(List("a", "b", "c", "d", "c", "e", "f"))(newCoalesceDuplicatesOnList(listOfStringsWithMergedDuplicates))
  }

  "speed test" should "new flat list should work faster" in {
    val inList = ((1 to 15000) map(_.toString)).toList
    val oldStart = System.currentTimeMillis()
    oldFlatList(inList)
    val oldElapsed = System.currentTimeMillis() - oldStart
    val newStart = System.currentTimeMillis()
    newFlatList(inList)
    val newElapsed = System.currentTimeMillis() - newStart
    println(s"old flat list speed on ${inList.length} elems: $oldElapsed ms")
    println(s"new flat list speed on ${inList.length} elems: $newElapsed ms")
    //using string builder improve our speed on 15k elems in 300 times
  }

  it should "new coalesce duplicates should work faster" in {
    val inList = ((1 to 10000) map(_.toString)).toList
    val oldStart = System.currentTimeMillis()
    oldCoalesceDuplicates(inList)
    val oldElapsed = System.currentTimeMillis() - oldStart
    val newStart = System.currentTimeMillis()
    newCoalesceDuplicates(inList)
    val newElapsed = System.currentTimeMillis() - newStart
    println(s"old coalesce duplicates speed on ${inList.length} elems: $oldElapsed ms")
    println(s"new coalesce duplicates speed on ${inList.length} elems: $newElapsed ms")
    //using string builder improve our speed on 10k elems in 250 times
  }

  it should "test list builder vs head/reverse on coalesce duplicates" in {
    val inList = ((1 to 2500000) map(_.toString)).toList
    val oldStart = System.currentTimeMillis()
    newCoalesceDuplicates(inList)
    val oldElapsed = System.currentTimeMillis() - oldStart
    val newStart = System.currentTimeMillis()
    newCoalesceDuplicatesOnList(inList)
    val newElapsed = System.currentTimeMillis() - newStart
    println(s"new coalesce duplicates speed on ${inList.length} elems: $oldElapsed ms")
    println(s"new coalesce duplicates on list with headOption and reverse speed on ${inList.length} elems: $newElapsed ms")
    //on 2.5kk+ elems string builder work 25 times longer than headOption/reverse
    //on 2kk- elems string builder work 2 times faster than headOption/reverse

  }

  it should "test list builder vs head/reverse on flat list" in {
    val inList = ((1 to 10000000) map(_.toString)).toList
    val oldStart = System.currentTimeMillis()
    newFlatList(inList)
    val oldElapsed = System.currentTimeMillis() - oldStart
    val newStart = System.currentTimeMillis()
    newFlatListOnList(inList)
    val newElapsed = System.currentTimeMillis() - newStart
    println(s"new flat list speed on ${inList.length} elems: $oldElapsed ms")
    println(s"new flat list on list with headOption and reverse speed on ${inList.length} elems: $newElapsed ms")
    //on 10kk elems string builder work 40 times faster than headOption/reverse
  }

  //after all we should use newFlatList every time
  //we should use newCoalesceDuplicates if num of elems lower than 2kk and newCoalesceDuplicatesOnList if bigger

}
