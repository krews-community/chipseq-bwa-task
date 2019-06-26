package util


fun strip_ext_tar(tar:String):String{
    val regex = """.tar""".toRegex()
    return regex.replace(tar, "")
}
