/**
 * Class representing an impact matrix in an EXIT cross-impact model.
 * Can be used to represent a direct impact matrix (the input matrix)
 * or a summed/total impact matrix (the output matrix).
 */
class ExitMatrix {

    final int varCount

    final double maxValue

    final List<List<Double>> values /* Mutable   */

    /**
     * Constructor for ExitMatrix.
     * @param varCount Number of variables in the matrix
     * @param maxValue EXIT maximum value of the model
     * @param values Matrix values. For all values of i in 1..varCount, value for entry values(i,i) must be 0.
     */
    ExitMatrix(int varCount, double maxValue, List<List<Double>> values) {

        if(varCount<2)  throw new Exception("varCount is $varCount, minimum is 2")
        if(maxValue<=0) throw new Exception("maxValue is $maxValue. maxValue must be a positive real")

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

        // Impacts from Hx to Hx itself are not allowed, check
        values.eachWithIndex { it, i -> if(it[i] != 0) throw new Exception("Impact on hypothesis itself on row ${i+1}") }
        this.values = values.clone()
        }


    def show() {
      println "Exit matrix with $varCount variables, maxValue = $maxValue"

      java.text.DecimalFormat fmt = new java.text.DecimalFormat("+0.00;-0.00");

      values.eachWithIndex {it,  i  ->
         it.eachWithIndex  {iit, ii -> if(i==ii) print "0\t"; else print "${fmt.format(iit)}\t"};
         print "\n"
      }
    }

    double absMax(List<Double> list) {list.collect { Math.abs(it) }.max()}

    double get(int row, int column) { values[row-1][column-1] }

    double set(int row, int column, double value) {
      assert row != column
      assert Math.abs(value) <= maxValue
      values[row-1][column-1] = value
    }


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

    List<Integer> chainIndices(int impactor, int impacted, int length) {
      ArrayList<Integer> availableIndices = (1..varCount).step(1).minus([impactor,impacted])
      Collections.shuffle(availableIndices)
      ArrayList<Integer> indices = availableIndices.take(length-2)
      indices.plus(0,[impactor]).plus(impacted)
    }

    double relativeImpact(List<Integer> listOfIndices) { this.impacts(listOfIndices).inject(1) { prod, val -> (val / maxValue) * prod }}

    double absMean() { (this.values.flatten().inject(0) {Double sum, Double val -> sum += Math.abs(val) }) / (this.varCount**2 - this.varCount) }



   
   
   

}
