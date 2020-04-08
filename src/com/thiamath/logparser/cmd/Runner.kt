package com.thiamath.logparser.cmd

fun launchApp(args: Array<String>) {
    try {
        commandFactory(args).execute()
    } catch (e: IllegalArgumentException) {
        println("Bad arguments")
        showHelp()
    }
}

private fun commandFactory(args: Array<String>): Command =
        when {
            args.size == 1 && args[0] == Command.Action.TEST.command -> TestCommand()
            args.size == 5 && args[0] == Command.Action.PARSE.command -> ParseCommand(
                    filename = args[1],
                    initDatetime = args[2],
                    endDatetime = args[3],
                    hostname = args[4]
            )
            args.size == 2 && args[0] == Command.Action.FOLLOW.command -> FollowCommand(
                    filename = args[1],
                    hostname = args[2]
            )
            else -> throw IllegalArgumentException()
        }

private fun showHelp() {
    print("""
            Usage of the parser:
            
            Arguments:
              - ACTION: The first argument indicates the action to run.
                        Possible values: test, parse, follow
                  - test: Runs the internal tests and shows the outcome.
                          Example: java -jar parser test
                  - parse: Given the name of a file (with the format described above), an init_datetime, an
                           end_datetime, and a Hostname, returns a list of hostnames connected to the given
                           host during the given period.
                           
                           The arguments accepted are (in order) filename, init_datetime (in milliseconds), end_datetime (in milliseconds),
                           hostname.
                           Example: java -jar parser parse input-file-10000.txt 1565647300338 1565650675061
                  - follow: parse previously written log files and terminate or collect input from a new log
                            file while it's being written and run indefinitely.
                            The script will output, once every hour:
                            ● a list of hostnames connected to a given (configurable) host during the last hour.
                            ● a list of hostnames received connections from a given (configurable) host during the last hour.
                            ● the hostname that generated most connections in the last hour.
                            
                            The arguments accepted are (in order) filename, hostname.
                            Example: java -jar parser follow input-file-10000.txt
        """.trimIndent())
}