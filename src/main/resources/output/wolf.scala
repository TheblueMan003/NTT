@Lift
class Wolf{
	var test2 : Any = 0
	var test5_test_ : Any = 0
	def get_test2(): Any = test2
	def set_test2(__value__ : Any): Unit = {
		test2 = __value__
	}
	def get_test5_test_(): Any = test5_test_
	def set_test5_test_(__value__ : Any): Unit = {
		test5_test_ = __value__
	}
	def main():Unit = {
		
		while(true){
			handleMessages()
			waitLabel(Turn, 1)
		}
	}
}