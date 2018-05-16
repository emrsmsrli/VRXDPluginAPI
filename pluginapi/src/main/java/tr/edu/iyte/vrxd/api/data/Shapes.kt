package tr.edu.iyte.vrxd.api.data

import tr.edu.iyte.vrxd.api.helper.between

sealed class Shape(var x: Int, var y: Int, rotation: Double, var fill: Long) {
    var rotation: Double = rotation
        set(value) {
            if(value.between(0.0, 360.0))
                throw IllegalArgumentException("rotation must be between [0-360)")
            field = value
        }
    abstract val type: String
}

class Circle(x: Int, y: Int, var radius: Double, fill: Long) :
        Shape(x, y, 0.0, fill) {
    override val type = "circle"

    override fun toString(): String {
        return "$type,$x,$y,$radius"
    }
}
class Rectangle(x: Int, y: Int, var width: Int, var height: Int, rotation: Double, fill: Long) :
        Shape(x, y, rotation, fill) {
    override val type = "rectangle"

    override fun toString(): String {
        return "$type,$x,$y,$width,$height"
    }
}
class EquiTriangle(x: Int, y: Int, var radius: Double, rotation: Double, fill: Long) :
        Shape(x, y, rotation, fill) {
    override val type = "equitriangle"

    override fun toString(): String {
        return "$type,$x,$y,$radius"
    }
}
class Sprite(x: Int, y: Int, var width: Int, var height: Int, rotation: Double, var filePath: String) :
        Shape(x, y, rotation, 0L) {
    override val type = "sprite"

    override fun toString(): String {
        return "$type,$x,$y,$width,$height,$filePath"
    }
}
class Text(x: Int, y: Int, var size: Int, rotation: Double, var text: String, color: Long) :
        Shape(x, y, rotation, color) {
    override val type = "text"

    override fun toString(): String {
        return "$type,$x,$y,$text"
    }
}