class ExitMatrix {
   
   final int varCount
   final double maxValue
   private List<List<Double>> values
   
   ExitMatrix(int varCount, double maxValue) {
      this.varCount = varCount
      this.values = new double[varCount][varCount]      
      this.maxValue = maxValue
   }

   ExitMatrix(int varCount, ArrayList<ArrayList<Double>> values) {
      this.varCount = varCount
      this.values   = values
      this.maxValue = this.values.collect { absMax(it) }.max()
   }
   
   ExitMatrix(int varCount, double maxValue, ArrayList<ArrayList<Double>> values) {
      this.varCount = varCount
      this.maxValue = maxValue
      if(values.size() != this.varCount) throw new Exception("varCount is $varCount, only $values.size rows provided")
      values.eachWithIndex { it, i -> if(it.size() != this.varCount) throw new Exception("varCount is $varCount, row $i has length $it.size") }
      values.eachWithIndex { row, rowIndex -> 
         row.eachWithIndex { value, colIndex ->
            if(Math.abs(value) > this.maxValue) 
               throw new Exception("row ${rowIndex+1}, column ${colIndex+1} has value $value, greater than the defined maxValue ($maxValue)") 
         }
      }
      this.values = values
   }
   
   def show() {
      println "Exit matrix with $varCount variables, maxValue = $maxValue"
      values.each { println it }
      println "---------------"
   }
   
   String toString() {
      values.toString()
   }
   
   double absMax(List<Double> list) {
      def am = 0
      list.collect { Math.abs(it) }.max()
   }
   
   double get(int row, int column) { println "get at ($row,$column)"; values[row-1][column-1] }
   
   double set(int row, int column, double value) { values[row-1][column-1] = value }
   
   def setValuesToZero() {values.each { row -> row.each { value -> value = 0.0 } }}
   
   List<Double> valuesToList() {this.values.flatten()}
   
   List<Double> impacts(indices) {
   
      (0..indices.size()-2).step(1).collect { get(indices[it],indices[it+1]) }
   
      /*
      def vs = new LinkedList<Double>()
      for(int i=0;i<indices.size()-1;i++) {
         vs << values [indices[i]-1] [indices[i+1]-1]
      }
      vs
      */
   }
   
   List<Integer> randomIndices(int length) {
      if( length < 1 || length > this.varCount ) throw new Exception("length > variable count")
      List<Integer> indices = (1..varCount).step(1)
      Collections.shuffle(indices)      
      indices.take(length)
   }
   
   double relativeImpact(List<Integer> listOfIndices) {
      this.impacts(listOfIndices).sum() / (this.maxValue ** (listOfIndices.size()-1))
   }

}
