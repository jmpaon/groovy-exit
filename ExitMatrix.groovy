class ExitMatrix {
   
   final int varCount
   final double maxValue
   final List<String> varNames     /* Immutable */
   final List<List<Double>> values /* Mutable   */
   
   ExitMatrix(int varCount, List<List<Double>> values) {
      this.varCount = varCount
      this.values   = values
      this.maxValue = this.values.collect { absMax(it) }.max()
   }
   
   ExitMatrix(int varCount, double maxValue, List<String> varNames, List<List<Double>> values) {
      if(varCount<2)  throw new Exception("varCount is $varCount, minimum is 2")
      if(maxValue<=0) throw new Exception("maxValue is $maxValue. maxValue must be a positive real")
      if(!varNames.size() == varCount) throw new Exception ("varNames has ${varNames.size()} variable names, count must be equal to varCount ($varCount)")
      
      this.varCount = varCount
      this.maxValue = maxValue
      this.varNames = varNames.clone().asImmutable()
      
      if(values.size() != this.varCount) throw new Exception("varCount is $varCount, only $values.size rows provided")
      values.eachWithIndex { it, i -> if(it.size() != this.varCount) throw new Exception("varCount is $varCount, row $i has length $it.size") }
      values.eachWithIndex { row, rowIndex -> 
         row.eachWithIndex { value, colIndex ->
            if(Math.abs(value) > this.maxValue) 
               throw new Exception("row ${rowIndex+1}, column ${colIndex+1} has value $value, greater than the defined maxValue ($maxValue)") 
         }
      }
      
      // Impacts from Hx to Hx itself are not allowed, check
      values.eachWithIndex { it, i -> if(it[i] != 0) throw new Exception("Impact on hypothesis itself on row ${i+1}") }
      this.values = values.clone()
   }
   
   def show() {
      println "Exit matrix with $varCount variables, maxValue = $maxValue"
      Iterator varName = varNames.iterator()
      java.text.DecimalFormat fmt = new java.text.DecimalFormat("+0.00;-0.00");
      
      values.eachWithIndex { it, i -> 
         print "${varName.next()}:\t"
         it.eachWithIndex {iit, ii -> if(i==ii) print "0\t"; else print "${fmt.format(iit)}\t"};
         print "\n"
      }
   }
   
   String toString() {
      values.toString()
   }
   
   double absMax(List<Double> list) {
      def am = 0
      list.collect { Math.abs(it) }.max()
   }
   
   double get(int row, int column) { values[row-1][column-1] }
   
   double set(int row, int column, double value) { values[row-1][column-1] = value }
   
   def setValuesToZero() {values.each { row -> row.each { value -> value = 0.0 } }}
   
   List<Double> impacts(indices) {
      (0..indices.size()-2).collect { get(indices[it],indices[it+1]) }
   }
   
   List<Integer> randomIndices(int length) {
      if( length < 1 || length > this.varCount ) throw new Exception("length > variable count")
      List<Integer> indices = (1..varCount).step(1)
      Collections.shuffle(indices)
      indices.take(length)
   }
   
   double relativeImpact(List<Integer> listOfIndices) {
      this.impacts(listOfIndices).inject(1) { prod, val -> (val / maxValue) * prod }      
   }
   
   double absMean() {}
   
   

}
