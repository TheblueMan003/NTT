
object MainInit extends {
	val liftedMain = meta.classLifting.liteLift{
		def apply(): List[Actor] = {
			List(new Observer())
		}
	}
}