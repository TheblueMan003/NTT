@Lift
class Wolf{
	var test2 : Int = 0
	var test5_test_ : Int = 0
	def get_test2(): Int = test2
	def set_test2(__value__ : Int): Unit = {
		test2 = __value__
	}
	def get_test5_test_(): Int = test5_test_
	def set_test5_test_(__value__ : Int): Unit = {
		test5_test_ = __value__
	}
	def main():Unit = {
		
		while(true){
			handleMessages()
			waitLabel(Turn, 1)
		}
	}
}