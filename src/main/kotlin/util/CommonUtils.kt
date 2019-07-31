package util


fun strip_ext_tar(tar:String):String{
    val regex = """.tar""".toRegex()
    return regex.replace(tar, "")
}

fun CmdRunner.rm_f(tmpFiles: List<String>)
{
    val cmd ="rm -f ${tmpFiles.joinToString(" ")}"
    this.run(cmd)
}