package plugins

import groovyx.net.http.RESTClient
import groovy.json.JsonOutput

def execute(){
    float allowedTimeDifference = 5;
    @Grab (group = 'org.codehaus.groovy.modules.http-builder', module = 'http-builder', version = '0.7')
    daList = ["10.100.16.231", "10.100.16.230"]
    //daList = ["192.168.133.105", "192.168.133.105"]
    responceList = []
    daList.each { ip ->

        long startTime = System.currentTimeSeconds()
        long remoteTime = getRemoteTime(ip)
        long endTime = System.currentTimeSeconds()

        double timeDiff = Math.abs(remoteTime - (startTime+endTime)/2);
        if (timeDiff > allowedTimeDifference)
        {
            responceList << [ip, timeDiff]

        }
    }
    if(responceList.empty) {
        return ["status": "OK", "details": "test succeeded", "solution_suggestion": ""]
    }
    def detStr = "Your machine times are not synchronized "
    responceList.each { l ->
        detStr += l[0] + " diff is " + l[1] + " secs, while expected " + allowedTimeDifference + " secs "
    }

    return ["status":"Failed", "details":detStr, "solution_suggestion":"Synch you Tufin machines"]
}

def getRemoteTime(String ip)
{
    url = "http://"+ip+":9999/health/"
    def client = new RESTClient(url)
    def response = client.get(path: "check/DATimeHelper",
            headers: [Accept: 'application/json'])

    println("Status: " + response.status)
    if (response.data) {
        println("Content Type: " + response.contentType)
        println("Headers: " + response.getAllHeaders())
        println("Body:\n" + JsonOutput.prettyPrint(JsonOutput.toJson(response.data)))
    }
    println response.data["details"]

    return Long.parseLong(response.data["details"])
}


def getDescription(){
    return "Checks is all servers are in synch"
}
this
//println execute()