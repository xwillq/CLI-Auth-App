import services.*
import kotlin.system.exitProcess
import ExitCodes.*
import kotlin.IndexOutOfBoundsException as KotlinIndexOutOfBoundsException

class Handler(args: Array<String>) {
    var login = ""
        private set
    var password = ""
        private set
    var role = ""
        private set
    var resource = ""
        private set
    var dateStart = ""
        private set
    var dateEnd = ""
        private set
    var volume = ""
        private set

    init {
        when {
            args.isEmpty() -> terminate(true, SuccessCode.code)
            args[0] == "-h" -> terminate(true, HelpCode.code)
            args.size != 4 || args.size != 6 || args.size != 8 -> terminate(true, SuccessCode.code)
            args[0] != "-h" || args[0] != "-login" -> terminate(true, HelpCode.code)
            !args.contains("-h") && !args.contains("-login") -> terminate(true, SuccessCode.code)
        }
        for (arg in args) {
            try {
                when {
                    arg == "-login" -> login = args[args.indexOf(arg) + 1]
                    arg == "-pass" -> password = args[args.indexOf(arg) + 1]
                    arg == "-role" -> role = args[args.indexOf(arg) + 1]
                    arg == "-res" -> resource = args[args.indexOf(arg) + 1]
                    arg == "-ds" -> dateStart = args[args.indexOf(arg) + 1]
                    arg == "-de" -> dateEnd = args[args.indexOf(arg) + 1]
                    arg == "-vol" -> volume = args[args.indexOf(arg) + 1]
                }
            }
            catch (e: IndexOutOfBoundsException)
            {
                terminate(true, SuccessCode.code)
            }
        }
        if (role != "" && !(role == "WRITE" || role == "READ" || role == "DELETE")) {
            exitProcess(UnknownRole.code)
        }
    }

    fun authenNeeded(): Boolean {
        return login != "" && password != ""
    }

    fun authorizeNeeded(): Boolean {
        return authenNeeded() && resource != "" && password != ""
    }

    fun accountingNeeded(): Boolean {
        return authorizeNeeded() && dateEnd != "" && dateStart != "" && volume != ""
    }

    private fun terminate(printHelp: Boolean, errorCode: Int) {
        if (printHelp) {
            printHelp()
        }
        exitProcess(errorCode)
    }
}