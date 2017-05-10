package plugins

def execute() {
    def ownerDefinition = [
            ['securetrack\\..+', 'st', 'st'],
            ['cgi', 'apache', 'apache'],
            ['cgi\\.scw\\..+', 'apache', 'apache'],
            ['cgi\\..+', 'apache', 'apache'],
            ['.+\\.get_.+_.+\\.log', 'apache', 'apache']
    ]

    def logs_dir = "/var/log/st"

    def sout = new StringBuilder(), serr = new StringBuilder()
    def proc = ('ls -l ' + logs_dir).execute()
    proc.consumeProcessOutput(sout, serr)
    proc.waitForOrKill(10000)
    def fileLines = sout.toString().split('\n')

//    def res = '{"status":"OK","details":"test succeeded","solution_suggestion":""}'
    def res1 = ["status":"OK", "details":"test succeeded", "solution_suggestion":""]

    fileLines.each { fileLine ->
        def fileInfo = fileLine.split("\\s+")

        if (fileInfo.size() == 9) {
            println "Yahoo " + fileInfo[0] + ", " + fileInfo[2] + ", " + fileInfo[3] + ", " + fileInfo[8]
            ownerDefinition.each { reqs ->
                if (fileInfo[8] ==~ reqs[0]) {
                    if (fileInfo[2] != reqs[1] && fileInfo[3] != reqs[2]) {
                        res1["status"] = "Failed"
                        res1["details"] = fileInfo[8] + " has incorrect owner"
                        res1["solution_suggestion"] = "Change owner of the file to " + reqs[1] + ":" + reqs[2]
                        //res = '{"status":"Failed","details":"' + fileInfo[8] + ' has incorrect owner","solution_suggestion":"Change owner of the file to "' + reqs[1] + ':' + reqs[2] +'"}'
                    }
                }
            }
        }
    }
    return res1
}

