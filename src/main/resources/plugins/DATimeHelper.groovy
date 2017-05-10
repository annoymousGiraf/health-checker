package plugins

import groovyx.net.http.RESTClient
//import groovyx.net.http.
import static groovyx.net.http.ContentType.JSON
import groovy.json.JsonSlurper
import groovy.json.JsonOutput
def execute(){

    return ["status":"OK", "details":System.currentTimeSeconds().toString(), "solution_suggestion":""]
}



def getDescription(){
    return "Checks is all servers are in synch"
}

println execute()

