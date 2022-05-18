
object MainInit extends {
	val liftedMain = meta.classLifting.liteLift{
		def apply(size_x: Int, size_y: Int): List[Actor] = {
			val observer = new Observer()
			val patches = (1 to size_x).map(x => (1 to size_y).map(y =>{
				new Patch(observer, x, y)
			})).flatten
			observer :: patches
		}
	}
}