@lift
class Wolf extends Actor{
	var test2 : Any = 0
	var test5_test_ : Any = 0
	def get_test2(): Any = test2
	def set_test2(__value : Any): Unit = {
		test2 = __value
	}
	def get_test5_test_(): Any = test5_test_
	def set_test5_test_(__value : Any): Unit = {
		test5_test_ = __value
	}
	def main():Unit = {
		
		while(true){
			handleMessages()
			waitLabel(Turn, 1)
		}
	}
}