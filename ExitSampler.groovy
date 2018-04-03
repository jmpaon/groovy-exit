package exit

class ExitSampler {

    final static int SENSIBLE_SAMPLE_SIZE = 10000

    final int sampleSize

    final ExitMatrix matrix

    ExitSampler(ExitMatrix sampledMatrix, java.lang.Integer size = SENSIBLE_SAMPLE_SIZE) {
        Objects.requireNonNull(sampledMatrix)
        assert size > 0

        this.matrix = sampledMatrix
        this.sampleSize = size

    }


    ExitMatrix summedImpactMatrix() {
        ArrayList<ArrayList<Double>> summed = new ArrayList<>(matrix.varCount)
        summed.each { it = new ArrayList<Double>(matrix.varCount)}



        return new ExitMatrix(matrix.varCount, 100, summed)
    }

    double estimateImpact(int impactorIndex, int impactedIndex, int length) {

        assert impactorIndex > 0 && impactorIndex <= this.matrix.varCount
        assert impactedIndex > 0 && impactedIndex <= this.matrix.varCount

        def chain = matrix.chainIndices(impactorIndex, impactedIndex, length)
        StreamingAverage sa = new StreamingAverage(matrix.relativeImpact(chain))

        println "row 0 chain $chain with relative impact ${matrix.relativeImpact(chain)}, computed impact is now ${sa.getAverage()}"

        for(int sampleRow=1; sampleRow <= this.sampleSize; sampleRow++) {

            chain = matrix.chainIndices(impactorIndex, impactedIndex, length)

            double sampledImpact = matrix.relativeImpact(chain)
            sa.putIntoAverage(sampledImpact)

            println "row $sampleRow chain $chain with relative impact ${matrix.relativeImpact(chain)}, computed impact is now ${sa.getAverage()}"

        }

        return sa.getAverage()

    }





}
