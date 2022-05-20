
object MainInit extends {
	val liftedMain = meta.classLifting.liteLift{
		def apply(size_x: Int, size_y: Int): List[Actor] = {
			val observer = new Observer()
			val patches = (1 to size_x).map(x => (1 to size_y).map(y =>{
				new Patch(codegen.MainGen$@4657037f.observerVariableName, x, y)
			})).flatten
			observer :: patches
		}
	}
}