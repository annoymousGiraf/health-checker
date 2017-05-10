package plugins

import groovy.sql.Sql


/**
 * @author marina.bunin
 * @since 10/05/2017
 */

println("DB Lock plugin greeting")

def execute() {
        def prop = new Properties()
        new File("../db.properties").withInputStream { p ->
                prop.load(p)
        }

// load postgresql driver
//this.class.classLoader.rootLoader.addURL(new URL('file:///../../../../postgresql-9.3-1102-jdbc4.jar'))

        println "Going to connect to " + prop.getProperty("db.url")

        def sql = Sql.newInstance(prop.getProperty("db.url"), prop.getProperty("db.username"), prop.getProperty("db.password"), prop.getProperty("db.driverClassName"))
        println("ST DB connected")
        sql.connection.autoCommit = false

        def listDBLocks90 =
                "SELECT                                                   \n" +
                        "    ka.client_port as client_port, kl.pid as blocking_pid,\n" +
                        "    ka.usename as blocking_user,\n" +
                        "    ka.current_query as blocking_query,\n" +
                        "    a.client_port as client_port, bl.pid as blocked_pid,\n" +
                        "    a.usename as blocked_user,\n" +
                        "    a.current_query as blocked_query,\n" +
                        "    to_char(age(now(), a.query_start),'HH24h:MIm:SSs') as age\n" +
                        "FROM pg_catalog.pg_locks bl\n" +
                        "    JOIN pg_catalog.pg_stat_activity a\n" +
                        "        ON bl.pid = a.procpid\n" +
                        "    JOIN pg_catalog.pg_locks kl\n" +
                        "        ON bl.locktype = kl.locktype\n" +
                        "        and bl.database is not distinct from kl.database\n" +
                        "        and bl.relation is not distinct from kl.relation\n" +
                        "        and bl.page is not distinct from kl.page\n" +
                        "        and bl.tuple is not distinct from kl.tuple\n" +
                        "        and bl.virtualxid is not distinct from kl.virtualxid\n" +
                        "        and bl.transactionid is not distinct from kl.transactionid\n" +
                        "        and bl.classid is not distinct from kl.classid\n" +
                        "        and bl.objid is not distinct from kl.objid\n" +
                        "        and bl.objsubid is not distinct from kl.objsubid\n" +
                        "        and bl.pid <> kl.pid\n" +
                        "    JOIN pg_catalog.pg_stat_activity ka\n" +
                        "        ON kl.pid = ka.procpid\n" +
                        "WHERE kl.granted and not bl.granted\n" +
                        "ORDER BY age DESC;"


        def listDBLocks94 =
                "SELECT                                                   \n" +
                        "    ka.client_port as client_port, kl.pid as blocking_pid,\n" +
                        "    ka.usename as blocking_user,\n" +
                        "    ka.query as blocking_query,\n" +
                        "    a.client_port as client_port, bl.pid as blocked_pid,\n" +
                        "    a.usename as blocked_user,\n" +
                        "    a.query as blocked_query,\n" +
                        "    to_char(age(now(), a.query_start),'HH24h:MIm:SSs') as age\n" +
                        "FROM pg_catalog.pg_locks bl\n" +
                        "    JOIN pg_catalog.pg_stat_activity a\n" +
                        "        ON bl.pid = a.pid\n" +
                        "    JOIN pg_catalog.pg_locks kl\n" +
                        "        ON bl.locktype = kl.locktype\n" +
                        "        and bl.database is not distinct from kl.database\n" +
                        "        and bl.relation is not distinct from kl.relation\n" +
                        "        and bl.page is not distinct from kl.page\n" +
                        "        and bl.tuple is not distinct from kl.tuple\n" +
                        "        and bl.virtualxid is not distinct from kl.virtualxid\n" +
                        "        and bl.transactionid is not distinct from kl.transactionid\n" +
                        "        and bl.classid is not distinct from kl.classid\n" +
                        "        and bl.objid is not distinct from kl.objid\n" +
                        "        and bl.objsubid is not distinct from kl.objsubid\n" +
                        "        and bl.pid <> kl.pid\n" +
                        "    JOIN pg_catalog.pg_stat_activity ka\n" +
                        "        ON kl.pid = ka.pid\n" +
                        "WHERE kl.granted and not bl.granted\n" +
                        "ORDER BY age DESC;"


        def versionQuery = "SELECT version();"
//        PostgreSQL 9.0.23 on x86_64-unknown-linux-gnu, compiled by GCC gcc (GCC) 4.4.7 20120313 (Red Hat 4.4.7-16), 64-bit
        sql.eachRow(versionQuery) { row ->
                println row
        }

// based on version, run the appropriate query
    def resultList = []
    sql.eachRow(listDBLocks90) { row ->
        println row
        resultList << row
        //  client_port | blocking_pid | blocking_user | blocking_query | client_port | blocked_pid | blocked_user | blocked_query | age
        // no rows --> no locks
    }


    if(resultList.size() > 0)
    {
        return ["status":"OK", "details":"test succeeded", "solution_suggestion":""]
    }
    return ["status":"Failed", "details":"Found one or more locks", "solution_suggestion":"Contact Tufin support"]
}

def getDescrption(){
    return "Search for DB locks"
}


println execute()

this