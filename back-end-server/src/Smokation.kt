import java.io.Serializable

class Smokation : Serializable {
    var latitude: Double = 0.toDouble()
    var longitude: Double = 0.toDouble()

    constructor() {

    }

    constructor(lat: Double, longi: Double) {
        this.latitude = lat
        this.longitude = longi
    }

    @Override
    override fun toString(): String {
        return "Smokation [Latitude=$latitude, Longitude=$longitude]"
    }

    companion object {

        private val serialVersionUID = 1L
    }

}
