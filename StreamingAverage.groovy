
class StreamingAverage {

    private int times
    private double value

    StreamingAverage(double initialValue) {
        this.value = initialValue
        this.times = 1
    }

    private StreamingAverage(double copiedValue, int copiedTimes) {
        this.value = copiedValue
        this.times = copiedTimes
    }

    def put(double newValue) {
        value = (value * times + newValue) / ++times
    }

    def put(StreamingAverage another) {
        this.value = (this.value * this.times + another.value * another.times) / (this.times + another.times)
    }

    StreamingAverage putToNew(StreamingAverage another) {
        StreamingAverage newStreamingAverage = new StreamingAverage(this.value. this.times)
        newStreamingAverage.put(another);
        return newStreamingAverage;
    }

    double getAverage() {
        value
    }

}
