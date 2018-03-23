package tr.edu.iyte.vrxd.api.data

sealed class Config(val label: String)

class IntConfig(label: String, val data: Int) : Config(label)
class DoubleConfig(label: String, val data: Double) : Config(label)