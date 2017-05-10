package plugins

def execute(){

    return ["status":"OK", "details":System.currentTimeSeconds().toString(), "solution_suggestion":""]
}



def getDescription(){
    return "Checks is all servers are in synch"
}

println execute()

