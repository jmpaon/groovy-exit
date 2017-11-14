class ExitSampler {

    final int sampleSize
    final ExitMatrix matrix

    ExitSampler(ExitMatrix matrix, int sampleSize) {
        assert matrix != null
        assert sampleSize > 0

        this.matrix = matrix
        this.sampleSize = sampleSize

    }

    ExitMatrix summedImpactMatrix() {
        ArrayList<ArrayList<Double>> summed = new ArrayList<>(matrix.varCount)
        summed.each { it = new ArrayList<Double>(matrix.varCount)}


        return new ExitMatrix(matrix.varCount, 100, summed)
    }





}
