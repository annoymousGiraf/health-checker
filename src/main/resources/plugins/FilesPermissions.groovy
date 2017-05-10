package plugins

def execute() {
    def ownerDefinition = [
            ['securetrack\\..+', 'st', 'st'],
            ['cgi', 'apache', 'apache'],
            ['cgi\\.scw\\..+', 'apache', 'apache'],
            ['cgi\\..+', 'apache', 'apache'],
            ['.+\\.get_.+_.+\\.log', 'st', 'st']
    ]

    def logs_dir = "/var/log/st"

    def sout = new StringBuilder(), serr = new StringBuilder()
    def proc = ('ls -l ' + logs_dir).execute()
    proc.consumeProcessOutput(sout, serr)
    proc.waitForOrKill(10000)
    def fileLines = sout.toString().split('\n')

    def res1 = ["status":"OK", "details":"test succeeded", "solution_suggestion":""]

    problematicFiles = []

    fileLines.each { fileLine ->
        def fileInfo = fileLine.split("\\s+")

        if (fileInfo.size() == 9) {
            println "File " + fileInfo[0] + ", " + fileInfo[2] + ", " + fileInfo[3] + ", " + fileInfo[8]
            ownerDefinition.each { reqs ->
                if (fileInfo[8] ==~ reqs[0]) {
                    if (fileInfo[2] != reqs[1] && fileInfo[3] != reqs[2]) {
                        problematicFiles << fileInfo[8]
                    }
                }
            }
        }
    }

    if(problematicFiles.size() > 0) {
        res1["status"] = "Failed"
        res1["details"] = problematicFiles.toString() + " has incorrect owner"
        res1["solution_suggestion"] = "Change owner of the file to correct user"
    }
    return res1
}

def getDescription(){
    return "Checks executable files owner"
}
this
