import org.scalatest.funsuite.AnyFunSuite
import codegen.Renamer
    
 class RenamerTest extends AnyFunSuite {
    test("rename correctly") {
        assert(Renamer.toValidName(".?=*!<>:#+/%$_^'&-a") === "__________________a")
    }
 }